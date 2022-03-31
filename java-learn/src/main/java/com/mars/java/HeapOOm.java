package com.mars.java;

import java.util.ArrayList;
import java.util.List;

/**
 * -Xms5m -Xmx5m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./heapdump.hprof
 */
public class HeapOOm {

    static class OOMObject{

    }

    public static void main(String[] args) {
        List<OOMObject> l = new ArrayList<>();
        while (true){
            l.add(new OOMObject());
        }
    }
}
