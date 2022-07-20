package com.store.chichi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.store.chichi.domain.order.Order;
import javax.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public void changePassword(String newPassword) {
        if (newPassword.isBlank()) {
            throw new RuntimeException();
        }
        this.password = newPassword;
    }

    public void changeEmail(String newEmail) {
        if (newEmail.isBlank()) {
            throw new RuntimeException();
        }
        this.eMail = newEmail;
    }

    public void changeAddress(Address newAddress) {
        if (newAddress == null) {
            throw new RuntimeException();
        }
        this.address = newAddress;
    }

    public void changePhoneNumber(String newPhoneNumber) {
        if (newPhoneNumber.isBlank()) {
            throw new RuntimeException();
        }
        this.phoneNumber = newPhoneNumber;
    }

    public void changeMemberGrade(MemberGrade newMemberGrade) {
        if (newMemberGrade == null) {
            throw new RuntimeException();
        }
        this.memberGrade = newMemberGrade;
    }
}
