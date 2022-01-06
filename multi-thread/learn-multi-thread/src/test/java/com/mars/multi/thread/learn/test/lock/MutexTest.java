package com.mars.multi.thread.learn.test.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class MutexTest {
    static Mutex mutex = new Mutex();

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            Thread thread2 = new Thread(() -> {
                System.out.println("threadName:" + Thread.currentThread().getName() + ",lock:" + mutex.isLocked());
                mutex.lock();
                System.out.println("threadName:" + Thread.currentThread().getName() + ",lock:" + mutex.isLocked());
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mutex.unlock();
            }, "thread-" + i);
            thread2.start();
        }
    }

    static class Mutex implements Lock {

        private final Sync sync = new Sync();

        @Override
        public void lock() {
            sync.acquire(1);
        }

        @Override
        public void lockInterruptibly() throws InterruptedException {
            sync.acquireInterruptibly(1);
        }

        @Override
        public boolean tryLock() {
            return sync.tryAcquire(1);
        }

        @Override
        public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
            return sync.tryAcquireNanos(1, unit.toNanos(time));
        }

        @Override
        public void unlock() {
            sync.release(1);
        }

        @Override
        public Condition newCondition() {
            return sync.newCondition();
        }

        public boolean isLocked() {
            return sync.isHeldExclusively();
        }

        public boolean hasQueuedThreads() {
            return sync.hasQueuedThreads();
        }

        private class Sync extends AbstractQueuedSynchronizer {
            /**
             * 是否处于占用状态
             */
            protected boolean isHeldExclusively() {
                return getState() == 1;
            }

            /**
             * 当状态为0时获取锁
             *
             * @param acquires
             * @return
             */
            public boolean tryAcquire(int acquires) {
                if (compareAndSetState(0, 1)) {
                    setExclusiveOwnerThread(Thread.currentThread());//设置当前线程占用
                    return true;
                }
                return false;
            }

            /**
             * 释放锁，状态设置为0
             *
             * @param release
             * @return
             */
            protected boolean tryRelease(int release) {
                if (getState() == 0)
                    throw new IllegalMonitorStateException();
                setExclusiveOwnerThread(null);
                setState(0);
                return true;

            }

            Condition newCondition() {
                return new ConditionObject();
            }

        }
    }
}
