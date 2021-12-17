package com.mars.multi.thread.learn.test;

import org.junit.Test;

public class FinalReferenceTest {

    static FinalReferenceTest finalTest;
    final int[] intArray;

    public FinalReferenceTest() {
        intArray = new int[2];
        intArray[0] = 1;
        System.out.println("create");
    }

    public static void writerOne() {
        finalTest = new FinalReferenceTest();
        System.out.println("writerOne");
    }

    public static void writerTwo() {
        finalTest.intArray[1] = 2;
        System.out.println("writerTwo");
    }

    public static void reader() {
        if (finalTest != null) {
            int temp1 = finalTest.intArray[0];
            System.out.println("finalTest:" + finalTest.intArray[1]);
        }
        System.out.println("reader");
    }

    @Test
    public void test() {
        final FinalReferenceTest finalTest = new FinalReferenceTest();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                finalTest.writerOne();
            }
        });
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                finalTest.writerTwo();
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                finalTest.reader();
            }
        });

        thread.start();
        thread1.start();
        thread2.start();
    }
}
