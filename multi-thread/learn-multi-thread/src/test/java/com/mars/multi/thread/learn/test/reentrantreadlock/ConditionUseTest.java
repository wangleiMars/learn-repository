package com.mars.multi.thread.learn.test.reentrantreadlock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionUseTest {
    Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();

    public static void main(String[] args) {
        ConditionUseTest conditionUseTest = new ConditionUseTest();
        Thread thread = new Thread(() -> {
            try {
                conditionUseTest.conditionWait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread thread1 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            conditionUseTest.conditionSignal();
        });

        thread.start();
        thread1.start();
    }

    public void conditionWait() throws InterruptedException {
        lock.lock();
        try {
            System.out.println("等待");
            condition.await();
            System.out.println("被唤醒");
        } finally {
            lock.unlock();
        }
    }

    public void conditionSignal() {
        lock.lock();
        try {
            condition.signal();
            System.out.println("通知唤醒");
        } finally {
            lock.unlock();
        }
    }
}
