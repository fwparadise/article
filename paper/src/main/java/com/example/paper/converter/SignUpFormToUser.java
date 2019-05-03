package com.example.paper.converter;

import com.example.paper.entity.User;
import com.example.paper.bean.SignUpForm;

public class SignUpFormToUser {
    public static User convert(SignUpForm form){
        User user=new User();
        user.setAccount(form.getAccount());
        user.setUsername((form.getUsername()));
        user.setPassword(form.getPassword());
        user.setGender(1);
        user.setDescription("这个人很懒，什么都没有留下...");
        return user;
    }
}
