package com.mars.netty.test.two.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

class Reactor implements Runnable {
    final Selector selector;
    final ServerSocketChannel serverSocket;

    Reactor(int port) throws IOException { //Reactor初始化
        selector = Selector.open();
        serverSocket = ServerSocketChannel.open();
        //非阻塞
        serverSocket.configureBlocking(false);
        serverSocket.socket().bind(new InetSocketAddress(port),1024);

        //分步处理,第一步,接收accept事件
        SelectionKey sk = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        //attach callback object, Acceptor
        sk.attach(new Acceptor());
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                System.out.println("Reactor.run" + Thread.currentThread().getId());
                selector.select();
                Set selected = selector.selectedKeys();
                Iterator it = selected.iterator();
                while (it.hasNext()) {
                    //Reactor负责dispatch收到的事件
                    dispatch((SelectionKey) (it.next()));
                }
                selected.clear();
            }
        } catch (IOException ex) { /* ... */ }
    }

    void dispatch(SelectionKey k) {
        System.out.println("dispatch" + Thread.currentThread().getId());
        Runnable r = (Runnable) (k.attachment());
        //调用之前注册的callback对象
        if (r != null) {
            r.run();
        }
    }

    // inner class
    class Acceptor implements Runnable {
        public void run() {
            System.out.println("Acceptor" + Thread.currentThread().getId());
            try {
                SocketChannel channel = serverSocket.accept();
                if (channel != null) {
                    System.out.println("Acceptor2" + Thread.currentThread().getId());
                    new Handler(selector, channel);
                }
            } catch (IOException ex) { /* ... */ }
        }
    }
}