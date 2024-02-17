package com.kmhoon.mall.dto.cart;

import lombok.*;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CartItemListDto {

    private Long cino;
    private int qty;
    private Long pno;
    private String pname;
    private int price;
    private String imageFile;

    @Builder
    public CartItemListDto(Long cino, int qty, Long pno, String pname, int price, String imageFile) {
        this.cino = cino;
        this.qty = qty;
        this.pno = pno;
        this.pname = pname;
        this.price = price;
        this.imageFile = imageFile;
    }
}
