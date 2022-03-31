package com.mars.netty.test.two.nio;

import java.io.IOException;

public class NioTimeClient {
    public static void main(String[] args) throws IOException {
        int port = 19090;
        new Thread(new TimeClientHandle("127.0.0.1", port)).start();
    }
}
