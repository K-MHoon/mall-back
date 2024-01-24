package com.kmhoon.mall.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;

import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.ZonedDateTime;
import java.util.Map;

@Log4j2
public class JwtUtil {

    private static String key = "https://shiba-holic.tistory.com/";

    public static String generateToken(Map<String, Object> valueMap, int min) {
        try {
            SecretKey key  = Keys.hmacShaKeyFor(JwtUtil.key.getBytes(StandardCharsets.UTF_8));

            return Jwts.builder()
                    .setHeader(Map.of("typ", "jwt"))
                    .setClaims(valueMap)
                    .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                    .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant()))
                    .signWith(key)
                    .compact();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static Map<String, Object> validateToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(JwtUtil.key.getBytes(StandardCharsets.UTF_8));

            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (MalformedJwtException malformedJwtException) {
            throw new CustomJwtException("Malformed");
        } catch (ExpiredJwtException expiredJwtException) {
            throw new CustomJwtException("Expired");
        } catch (InvalidClaimException invalidClaimException) {
            throw new CustomJwtException("Invalid");
        } catch (JwtException jwtException) {
            throw new CustomJwtException("JWTError");
        } catch (Exception e) {
            throw new CustomJwtException("Error");
        }
    }

}
