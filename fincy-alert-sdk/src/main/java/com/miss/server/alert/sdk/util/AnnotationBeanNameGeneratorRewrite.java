package com.miss.server.alert.sdk.util;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.util.Assert;

public class AnnotationBeanNameGeneratorRewrite extends AnnotationBeanNameGenerator {
    public AnnotationBeanNameGeneratorRewrite() {
    }

    @Override
    protected String buildDefaultBeanName(BeanDefinition definition) {
        String beanClassName = definition.getBeanClassName();
        Assert.state(beanClassName != null, "No bean class name set");
        return beanClassName;
    }
}
