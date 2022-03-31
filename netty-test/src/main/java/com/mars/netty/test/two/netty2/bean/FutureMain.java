package com.mars.netty.test.two.netty2.bean;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class FutureMain {
    public static void main(String[] args) throws InterruptedException {
        List<RequestFuture> reqs = new ArrayList<>();
        for (int i = 1; i < 100; i++) {
            long id = i;
            RequestFuture req = new RequestFuture();
            req.setId(id);
            req.setRequest("hello world");
            RequestFuture.addFutures(req);
            reqs.add(req);
            sendMsg(req);
            SubThread subThread = new SubThread(req);
            subThread.start();
        }

        for (RequestFuture requestFuture : reqs) {
            Object result = requestFuture.get();
            System.out.println(result.toString());
        }
    }

    private static void sendMsg(RequestFuture requestFuture) {
        log.info("客户端发送数据,请求id为:[{}]", requestFuture.getId());
    }
}
