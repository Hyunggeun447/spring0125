package com.store.chichi.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name; //이름

    private String nickname; //닉네임Id

    private String password; //비밀번호

    private String eMail;

    private String phoneNumber;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private MemberGrade memberGrade; //    ADMIN, NORMAL



}
