package com.mars.netty.test.two.netty2.client;

import com.alibaba.fastjson.JSONObject;
import com.mars.netty.test.two.netty2.bean.RequestFuture;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyClient {
    private static EventLoopGroup group = new NioEventLoopGroup(100);

    public static Bootstrap getBootstrap(){
        Bootstrap  bootstrap = new Bootstrap();
        group = new NioEventLoopGroup();
        bootstrap.group(group);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
//        Promise<Response> promise = new DefaultPromise<>(group.next());
        final NettyClientHandler handler = new NettyClientHandler();
//        handler.setPromis/e(promise);
        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                ch.pipeline().addLast(new StringDecoder());
                ch.pipeline().addLast(handler);
                ch.pipeline().addLast(new LengthFieldPrepender(4, false));
                ch.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
            }
        });
        return bootstrap;

    }

    public static Object sendRequest(Object msg) throws Exception {
        RequestFuture requestFuture = new RequestFuture();
//        requestFuture.setId(1);
        requestFuture.setRequest(msg);
        requestFuture.setPath("getUserNameById");
        String requsetStr = JSONObject.toJSONString(requestFuture);
        ChannelFuture future =ChannelFutureManager.get();
        future.channel().writeAndFlush(requsetStr);
        Object response = requestFuture.get();
        return response;
    }

    public static Object sendRequest(RequestFuture msg) throws Exception {
        String requsetStr = JSONObject.toJSONString(msg);
        ChannelFuture future =ChannelFutureManager.get();
        future.channel().writeAndFlush(requsetStr);
        Object response = msg.get();
        return response;
    }

    public static void main(String[] args) throws Exception {
        NettyClient nettyClient = new NettyClient();
        for (int i = 1; i < 100; i++) {
            log.info("响应信息:{}", nettyClient.sendRequest("id:" + i));
        }
    }
}
