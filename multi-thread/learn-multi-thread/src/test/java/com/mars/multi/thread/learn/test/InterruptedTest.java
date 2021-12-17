package com.mars.multi.thread.learn.test;

import java.util.concurrent.TimeUnit;

public class InterruptedTest {

    public static void main(String[] args) throws Exception {
        Thread thread = new Thread(new SleepRunner(), "SleepeRunner");
        thread.setDaemon(true);
        Thread thread1 = new Thread(new BusyRunner(), "busyRunner");
        thread1.setDaemon(true);
        thread.start();
        thread1.start();
        TimeUnit.SECONDS.sleep(5);
        thread.interrupt();
        thread1.interrupt();//中断
        System.out.println("sleepRunner interrupt is " + thread.isInterrupted());
        System.out.println("busyRunner interrupt is " + thread1.isInterrupted());
        TimeUnit.SECONDS.sleep(2);
    }

    static class SleepRunner implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class BusyRunner implements Runnable {
        @Override
        public void run() {
            while (true) {

            }
        }
    }
}
