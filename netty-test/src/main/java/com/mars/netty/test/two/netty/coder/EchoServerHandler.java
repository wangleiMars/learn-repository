package com.mars.netty.test.two.netty.coder;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class EchoServerHandler extends ChannelHandlerAdapter {
    private int counter;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        UserInfo body = (UserInfo) msg;
        System.out.println("The time server receive order:" + body + " ;the counter is :" + (++counter));
    }

    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}