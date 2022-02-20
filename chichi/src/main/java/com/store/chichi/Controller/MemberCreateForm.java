package com.store.chichi.Controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class MemberCreateForm {

    @NotEmpty(message = "아이디는 필수입니다.")
    private String nickname;

    @NotEmpty(message = "이름은 필수입니다.")
    private String name;

    @NotEmpty(message = "비밀번호는 필수입니다.")
    private String password;

    @NotEmpty(message = "비밀번호는 필수입니다.")
    private String password2;


    private String eMail;
    private String phoneNumber;
    private String address1;
    private String address2;

}
