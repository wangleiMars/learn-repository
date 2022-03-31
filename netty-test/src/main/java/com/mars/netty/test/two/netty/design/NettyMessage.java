package com.mars.netty.test.two.netty.design;

public class NettyMessage {
    /**
     * 消息头
     */
    private Head head;
    /**
     * 消息体
     */
    private Object body;

    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "NettyMessage{" +
                "head=" + head +
                ", body=" + body +
                '}';
    }
}
