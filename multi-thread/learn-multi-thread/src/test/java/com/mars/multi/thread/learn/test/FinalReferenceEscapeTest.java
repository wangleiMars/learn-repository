package com.mars.multi.thread.learn.test;

import org.junit.Test;

public class FinalReferenceEscapeTest {
    static FinalReferenceEscapeTest finalReferenceEscapeTest;
    final int i;

    public FinalReferenceEscapeTest() {
        i = 0;
        finalReferenceEscapeTest = this;
    }

    public static void writer() {
        new FinalReferenceEscapeTest();
        System.out.println("writer");
    }

    public static void reader() {
        if (finalReferenceEscapeTest != null) {
            int temp = finalReferenceEscapeTest.i;
            System.out.println("TEMP:" + temp);
        }
        System.out.println("Reader");
    }

    @Test
    public void test() {
        final FinalReferenceEscapeTest finalTest = new FinalReferenceEscapeTest();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                finalTest.writer();
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
