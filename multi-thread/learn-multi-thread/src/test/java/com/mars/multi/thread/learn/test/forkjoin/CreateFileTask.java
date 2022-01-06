package com.mars.multi.thread.learn.test.forkjoin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

public class CreateFileTask extends RecursiveTask<FileBean> {

    private volatile List<FileBean> list;

    public CreateFileTask(List<FileBean> list) {
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
        CreateFileTask task = new CreateFileTask(list);
        Future<FileBean> result = forkJoinPool.submit(task);
        System.out.println(result.get());
    }

    @Override
    protected FileBean compute() {
        FileBean fileBean = new FileBean();
        if (list.size() <= 10) {
            fileBean = doFile(list, fileBean);
        } else {

            int middle = (0 + list.size()) / 2;

            CreateFileTask leftTask = new CreateFileTask(list.subList(0, middle));
            CreateFileTask rightTask = new CreateFileTask(list.subList(middle, list.size()));
            leftTask.fork();
            rightTask.fork();
            FileBean leftResult = leftTask.join();
            FileBean rightResult = rightTask.join();

            fileBean.setA(fileBean.getA() + "," + leftResult.getA());
            fileBean.setB(fileBean.getB() + "," + leftResult.getB());
            fileBean.setC(fileBean.getC() + "," + leftResult.getC());
            fileBean.setD(fileBean.getD() + "," + leftResult.getD());

            fileBean.setA(fileBean.getA() + "," + rightResult.getA());
            fileBean.setB(fileBean.getB() + "," + rightResult.getB());
            fileBean.setC(fileBean.getC() + "," + rightResult.getC());
            fileBean.setD(fileBean.getD() + "," + rightResult.getD());
        }
        return fileBean;
    }

    private FileBean doFile(List<FileBean> list, FileBean fileBean) {
        try {
            Thread.sleep(2);
            System.out.println("Thread:" + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (FileBean fileBeanFor : list) {
            fileBean.setA(fileBean.getA() + "," + fileBeanFor.getA());
            fileBean.setB(fileBean.getB() + "," + fileBeanFor.getB());
            fileBean.setC(fileBean.getC() + "," + fileBeanFor.getC());
            fileBean.setD(fileBean.getD() + "," + fileBeanFor.getD());
        }
        return fileBean;
    }
}
