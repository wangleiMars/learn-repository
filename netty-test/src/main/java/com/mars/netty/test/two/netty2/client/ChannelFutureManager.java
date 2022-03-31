package com.mars.netty.test.two.netty2.client;

import com.mars.netty.test.two.netty2.zk.ServerChanageWatcher;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class ChannelFutureManager {
    //服务器地址列表，由于这list涉及到多线程读写，
    //主要是Zookeeper的监听类监听到服务器列表发生变化时
    //会去修改，因此不能使用ArrayList
    public static CopyOnWriteArrayList<String> serverList = new CopyOnWriteArrayList<String>();

    public static final AtomicInteger position = new AtomicInteger(0);

    //与服务器构建连接的ChannelFuture列表，
    //此列表也涉及到多线程的读写
    private static volatile CopyOnWriteArrayList<ChannelFuture> channelFutures = new CopyOnWriteArrayList<>();

    /**
     * 从ChannelFutureManager中获取ChannelFuture
     * 假如未获取到时,通过初始化Zookeeper注册的服务器列表
     * 再次去获取
     */
    public static ChannelFuture get() throws Exception {
        ChannelFuture channelFuture = get(position);
        if (channelFuture == null) {
            log.info("==========进入初始化连接=get=====");
            //初始化Zookeeper注册的服务器列表，
            //在应用刚刚启动时可能会被调用到
            ServerChanageWatcher.initChannelFuture();
        }
        return get(position);
    }

    /**
     * 从channelFutures中获取channelFuture
     *
     * @param i
     * @return
     */
    private static ChannelFuture get(AtomicInteger i) {
        int size = channelFutures.size();
        if (size == 0) {
            return null;
        }
        ChannelFuture channel = null;
        synchronized (i) {
            //当下标为列表大小时需要变回0
            if (i.get() >= size) {
                channel = channelFutures.get(0);
                i.set(0);
            } else {
                //每次获取完后，下标+1
                channel = channelFutures.get(i.getAndIncrement());
            }
            //假如当前get的channel不可用时，
            // 需要移除，再次获取
            if (!channel.channel().isActive()) {
                channelFutures.remove(channel);
                return get(position);
            }
        }
        return channel;
    }

    public static void removeChannel(ChannelFuture channel) {
        channelFutures.remove(channel);
    }

    public static void add(ChannelFuture channel) {
        channelFutures.add(channel);
    }

    public static void addAll(List<ChannelFuture> channels) {
        channelFutures.addAll(channels);
    }

    public static void clear() {
        for (ChannelFuture future : channelFutures) {
            future.channel().close();
        }
        channelFutures.clear();
    }
}
