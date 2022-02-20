package com.store.chichi.controller.login;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class LoginForm {

    @NotEmpty
    private String nickName;

    @NotEmpty
    private String password;


}
