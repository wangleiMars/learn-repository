package com.mars.multi.thread.learn.test.forkjoin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class CreateFileNotReturnTask extends RecursiveAction {

    private volatile List<FileBean> list;

    public CreateFileNotReturnTask(List<FileBean> list) {
        this.list = list;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        List<FileBean> list = new ArrayList<>();
        FileBean fileBean = null;
        for (int i = 0; i < 100; i++) {
            fileBean = new FileBean();
            fileBean.setA("a" + i);
            fileBean.setB("b" + i);
            fileBean.setC("c" + i);
            fileBean.setD("d" + i);
            list.add(fileBean);
        }

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        CreateFileNotReturnTask task = new CreateFileNotReturnTask(list);
        Future result = forkJoinPool.submit(task);
        TimeUnit.SECONDS.sleep(1);
        System.out.println(forkJoinPool.getStealCount());
        System.out.println(forkJoinPool.awaitQuiescence(1, TimeUnit.MINUTES));
    }

    @Override
    protected void compute() {
        FileBean fileBean = new FileBean();
        if (list.size() <= 10) {
            fileBean = doFile(list, fileBean);
            System.out.println(fileBean);
        } else {

            int middle = (0 + list.size()) / 2;

            CreateFileNotReturnTask leftTask = new CreateFileNotReturnTask(list.subList(0, middle));
            CreateFileNotReturnTask rightTask = new CreateFileNotReturnTask(list.subList(middle, list.size()));
            leftTask.fork();
            rightTask.fork();
        }
    }

    private FileBean doFile(List<FileBean> list, FileBean fileBean) {
        try {
            TimeUnit.SECONDS.sleep(2);
            System.out.println("Thread:" + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (FileBean fileBeanFor : list) {
            fileBean.setA(((null == fileBean.getA() && "".equals(fileBean.getA())) ? "" : fileBean.getA()) + "," + fileBeanFor.getA());
            fileBean.setB(fileBean.getB() + "," + fileBeanFor.getB());
            fileBean.setC(fileBean.getC() + "," + fileBeanFor.getC());
            fileBean.setD(fileBean.getD() + "," + fileBeanFor.getD());
        }
        return fileBean;
    }
}
