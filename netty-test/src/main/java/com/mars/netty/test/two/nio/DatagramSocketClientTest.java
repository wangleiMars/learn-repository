package com.mars.netty.test.two.nio;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;
import java.util.List;

public class DatagramSocketClientTest {
    public static void main(String[] args) throws IOException {
        DatagramSocket datagramSocket = new DatagramSocket();
        datagramSocket.setBroadcast(true);
        datagramSocket.connect(new InetSocketAddress("localhost", 8888));
        datagramSocket.connect(InetAddress.getByName("192.168.1.1"), 8888);
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < 65509; i++) {
            str.append("a");
        }
        System.out.println(str.length());
        byte[] bytes = str.toString().getBytes();
        DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length);
        datagramSocket.send(datagramPacket);
        datagramSocket.close();

        Enumeration<NetworkInterface> eum1 = NetworkInterface.getNetworkInterfaces();
        while (eum1.hasMoreElements()) {
            NetworkInterface n = eum1.nextElement();
            System.out.println(n.getName() + " " + n.getDisplayName());
            List<InterfaceAddress> list = n.getInterfaceAddresses();
            for (InterfaceAddress interfaceAddress : list) {
                InetAddress ip = interfaceAddress.getBroadcast();
                if (null != ip) {
                    System.out.print(" " + ip.getHostAddress());
                }
            }
        }

    }
}
