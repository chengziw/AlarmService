package org.nom.mds.aop;

import org.nom.mds.matcher.ExpressionMatcher;
import org.nom.mds.matcher.Matcher;
import org.nom.mds.matcher.RegexMatcher;
import org.nom.mds.processor.DsProcessor;
import org.nom.mds.toolkit.DynamicDataSourceContextHolder;
import lombok.Setter;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamicDataSourceAdvisor extends AbstractPointcutAdvisor implements BeanFactoryAware {

    /**
     * The identification of SPEL
     */
    private static final String DYNAMIC_PREFIX = "#";
    private final Advice advice;
    private final Pointcut pointcut;
    private final Map<String, String> matchesCache = new HashMap<>();
    @Setter
    private DsProcessor dsProcessor;

    public DynamicDataSourceAdvisor(List<Matcher> matchers) {
        this.pointcut = buildPointcut(matchers);
        this.advice = buildAdvice();
    }

    private Advice buildAdvice() {
        return (MethodInterceptor) invocation -> {
            try {
                Method method = invocation.getMethod();
                String methodPath = invocation.getThis().getClass().getName() + "." + method.getName();
                String key = matchesCache.get(methodPath);
                if (key != null && !key.isEmpty() && key.startsWith(DYNAMIC_PREFIX)) {
                    key = dsProcessor.determineDataSource(invocation, key);
                }
                DynamicDataSourceContextHolder.push(key);
                return invocation.proceed();
            } finally {
                DynamicDataSourceContextHolder.poll();
            }
        };
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

    private Pointcut buildPointcut(List<Matcher> matchers) {
        ComposablePointcut composablePointcut = null;
        for (Matcher matcher : matchers) {
            if (matcher instanceof RegexMatcher) {
                RegexMatcher regexMatcher = (RegexMatcher) matcher;
                Pointcut pc = new DynamicJdkRegexpMethodPointcut(regexMatcher.getPattern(), regexMatcher.getDs(), matchesCache);
                if (composablePointcut == null) {
                    composablePointcut = new ComposablePointcut(pc);
                } else {
                    composablePointcut.union(pc);
                }
            } else {
                ExpressionMatcher expressionMatcher = (ExpressionMatcher) matcher;
                Pointcut pc = new DynamicAspectJExpressionPointcut(expressionMatcher.getExpression(), expressionMatcher.getDs(),
                        matchesCache);
                if (composablePointcut == null) {
                    composablePointcut = new ComposablePointcut(pc);
                } else {
                    composablePointcut.union(pc);
                }
            }
        }
        return composablePointcut;
    }
}
