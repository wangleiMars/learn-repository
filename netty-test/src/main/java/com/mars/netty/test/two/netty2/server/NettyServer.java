package com.mars.netty.test.two.netty2.server;

import com.mars.netty.test.two.netty2.constants.Constants;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class NettyServer {


    public static void main(String[] args) {
        start();
    }

    public static void start(){
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {

                            /*
                             * 前4B的int类型作为长度解码器，
                             * 第一个参数：包的最大长度，
                             * 第二个参数：长度偏移量，编码长度值在最前面无偏移，设置为0
                             * 第三个参数：长度值所占的字节数
                             * 第四个参数：长度值的调节，假设请求包的大小是20B，若长度值不包含本身则应该是20B，若长度值包含本身应该是24B,需要调整4个字节
                             * 第五个参数：解析时需要跳过的字节数
                             */
                            socketChannel.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                            socketChannel.pipeline().addLast(new StringDecoder());
                            socketChannel.pipeline().addLast(new NettyServerHandler());
                            /**
                             * 消息体前面新增4个字节长度值，第一个是参数是长度值所占用的字节数，第二个参数长度值调节，表明是否包含长度值本身
                             */
                            socketChannel.pipeline().addLast(new LengthFieldPrepender(4, false));
                            socketChannel.pipeline().addLast(new StringEncoder());
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(Constants.port).sync();

           /** CuratorFramework curatorFramework = ZookeeperFactory.create();
            InetAddress inetAddress = InetAddress.getLocalHost();
            Stat stat = curatorFramework.checkExists().forPath(Constants.SERVER_PATH);
            if(stat==null){
                curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(Constants.SERVER_PATH,"0".getBytes());
            }
            curatorFramework.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                    .forPath(Constants.SERVER_PATH+"/"+inetAddress.getHostAddress()+"#"+Constants.port+"#"+Constants.weight+"#");
            ServerWatcher.serverKey = inetAddress.getHostAddress()+Constants.port+Constants.weight;
            curatorFramework.getChildren().usingWatcher(ServerWatcher.getInstance()).forPath(Constants.SERVER_PATH);*/

            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
