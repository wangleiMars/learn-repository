package com.mars.netty.test.two.netty2.controller;

import com.mars.netty.test.two.netty2.annotation.Remote;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@Remote
public class UserController implements UserControllerI{
    @Remote("getUserNameById")
    public Object getUserNameById(String userId) {
        log.info("客户端请求userId:[{}]", userId);
        return "响应结果===用户张三:" + userId;
    }
}
