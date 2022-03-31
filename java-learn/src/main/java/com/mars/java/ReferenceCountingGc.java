package com.mars.java;

public class ReferenceCountingGc {
    private Object instance = null;
    private static final int _1MB=1024*1024;
    private byte[] bigSize = new byte[2*_1MB];

    public static void main(String[] args) {
        ReferenceCountingGc obja = new ReferenceCountingGc();

        ReferenceCountingGc objb = new ReferenceCountingGc();

        obja.instance=objb;

        objb.instance = obja;

        obja=null;
        objb=null;
        System.gc();
    }
}
