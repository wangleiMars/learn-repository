package com.mars.java;

public class Bar {
    int a = 1;
    static int b = 2;

    public int sum(int c) {
        return a + b + c;
    }

    /**
     * -XX:+PrintAssembly -Xcomp -XX:+UnlockDiagnosticVMOptions
     * -XX:+PrintAssembly 输出反汇编内容
     * -Xcomp 让虚拟机以编译模式执行代码
     *
     *  -XX:+UnlockDiagnosticVMOptions -XX:+TraceClassLoading -XX:+LogCompilation -XX:LogFile=test.log -XX:+PrintAssembly -XX:+TraceClassLoading
     *
     * @param args
     */
    public static void main(String[] args) {
        new Bar().sum(3);
    }
}
