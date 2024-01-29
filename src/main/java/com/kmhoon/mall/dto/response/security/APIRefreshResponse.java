package com.kmhoon.mall.dto.response.security;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class APIRefreshResponse {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    @ToString
    public static class Refresh {

        private String accessToken;
        private String refreshToken;
    }
}
