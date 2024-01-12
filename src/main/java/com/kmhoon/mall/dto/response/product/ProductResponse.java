package com.kmhoon.mall.dto.response.product;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProductResponse {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    @ToString
    public static class Register {

        private Long result;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    @ToString
    public static class Modify {

        private String result;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    @ToString
    public static class Delete {

        private String result;
    }
}
