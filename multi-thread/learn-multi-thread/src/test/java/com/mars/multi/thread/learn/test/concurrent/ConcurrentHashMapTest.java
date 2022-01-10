package com.mars.multi.thread.learn.test.concurrent;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapTest {

    public static void main(String[] args) {
        ConcurrentHashMap<StringOver, String> map = new ConcurrentHashMap<>();
        for (int i = 0; i < 100; i++) {
            StringOver stringOver = new StringOver("keykey" + String.valueOf(i));
            map.put(stringOver, "value" + i);

        }
        System.out.println(map.size());
    }

}
