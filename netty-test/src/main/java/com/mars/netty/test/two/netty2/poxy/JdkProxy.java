package com.mars.netty.test.two.netty2.poxy;

import com.alibaba.fastjson.JSONObject;
import com.mars.netty.test.two.netty2.annotation.RemoteInvoke;
import com.mars.netty.test.two.netty2.bean.RequestFuture;
import com.mars.netty.test.two.netty2.client.ChannelFutureManager;
import com.mars.netty.test.two.netty2.client.NettyClient;
import io.netty.channel.ChannelFuture;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

//@Component
public class JdkProxy implements InvocationHandler, BeanPostProcessor {

    private Field target;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RequestFuture requestFuture = new RequestFuture();
        requestFuture.setPath(target.getType().getName() + "." + method.getName());
        requestFuture.setRequest(args[0]);
        Object resp = NettyClient.sendRequest(requestFuture);
        Class returnType = method.getReturnType();
        if (resp == null) {
            return null;
        }
        resp = JSONObject.parseObject(JSONObject.toJSONString(resp), returnType);
        return resp;
    }

    private Object getJdkProxy(Field field) {
        this.target = field;
        return Proxy.newProxyInstance(field.getType().getClassLoader(), new Class[]{field.getType()}, this);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getAnnotation(RemoteInvoke.class) != null) {
                field.setAccessible(true);
                try {
                    field.set(bean, getJdkProxy(field));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public static Object sendReqest(RequestFuture requestFuture) throws Exception {
        String reqStr = JSONObject.toJSONString(requestFuture);
        ChannelFuture future = ChannelFutureManager.get();
        future.channel().writeAndFlush(requestFuture);
        return requestFuture.get();
    }
}
