package org.nom.mds.aop;

import org.nom.mds.annotation.DS;
import lombok.NonNull;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

public class DynamicDataSourceAnnotationAdvisor extends AbstractPointcutAdvisor implements BeanFactoryAware {

    private final Advice advice;

    private final Pointcut pointcut;

    public DynamicDataSourceAnnotationAdvisor(@NonNull DynamicDataSourceAnnotationInterceptor ai) {
        this.advice = ai;
        this.pointcut = buildPointcut();
    }

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override
    public Advice getAdvice() {
        return this.advice;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        if (this.advice instanceof BeanFactoryAware) {
            ((BeanFactoryAware) this.advice).setBeanFactory(beanFactory);
        }
    }

    private Pointcut buildPointcut() {
        Pointcut cpc = new AnnotationMatchingPointcut(DS.class, true);
        Pointcut mpc = AnnotationMatchingPointcut.forMethodAnnotation(DS.class);
        return new ComposablePointcut(cpc).union(mpc);
    }
}
