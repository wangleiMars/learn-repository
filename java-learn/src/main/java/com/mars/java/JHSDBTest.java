package com.mars.java;

import java.util.concurrent.TimeUnit;

public class JHSDBTest {
    static class Test{
        static ObjectHolder instanceObj = new ObjectHolder();

        void foo() throws InterruptedException {
            ObjectHolder instanceObj = new ObjectHolder();
            TimeUnit.SECONDS.sleep(100000);
            System.out.println("done");
        }
    }
    private static class ObjectHolder{}

    /**
     * -Xmx10m -XX:+UseSerialGC -XX:-UseCompressedOops
     * 关闭(-XX:-UseCompressedOops) 可以关闭压缩指针，对象头16字节(klass pointer 8字节)reference 8字节
     * 开启(-XX:+UseCompressedOops) 可以压缩指针对象头12字节(klass pointer 4字节)  reference 4字节
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        Test test = new JHSDBTest.Test();
        test.foo();
    }

}
