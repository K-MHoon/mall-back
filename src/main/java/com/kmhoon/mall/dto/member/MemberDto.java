package com.kmhoon.mall.dto.member;

import com.kmhoon.mall.domain.member.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Getter
@Setter
@ToString
public class MemberDto extends User {

    private String email;

    private String pw;

    private String nickname;

    private boolean social;

    private List<String> roleNames = new ArrayList<>();

    public MemberDto(String email, String pw, String nickname, boolean social, List<String> roleNames) {
        super(email, pw, roleNames.stream().map(str -> new SimpleGrantedAuthority("ROLE_"+str)).collect(Collectors.toList()));

        this.email = email;
        this.pw = pw;
        this.nickname = nickname;
        this.social = social;
        this.roleNames = roleNames;
    }

    public Map<String, Object> getClaims() {
        Map<String, Object> dataMap = new HashMap<>();

        dataMap.put("email", this.email);
        dataMap.put("pw", this.pw);
        dataMap.put("nickname", this.nickname);
        dataMap.put("social", this.social);
        dataMap.put("roleNames", this.roleNames);

        return dataMap;
    }

    public static MemberDto entityToDto(Member member) {
        return new MemberDto(member.getEmail(), member.getPw(), member.getNickname(), member.isSocial(), member.getRoleNames());
    }
}
