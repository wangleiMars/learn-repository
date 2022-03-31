package com.mars.netty.test.two.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class TimeClient {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        Socket socket = null;
        BufferedReader br = null;
        PrintWriter pw = null;
        try {
            socket = new Socket("127.0.0.1",port);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pw = new PrintWriter(socket.getOutputStream(),true);
            pw.println("ORDER TIME ORDER");
            System.out.println("Send order 2 server succeed");
            String rsp = br.readLine();
            System.out.println("now is :"+rsp);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(pw!=null){
                pw.close();
            }
            if(br!=null){
                br.close();
            }
            if(socket!=null){
                socket.close();
            }
        }
    }
}
