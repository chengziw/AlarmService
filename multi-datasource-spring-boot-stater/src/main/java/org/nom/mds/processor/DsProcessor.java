package org.nom.mds.processor;

import org.aopalliance.intercept.MethodInvocation;

public abstract class DsProcessor {

    private DsProcessor nextProcessor;

    public void setNextProcessor(DsProcessor dsProcessor) {
        this.nextProcessor = dsProcessor;
    }

    /**
     * 抽象匹配条件 匹配才会走当前执行器否则走下一级执行器
     *
     * @param key DS注解里的内容
     * @return 是否匹配
     */
    public abstract boolean matches(String key);

    /**
     * 决定数据源
     * <pre>
     *     调用底层doDetermineDatasource，
     *     如果返回的是null则继续执行下一个，否则直接返回
     * </pre>
     *
     * @param invocation 方法执行信息
     * @param key        DS注解里的内容
     * @return 数据源名称
     */
    public String determineDataSource(MethodInvocation invocation, String key) {
        if (matches(key)) {
            String dataSource = doDetermineDataSource(invocation, key);
            if (dataSource == null && nextProcessor != null) {
                return nextProcessor.determineDataSource(invocation, key);
            }
            return dataSource;
        }
        if (nextProcessor != null) {
            return nextProcessor.determineDataSource(invocation, key);
        }
        return null;
    }

    /**
     * 抽象最终决定数据源
     *
     * @param invocation 方法执行信息
     * @param key        DS注解里的内容
     * @return 数据源名称
     */
    public abstract String doDetermineDataSource(MethodInvocation invocation, String key);
}
