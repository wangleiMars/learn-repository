package com.mars.multi.thread.learn.test;

import java.util.concurrent.TimeUnit;

public class ProFileTest {
    private static final ThreadLocal<Long> TIME_THREADLOCAL = new ThreadLocal<Long>() {
        protected Long initialValue() {
            return System.currentTimeMillis();
        }
    };

    public static final void begin() {
        TIME_THREADLOCAL.set(System.currentTimeMillis());
    }

    public static final long end() {
        return System.currentTimeMillis() - TIME_THREADLOCAL.get();
    }

    public static void main(String[] args) throws Exception {
        ProFileTest.begin();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("Cost: " + ProFileTest.end() + " mills");
    }
}
