package com.kmhoon.mall.domain.product;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@ToString
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class ProductImage {

    private String fileName;
    private int ord;

    public void setOrd(int ord) {
        this.ord = ord;
    }
}
