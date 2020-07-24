package org.nom.mds.support;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

/**
 * 获取对mybatis-plus的支持
 */
@Slf4j
public class DataSourceClassResolver {

    private static boolean mpEnabled = false;

    private static Field mapperInterfaceField;

    static {
        Class<?> proxyClass = null;
        try {
            proxyClass = Class.forName("com.baomidou.mybatisplus.core.override.MybatisMapperProxy");
        } catch (ClassNotFoundException e1) {
            try {
                proxyClass = Class.forName("com.baomidou.mybatisplus.core.override.PageMapperProxy");
            } catch (ClassNotFoundException e2) {
                try {
                    proxyClass = Class.forName("org.apache.ibatis.binding.MapperProxy");
                } catch (ClassNotFoundException e3) {
                    // no op
                }
            }
        }
        if (proxyClass != null) {
            try {
                mapperInterfaceField = proxyClass.getDeclaredField("mapperInterface");
                mapperInterfaceField.setAccessible(true);
                mpEnabled = true;
            } catch (NoSuchFieldException e) {
                log.warn("no such field", e);
            }
        }
    }

    public Class<?> targetClass(MethodInvocation invocation) throws IllegalAccessException {
        if (mpEnabled) {
            Object target = invocation.getThis();
            Class<?> targetClass = target.getClass();
            return Proxy.isProxyClass(targetClass) ? (Class<?>) mapperInterfaceField.get(Proxy.getInvocationHandler(target)) : targetClass;
        }
        return invocation.getMethod().getDeclaringClass();
    }
}
