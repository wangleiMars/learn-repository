package com.mars.netty.test.two.netty.coder;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class EchoClientHandler extends ChannelHandlerAdapter {


    private int sendNumber;

    public EchoClientHandler(int sendNumber) {
        this.sendNumber = sendNumber;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常");
        cause.printStackTrace();
        ctx.close();
    }


    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        for (int i = 0; i < 10; i++) {
            UserInfo userInfo = new UserInfo();
            userInfo.setName("wwww" + 2);
            userInfo.setAge(1);
            ctx.writeAndFlush(userInfo);
//        }
//        ctx.flush();
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        UserInfo body = (UserInfo) msg;
        System.out.println("client recrive  :" + body + " ;the counter is :" + (++sendNumber));
    }

    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
