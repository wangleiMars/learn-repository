package com.mars.netty.test.two.netty2.constants;

public class Constants {
    public static final String SERVER_PATH = "/netty-test";
    public static int port = 8991;
    public static String zookeeperURL="localhost:2181";
    public static int weight=1;
    static{
//        port = PropertyUtil.getInt("server.port");
//        zookeeperURL = PropertyUtil.getString("zookeeper.url");
//        weight = PropertyUtil.getInt("server.weight");
    }
}
