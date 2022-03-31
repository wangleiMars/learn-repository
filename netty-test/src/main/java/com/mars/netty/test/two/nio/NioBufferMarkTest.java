package com.mars.netty.test.two.nio;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class NioBufferMarkTest {
    public static void main(String[] args) {
        byte[] bytes = new byte[]{1, 2, 3};
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        System.out.println("bytebuffer.capacity=" + byteBuffer.capacity());
        byteBuffer.position(1);
        byteBuffer.mark();
        System.out.println("bytebuffer.capacity=" + byteBuffer.position());
        byteBuffer.position(2);
        byteBuffer.reset();
        System.out.println();
        System.out.println("bytebuffer.capacity=" + byteBuffer.position());


        bytes = new byte[]{1, 2, 3, 4, 5, 6, 7, 8};
        ByteBuffer byteBuffer1 = ByteBuffer.wrap(bytes);
        ByteBuffer byteBuffer2 = ByteBuffer.wrap(bytes, 2, 4);
        System.out.println("byteBuffer1 capacity=" + byteBuffer1.capacity() + " limit=" + byteBuffer1.limit() + " capacity=" + byteBuffer1.capacity());
        System.out.println("byteBuffer2 capacity=" + byteBuffer2.capacity() + " limit=" + byteBuffer2.limit() + " capacity=" + byteBuffer2.capacity());

        ByteBuffer byteBuffer3 = ByteBuffer.allocate(10);
        System.out.println("A1 byteBuffer3="+byteBuffer3);
        byteBuffer3.put((byte) 125);
        System.out.println("A2 byteBuffer3="+byteBuffer3);
        byteBuffer3.put((byte) 126);
        System.out.println("A3 byteBuffer3="+byteBuffer3);
        byteBuffer3.put((byte) 127);
        System.out.println("B byteBuffer3="+byteBuffer3);
        byteBuffer3.rewind();
        System.out.println("C byteBuffer3="+byteBuffer3);
        System.out.println(byteBuffer3.get());
        System.out.println("E byteBuffer3="+byteBuffer3);
        System.out.println(byteBuffer3.get());
        System.out.println("F byteBuffer3="+byteBuffer3);
        System.out.println(byteBuffer3.get());
        byte[] getByteArray = byteBuffer3.array();
        for (int i = 0; i < getByteArray.length; i++) {
            System.out.print(getByteArray[i]+"-");
        }
        System.out.println();

        byte[] bytes1 = new byte[]{1,2,3,4,5,6,7,8};
        byte[] bytes2 = new byte[]{55,66,77,88};
        ByteBuffer byteBuffer4  = ByteBuffer.allocate(10);//开辟十个空间
        byteBuffer4.put(bytes1);//bytes1 放入缓存区
        byteBuffer4.position(2);// put 后position为3 ，更改位置
        byteBuffer4.put(bytes2,1,3);// 从下标2的位置开始替换 取值为：bytes2的下标1到3的值
        System.out.print("A=");
        byte[] getByte = byteBuffer4.array();
        for (int i = 0; i < getByte.length; i++) {
            System.out.print(getByte[i]+" ");
        }
        System.out.println();
        byteBuffer4.position(1);
        byte[] byteArrayOut = new byte[byteBuffer4.capacity()];
        byteBuffer4.get(byteArrayOut,3,4);//拷贝 byteBuffer4 下标 1的位置开始复制到byte下标3开始 4个长度
        System.out.print("B=");
        for (int i = 0; i < byteArrayOut.length; i++) {
            System.out.print(byteArrayOut[i]+" ");
        }
        System.out.println();

        byte[] bytes3 = new byte[]{1,2,3,4,5,6,7,8};
        ByteBuffer byteBuffer5 = ByteBuffer.wrap(bytes3);
        byteBuffer5.position(5);
        ByteBuffer byteBuffer6 = byteBuffer5.slice();
        System.out.println("byteBuffer5:"+byteBuffer5);
        System.out.println("byteBuffer6:"+byteBuffer6);
        byteBuffer6.put(0,(byte) 111);

        byte[] bytes4 = byteBuffer5.array();
        byte[] bytes5 = byteBuffer6.array();

        for (int i = 0; i < bytes4.length; i++) {
            System.out.print(bytes4[i]+" ");
        }
        System.out.println();
        for (int i = 0; i < bytes5.length; i++) {
            System.out.print(bytes5[i]+" ");
        }


        byte[] bytes6 = "我是中国人111".getBytes(StandardCharsets.UTF_8);
        System.out.println(Charset.defaultCharset().name());
        ByteBuffer byteBuffer7 = ByteBuffer.wrap(bytes6);
        CharBuffer charBuffer = Charset.forName("UTF-8").decode(byteBuffer7);
//        CharBuffer charBuffer = byteBuffer7.asCharBuffer();
        System.out.println("byteBuffer="+byteBuffer7.getClass().getName());
        System.out.println("charBuffer="+charBuffer.getClass().getName());
        System.out.println(byteBuffer7);
        System.out.println("charBuffer="+charBuffer);
        charBuffer.position(0);
        for (int i=0;i<charBuffer.limit();i++){
            System.out.print(charBuffer.get()+" ");

        }
        System.out.println();


        int value = 123456789;
        ByteBuffer byteBuffer8 = ByteBuffer.allocate(4);
        System.out.println(byteBuffer8.order()+" "+byteBuffer8.order());
        byteBuffer8.putInt(value);
        byte[] bytes7 = byteBuffer8.array();
        for (byte b : bytes7) {
            System.out.print(b+" ");
        }
        System.out.println();

        byteBuffer8 = ByteBuffer.allocate(10);
        System.out.println(byteBuffer8.order());
        byteBuffer8.order(ByteOrder.BIG_ENDIAN);//表示BIG-ENDIAN字节顺序的常量。按照此顺序，多字节值的字节顺序是从最高有效位到最低有效位的。
        System.out.println(byteBuffer8.order());
        byteBuffer8.putInt(value);
        byte[] bytes8 = byteBuffer8.array();
        for (byte b : bytes8) {
            System.out.print(b+" ");
        }
        System.out.println();

        byteBuffer8 = ByteBuffer.allocate(4);
        System.out.println(byteBuffer8.order());
        byteBuffer8.order(ByteOrder.LITTLE_ENDIAN);//表示LITTLE-ENDIAN字节顺序的常量。按照此顺序，多字节值的字节顺序是从最低有效位到最高有效位的。
        System.out.println(byteBuffer8.order());
        byteBuffer8.putInt(value);
        byte[] bytes9 = byteBuffer8.array();
        for (byte b : bytes9) {
            System.out.print(b+" ");
        }
        System.out.println();

        ByteBuffer byteBuffer9 = ByteBuffer.wrap(new byte[]{1,2,3,4,5,6});
        System.out.println(byteBuffer9);
        System.out.println("1 getValue="+byteBuffer9.get());
        System.out.println(byteBuffer9);
        System.out.println("2 getValue="+byteBuffer9.get());
        System.out.println(byteBuffer9);
        byteBuffer9.compact();
        System.out.println(byteBuffer9);
        byte[] bytes10 = byteBuffer9.array();
        for (byte b : bytes10) {
            System.out.print(b+" ");
        }
        System.out.println();
    }
}
