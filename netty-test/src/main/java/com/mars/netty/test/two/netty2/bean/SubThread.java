package com.mars.netty.test.two.netty2.bean;

public class SubThread extends Thread{
    private RequestFuture requestFuture;

    public SubThread(RequestFuture requestFuture) {
        this.requestFuture = requestFuture;
    }

    @Override
    public void run() {
        Response response = new Response();
        response.setId(requestFuture.getId());
        response.setResult("server response:"+Thread.currentThread().getId());
        try {
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        RequestFuture.received(response);
    }
}
