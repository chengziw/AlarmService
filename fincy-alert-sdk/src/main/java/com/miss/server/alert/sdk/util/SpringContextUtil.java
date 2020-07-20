package com.miss.server.alert.sdk.util;

import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    public SpringContextUtil() {
    }

    public static Map<String, DataSource> getBeansOfType(Class<DataSource> dataSourceClass) {
        return applicationContext.getBeansOfType(dataSourceClass);
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (applicationContext != null) {
            SpringContextUtil.applicationContext = applicationContext;
        }

    }

    public ApplicationContext applicationContext() {
        return applicationContext;
    }

    /** @deprecated */
    @Deprecated
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }
}
