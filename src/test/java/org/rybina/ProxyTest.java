package org.rybina;

import org.junit.jupiter.api.Test;
import org.rybina.entity.Company;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyTest {


//    Такое уже давно не используется
    @Test
    void testDynamic() {
        Company company = new Company();
        Proxy.newProxyInstance(company.getClass().getClassLoader(), company.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return method.invoke(company, args);
            }
        });
    }
}
