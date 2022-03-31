package com.mars.netty.test.two.netty2.zk;

import com.mars.netty.test.two.netty2.constants.Constants;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class ZookeeperFactory {
    private static CuratorFramework curatorFramework;

    private ZookeeperFactory() {
    }

    public static CuratorFramework create(){
        if(curatorFramework==null){
            RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);
            curatorFramework = CuratorFrameworkFactory.newClient("localhost:2181",1000,5000,retryPolicy);
            curatorFramework.start();
        }
        return curatorFramework;
    }
    public static CuratorFramework recreate(){
        curatorFramework = null;
        create();
        return curatorFramework;
    }

    public static void main(String[] args) throws Exception {
        CuratorFramework c = create();
        c.create().forPath(Constants.SERVER_PATH);
    }
}
