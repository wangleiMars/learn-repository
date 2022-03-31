package com.mars.netty.test.two.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class NettyTimeClient {
    public void bind(int port) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(bossGroup).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
//                            ByteBuf byteBuf = Unpooled.copiedBuffer("$_".getBytes());//$_ 作为分隔符
//                            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,byteBuf));
//            ch.pipeline().addLast(new LineBasedFrameDecoder(1024));// 换行符作为分隔符
                            ch.pipeline().addLast(new FixedLengthFrameDecoder(20));
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new NettyTimeClientHandler());
                        }
                    });
            ChannelFuture f = b.connect("127.0.0.1", port).sync();
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();//优雅关闭
        }
    }


    public static void main(String[] args) throws Exception {
        new NettyTimeClient().bind(8080);
    }
}
