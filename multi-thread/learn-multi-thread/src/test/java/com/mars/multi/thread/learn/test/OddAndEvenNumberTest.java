package com.mars.multi.thread.learn.test;

import java.util.concurrent.TimeUnit;

public class OddAndEvenNumberTest {

    private static final Object flag = new Object();
    private static volatile int oddNumber = 1;

    public static void main(String[] args) throws Exception {

//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                synchronized (flag) {
//                    while (oddNumber <= 100) {
//                        flag.notify();
//                        if (oddNumber % 2 == 0) {
//                            System.out.println("奇数：" + oddNumber);
//                            oddNumber++;
//                            try {
//                                flag.wait();
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                    Thread.currentThread().interrupt();
//                }
//            }
//        });
//        Thread thread1 = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                synchronized (flag) {
//                    while (oddNumber <= 100) {
//                        flag.notify();
//                        if (oddNumber % 2 != 0) {
//                            System.out.println("偶数：" + oddNumber);
//                            oddNumber++;
//                            try {
//                                flag.wait();
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                    Thread.currentThread().interrupt();
//                }
//            }
//        });
//        thread1.start();
//        thread.start();


        Thread thread2 = new Thread(new OddEven(), "奇数");
        Thread thread3 = new Thread(new OddEven(), "偶数");
        thread2.start();
        thread3.start();

    }

    public static class OddEven extends Thread {

        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (flag) {
                while (oddNumber <= 100) {
                    System.out.println("name:" + Thread.currentThread().getName() + ",int i:" + oddNumber);
                    oddNumber++;
                    flag.notify();
                    try {
                        flag.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }
            }
        }
    }

}
