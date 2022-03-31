package com.mars.netty.test.two.netty2.zk;

import com.mars.netty.test.two.netty2.client.ChannelFutureManager;
import com.mars.netty.test.two.netty2.client.NettyClient;
import com.mars.netty.test.two.netty2.constants.Constants;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ServerChanageWatcher implements CuratorWatcher {

    private static  ServerChanageWatcher serverChanageWatcher = null;

    private ServerChanageWatcher() {
    }

    public static final int SERVER_COUNT = 100;

    public static ServerChanageWatcher getInstance(){
        if(serverChanageWatcher==null){
            serverChanageWatcher = new ServerChanageWatcher();
        }
        return serverChanageWatcher;
    }

    @Override
    public synchronized void process(WatchedEvent watchedEvent) throws Exception {
        log.info("process客户端监控开始，watch.stat:{}",watchedEvent.getState());
        if(watchedEvent.getState().equals(Watcher.Event.KeeperState.Disconnected)
                || watchedEvent.getState().equals(Watcher.Event.KeeperState.Expired)) {
            CuratorFramework client = ZookeeperFactory.create();
            client.getChildren().usingWatcher(this).forPath(Constants.SERVER_PATH);
            return ;
        }else if(watchedEvent.getState().equals(Watcher.Event.KeeperState.SyncConnected) //重新连上，并未过期
                && !watchedEvent.getState().equals(Watcher.Event.EventType.NodeChildrenChanged)){
            CuratorFramework client = ZookeeperFactory.create();
            client.getChildren().usingWatcher(this).forPath(Constants.SERVER_PATH);
            return ;
        }
        log.info("=========重新初始化服务端连接=========");
        CuratorFramework client = ZookeeperFactory.create();
        client.getChildren().usingWatcher(this).forPath(Constants.SERVER_PATH);
        List<String> serverPaths = client.getChildren().forPath(Constants.SERVER_PATH);
        List<String> servers = new ArrayList<>();
        for (String serverPath:serverPaths){
            log.info("===服务器链表=="+serverPath);
            String[] str = serverPath.split("#");
            int weight = Integer.valueOf(str[2]);
            //不同server，其权重值可能不同
            //此处权重做了简单处理，为1时构建SERVER_COUNT个连接，2翻倍，依此类崔
            if(weight>0){
                for(int w=0;w<=weight*SERVER_COUNT;w++){
                    servers.add(str[0]+"#"+str[1]);
                }
            }
        }
        ChannelFutureManager.serverList.clear();
        ChannelFutureManager.serverList.addAll(servers);
        List<ChannelFuture> futures = new ArrayList<>();
        for (String s : ChannelFutureManager.serverList) {
            String[] str = s.split("#");
            try {
                ChannelFuture channelFuture = NettyClient.getBootstrap().connect(str[0],Integer.parseInt(str[1])).sync();
                futures.add(channelFuture);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        synchronized (ChannelFutureManager.position){
            ChannelFutureManager.clear();
            ChannelFutureManager.addAll(futures);
        }
    }

    public static void initChannelFuture()throws Exception{
        CuratorFramework client = ZookeeperFactory.create();
        List<String> servers = client.getChildren().forPath(Constants.SERVER_PATH);
        log.info("初始化服务连接");
        for (String server : servers) {
            String[] str = server.split("#");
            try {
                int weight = Integer.valueOf(str[2]);
                if(weight>=0){
                    for(int w=0;w<=weight*SERVER_COUNT;w++){
                        ChannelFuture  channelFuture = NettyClient. getBootstrap().connect(str[0], Integer.valueOf(str[1])).sync();
                        ChannelFutureManager.add(channelFuture);;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        client.getChildren().usingWatcher(getInstance()).forPath(Constants.SERVER_PATH);
    }

    public static void main(String[] args) throws Exception {
        initChannelFuture();
    }
}
