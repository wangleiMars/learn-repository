package com.mars.multi.thread.learn.test.executor;

import java.util.concurrent.*;

public class FutureTaskTest {
    private static final ConcurrentHashMap<Object, Future<String>> taskCache = new ConcurrentHashMap<>();

    private static String executionTask(String taskName) {
        while (true) {
            Future<String> future = taskCache.get(taskName);
            if (future == null) {
                Callable<String> task = new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return taskName;
                    }
                };

                FutureTask<String> futureTask = new FutureTask<>(task);
                future = taskCache.putIfAbsent(taskName, futureTask);
                if (future == null) {
                    future = futureTask;
                    futureTask.run();
                }
            }
            try {
                return future.get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
                taskCache.remove(taskName, future);
            }
        }
    }

    public static void main(String[] args) {
        ScheduledExecutorService executors = new ScheduledThreadPoolExecutor(5);

        ScheduledFuture<String> task = executors.schedule(new Callable<String>() {
            @Override
            public String call() throws Exception {

                System.out.println(Thread.currentThread().getName() + ",任务一");
                try {
                    TimeUnit.MINUTES.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + ",任务一结束");


                return "hahahh1";
            }
        }, 10, TimeUnit.SECONDS);

        taskCache.put("task1", task);

        ScheduledFuture<String> task2 = executors.schedule(new Callable<String>() {
            @Override
            public String call() throws Exception {

                System.out.println(Thread.currentThread().getName() + ",任务一");
                try {
                    TimeUnit.MINUTES.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + ",任务一结束");


                return "hahahh2";
            }
        }, 10, TimeUnit.SECONDS);

        taskCache.put("task2", task2);

        ScheduledFuture<?> task3 = executors.schedule(new Runnable() {
            @Override
            public void run() {


                System.out.println(Thread.currentThread().getName() + ",任务二");
                try {
                    TimeUnit.MINUTES.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + ",任务二结束");


            }
        }, 10, TimeUnit.SECONDS);

        taskCache.put((Object) "task3", (Future<String>) task3);


        while (true) {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(executionTask("task1"));
            System.out.println(executionTask("task2"));
        }
    }


}
