package org.nom.mds.plugin;

import org.nom.mds.spring.boot.autoconfigure.DynamicDataSourceProperties;
import org.nom.mds.support.DbHealthIndicator;
import org.nom.mds.support.DdConstants;
import org.nom.mds.toolkit.DynamicDataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.Properties;

/**
 * Master-slave Separation Plugin with mybatis
 */
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
@Slf4j
public class MasterSlaveAutoRoutingPlugin implements Interceptor {

    @Autowired
    private DynamicDataSourceProperties properties;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        boolean empty = true;
        try {
            empty = StringUtils.isEmpty(DynamicDataSourceContextHolder.peek());
            if (empty) {
                DynamicDataSourceContextHolder.push(getDataSource(ms));
            }
            return invocation.proceed();
        } finally {
            if (empty) {
                DynamicDataSourceContextHolder.clear();
            }
        }
    }

    /**
     * 获取动态数据源名称，重写注入 DbHealthIndicator 支持数据源健康状况判断选择
     *
     * @param mappedStatement mybatis MappedStatement
     * @return 获取真实的数据源名称
     */
    public String getDataSource(MappedStatement mappedStatement) {
        String slave = DdConstants.SLAVE;
        if (properties.isHealthyCheck()) {
            /*
             * 根据从库健康状况，判断是否切到主库
             */
            boolean health = DbHealthIndicator.getDbHealth(DdConstants.SLAVE);
            if (!health) {
                health = DbHealthIndicator.getDbHealth(DdConstants.MASTER);
                if (health) {
                    slave = DdConstants.MASTER;
                }
            }
        }
        return SqlCommandType.SELECT == mappedStatement.getSqlCommandType() ? slave : DdConstants.MASTER;
    }

    @Override
    public Object plugin(Object target) {
        return target instanceof Executor ? Plugin.wrap(target, this) : target;
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
