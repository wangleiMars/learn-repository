package com.mars.multi.thread.learn.test.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class TwinsLock implements Lock {
    TwinsSync twinsSync = new TwinsSync(2);

    @Override
    public void lock() {
        twinsSync.acquireShared(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        twinsSync.acquireSharedInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return twinsSync.tryAcquireShared(1) > 0;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return twinsSync.tryAcquireSharedNanos(1, unit.toNanos(time));
    }

    @Override
    public void unlock() {
        twinsSync.releaseShared(1);
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    private final static class TwinsSync extends AbstractQueuedSynchronizer {
        TwinsSync(int count) {
            if (count <= 0) {
                throw new IllegalArgumentException("count must large than zero");
            }
            setState(count);
        }

        public int tryAcquireShared(int reduceCount) {
            for (; ; ) {
                int current = getState();
                int newCount = current - reduceCount;
                if (newCount < 0 || compareAndSetState(current, newCount)) {
                    return newCount;
                }
            }
        }

        @Override
        protected boolean tryReleaseShared(int returnCount) {
            for (; ; ) {
                int current = getState();
                int newCount = current + returnCount;
                if (compareAndSetState(current, newCount)) {
                    return true;
                }
            }
        }

    }
}
