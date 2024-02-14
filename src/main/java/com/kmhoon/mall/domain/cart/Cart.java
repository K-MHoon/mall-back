package com.kmhoon.mall.domain.cart;

import com.kmhoon.mall.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = "owner")
@Table(
        name = "tbl_cart",
        indexes = { @Index(name = "idx_cart_email", columnList = "member_owner")}
)
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cno;

    @OneToOne
    @JoinColumn(name = "member_owner")
    private Member owner;
}
