package com.kmhoon.mall.domain.member;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {

    @Id
    private String email;

    private String pw;

    private String nickname;

    private boolean social;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private List<MemberRole> memberRoleList = new ArrayList<>();

    public void addRole(MemberRole memberRole) {
        this.memberRoleList.add(memberRole);
    }

    public void clearRole() {
        this.memberRoleList.clear();
    }

    public void changePw(String pw) {
        this.pw = pw;
    }

    public void changeSocial(boolean social) {
        this.social = social;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<String> getRoleNames() {
        return this.memberRoleList.stream().map(Enum::name).collect(Collectors.toList());
    }
}
