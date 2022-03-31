package com.mars.netty.test.two.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.StandardCharsets;

public class NettyTimeClientHandler extends ChannelHandlerAdapter {


    private ByteBuf firstMessage;
    private int counter;
    private byte[] req;

    public NettyTimeClientHandler() {
        req = ("ORDER TIME ORDER" + System.getProperty("line.separator")).getBytes(StandardCharsets.UTF_8);
        req = ("ORDER TIME ORDER" + "$_").getBytes(StandardCharsets.UTF_8);
        req = ("ORDER TIME ORDER11112").getBytes(StandardCharsets.UTF_8);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常");
        cause.printStackTrace();
        ctx.close();
    }


    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 10; i++) {
            firstMessage = Unpooled.buffer(req.length);
            firstMessage.writeBytes(req);
            ctx.writeAndFlush(firstMessage);
        }
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ByteBuf byteBuf = (ByteBuf) msg;
        String body = (String) msg;
        System.out.println("now is :" + body + " ;the counter is :" + (++counter));
//        byte[] rspBytes = new byte[byteBuf.readableBytes()];
//        byteBuf.readBytes(rspBytes);
//        System.out.println("now is " + new String(rspBytes, StandardCharsets.UTF_8));
    }


}
