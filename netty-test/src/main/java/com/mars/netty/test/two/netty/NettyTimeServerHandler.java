package com.mars.netty.test.two.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.StandardCharsets;
import java.util.Date;

public class NettyTimeServerHandler extends ChannelHandlerAdapter {
    private int counter;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ByteBuf byteBuffer = (ByteBuf) msg;
//        byte[] req = new byte[byteBuffer.readableBytes()];
//        byteBuffer.readBytes(req);
//        String body = new String(req, StandardCharsets.UTF_8);
        String body = (String) msg;
        System.out.println("The time server receive order:" + body + " ;the counter is :" + (++counter));
        String currentTime = "ORDER TIME ORDER".equalsIgnoreCase(body) ? new Date(System.currentTimeMillis()).toString() : "BAD ORDER";
//        currentTime =  currentTime+System.getProperty("line.separator");
//        currentTime +="$_";
        ByteBuf rspByteBuf = Unpooled.copiedBuffer(currentTime.getBytes(StandardCharsets.UTF_8));
        ctx.writeAndFlush(rspByteBuf);
    }

    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }


}
