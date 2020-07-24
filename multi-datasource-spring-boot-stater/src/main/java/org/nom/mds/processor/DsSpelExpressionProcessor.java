package org.nom.mds.processor;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.reflect.Method;

public class DsSpelExpressionProcessor extends DsProcessor {

    /**
     * 参数发现器
     */
    private static final ParameterNameDiscoverer NAME_DISCOVERER = new DefaultParameterNameDiscoverer();
    /**
     * Express语法解析器
     */
    private static final ExpressionParser PARSER = new SpelExpressionParser();
    /**
     * 解析上下文的模板
     * 对于默认不设置的情况下,从参数中取值的方式 #param1
     * 设置指定模板 ParserContext.TEMPLATE_EXPRESSION 后的取值方式: #{#param1}
     * issues: https://github.com/baomidou/dynamic-datasource-spring-boot-starter/issues/199
     */
    private ParserContext parserContext = new ParserContext() {

        @Override
        public boolean isTemplate() {
            return false;
        }

        @Override
        public String getExpressionPrefix() {
            return null;
        }

        @Override
        public String getExpressionSuffix() {
            return null;
        }
    };

    @Override
    public boolean matches(String key) {
        return true;
    }

    @Override
    public String doDetermineDataSource(MethodInvocation invocation, String key) {
        Method method = invocation.getMethod();
        Object[] arguments = invocation.getArguments();
        EvaluationContext context = new MethodBasedEvaluationContext(null, method, arguments, NAME_DISCOVERER);
        final Object value = PARSER.parseExpression(key, parserContext).getValue(context);
        return value == null ? null : value.toString();
    }

    public void setParserContext(ParserContext parserContext) {
        this.parserContext = parserContext;
    }
}
