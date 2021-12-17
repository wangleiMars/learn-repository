package com.mars.multi.thread.learn.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class WaitNotifyTest {

    static boolean flag = true;
    static Object lock = new Object();

    public static void main(String[] args) throws Exception {
        Thread thread = new Thread(new Wait(), "WaitThread");
        thread.start();
        TimeUnit.SECONDS.sleep(1);
        Thread thread1 = new Thread(new Notify(), "NotilyThread");
        thread1.start();
    }

    static class Wait implements Runnable {
        @Override
        public void run() {
            synchronized (lock) {
                while (flag) {
                    try {
                        System.out.println(Thread.currentThread() + "flag is true,wait @ "
                                + new SimpleDateFormat("HH:mm:ss").format(new Date()));
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(Thread.currentThread() + "flag is false ,running @ "
                        + new SimpleDateFormat("HH:mm:ss").format(new Date()));
            }
        }
    }

    static class Notify implements Runnable {
        @Override
        public void run() {
            synchronized (lock) {
                while (flag) {
                    try {
                        System.out.println(Thread.currentThread() + "hold lock,notify @ "
                                + new SimpleDateFormat("HH:mm:ss").format(new Date()));
                        lock.notifyAll();
                        flag = false;
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                synchronized (lock) {
                    System.out.println(Thread.currentThread() + "hold lock again,sleep @ "
                            + new SimpleDateFormat("HH:mm:ss").format(new Date()));
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
