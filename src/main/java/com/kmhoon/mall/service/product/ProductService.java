package com.kmhoon.mall.service.product;

import com.kmhoon.mall.domain.product.Product;
import com.kmhoon.mall.domain.product.ProductImage;
import com.kmhoon.mall.dto.common.page.PageRequestDto;
import com.kmhoon.mall.dto.common.page.PageResponseDto;
import com.kmhoon.mall.dto.domain.product.ProductDto;
import com.kmhoon.mall.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public PageResponseDto<ProductDto> getList(PageRequestDto pageRequestDto) {
        PageRequest pageable = PageRequest.of(pageRequestDto.getPage() - 1,
                pageRequestDto.getSize(),
                Sort.by("pno").descending());

        Page<Object[]> result = productRepository.selectList(pageable);

        List<ProductDto> dtoList = result.get().map(arr -> {
            Product product = (Product) arr[0];
            ProductImage productImage = (ProductImage) arr[1];

            ProductDto productDto = ProductDto.builder()
                    .pno(product.getPno())
                    .pname(product.getPname())
                    .pdesc(product.getPdesc())
                    .price(product.getPrice())
                    .build();

            String imageStr = productImage.getFileName();
            productDto.setUploadFileNames(List.of(imageStr));
            return productDto;
        }).collect(Collectors.toList());

        long totalCount = result.getTotalElements();

        return PageResponseDto.<ProductDto>withAll()
                .dtoList(dtoList)
                .totalCount(totalCount)
                .pageRequestDTO(pageRequestDto)
                .build();
    }

    @Transactional
    public Long register(ProductDto productDto) {

        Product product = dtoToEntity(productDto);

        Product result = productRepository.save(product);

        return result.getPno();
    }

    private Product dtoToEntity(ProductDto productDto) {
        Product product = Product.builder()
                .pno(productDto.getPno())
                .pname(productDto.getPname())
                .pdesc(productDto.getPdesc())
                .price(productDto.getPrice())
                .build();

        List<String> uploadFileNames = productDto.getUploadFileNames();
        if(uploadFileNames == null || uploadFileNames.isEmpty()) {
            return product;
        }

        uploadFileNames.forEach(product::addImageString);

        return product;
    }

    @Transactional(readOnly = true)
    public ProductDto get(Long pno) {
        Product product = productRepository.selectOne(pno).orElseThrow();
        return entityToDto(product);
    }

    private ProductDto entityToDto(Product product) {

        ProductDto productDto = ProductDto.builder()
                .pno(product.getPno())
                .pname(product.getPname())
                .pdesc(product.getPdesc())
                .price(product.getPrice())
                .build();

        List<ProductImage> imageList = product.getImageList();
        if(imageList == null || imageList.isEmpty()) {
            return productDto;
        }

        List<String> fileNameList = imageList.stream().map(ProductImage::getFileName).toList();
        productDto.setUploadFileNames(fileNameList);
        return productDto;
    }

    @Transactional
    public void modify(ProductDto productDto) {
        Product product = productRepository.findById(productDto.getPno()).orElseThrow();

        product.changeName(productDto.getPname());
        product.changeDesc(productDto.getPdesc());
        product.changePrice(productDto.getPrice());

        product.clearList();

        List<String> uploadFileNames = productDto.getUploadFileNames();
        if(uploadFileNames != null && !uploadFileNames.isEmpty()) {
            uploadFileNames.forEach(product::addImageString);
        }
        productRepository.save(product);
    }

    @Transactional
    public void remove(Long pno) {
        productRepository.updateToDelete(pno, true);
    }
}
