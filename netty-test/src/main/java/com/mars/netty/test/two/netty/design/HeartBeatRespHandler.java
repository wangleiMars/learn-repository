package com.mars.netty.test.two.netty.design;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class HeartBeatRespHandler extends ChannelHandlerAdapter {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("心跳请求");
        NettyMessage nettyMessage = (NettyMessage) msg;
        if (nettyMessage.getHead() != null && nettyMessage.getHead().getType() == MessageType.HEARTBEAT_REQ.value()) {
            System.out.println("Receive client heart beat message:---> " + nettyMessage);
            NettyMessage heartMsg = buildHeartBeat();
            System.out.println("Send Heart beat response message to client:--->" + heartMsg);
            ctx.writeAndFlush(heartMsg);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    private NettyMessage buildHeartBeat() {
        NettyMessage nettyMessage = new NettyMessage();
        Head head = new Head();
        head.setType(MessageType.HEARTBEAT_REQ.value());
        nettyMessage.setHead(head);
        return nettyMessage;
    }
}
