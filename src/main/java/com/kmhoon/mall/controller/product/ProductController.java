package com.kmhoon.mall.controller.product;

import com.kmhoon.mall.dto.common.page.PageRequestDto;
import com.kmhoon.mall.dto.common.page.PageResponseDto;
import com.kmhoon.mall.dto.domain.product.ProductDto;
import com.kmhoon.mall.dto.response.product.ProductResponse;
import com.kmhoon.mall.service.product.ProductService;
import com.kmhoon.mall.util.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/products")
public class ProductController {

    private final CustomFileUtil fileUtil;
    private final ProductService productService;

    @PostMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse.Register register(ProductDto productDto) {
        log.info("register: " + productDto);
        List<MultipartFile> files = productDto.getFiles();
        List<String> uploadFileNames = fileUtil.saveFiles(files);
        productDto.setUploadFileNames(uploadFileNames);
        log.info(uploadFileNames);
        Long pno = productService.register(productDto);
        return ProductResponse.Register.builder()
                .result(pno)
                .build();
    }

    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGet(@PathVariable(name = "fileName") String fileName) {
        return fileUtil.getFile(fileName);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public PageResponseDto<ProductDto> list(PageRequestDto pageRequestDto) {
        return productService.getList(pageRequestDto);
    }

    @GetMapping("/{pno}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDto read(@PathVariable(name = "pno") Long pno) {
        return productService.get(pno);
    }

    @PutMapping("/{pno}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse.Modify modify(@PathVariable(name = "pno") Long pno, ProductDto productDto) {
        productDto.setPno(pno);
        ProductDto oldProductDto = productService.get(pno);

        // 기존의 파일들
        List<String> oldFileNames = oldProductDto.getUploadFileNames();

        // 새로 업로드 해야 하는 파일들
        List<MultipartFile> files = productDto.getFiles();

        // 새로 업로드되어서 만들어진 파일 이름들
        List<String> currentUploadFileNames = fileUtil.saveFiles(files);

        // 화면에서 변화 없이 계속 유지된 파일들
        List<String> uploadFileNames = productDto.getUploadFileNames();

        // 유지되는 파일들 + 새로 업로드된파일 이름들이 저장해야 하는 파일 목록이 됨
        if(currentUploadFileNames != null && !currentUploadFileNames.isEmpty()) {
            uploadFileNames.addAll(currentUploadFileNames);
        }
        // 수정 작업
        productService.modify(productDto);

        if(oldFileNames != null && !oldFileNames.isEmpty()) {
            // 지워야 하는 파일 목록 찾기
            // 예전 파일들 중에서 지워야 하는 파일이름들
            List<String> removeFiles = oldFileNames.stream()
                    .filter(fileName -> !uploadFileNames.contains(fileName))
                    .collect(Collectors.toList());

            // 실제 파일 삭제
            fileUtil.deleteFiles(removeFiles);
        }
        return ProductResponse.Modify.builder().result("success").build();
    }

    @DeleteMapping("/{pno}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse.Delete remove(@PathVariable("pno") Long pno) {
        List<String> oldFileNames = productService.get(pno).getUploadFileNames();

        productService.remove(pno);

        fileUtil.deleteFiles(oldFileNames);

        return ProductResponse.Delete.builder().result("success").build();
    }
}
