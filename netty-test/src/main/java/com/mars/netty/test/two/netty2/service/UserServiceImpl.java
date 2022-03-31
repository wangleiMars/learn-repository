package com.mars.netty.test.two.netty2.service;

import com.mars.netty.test.two.netty2.annotation.Remote;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Remote
public class UserServiceImpl implements UserService {
    @Override
    public Object getUserNameById(String userName) {
        log.info("请求信息：{}", userName);
        return "服务器响应ok，threadid:[" + Thread.currentThread().getName() + "]";
    }
}
