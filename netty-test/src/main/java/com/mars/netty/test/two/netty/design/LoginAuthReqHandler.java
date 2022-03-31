package com.mars.netty.test.two.netty.design;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class LoginAuthReqHandler extends ChannelHandlerAdapter {
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(buildLoginReq());
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage nettyMessage = (NettyMessage) msg;
        if (nettyMessage.getHead() != null && nettyMessage.getHead().getType() == MessageType.LOGIN_REQ.value()) {
            byte longResult = (byte) nettyMessage.getBody();
            if (longResult != (byte) 0) {
                ctx.close();
            } else {
                System.out.println("login is ok:" + nettyMessage);
            }
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    private NettyMessage buildLoginReq() {
        NettyMessage nettyMessage = new NettyMessage();
        Head head = new Head();
        head.setType(MessageType.LOGIN_REQ.value());
        nettyMessage.setHead(head);
        return nettyMessage;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.fireExceptionCaught(cause);
    }
}
