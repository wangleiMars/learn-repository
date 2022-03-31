package com.mars.netty.test.two.netty2.controller;

import com.mars.netty.test.two.netty2.annotation.RemoteInvoke;
import com.mars.netty.test.two.netty2.service.UserService;
import org.springframework.stereotype.Component;

@Component
public class LoginController {

    @RemoteInvoke
    UserService userService;

    public Object getUserNameById(String userName) {
        return userService.getUserNameById(userName);
    }
}
