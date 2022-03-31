package com.mars.netty.test.two.netty.design;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class NettyMessageEncoder extends MessageToByteEncoder<NettyMessage> {

    MarshallingEncoder marshallingEncoder;

    public NettyMessageEncoder() throws IOException {
        this.marshallingEncoder = new MarshallingEncoder();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, NettyMessage msg, ByteBuf senbuf) throws Exception {
        if(null==msg || null==msg.getHead())
            throw new Exception("msg is null");
        senbuf.writeInt(msg.getHead().getCrcCode());
        senbuf.writeInt(msg.getHead().getLength());
        senbuf.writeLong(msg.getHead().getSessionID());
        senbuf.writeByte(msg.getHead().getType());
        senbuf.writeByte(msg.getHead().getPriority());
        senbuf.writeInt(msg.getHead().getAttachment().size());
        String key = null;
        byte[] keyArray = null;
        Object value = null;
        for(Map.Entry<String,Object> param: msg.getHead().getAttachment().entrySet()){
            key = param.getKey();
            keyArray = key.getBytes(StandardCharsets.UTF_8);
            senbuf.writeInt(keyArray.length);
            senbuf.writeBytes(keyArray);
            value = param.getValue();
            marshallingEncoder.encode(value,senbuf);
        }
        key = null;
        keyArray = null;
        value= null;
        if(null!=msg.getBody()){
            marshallingEncoder.encode(msg.getBody(),senbuf);
        }else {
            senbuf.writeInt(0);
            senbuf.setInt(4,senbuf.readableBytes());
        }
    }
}
