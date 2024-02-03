package com.kmhoon.mall.dto.member;

import lombok.Data;

@Data
public class MemberModifyDto {

    private String email;
    private String pw;
    private String nickname;
}
