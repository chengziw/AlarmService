package org.nom.mds;

import org.nom.mds.matcher.ExpressionMatcher;
import org.nom.mds.matcher.Matcher;
import org.nom.mds.matcher.RegexMatcher;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

/**
 * 基于多种策略的自动切换数据源
 */
public class DynamicDataSourceConfigure {

    @Getter
    private final List<Matcher> matchers = new LinkedList<>();

    private DynamicDataSourceConfigure() {
    }

    public static DynamicDataSourceConfigure config() {
        return new DynamicDataSourceConfigure();
    }

    public DynamicDataSourceConfigure regexMatchers(String pattern, String ds) {
        matchers.add(new RegexMatcher(pattern, ds));
        return this;
    }

    public DynamicDataSourceConfigure expressionMatchers(String expression, String ds) {
        matchers.add(new ExpressionMatcher(expression, ds));
        return this;
    }

}
