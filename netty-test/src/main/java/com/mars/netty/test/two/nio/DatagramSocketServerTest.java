package com.mars.netty.test.two.nio;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class DatagramSocketServerTest {
    public static void main(String[] args) throws IOException {
        DatagramSocket datagramSocket=new DatagramSocket(8888);
        byte[] bytes = new byte[65509];
        DatagramPacket myPacket = new DatagramPacket(bytes,bytes.length);
        datagramSocket.receive(myPacket);
        datagramSocket.close();
        System.out.println("包中数据的长度："+myPacket.getLength());
        System.out.println(new String(myPacket.getData(),0,myPacket.getLength()));
    }
}
