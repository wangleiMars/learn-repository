package com.mars.multi.thread.learn.test.concurrent;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExchangerTest {
    private static final Exchanger<String> EXCHANGER = new Exchanger<>();

    private static ExecutorService executorService = Executors.newFixedThreadPool(2);

    public static void main(String[] args) {
        executorService.execute(() -> {
            String a = "银行流水a";
            try {
                String s = EXCHANGER.exchange(a);
                System.out.println("1b:" + s + ",a:" + a);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }System.out.println("结束");
        });

        executorService.execute(() -> {
            String b = "银行流水b";
            try {
                String c = EXCHANGER.exchange(b);
                System.out.println("2b:" + b + ",b:" + c);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }System.out.println("结束");
        });

        executorService.execute(() -> {
            String b = "银行流水c";
            try {
                String c = EXCHANGER.exchange(b);
                System.out.println("3b:" + c + ",b:" + c);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("结束");
        });
        executorService.execute(() -> {
            String b = "银行流水4";
            try {
                String c = EXCHANGER.exchange(b);
                System.out.println("4b:" + c + ",b:" + c);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("结束");
        });
//        executorService.shutdown();
    }
}
