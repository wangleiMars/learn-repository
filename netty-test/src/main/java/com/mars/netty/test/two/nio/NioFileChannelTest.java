package com.mars.netty.test.two.nio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.TimeUnit;

public class NioFileChannelTest {
    public static void main(String[] args) throws IOException, InterruptedException {

        FileOutputStream fileOutputStream = new FileOutputStream(new File("/Users/wanglei/Downloads/test.txt"));
        FileChannel fileChannel = fileOutputStream.getChannel();
//        try {
//            ByteBuffer byteBuffer = ByteBuffer.wrap("abcde".getBytes());
//            System.out.println("A filechannel.position:"+fileChannel.position());
//            System.out.println("write() 1 返回值："+fileChannel.write(byteBuffer));
//            System.out.println("B filechannel.position:"+fileChannel.position());
//            fileChannel.position(2);
//            byteBuffer.rewind();
//            System.out.println("write() 2 返回值："+fileChannel.write(byteBuffer));
//            System.out.println("c filechannel.position:"+fileChannel.position());
//        }catch (IOException e){
//            e.printStackTrace();
//        }finally {
//            fileOutputStream.close();
//            fileChannel.close();
//        }
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
                ByteBuffer byteBuffer = ByteBuffer.wrap("abcde\r\n".getBytes());
                try {
                    fileChannel.write(byteBuffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            int finalI = i;
            Thread thread2 = new Thread(() -> {
                ByteBuffer byteBuffer = ByteBuffer.wrap(("哈喽你好" + finalI + "\r\n").getBytes());
                try {
                    fileChannel.write(byteBuffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
            thread2.start();
        }
        TimeUnit.MINUTES.sleep(1);
        fileOutputStream.close();
        fileChannel.close();
    }
}
