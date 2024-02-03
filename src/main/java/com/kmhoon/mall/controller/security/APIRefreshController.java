package com.kmhoon.mall.controller.security;

import com.kmhoon.mall.dto.response.security.APIRefreshResponse;
import com.kmhoon.mall.util.CustomJwtException;
import com.kmhoon.mall.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
public class APIRefreshController {

    @GetMapping("/api/member/refresh")
    @ResponseStatus(HttpStatus.OK)
    public APIRefreshResponse.Refresh refresh(@RequestHeader("Authorization") String authHeader,
                                              @RequestParam("refreshToken") String refreshToken) {

        if(!StringUtils.hasText(refreshToken)) {
            throw new CustomJwtException("EMPTY_REFRESH");
        }

        if(!StringUtils.hasText(authHeader) || authHeader.length() < 7) {
            throw new CustomJwtException("INVALID_STRING");
        }

        String accessToken = authHeader.substring(7);

        if(!checkExpiredToken(accessToken)) {
            return APIRefreshResponse.Refresh.builder().accessToken(accessToken).refreshToken(refreshToken).build();
        }

        Map<String, Object> claims = JwtUtil.validateToken(refreshToken);

        log.info("refresh ... claims: " + claims);

        String newAccessToken = JwtUtil.generateToken(claims, 10);
        String newRefreshToken = checkTime((Integer)claims.get("exp")) ? JwtUtil.generateToken(claims, 60*24) : refreshToken;

        return APIRefreshResponse.Refresh.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    private boolean checkTime(Integer exp) {

        // JWT exp를 날짜로 변환한다.
        Date expDate = new Date((long) exp * (1000));

        // 현재 시간과의 차이를 계산한다 - 밀리세컨즈
        long gap = expDate.getTime() - System.currentTimeMillis();

        // 분 단위 계산
        long leftMin = gap / (1000 * 60);

        // 1시간도 안남았는지 확인한다.
        return leftMin < 60;
    }

    private boolean checkExpiredToken(String token) {
        try {
            JwtUtil.validateToken(token);
        } catch (CustomJwtException ex) {
            if(ex.getMessage().equals("Expired")) {
                return true;
            }
        }
        return false;
    }
}
