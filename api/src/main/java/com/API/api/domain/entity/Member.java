package com.API.api.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true, updatable = false)
    @NotBlank
    @Size(min = 2, max = 12, message = "2 ~ 12")

    private String loginName;

    @Column(updatable = false)
    private String memberName;

    @Column(nullable = false)
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-z])(?=.*\\W)(?=\\S+$).{6,12}",
            message = "비밀번호는 영문자,숫자,특수기호가 적어도 1개 이상 포함된 6~12자의 비밀번호여야 합니다.")
    private String password;

    @Email
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Enumerated(EnumType.STRING)
    private MemberType memberType;

    public Member(String loginName, String memberName, String password, String email) {
        this.loginName = loginName;
        this.memberName = memberName;
        this.password = password;
        this.email = email;
    }

    public void addTeam(Team team) {
        if (this.team != null) {
            this.team.getMembers().remove(this);
        }
        this.team = team;
        team.getMembers().add(this);
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void updateType(MemberType memberType) {
        this.memberType = memberType;
    }

    public void updateEmail(String newEmail) {
        this.email = newEmail;
    }
}
