package com.example.paper.bean;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class SignUpForm {
    @NotEmpty(message = "账号必填")
    private String  account;
    @NotEmpty(message = "密码必填")
    private String password;
    @NotEmpty(message = "用户名必填")
    private String username;
}
