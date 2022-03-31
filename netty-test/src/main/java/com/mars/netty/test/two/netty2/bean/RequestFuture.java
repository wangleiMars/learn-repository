package com.mars.netty.test.two.netty2.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Getter
@Setter
@ToString
public class RequestFuture {
    private static final AtomicLong aid = new AtomicLong();
    public static Map<Long, RequestFuture> futures = new ConcurrentHashMap<>();
    private long id;
    private Object request;
    private Object result;
    private long timeout = 5000;
    private String path;

    public RequestFuture() {
        id= aid.incrementAndGet();
        addFutures(this);
    }

    public static void addFutures(RequestFuture future) {
        futures.put(future.getId(), future);
    }

    public Object get() throws InterruptedException {
        synchronized (this) {
            while (this.result == null) {
                this.wait(timeout);
            }
        }
        return this.result;
    }

    public static void received(Response resp) {
        RequestFuture future = futures.remove(resp.getId());
        if (future != null) {
            future.setResult(resp.getResult());
            synchronized (future){
                future.notify();
            }
        }
    }
}
