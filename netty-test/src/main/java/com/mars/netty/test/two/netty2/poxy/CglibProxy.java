package com.mars.netty.test.two.netty2.poxy;

import com.alibaba.fastjson.JSONObject;
import com.mars.netty.test.two.netty2.annotation.RemoteInvoke;
import com.mars.netty.test.two.netty2.bean.RequestFuture;
import com.mars.netty.test.two.netty2.client.NettyClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Component
public class CglibProxy implements BeanPostProcessor {
    /**
     * 在所有bean初始化完成之前，
     * 对bean中包含有RemoteInvoke注解的属性
     * ，做重新赋值
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(RemoteInvoke.class)) {
                field.setAccessible(true);
                final Map<Method, Class> methodClassMap = new HashMap<Method, Class>();
                //此处需要把属性和属性对应的方法放入final对象中
                //方便给callBack中的intercept方法使用
                putMethodClass(methodClassMap, field);
                //此类是cglib非常重要的类，能动态的创建给定类的子类
                //并且能拦截代理类的所有方法
                Enhancer enhancer = new Enhancer();
                //此处和JDK的动态代理不一样，不管是接口还是类都可以
                enhancer.setInterfaces(new Class[]{field.getType()});
                //设置回调方法
                enhancer.setCallback(new MethodInterceptor() {
                    /**
                     * 拦截代理类的所有方法
                     * @param instance
                     * @param method
                     * @param args
                     * @param proxy
                     * @return
                     * @throws Throwable
                     */
                    @Override
                    public Object intercept(Object instance, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                        //采用netty客户端去需要去调用服务器
                        RequestFuture request = new RequestFuture();
                        //把接口类名+方法名组装成path
                        request.setPath(methodClassMap.get(method).getName() + "." + method.getName());
                        //设置参数
                        request.setRequest(args[0]);
                        //远程调用
                        Object resp = NettyClient.sendRequest(request);
                        Class returnType = method.getReturnType();
                        if (resp == null) {
                            return null;
                        }
                        //对返回结果做反序列化
                        resp = JSONObject.parseObject(JSONObject.toJSONString(resp), returnType);
                        return resp;
                    }
                });
                try {
                    //对包含有RemoteInvoke注解的属性重新赋值
                    field.set(bean, enhancer.create());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }

    /**
     * 对属性的所有方法和属性接口类型放入到一个map中
     *
     * @param methodClassMap
     * @param field
     */
    private void putMethodClass(Map<Method, Class> methodClassMap, Field field) {
        Method[] methods = field.getType().getDeclaredMethods();
        for (Method m : methods) {
            methodClassMap.put(m, field.getType());
        }

    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
