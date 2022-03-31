package com.mars.netty.test.two.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class NettyTimeServer {
    public void bind(int port) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChildChannelHandler());
            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();//优雅关闭
            workerGroup.shutdownGracefully();
        }
    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
        protected void initChannel(SocketChannel ch) throws Exception {
//            ByteBuf byteBuf = Unpooled.copiedBuffer("$_".getBytes());//$_ 作为分隔符
//            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,byteBuf));//指定分隔符
//            ch.pipeline().addLast(new LineBasedFrameDecoder(1024));// 换行符作为分隔符
            ch.pipeline().addLast(new FixedLengthFrameDecoder(20));
            ch.pipeline().addLast(new StringDecoder());
            ch.pipeline().addLast(new NettyTimeServerHandler());
        }
    }

    public static void main(String[] args) throws Exception {
        new NettyTimeServer().bind(8080);
    }
}
