package com.sky.common.exception.Interceptor;

import com.sky.common.exception.domain.SqlExceptionLog;
import com.sky.common.exception.exception.UniteSqlException;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Value;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Mybatis拦截器. 拦截SQL执行时的异常，写入DB.
 */
@Slf4j
@Intercepts({
        //插入和删除底层都是通过update实现的
        @Signature(type = Executor.class, method = "update", args = {
                MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {
                MappedStatement.class, Object.class, RowBounds.class,
                ResultHandler.class})})
public class SqlExceptionInterceptor implements Interceptor {

    @Value("${dubbo.application.name}")
    private String application;

    /**
     * 线程池.
     */
    private static ExecutorService executor = Executors.newFixedThreadPool(5);

    /**
     * 拦截方法.
     */
    public Object intercept(Invocation invocation) throws Throwable {

        try {
            return invocation.proceed();
        } catch (Exception e) {
            // 日志对象
            final SqlExceptionLog record = new SqlExceptionLog();
            try {
                // 取得各种值
                MappedStatement statement = (MappedStatement) invocation
                        .getArgs()[0];
                Object parameter = invocation.getArgs()[1];
                BoundSql boundSql = statement.getBoundSql(parameter);

                // 防止死循环
                if (statement.getId().toUpperCase()
                        .contains("sqlExceptionLogMapper".toUpperCase())) {
                    throw e;
                }
                record.setExceptionMessage(e.getCause().toString());
                record.setExceptionStack(getExceptionStackTrace(e));
                record.setCreateTime(new Date());
                record.setSystemName(application);
                record.setSqlId(statement.getId());
                record.setSqlParameter(parameter.toString());
                record.setSqlSource(boundSql.getSql());
                record.setSqlType(statement.getSqlCommandType().toString());

                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        //持久化处理
                        //报警处理
                    }
                });

                log.warn("SQL异常处理", application, "【异常信息：{},异常点：{},时间：{}】", record.getExceptionMessage(), record.getSqlId(), record.getCreateTime());
            } catch (Exception ex) {
                log.error("SQL异常处理", application, ex, "【拦截异常信息处理失败,失败原因：{}】", ex.getCause().toString());
                // ex.printStackTrace();
            }
            // 抛出异常
            throw new UniteSqlException(record.toString());
        }
    }

    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    /**
     * 配置文件读取属性.
     */
    public void setProperties(Properties properties) {
    }

    /**
     * 获取异常的堆栈信息.
     *
     * @param e
     * @return
     */
    private String getExceptionStackTrace(Exception e) {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        e.printStackTrace(new java.io.PrintWriter(buf, true));
        String expMessage = buf.toString();
        try {
            buf.close();
        } catch (IOException ex) {
        }
        return expMessage;
    }
}