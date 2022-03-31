package com.mars.netty.test.two.netty2.zk;

import cn.hutool.core.date.DateTime;
import com.mars.netty.test.two.netty2.constants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;

import java.net.InetAddress;

@Slf4j
public class ServerWatcher implements CuratorWatcher {
    public static String serverKey = "";
    public static ServerWatcher serverWatcher = null;

    public static ServerWatcher getInstance() {
        if (serverWatcher == null) {
            serverWatcher = new ServerWatcher();
        }
        return serverWatcher;
    }

    @Override
    public void process(WatchedEvent watchedEvent) throws Exception {
        log.info("==========服务器监听 Zookeeper，event.start:{},时间:{}==========", watchedEvent.getState(), DateTime.now().toString());
        if (watchedEvent.getState().equals(Watcher.Event.KeeperState.Disconnected) || watchedEvent.getState().equals(Watcher.Event.KeeperState.Expired)) {
            try {
                try {
                    //先尝试去关闭旧的连接
                    ZookeeperFactory.create().close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                CuratorFramework curatorFramework = ZookeeperFactory.recreate();
                InetAddress inetAddress = InetAddress.getLocalHost();
//                client.getChildren().usingWatcher(this).forPath(Constants.SERVER_PATH);
                Stat stat = curatorFramework.checkExists().forPath(Constants.SERVER_PATH);
                if(stat==null){
                    curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(Constants.SERVER_PATH,"0".getBytes());
                }
                curatorFramework.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                        .forPath(Constants.SERVER_PATH+"/"+inetAddress.getHostAddress()+"#"+Constants.port+"#"+Constants.weight+"#");
                ServerWatcher.serverKey = inetAddress.getHostAddress()+Constants.port+Constants.weight;
                curatorFramework.getChildren().usingWatcher(ServerWatcher.getInstance()).forPath(Constants.SERVER_PATH);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            CuratorFramework client = ZookeeperFactory.create();
            client.getChildren().usingWatcher(ServerWatcher.getInstance()).forPath(Constants.SERVER_PATH);
        }
    }
}
