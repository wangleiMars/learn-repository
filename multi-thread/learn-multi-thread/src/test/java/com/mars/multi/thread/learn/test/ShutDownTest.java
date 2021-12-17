package com.mars.multi.thread.learn.test;

import java.util.concurrent.TimeUnit;

public class ShutDownTest {

    public static void main(String[] args) throws Exception {
        Runner runner = new Runner();
        Thread thread = new Thread(runner, "CountThread");
        thread.start();
        TimeUnit.SECONDS.sleep(1);
        thread.interrupt();
        Runner runner1 = new Runner();
        Thread thread1 = new Thread(runner1, "CountThread");
        thread1.start();
        TimeUnit.SECONDS.sleep(1);
        runner1.canel();
    }

    private static class Runner implements Runnable {
        private long i;
        private volatile boolean on = true;

        @Override
        public void run() {
            System.out.println("i:" + i);
            while (on && !Thread.currentThread().isInterrupted()) {
                i++;
            }
            System.out.println("Count i=" + i);
        }

        private void canel() {
            on = false;
        }
    }

}
