package com.mars.multi.thread.learn.test;

/**
 * JVM在类的初始化阶段（即在Class被加载后，且被线程使用之前），会执行类的初始化。
 * 在执行类的初始化期间，JVM会去获取一个锁。这个锁可以同步多个线程对同一个类的初始化。
 */
public class InstanceFactory {
    public static Instance getInstance() {
        return InstanceHolder.instance;
    }

    private static class InstanceHolder {
        public static Instance instance = new Instance();
    }

    public static class Instance {

    }
}
