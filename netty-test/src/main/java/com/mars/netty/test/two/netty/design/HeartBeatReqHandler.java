package com.mars.netty.test.two.netty.design;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class HeartBeatReqHandler extends ChannelHandlerAdapter {
    private volatile ScheduledFuture<?> heartBeat;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        if(heartBeat!=null){
            heartBeat.cancel(true);
             heartBeat = null;
        }
        ctx.fireExceptionCaught(cause);
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage nettyMessage = (NettyMessage) msg;
        if (nettyMessage.getHead() != null && nettyMessage.getHead().getType() == MessageType.LOGIN_RESP.value()) {
            heartBeat = ctx.executor().scheduleAtFixedRate(new HeartBeatTask(ctx), 0, 5000, TimeUnit.MILLISECONDS);

        } else if (nettyMessage.getHead() != null && nettyMessage.getHead().getType() == MessageType.HEARTBEAT_REQ.value()) {
            System.out.println("Client receive server heart beat messgae: ---> " + nettyMessage);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    public class HeartBeatTask implements Runnable {
        private final ChannelHandlerContext ctx;

        public HeartBeatTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            NettyMessage heatBeat = buildHeatBeat();
            ctx.writeAndFlush(heatBeat);
        }

        private NettyMessage buildHeatBeat() {
            NettyMessage nettyMessage = new NettyMessage();
            Head head = new Head();
            head.setType(MessageType.HEARTBEAT_REQ.value());
            nettyMessage.setHead(head);
            return nettyMessage;
        }
    }
}
