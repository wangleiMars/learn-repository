package com.mars.netty.test.two.reactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class MthreadHandler implements Runnable {
    final SocketChannel channel;
    final SelectionKey selectionKey;
    ByteBuffer input = ByteBuffer.allocate(1024);
    ByteBuffer output = ByteBuffer.allocate(1024);
    static final int READING = 0, SENDING = 1;
    int state = READING;


    ExecutorService pool = Executors.newFixedThreadPool(2);
    static final int PROCESSING = 3;

    MthreadHandler(Selector selector, SocketChannel c) throws IOException {
        channel = c;
        c.configureBlocking(false);
        // Optionally try first read now
        selectionKey = channel.register(selector, 0);

        //将Handler作为callback对象
        selectionKey.attach(this);

        //第二步,注册Read就绪事件
        selectionKey.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
    }

    boolean inputIsComplete() {
        /* ... */
        return true;
    }

    boolean outputIsComplete() {

        /* ... */
        return false;
    }

    void process() {
        System.out.println("process" + Thread.currentThread().getId());
        return;
    }

    public void run() {
        try {
            if (state == READING) {
                System.out.println("read" + Thread.currentThread().getId());
                read();
            } else if (state == SENDING) {
                System.out.println("send" + Thread.currentThread().getId());
                send();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    synchronized void read() throws IOException {
        // ...
        channel.read(input);
        if (inputIsComplete()) {
            state = PROCESSING;
            //使用线程pool异步执行
            pool.execute(new Processer());
        }
    }

    void send() throws IOException {
        System.out.println("send" + Thread.currentThread().getId());
        channel.write(output);

        //write完就结束了, 关闭select key
        if (outputIsComplete()) {
            selectionKey.cancel();
        }
    }

    synchronized void processAndHandOff() {
        System.out.println("processAndHandOff" + Thread.currentThread().getId());
        process();
        state = SENDING;
        // or rebind attachment
        //process完,开始等待write就绪
        selectionKey.interestOps(SelectionKey.OP_WRITE);
    }

    class Processer implements Runnable {
        public void run() {
            processAndHandOff();
        }
    }

}

