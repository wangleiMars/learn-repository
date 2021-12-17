package com.mars.multi.thread.learn.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ProrityTest {
    private static volatile boolean noStart = true;
    private static volatile boolean notEnd = true;

    public static void main(String[] args) throws Exception {
        List<Job> jobs = new ArrayList<Job>();
        for (int i = 0; i < 10; i++) {
            int priority = i < 5 ? Thread.MIN_PRIORITY : Thread.MAX_PRIORITY;
            Job job = new Job(priority);
            jobs.add(job);
            Thread thread = new Thread(job, "Thread" + i);
            thread.setPriority(priority);
            thread.start();
        }
        noStart = false;
        TimeUnit.SECONDS.sleep(10);
        notEnd = false;
        for (Job job : jobs) {
            System.out.println("Job priority:" + job.porority + ",count:" + job.jobCount);
        }
    }

    static class Job implements Runnable {
        private int porority;
        private long jobCount;

        public Job(int porority) {
            this.porority = porority;
        }

        @Override
        public void run() {
            while (noStart) {
                Thread.yield();
            }
            while (notEnd) {
                Thread.yield();
                jobCount++;
            }
        }
    }
}
