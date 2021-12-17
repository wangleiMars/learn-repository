package com.mars.multi.thread.learn.test;

import org.junit.Test;

public class SynchroniezdTest {
    int a = 0;
    boolean flag = false;

    public synchronized void writer() {
        a = 1;
        flag = true;
        System.out.println("writer");
    }

    public synchronized void reader() {
        System.out.println("reader");
        if (flag) {
            System.out.println("reader flag true");
            int i = a;
            flag = false;
        } else {
            System.out.println("reader flag flase");
        }
    }

    @Test
    public void TestSyn() {
        final SynchroniezdTest synchroniezdTest = new SynchroniezdTest();
        for (int i = 0; i < 100; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    synchroniezdTest.writer();
                }
            });
            thread.start();
            Thread thread1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    synchroniezdTest.reader();
                }
            });
            thread1.start();
        }

    }

}
