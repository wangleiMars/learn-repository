package com.mars.multi.thread.learn.test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 线程的suspend中断，resume恢复，stop中断，api是过时的，过时原因是：suspend调用后，
 * 不会释放已占有的资源，容易引发死锁，stop也不保证终止线程后，能完全释放资源
 */
public class DeprecatedTest {

    public static void main(String[] args) throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Thread thread = new Thread(new Runner(), "PrintThread");
        thread.setDaemon(true);
        thread.start();
        TimeUnit.SECONDS.sleep(3);
        thread.suspend();//暂停
        System.out.println("main suspend PrintThread at " + dateFormat.format(new Date()));
        TimeUnit.SECONDS.sleep(3);
        thread.resume();//恢复
        System.out.println("main resume PrintThread at " + dateFormat.format(new Date()));
        TimeUnit.SECONDS.sleep(3);
        thread.stop();
        System.out.println("main stop PrintThread at " + dateFormat.format(new Date()));
        TimeUnit.SECONDS.sleep(3);
    }

    static class Runner implements Runnable {
        @Override
        public void run() {
            DateFormat format = new SimpleDateFormat("HH:mm:ss");
            while (true) {
                System.out.println(Thread.currentThread().getName() + "Run at" + format.format(new Date()));
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
