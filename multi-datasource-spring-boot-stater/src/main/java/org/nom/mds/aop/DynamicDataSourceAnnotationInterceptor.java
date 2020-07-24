package org.nom.mds.aop;

import org.nom.mds.annotation.DS;
import org.nom.mds.processor.DsProcessor;
import org.nom.mds.support.DataSourceClassResolver;
import org.nom.mds.toolkit.DynamicDataSourceContextHolder;
import lombok.Setter;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

/**
 * Core Interceptor of Dynamic Datasource
 */
public class DynamicDataSourceAnnotationInterceptor implements MethodInterceptor {

    /**
     * The identification of SPEL.
     */
    private static final String DYNAMIC_PREFIX = "#";
    private static final DataSourceClassResolver RESOLVER = new DataSourceClassResolver();
    @Setter
    private DsProcessor dsProcessor;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            DynamicDataSourceContextHolder.push(determineDataSource(invocation));
            return invocation.proceed();
        } finally {
            DynamicDataSourceContextHolder.poll();
        }
    }

    private String determineDataSource(MethodInvocation invocation) throws IllegalAccessException {
        Method method = invocation.getMethod();
        DS ds = method.isAnnotationPresent(DS.class) ? method.getAnnotation(DS.class)
                : AnnotationUtils.findAnnotation(RESOLVER.targetClass(invocation), DS.class);
        if (ds == null) {
            return null;
        }
        String key = ds.value();
        return (!key.isEmpty() && key.startsWith(DYNAMIC_PREFIX)) ?
                dsProcessor.determineDataSource(invocation, key) : key;
    }
}
