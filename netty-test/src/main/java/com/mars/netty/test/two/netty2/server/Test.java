package com.mars.netty.test.two.netty2.server;

import io.netty.channel.ChannelOption;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Test {
    private static final Map.Entry<ChannelOption<?>, Object>[] EMPTY_OPTION_ARRAY = new Map.Entry[0];

    public static void main(String[] args) {

        Map<ChannelOption<?>, Object> options = new HashMap<>();
        options.put(ChannelOption.SO_BACKLOG, 128);
        options.put(ChannelOption.SO_TIMEOUT, 128);
        Map.Entry<ChannelOption<?>, Object>[] map = new LinkedHashMap<ChannelOption<?>, Object>(options).entrySet().toArray(EMPTY_OPTION_ARRAY);
        System.out.println(map.length);
        int i =0;
        for (Map.Entry<ChannelOption<?>, Object> channelOptionObjectEntry : map) {
            System.out.println(i++);
            System.out.println(channelOptionObjectEntry.getKey());
        }
        System.out.println(EMPTY_OPTION_ARRAY.length);
    }
}
