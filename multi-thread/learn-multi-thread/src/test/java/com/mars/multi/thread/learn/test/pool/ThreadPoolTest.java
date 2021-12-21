package com.mars.multi.thread.learn.test.pool;

import java.util.concurrent.TimeUnit;

public class ThreadPoolTest {


    public static void main(String[] args) {
        ThreadPool<Job> threadPool = new DefaultThreadPool<Job>(10);
        for (int i = 0; i < 20; i++) {
            Job job = new Job("", i);
            threadPool.execute(job);
        }
    }

//    @Test
//    public void test1() {
//
//    }

    public static class Job implements Runnable {
        private String name;
        private int i;

        public Job(String name, int i) {
            this.name = name;
            this.i = i;
        }

        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("name:" + name + ",int i:" + i);
        }
    }

}
