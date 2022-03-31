package com.mars.netty.test.two.netty2.spring;

import com.mars.netty.test.two.netty2.annotation.Mediator;
import com.mars.netty.test.two.netty2.annotation.Remote;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

@Component
public class InitLoadRemoteMethod implements ApplicationListener<ContextRefreshedEvent>, Ordered {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        Map<String, Object> controllerBeans = contextRefreshedEvent.getApplicationContext().getBeansWithAnnotation(Remote.class);
        for (String key : controllerBeans.keySet()) {
            Object bean = controllerBeans.get(key);
            Method[] methods = bean.getClass().getDeclaredMethods();
            for (Method method : methods) {
                String methodVal = bean.getClass().getInterfaces()[0].getName()+"."+method.getName();

                Mediator.MethodBean methodBean = new Mediator.MethodBean();
                methodBean.setBean(bean);
                methodBean.setMethod(method);
                Mediator.methodBeanMap.put(methodVal, methodBean);

//                Remote remote = method.getAnnotation(Remote.class);
//                if (null != remote) {
//                    Mediator.MethodBean methodBean = new Mediator.MethodBean();
//                    methodBean.setBean(bean);
//                    methodBean.setMethod(method);
//                    Mediator.methodBeanMap.put(remote.value(), methodBean);
//                }
            }
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
