package com.mars.multi.thread.learn.test.concurrent;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierTest {

    static CyclicBarrier cyclicBarrier = new CyclicBarrier(2);

    static CyclicBarrier cyclicBarrierUser = new CyclicBarrier(2, new CyclicBarrierUser());

    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    cyclicBarrier.await();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (BrokenBarrierException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//
//        cyclicBarrier.await();
//
//        System.out.println("aaa");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    cyclicBarrierUser.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                System.out.println("1");
            }
        }).start();

        cyclicBarrierUser.await();
        System.out.println("2");
    }

    static class CyclicBarrierUser implements Runnable {
        @Override
        public void run() {
            System.out.println("3");
        }
    }
}
