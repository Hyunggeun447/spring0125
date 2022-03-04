package com.store.chichi.domain;

import com.store.chichi.domain.order.Order;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name; //이름

    private String loginName; //닉네임Id

    private String password; //비밀번호

    private String eMail;

    private String phoneNumber;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private MemberGrade memberGrade; //    ADMIN, NORMAL

    public Member(String name, String loginName, String password,
                  String eMail, String phoneNumber) {
        this.name = name;
        this.loginName = loginName;
        this.password = password;
        this.eMail = eMail;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.orders = orders;
        this.memberGrade = memberGrade;
    }

}
