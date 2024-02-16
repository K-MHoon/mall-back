package com.kmhoon.mall.dto.cart;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CartItemDto {

    @NotNull
    private String email;
    private Long pno;
    private int qty;
    private Long cino;
}
