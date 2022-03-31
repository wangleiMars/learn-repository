package com.mars.netty.test.two.netty2.annotation;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mars.netty.test.two.netty2.bean.RequestFuture;
import com.mars.netty.test.two.netty2.bean.Response;
import lombok.Data;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mediator {
    public static Map<String, MethodBean> methodBeanMap = null;

    static {
        methodBeanMap = new HashMap<>();
    }

    public static Response process(RequestFuture requestFuture) {
        Response response = new Response();
        try {
            String path = requestFuture.getPath();
            MethodBean methodBean = methodBeanMap.get(path);

            if (methodBean != null) {
                Object bean = methodBean.getBean();
                Method method = methodBean.getMethod();
                Object body = requestFuture.getRequest();
                Class[] paramTypes = method.getParameterTypes();
                Class paramType = paramTypes[0];
                Object param = null;
                if (paramType.isAssignableFrom(List.class)) {
                    param = JSONArray.parseArray(JSONObject.toJSONString(body), paramType);
                } else if (paramType.getName().equals(String.class.getName())) {
                    param = body;
                } else {
                    param = JSONObject.parseObject(JSONObject.toJSONString(body), paramType);
                }
                Object result = null;

                result = method.invoke(bean, param);

                response.setResult(result);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        response.setId(requestFuture.getId());
        return response;
    }

    @Data
    public static class MethodBean {
        private Object bean;
        private Method method;
    }
}
