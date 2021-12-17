package com.mars.multi.thread.learn.test;

import org.junit.Test;

public class FinalTest {
    static FinalTest finalTest;
    final int j;
    int i;

    public FinalTest() {
        i = 1;
        j = 2;
    }

    public static void write() {
        finalTest = new FinalTest();
        System.out.println("write");
    }

    public static void reader() {
        FinalTest obj = finalTest;
        int a = obj.i;
        int b = obj.j;
        System.out.println("a:" + a + ",b:" + b);
    }

    @Test
    public void test() {
        final FinalTest finalTest = new FinalTest();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                finalTest.write();
            }
        });
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                finalTest.reader();
            }
        });

        thread.start();
        thread1.start();
    }
}
