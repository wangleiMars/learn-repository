package com.mars.multi.thread.learn.test.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class TwinsLockTest {
    final static Lock lock = new TwinsLock();

    public static void main(String[] args) throws Exception {

        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new Work(), "thread-" + i);
//            thread.setDaemon(true);
            thread.start();
        }
        for (int i = 0; i < 10; i++) {
            TimeUnit.SECONDS.sleep(1);
            System.out.println();
        }


    }

    public static class Work implements Runnable {
        @Override
        public void run() {
            while (true) {
                lock.lock();
                try {
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println(Thread.currentThread().getName());
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}
