package com.mars.netty.test.two.camouflagenio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class CamouflagTimeServer {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("time server is start port " + port);
            Socket socket = null;
            CamouflagTimeServerExecutePool camouflagTimeServerExecutePool = new CamouflagTimeServerExecutePool(50, 10000);
            while (true) {
                socket = serverSocket.accept();

                camouflagTimeServerExecutePool.execute(new CamouflagTimeServerHandler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                serverSocket.close();
            }
        }
    }
}
