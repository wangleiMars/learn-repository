package com.mars.netty.test.two.netty2.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
public class ApplicationMain {
    private static volatile boolean running = true;

    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext configApplicationContext =
                new AnnotationConfigApplicationContext(
                        "com.mars.netty.test.two.netty2.spring",
                        "com.mars.netty.test.two.netty2.controller",
                        "com.mars.netty.test.two.netty2.service");

        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                configApplicationContext.stop();
                synchronized (ApplicationMain.class){
                    running = false;
                    ApplicationMain.class.notify();
                }
            }
        });

        configApplicationContext.start();
        log.info("服务已启动");
        synchronized (ApplicationMain.class){
            while (running) {
                running = false;
                ApplicationMain.class.wait();
            }
        }
    }
}
