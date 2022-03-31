package com.mars.netty.test.two.netty2;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

public class WeekReferemce {
    public static void main(String[] args) {

        String str = new String("内存泄漏测试");
        ReferenceQueue queue = new ReferenceQueue();
        WeakReference weakReference = new WeakReference(str,queue);
        str=null;
        System.out.println(queue.poll());
        System.gc();
        System.runFinalization();
        System.out.println(queue.poll()==weakReference);
    }
}
