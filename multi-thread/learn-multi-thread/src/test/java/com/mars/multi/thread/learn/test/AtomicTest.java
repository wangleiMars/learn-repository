package com.mars.multi.thread.learn.test;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicTest {

    private AtomicInteger atomicInteger = new AtomicInteger(0);
    private int i = 0;

    @Test
    public void testCAS() {
        final AtomicTest atomicTest = new AtomicTest();
        List<Thread> list = new ArrayList<Thread>(600);
        long start = System.currentTimeMillis();
        for (int j = 0; j < 100; j++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 10000; i++) {
                        atomicTest.count();

                        atomicTest.safaCount();
                    }
                }
            });
            list.add(thread);
        }
        for (Thread thread : list) {
            thread.start();
        }

        for (Thread t : list) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(atomicTest.i);
        System.out.println(atomicTest.atomicInteger.get());
        System.out.println(System.currentTimeMillis() - start);
    }

    private void safaCount() {
        for (; ; ) {
            int i = atomicInteger.get();
            boolean suc = atomicInteger.compareAndSet(i, ++i);
            if (suc) {
                break;
            }
        }
    }

    private void count() {
        i++;
    }

}
