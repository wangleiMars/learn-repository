package com.mars.multi.thread.learn.test.concurrent;

import java.util.concurrent.locks.StampedLock;

public class StampedLockTest {

    public static void main(String[] args) {
        System.out.println(4 << 4);

        System.out.println(128 | 129);

        int x = -5;
        int y = x >> 31;
        System.out.println(y);
        int r = (x + y) ^ y;
        System.out.println(r);
        long WBIT  = 1L << 7;
        System.out.println(WBIT);
    }

    class Point {
        private double x, y;
        private final StampedLock sl = new StampedLock();

        void move(double deltaX, double deltaY) { // 多个线程修改x,y的值
            long stamp = sl.writeLock();
            try {
                x += deltaX;
                y += deltaY;
            } finally {
                sl.unlockWrite(stamp);
            }
        }

        /**
         * 这个用法和ReadWriteLock的用法没有区别，写操作和写操作也是互斥的。关键在于读的时候，
         * 用了一个“乐观读”sl.tryOptimisticRead（），相当于在读之前给数据的状态做了一个“快照”。
         * 然后，把数据拷贝到内存里面，在用之前，再比对一次版本号。如果版本号变了，
         * 则说明在读的期间有其他线程修改了数据。读出来的数据废弃，重新获取读锁。
         *
         * @return
         */
        double distanceFromOrigin() { // 多个线程调用函数求距离
            long stamp = sl.tryOptimisticRead();//使用乐观锁，
            double currentX = x, currentY = y;//将共享变量拷贝到线程栈
            if (!sl.validate(stamp)) {//读的期间有其他线程修改数据，读的是脏数据，放弃, validate插入了内存屏障
                stamp = sl.readLock();//升级为悲观锁
                try {
                    currentX = x;
                    currentY = y;
                } finally {
                    sl.unlockRead(stamp);
                }
            }
            return Math.sqrt(currentX * currentX + currentY * currentY);
        }

        void moveIfAtOrigin(double newX, double newY) { // upgrade
            // Could instead start with optimistic, not read mode
            long stamp = sl.readLock();
            try {
                while (x == 0.0 && y == 0.0) {
                    long ws = sl.tryConvertToWriteLock(stamp); //读锁转换为写锁
                    if (ws != 0L) {
                        stamp = ws;
                        x = newX;
                        y = newY;
                        break;
                    } else {
                        sl.unlockRead(stamp);
                        stamp = sl.writeLock();
                    }
                }
            } finally {
                sl.unlock(stamp);
            }
        }
    }

}
