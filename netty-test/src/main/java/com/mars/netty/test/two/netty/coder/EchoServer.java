package com.mars.netty.test.two.netty.coder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EchoServer {
    public void bind(int port) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new EchoServer.ChildChannelHandler());
            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();//优雅关闭
            workerGroup.shutdownGracefully();
        }
    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
        protected void initChannel(SocketChannel ch) throws Exception {
//            ch.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65535,0,2,0,2));
            ch.pipeline().addLast("msgpack decoder", new MsgPackDecoder());
//            ch.pipeline().addLast("frameEncoder",new LengthFieldPrepender(2));
            ch.pipeline().addLast("msgpack encoder", new MsgPackEncoder());

//            ch.pipeline().addLast(new FixedLengthFrameDecoder(20));
//            ch.pipeline().addLast(new StringDecoder());

            ch.pipeline().addLast(new EchoServerHandler());
        }
    }

    public static void main(String[] args) throws Exception {
        new EchoServer().bind(9090);
    }
}
