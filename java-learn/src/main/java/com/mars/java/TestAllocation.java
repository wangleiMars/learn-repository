package com.mars.java;

public class TestAllocation {
    private static final int _1MB = 1024*1024;

    /**
     * -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:+UseSerialGC
     * -verbose:gc 拟机发生内存回收时在输出设备显示信息
     * -Xms20M 初始分配的内存
     * -Xmx20M 最大堆内存
     * -Xmn10M 年轻代大小
     * -XX:+PrintGCDetails 打印gc详情
     * -XX:SurvivorRatio=8 年轻代中Eden区与两个Survivor区的比值。注意Survivor区有两个。如：3，表示Eden：Survivor=3：2，一个Survivor区占整个年轻代的1/5
     * -XX:+UseSerialGC
     */
    public static void testallocation(){
        byte[] allocation1,allocation2,allocation3,allocation4;
        allocation1 = new byte[2*_1MB];
        allocation2 = new byte[2*_1MB];
        allocation3 = new byte[2*_1MB];
        allocation4 = new byte[4*_1MB];
//        System.gc();
    }

    /**
     * -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:+UseSerialGC -XX:PretenureSizeThreshold=3M
     * -verbose:gc 拟机发生内存回收时在输出设备显示信息
     * -Xms20M 初始分配的内存
     * -Xmx20M 最大堆内存
     * -Xmn10M 年轻代大小
     * -XX:+PrintGCDetails 打印gc详情
     * -XX:SurvivorRatio=8 年轻代中Eden区与两个Survivor区的比值。注意Survivor区有两个。如：3，表示Eden：Survivor=3：2，一个Survivor区占整个年轻代的1/5
     * -XX:+UseSerialGC
     * -XX:PretenureSizeThreshold 指定大于该设置值的对象直接在老年代分配
     */
    public static void testallocation2(){
        byte[] allocation1 ;
        allocation1 = new byte[4*1024];
    }
    /**
     * -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:+UseSerialGC -XX:MaxTenuringThreshold=1 -XX:+PrintTenuringDistribution
     * -verbose:gc 拟机发生内存回收时在输出设备显示信息
     * -Xms20M 初始分配的内存
     * -Xmx20M 最大堆内存
     * -Xmn10M 年轻代大小
     * -XX:+PrintGCDetails 打印gc详情
     * -XX:SurvivorRatio=8 年轻代中Eden区与两个Survivor区的比值。注意Survivor区有两个。如：3，表示Eden：Survivor=3：2，一个Survivor区占整个年轻代的1/5
     * -XX:+UseSerialGC
     * -XX:PretenureSizeThreshold 指定大于该设置值的对象直接在老年代分配
     * -XX:+PrintTenuringDistribution参数作用：JVM 在每次新生代GC时，打印出幸存区中对象的年龄分布。
     */
    public static void testallocation3(){
        byte[] allocation1,allocation2,allocation3;
        allocation1 = new byte[_1MB/4];
        allocation2 = new byte[4*_1MB];
        allocation3 = new byte[4*_1MB];
        allocation3 = null;
        allocation3 = new byte[4*_1MB];
    }


    public static void main(String[] args) {
        testallocation3();
    }
}
