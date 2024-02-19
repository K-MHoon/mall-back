package com.kmhoon.mall.controller.member;

import com.kmhoon.mall.dto.member.MemberDto;
import com.kmhoon.mall.dto.member.MemberModifyDto;
import com.kmhoon.mall.service.member.MemberService;
import com.kmhoon.mall.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
public class SocialController {

    private final MemberService memberService;

    @GetMapping("/api/member/kakao")
    public Map<String, Object> getMemberFromKakao(@RequestParam("accessToken") String accessToken) {

        log.info("access Token: " + accessToken);

        MemberDto memberDto = memberService.getKakaoMember(accessToken);

        Map<String, Object> claims = memberDto.getClaims();

        String jwtAccessToken = JwtUtil.generateToken(claims, 10);
        String jwtRefreshToken = JwtUtil.generateToken(claims, 60 * 24);

        claims.put("accessToken", jwtAccessToken);
        claims.put("refreshToken", jwtRefreshToken);

        return claims;
    }

    @PutMapping("/api/member/modify")
    public Map<String, String> modify(@RequestBody MemberModifyDto memberModifyDto) {
        log.info("member modify: " + memberModifyDto);

        memberService.modifyMember(memberModifyDto);

        return Map.of("result", "modified");
    }
}
