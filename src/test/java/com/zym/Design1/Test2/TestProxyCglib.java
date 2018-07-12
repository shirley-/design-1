package com.zym.Design1.Test2;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by YM on 2018/7/6.
 */
public class TestProxyCglib implements MethodInterceptor {

    public Object getProxy(Class clz) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clz);
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("before");
        Object ret = methodProxy.invokeSuper(o, objects);
        System.out.println("after");
        return ret;
    }
}
