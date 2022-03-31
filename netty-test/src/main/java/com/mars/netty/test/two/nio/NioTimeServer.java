package com.mars.netty.test.two.nio;

public class NioTimeServer {
    public static void main(String[] args) {
        int port =19090;
        MultiplexerTimeServer multiplexerTimeServer = new MultiplexerTimeServer(port);
        new Thread(multiplexerTimeServer,"NIO-MultiplexerTimeServer-001").start();
    }
}
