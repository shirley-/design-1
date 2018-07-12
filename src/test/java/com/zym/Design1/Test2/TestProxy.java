package com.zym.Design1.Test2;

import com.zym.Design1.entity.User;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by YM on 2018/7/6.
 */
public class TestProxy {

    @org.junit.Test
    public void testMain() {
        MyProxy handler = new MyProxy();
        Object proxy = handler.bind(new User());
        System.out.println(proxy.toString());

    }

    @org.junit.Test
    public void testMain2CGlib() {
        TestProxyCglib handler = new TestProxyCglib();
        Object proxy = handler.getProxy(new User().getClass());
        System.out.println(proxy.toString());

    }
}

class MyProxy implements InvocationHandler {

    private Object target = null;


    public Object bind(Object t ) {
        target = t;
        return Proxy.newProxyInstance(this.getClass().getClassLoader(), this.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before");
        Object ret = method.invoke(target, args);
        System.out.println("after");
        return ret;
    }
}
