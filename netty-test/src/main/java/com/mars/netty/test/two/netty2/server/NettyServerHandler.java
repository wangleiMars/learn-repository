package com.mars.netty.test.two.netty2.server;

import com.alibaba.fastjson.JSONObject;
import com.mars.netty.test.two.netty2.annotation.Mediator;
import com.mars.netty.test.two.netty2.bean.RequestFuture;
import com.mars.netty.test.two.netty2.bean.Response;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@ChannelHandler.Sharable//表示对所有channel共享，无状态，注意多线程并发
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

//        if (msg instanceof ByteBuf) {
//            log.info("msg:{}", ((ByteBuf) msg).toString(CharsetUtil.UTF_8));
//        }
//        ctx.channel().writeAndFlush("msg has recived");

        RequestFuture requestFuture = JSONObject.parseObject(msg.toString(), RequestFuture.class);
//        log.info("请求信息:{}",requestFuture);
//        Response response = new Response();
//        response.setId(requestFuture.getId());
//        response.setResult("服务器响应ok");
//        ctx.channel().writeAndFlush(JSONObject.toJSONString(response));
        Response response = Mediator.process(requestFuture);
        ctx.channel().writeAndFlush(JSONObject.toJSONString(response));
    }
}
