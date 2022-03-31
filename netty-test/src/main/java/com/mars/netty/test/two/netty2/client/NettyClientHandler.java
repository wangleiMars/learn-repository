package com.mars.netty.test.two.netty2.client;

import com.alibaba.fastjson.JSONObject;
import com.mars.netty.test.two.netty2.bean.RequestFuture;
import com.mars.netty.test.two.netty2.bean.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
//    private Promise<Response> promise;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Response response = JSONObject.parseObject(msg.toString(), Response.class);
        RequestFuture.received(response);
    }


}
