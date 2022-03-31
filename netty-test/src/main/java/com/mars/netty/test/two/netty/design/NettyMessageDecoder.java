package com.mars.netty.test.two.netty.design;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder {
    MarshallingDecoder marshallingDecoder;

    public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) throws IOException {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
        marshallingDecoder = new MarshallingDecoder();
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = (ByteBuf) super.decode(ctx, in);
        if (null == frame) {
            return null;
        }
        NettyMessage message = new NettyMessage();
        Head head = new Head();
        head.setCrcCode(in.readInt());
        head.setLength(in.readInt());
        head.setSessionID(in.readLong());
        head.setType(in.readByte());
        head.setPriority(in.readByte());

        int size = in.readInt();
        if (size > 0) {
            Map<String, Object> attch = new HashMap<>(size);
            int keysize = 0;
            byte[] keyArray = null;
            String key = null;
            for (int i = 0; i < size; i++) {
                keysize = in.readInt();
                keyArray = new byte[keysize];
                in.readBytes(keyArray);
                key = new String(keyArray, StandardCharsets.UTF_8);
                attch.put(key, marshallingDecoder.decode(in));
            }
            keyArray = null;
            key = null;
            head.setAttachment(attch);
            if (in.readableBytes() > 4) {
                message.setBody(marshallingDecoder.decode(in));
            }
            message.setHead(head);
            return message;
        }
        return null;
    }
}
