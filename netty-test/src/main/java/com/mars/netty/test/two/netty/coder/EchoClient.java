package com.mars.netty.test.two.netty.coder;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class EchoClient {
    private final String host;
    private final int port;
    private final int sendNumber;

    public EchoClient(String host, int port, int sendNumber) {
        this.host = host;
        this.port = port;
        this.sendNumber = sendNumber;
    }

    public void run() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
//                            ch.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65535,0,2,0,2));
                            ch.pipeline().addLast("msgpack decoder", new MsgPackDecoder());
//                            ch.pipeline().addLast("frameEncoder",new LengthFieldPrepender(2));
                            ch.pipeline().addLast("msgpack encoder", new MsgPackEncoder());

//                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new EchoClientHandler(sendNumber));
                        }
                    });
            ChannelFuture f = b.connect("127.0.0.1", port).sync();
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();//优雅关闭
        }
    }

    public static void main(String[] args) throws Exception {
        new EchoClient("127.0.0.1", 9090, 1).run();
    }
}
