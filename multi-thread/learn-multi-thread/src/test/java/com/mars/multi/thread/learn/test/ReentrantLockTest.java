package com.mars.multi.thread.learn.test;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest {

    int i = 0;
    ReentrantLock reentrantLock = new ReentrantLock();

    public void writer() {
        reentrantLock.lock();
        try {
            i++;
        } finally {
            reentrantLock.unlock();
        }
    }

    public void reader() {
        reentrantLock.lock();
        try {
            int a = i;
        } finally {
            reentrantLock.unlock();
        }
    }
}
