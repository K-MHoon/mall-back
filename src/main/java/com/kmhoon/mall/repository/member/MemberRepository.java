package com.kmhoon.mall.repository.member;

import com.kmhoon.mall.domain.member.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {

    @EntityGraph(attributePaths = {"memberRoleList"})
    Optional<Member> findByEmail(String email);
}
