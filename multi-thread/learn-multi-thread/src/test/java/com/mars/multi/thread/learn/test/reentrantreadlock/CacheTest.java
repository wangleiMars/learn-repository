package com.mars.multi.thread.learn.test.reentrantreadlock;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CacheTest {
    static volatile Map<String, Object> map = new ConcurrentHashMap<>();
    static ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    static AtomicInteger integer = new AtomicInteger();
    static Lock r = rwl.readLock();
    static Lock w = rwl.writeLock();

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            Thread write = new Thread(new Write(), "Write-" + i);
            write.start();
            Thread read = new Thread(new Read(), "Read-" + i);
            read.start();
        }
        while (true) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(integer.get() + ",threadName:" + Thread.currentThread().getName() + ",size:" + map.size()
                    + ",QueueLength:" + rwl.getQueueLength() + ",ReadHoldCount:" + rwl.getReadHoldCount());
        }
    }

    public static Object get(String key) {
        r.lock();
        try {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return map.get(key);
        } finally {
            r.unlock();
        }
    }

    public static Object put(String key, Object o) {
        w.lock();
        try {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return map.put(key, o);
        } finally {
            w.unlock();
            System.out.println("WriteThread:" + Thread.currentThread().getName() + "释放");
        }
    }

    public static void clear() {
        w.lock();
        try {
            map.clear();
        } finally {
            w.unlock();
        }
    }

    public static class Write implements Runnable {
        @Override
        public void run() {
            long g = System.currentTimeMillis();
            System.out.println("WriteThread:" + Thread.currentThread().getName() + "开始");
//            for (int i = 0; i < 10; i++) {
            integer.addAndGet(1);
            put("key" + integer.get(), integer.get());
//            }
            System.out.println("WriteThread:" + Thread.currentThread().getName() + ",写锁时长：" + (System.currentTimeMillis() - g));
        }
    }

    public static class Read implements Runnable {
        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(1);//read 晚启动
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long i = System.currentTimeMillis();
            get("key1");
            System.out.println("readThread:" + Thread.currentThread().getName() + ",读锁时长：" + (System.currentTimeMillis() - i));
        }
    }
}
