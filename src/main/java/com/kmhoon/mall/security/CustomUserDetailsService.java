package com.kmhoon.mall.security;

import com.kmhoon.mall.domain.member.Member;
import com.kmhoon.mall.domain.member.MemberRole;
import com.kmhoon.mall.dto.member.MemberDto;
import com.kmhoon.mall.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("-------------loadUserByUsername--------------");

        Member member = memberRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Not Found"));

        MemberDto memberDto = new MemberDto(member.getEmail(), member.getPw(), member.getNickName(), member.isSocial(), member.getMemberRoleList().stream().map(MemberRole::name).collect(Collectors.toList()));

        log.info(memberDto);

        return memberDto;
    }
}
