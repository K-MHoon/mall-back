package com.kmhoon.mall.dto.response.todo;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TodoResponse {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    @ToString
    public static class Register {

        private Long tno;
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
    public static class Remove {

        private String result;
    }
}
