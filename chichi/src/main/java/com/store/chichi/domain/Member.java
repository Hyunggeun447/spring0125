package com.store.chichi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.store.chichi.domain.order.Order;
import javax.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Column(nullable = false)
    private String loginName;

    @Column(nullable = false)
    private String password;

    @Email
    private String eMail;

    private String phoneNumber;

    @Embedded
    private Address address;

    @JsonIgnore
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private MemberGrade memberGrade; //    ADMIN, NORMAL

    public Member(String name, String loginName, String password,
                  String eMail, String phoneNumber, Address address, MemberGrade memberGrade) {
        this.name = name;
        this.loginName = loginName;
        this.password = password;
        this.eMail = eMail;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.memberGrade = memberGrade;
    }
}
