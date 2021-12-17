package com.mars.multi.thread.learn.test;

public class SafeDoubleCheckedLocking {

    private volatile static InstanceSafeDouble instance;

    public static InstanceSafeDouble getInstance() {
        if (instance == null) {
            /**
             * 这里防止内存指令重排，实际在new会做三件事
             * 1.分配对象的内存
             * 2.初始化对象
             * 3.设置instance指向内存空间
             * 若 无锁 synchronized 这三个指令可能会导致重排，向 3，2，1 会出现访问返回null
             */
            synchronized (SafeDoubleCheckedLocking.class) {
                if (instance == null) {
                    instance = new InstanceSafeDouble();
                }
            }
        }
        return instance;
    }

    public static class InstanceSafeDouble {

    }
}
