package com.mars.multi.thread.learn.test.pool;

public interface ThreadPool<T extends Runnable> {
    /**
     * 执行
     *
     * @param job
     */
    void execute(T job);

    /**
     * 停止
     */
    void shutdown();

    /**
     * 增加工作者
     *
     * @param num
     */
    void addWorks(int num);

    /**
     * 减少工作者
     *
     * @param num
     */
    void removeWorks(int num);

    /**
     * 获取等待执行的任务数量
     *
     * @return
     */
    int getJobSize();
}
