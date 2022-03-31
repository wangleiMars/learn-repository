package com.mars.netty.test.two.reactor;

public class ReactorServer {
    public static void main(String[] args) throws Exception {
        new Thread(new Reactor(19090)).start();
    }
}
