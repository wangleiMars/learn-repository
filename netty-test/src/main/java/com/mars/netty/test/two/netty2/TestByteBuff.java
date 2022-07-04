package com.mars.netty.test.two.netty2;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class TestByteBuff {
    public static void main(String[] args) {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        byteBuf.writeLong(1);
        System.out.println("capacity:" + byteBuf.capacity() + ",readerIndex:" + byteBuf.readerIndex() + ",writerIndex:" + byteBuf.writerIndex());
//        long l = byteBuf.readLong();
//        System.out.println(l);

        byte[] bytes = new byte[8];
        byteBuf.readBytes(bytes);

        System.out.println(new String(bytes));

        ByteBuffer buffer = ByteBuffer.wrap(bytes, 0, 8);
        // ByteBuffer.order(ByteOrder) 方法指定字节序,即大小端模式(BIG_ENDIAN/LITTLE_ENDIAN)
        // ByteBuffer 默认为大端(BIG_ENDIAN)模式
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        System.out.println(buffer.getLong());

    }
}
