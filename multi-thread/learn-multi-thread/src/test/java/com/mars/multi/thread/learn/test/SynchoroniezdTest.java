package com.mars.multi.thread.learn.test;

public class SynchoroniezdTest {

    public static void main(String[] args) {
        synchronized (SynchoroniezdTest.class) {

        }
        m2();
    }

    public static synchronized void m2() {

    }
}
