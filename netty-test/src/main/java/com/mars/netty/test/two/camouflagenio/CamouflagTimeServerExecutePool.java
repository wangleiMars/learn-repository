package com.mars.netty.test.two.camouflagenio;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CamouflagTimeServerExecutePool {
    private ExecutorService executorService;
    public CamouflagTimeServerExecutePool(int maxPoolSize,int queueSize){
        executorService = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),maxPoolSize,
                120l, TimeUnit.SECONDS,new ArrayBlockingQueue<>(queueSize));
    }
    public void execute(Runnable runnable){
        executorService.execute(runnable);
    }
}
