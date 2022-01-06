package com.mars.multi.thread.learn.test.lock;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FairAndUnfairTest {

    private static Lock fairLock = new ReentrantLock2(true);//公平锁
    private static Lock unfairLock = new ReentrantLock2(false);//非公平锁

    public static void main(String[] args) {
//        new FairAndUnfairTest().testLock(unfairLock);
        new FairAndUnfairTest().testLock(fairLock);
    }

    private void testLock(Lock lock) {
        //启动五个线程
        for (int i = 0; i < 10; i++) {
            Thread job = new Thread(new Job(lock), "job-" + i);
            job.start();
        }
    }

    public static class Job implements Runnable {
        private final Lock lock;

        public Job(Lock lock) {
            this.lock = lock;
        }

        public void run() {
            lock.lock();
            System.out.println("当前线程名称:" + Thread.currentThread().getName() + ",等待队列:" + Arrays.toString(((ReentrantLock2) lock).getQueuedThreads().toArray()));
            System.out.println("当前线程名称:" + Thread.currentThread().getName() + ",等待队列:" + Arrays.toString(((ReentrantLock2) lock).getQueuedThreads().toArray()));
            lock.unlock();
        }
    }

    private static class ReentrantLock2 extends ReentrantLock {

        public ReentrantLock2(boolean fair) {
            super(fair);
        }

        public Collection<Thread> getQueuedThreads() {
            List<Thread> arrayList = new ArrayList<Thread>(super.getQueuedThreads());
            Collections.reverse(arrayList);
            return arrayList;
        }
    }
}
