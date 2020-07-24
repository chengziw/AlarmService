package org.nom.mds.aop;

import org.springframework.aop.support.JdkRegexpMethodPointcut;
import org.springframework.lang.Nullable;

import java.util.Arrays;
import java.util.Map;

public class DynamicJdkRegexpMethodPointcut extends JdkRegexpMethodPointcut {

    private final Map<String, String> matchesCache;

    private final String ds;

    public DynamicJdkRegexpMethodPointcut(String pattern, String ds, Map<String, String> matchesCache) {
        this.ds = ds;
        this.matchesCache = matchesCache;
        setPattern(pattern);
    }

    @Override
    protected boolean matches(String pattern, int patternIndex) {
        boolean matches = super.matches(pattern, patternIndex);
        if (matches) {
            matchesCache.put(pattern, ds);
        }
        return matches;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof DynamicJdkRegexpMethodPointcut)) {
            return false;
        }
        DynamicJdkRegexpMethodPointcut otherPointcut = (DynamicJdkRegexpMethodPointcut) other;
        return this.ds.equals(otherPointcut.ds) && this.matchesCache.equals(otherPointcut.matchesCache)
                && Arrays.equals(this.getPatterns(), otherPointcut.getPatterns());
    }
}
