package com.mars.netty.test.two.netty2.poxy;

import com.mars.netty.test.two.netty2.controller.LoginController;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class TestProxy {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext annotationConfigApplicationContext =
                new AnnotationConfigApplicationContext(
                        "com.mars.netty.test.two.netty2.controller",
                        "com.mars.netty.test.two.netty2.poxy");
        LoginController loginController = annotationConfigApplicationContext.getBean(LoginController.class);

        Object o = loginController.getUserNameById("张三");
        System.out.println(o);
    }
}
