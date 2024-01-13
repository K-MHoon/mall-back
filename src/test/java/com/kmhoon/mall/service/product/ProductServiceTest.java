package com.kmhoon.mall.service.product;

import com.kmhoon.mall.dto.common.page.PageRequestDto;
import com.kmhoon.mall.dto.common.page.PageResponseDto;
import com.kmhoon.mall.dto.domain.product.ProductDto;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class ProductServiceTest {

    @Autowired
    ProductService productService;

    @Test
    void testList() {
        PageRequestDto pageRequestDto = PageRequestDto.builder().build();

        PageResponseDto<ProductDto> result = productService.getList(pageRequestDto);

        result.getDtoList().forEach(dto -> log.info(dto));
    }

    @Test
    public void testRegister() {
        ProductDto productDto = ProductDto.builder()
                .pname("새로운상품")
                .pdesc("신규 추가 상품입니다.")
                .price(1000)
                .build();
        productDto.setUploadFileNames(
                List.of(UUID.randomUUID() + "_" + "Test1.jpg",
                        UUID.randomUUID() + "_" + "Test2.jpg")
        );

        productService.register(productDto);
    }

    @Test
    public void testRead() {
        Long pno = 1L;

        ProductDto productDto = productService.get(pno);

        log.info(productDto);
        log.info(productDto.getUploadFileNames());
    }
}