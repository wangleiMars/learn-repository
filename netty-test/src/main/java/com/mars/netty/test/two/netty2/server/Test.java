package com.mars.netty.test.two.netty2.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelOption;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Test {
    private static final Map.Entry<ChannelOption<?>, Object>[] EMPTY_OPTION_ARRAY = new Map.Entry[0];

    public static void main(String[] args) {

//        Map<ChannelOption<?>, Object> options = new HashMap<>();
//        options.put(ChannelOption.SO_BACKLOG, 128);
//        options.put(ChannelOption.SO_TIMEOUT, 128);
//        Map.Entry<ChannelOption<?>, Object>[] map = new LinkedHashMap<ChannelOption<?>, Object>(options).entrySet().toArray(EMPTY_OPTION_ARRAY);
//        System.out.println(map.length);
//        int i =0;
//        for (Map.Entry<ChannelOption<?>, Object> channelOptionObjectEntry : map) {
//            System.out.println(i++);
//            System.out.println(channelOptionObjectEntry.getKey());
//        }
//        System.out.println(EMPTY_OPTION_ARRAY.length);
        byte[] b = "00000009123456789".getBytes(StandardCharsets.UTF_8);
        ByteBuf byteBuf = Unpooled.copiedBuffer(b);
        System.out.println(byteBuf.capacity());
    }
}
