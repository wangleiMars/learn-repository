package com.mars.multi.thread.learn.test.concurrent;

import java.util.Map;
import java.util.concurrent.*;

public class BankService implements Runnable {

    private CyclicBarrier cyclicBarrier = new CyclicBarrier(4, this);

    private Executor executor = Executors.newFixedThreadPool(4);

    private ConcurrentHashMap<String, Integer> sheetBankWaterCount = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        BankService bankService = new BankService();
        bankService.count();
//        System.out.println("value:"+bankService.sheetBankWaterCount.values());
    }

    private void count() {
        for (int i = 0; i < 4; i++) {
            int finalI = i;
            executor.execute(() -> {
                sheetBankWaterCount.put(Thread.currentThread().getName(), finalI);
                try {
                    System.out.println("i:" + finalI);
                    cyclicBarrier.await();
                } catch (BrokenBarrierException | InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public void run() {
        int result = 0;
        for (Map.Entry<String, Integer> sheet : sheetBankWaterCount.entrySet()) {
            result += sheet.getValue();
        }
        System.out.println("result:" + result);
        sheetBankWaterCount.put("result", result);
        System.out.println(cyclicBarrier.getNumberWaiting());
        try {
            cyclicBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
