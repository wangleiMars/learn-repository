package com.mars.multi.thread.learn.test.pool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultThreadPool<T extends Runnable> implements ThreadPool<T> {

    private static final int MAX_WORKER_NUMER = 10;
    private static final int DEFAULT_WORK_NUMER = 5;
    private static final int MIN_WORK_NUMBER = 1;
    private final LinkedList<T> jobs = new LinkedList<T>();
    private final List<Worker> workers = Collections.synchronizedList(new ArrayList<Worker>());
    private int workNum = DEFAULT_WORK_NUMER;
    private AtomicInteger threadNm = new AtomicInteger();

    public DefaultThreadPool() {
        initializeWokers(DEFAULT_WORK_NUMER);
    }

    public DefaultThreadPool(int num) {
        workNum = num > MAX_WORKER_NUMER ? MAX_WORKER_NUMER : num < MIN_WORK_NUMBER ? MIN_WORK_NUMBER : num;
        initializeWokers(workNum);
    }


    @Override
    public void execute(T job) {
        if (null != job) {
            synchronized (jobs) {
                jobs.addLast(job);
                job.notify();
            }
        }
    }

    @Override
    public void shutdown() {
        for (Worker worker : workers) {
            worker.shutdown();
        }
    }

    @Override
    public void addWorks(int num) {
        synchronized (jobs) {
            if (num + this.workNum > MAX_WORKER_NUMER) {
                num = MAX_WORKER_NUMER - this.workNum;
            }
            initializeWokers(num);
            this.workNum += num;
        }
    }

    @Override
    public void removeWorks(int num) {
        synchronized (jobs) {
            if (num > this.workNum) {
                throw new IllegalArgumentException("beyond worknum");
            }
            int count = 0;
            while (count < count) {
                Worker worker = workers.get(count);
                if (workers.remove(worker)) {
                    worker.shutdown();
                    count++;
                }
            }
            this.workNum -= count;
        }
    }

    @Override
    public int getJobSize() {
        return jobs.size();
    }

    private void initializeWokers(int num) {
        for (int i = 0; i < num; i++) {
            Worker worker = new Worker();
            workers.add(worker);
            Thread thread = new Thread(worker, "Thread-worker-" + threadNm.incrementAndGet());
            thread.start();
        }
    }

    class Worker implements Runnable {

        private volatile boolean running = true;

        @Override
        public void run() {
            while (running) {
                T job = null;
                synchronized (jobs) {
                    while (jobs.isEmpty()) {
                        try {
                            jobs.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Thread.currentThread().interrupt();
                            return;
                        }
                        job = jobs.removeFirst();
                    }
                    if (null != job) {
                        job.run();
                    }
                }

            }
        }

        public void shutdown() {
            running = false;
        }
    }
}
