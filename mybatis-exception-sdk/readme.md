# mybatis-exception-sdk使用说明

#### 引入坐标
```xml
<dependency>
    <groupId>com.sdk</groupId>
    <artifactId>mybatis-exception-sdk</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

#### 项目配置
##### properties
```properties
dubbo.application.name

```

配置说明：
```text
dubbo.application.name 用于拼接日志信息
    
   
```

使用说明：
```text
pom文件引入坐标即可。
```
sdk效果
```java
日志：【warn】
2020-07-23 17:00:08,768 [WARN ] [scheduling-1] [SQL异常处理]                        [sky-user-unblock-timer] 【异常信息：java.sql.SQLSyntaxErrorException: Table 'dcpay_platform.platform_risk_forbid_record' doesn't exist,异常点：com.sky.server.user.listen.domain.dao.PlatformRiskForbidRecordDao.findNeedUnForBidRecord,时间：2020/7/23 下午5:00】
2020-07-23 17:31:47,760 [WARN ] [scheduling-1] [SQL异常处理]                        [sky-user-unblock-timer] 【异常信息：java.sql.SQLSyntaxErrorException: Unknown column 'forbid_type' in 'field list',异常点：com.sky.server.user.listen.domain.dao.PlatformRiskForbidRecordDao.updateById,时间：2020/7/23 下午5:31】
异常：UniteSqlException
Caused by: com.sky.common.exception.exception.UniteSqlException: SQL异常详情{系统名称='sky-user-unblock-timer', 异常点='com.sky.server.user.listen.domain.dao.PlatformRiskForbidRecordDao.findNeedUnForBidRecord', 参数='{ew=com.baomidou.mybatisplus.core.conditions.query.QueryWrapper@29e139bd, param1=com.baomidou.mybatisplus.core.conditions.query.QueryWrapper@29e139bd}', 来源='SELECT * FROM platform_risk_forbid_record where (forbid_status = ?) ORDER BY forbid_id ASC LIMIT 0,1000', 类型='SELECT', 异常信息='java.sql.SQLSyntaxErrorException: Table 'dcpay_platform.platform_risk_forbid_record' doesn't exist', 异常时间='Thu Jul 23 17:00:08 CST 2020', 堆栈信息='java.lang.reflect.InvocationTargetException
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.base/java.lang.reflect.Method.invoke(Method.java:566)
	at org.apache.ibatis.plugin.Invocation.proceed(Invocation.java:49)
	at com.sky.common.exception.Interceptor.SqlExceptionInterceptor.intercept(SqlExceptionInterceptor.java:46)
	at org.apache.ibatis.plugin.Plugin.invoke(Plugin.java:61)
	at com.sun.proxy.$Proxy98.query(Unknown Source)
	at org.apache.ibatis.session.defaults.DefaultSqlSession.selectList(DefaultSqlSession.java:147)
	at org.apache.ibatis.session.defaults.DefaultSqlSession.selectList(DefaultSqlSession.java:140)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.base/java.lang.reflect.Method.invoke(Method.java:566)
	at org.mybatis.spring.SqlSessionTemplate$SqlSessionInterceptor.invoke(SqlSessionTemplate.java:426)
	at com.sun.proxy.$Proxy81.selectList(Unknown Source)
	at org.mybatis.spring.SqlSessionTemplate.selectList(SqlSessionTemplate.java:223)
	at com.baomidou.mybatisplus.core.override.MybatisMapperMethod.executeForMany(MybatisMapperMethod.java:177)
	at com.baomidou.mybatisplus.core.override.MybatisMapperMethod.execute(MybatisMapperMethod.java:78)
	at com.baomidou.mybatisplus.core.override.MybatisMapperProxy.invoke(MybatisMapperProxy.java:96)
	at com.sun.proxy.$Proxy86.findNeedUnForBidRecord(Unknown Source)
	at com.sky.server.user.listen.infrastructure.repository.PlatformRiskForbidRepository.findNeedUnForBidRecord(PlatformRiskForbidRepository.java:24)
	at com.sky.server.user.listen.infrastructure.repository.PlatformRiskForbidRepository$$FastClassBySpringCGLIB$$31443c6e.invoke(<generated>)
	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:218)
	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:771)
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163)
	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:749)
	at org.springframework.dao.support.PersistenceExceptionTranslationInterceptor.invoke(PersistenceExceptionTranslationInterceptor.java:139)
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)
	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:749)
	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:691)
	at com.sky.server.user.listen.infrastructure.repository.PlatformRiskForbidRepository$$EnhancerBySpringCGLIB$$9ec0d40c.findNeedUnForBidRecord(<generated>)
	at com.sky.server.user.listen.domain.service.MonitorForbidUserService.monitorForbid(MonitorForbidUserService.java:27)
	at com.sky.server.user.listen.application.timer.DefaultTimer.run(DefaultTimer.java:20)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.base/java.lang.reflect.Method.invoke(Method.java:566)
	at org.springframework.scheduling.support.ScheduledMethodRunnable.run(ScheduledMethodRunnable.java:84)
	at org.springframework.scheduling.support.DelegatingErrorHandlingRunnable.run(DelegatingErrorHandlingRunnable.java:54)
	at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:515)
	at java.base/java.util.concurrent.FutureTask.runAndReset(FutureTask.java:305)
	at java.base/java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:305)
	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1128)
	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:628)
	at java.base/java.lang.Thread.run(Thread.java:834)
Caused by: java.sql.SQLSyntaxErrorException: Table 'dcpay_platform.platform_risk_forbid_record' doesn't exist
	at com.mysql.cj.jdbc.exceptions.SQLError.createSQLException(SQLError.java:120)
	at com.mysql.cj.jdbc.exceptions.SQLError.createSQLException(SQLError.java:97)
	at com.mysql.cj.jdbc.exceptions.SQLExceptionsMapping.translateException(SQLExceptionsMapping.java:122)
	at com.mysql.cj.jdbc.ClientPreparedStatement.executeInternal(ClientPreparedStatement.java:953)
	at com.mysql.cj.jdbc.ClientPreparedStatement.execute(ClientPreparedStatement.java:370)
	at com.zaxxer.hikari.pool.ProxyPreparedStatement.execute(ProxyPreparedStatement.java:44)
	at com.zaxxer.hikari.pool.HikariProxyPreparedStatement.execute(HikariProxyPreparedStatement.java)
	at org.apache.ibatis.executor.statement.PreparedStatementHandler.query(PreparedStatementHandler.java:64)
	at org.apache.ibatis.executor.statement.RoutingStatementHandler.query(RoutingStatementHandler.java:79)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.base/java.lang.reflect.Method.invoke(Method.java:566)
	at org.apache.ibatis.plugin.Plugin.invoke(Plugin.java:63)
	at com.sun.proxy.$Proxy100.query(Unknown Source)
	at com.baomidou.mybatisplus.core.executor.MybatisSimpleExecutor.doQuery(MybatisSimpleExecutor.java:67)
	at org.apache.ibatis.executor.BaseExecutor.queryFromDatabase(BaseExecutor.java:324)
	at org.apache.ibatis.executor.BaseExecutor.query(BaseExecutor.java:156)
	at com.baomidou.mybatisplus.core.executor.MybatisCachingExecutor.query(MybatisCachingExecutor.java:155)
	at com.baomidou.mybatisplus.core.executor.MybatisCachingExecutor.query(MybatisCachingExecutor.java:90)
	... 46 more
}



Caused by: com.sky.common.exception.exception.UniteSqlException: SQL异常详情{系统名称='sky-user-unblock-timer', 异常点='com.sky.server.user.listen.domain.dao.PlatformRiskForbidRecordDao.updateById', 参数='{param1=PlatformRiskForbidRecord(forbidId=483, platformId=null, platformUid=null, platformName=null, brandNo=null, riskMark=null, riskType=null, riskCode=null, userId=null, memberIp=null, payWay=null, forbidType=3, forbidTime=null, reliefForbidTime=null, forbidStatus=null, forBidBizOrder=null, ruleDesc=null, riskPrompt=null, remark=null, operatorId=null, createTime=null, modifyTime=null), et=PlatformRiskForbidRecord(forbidId=483, platformId=null, platformUid=null, platformName=null, brandNo=null, riskMark=null, riskType=null, riskCode=null, userId=null, memberIp=null, payWay=null, forbidType=3, forbidTime=null, reliefForbidTime=null, forbidStatus=null, forBidBizOrder=null, ruleDesc=null, riskPrompt=null, remark=null, operatorId=null, createTime=null, modifyTime=null)}', 来源='UPDATE platform_risk_forbid_record  SET forbid_type=?  WHERE forbid_id=?', 类型='UPDATE', 异常信息='java.sql.SQLSyntaxErrorException: Unknown column 'forbid_type' in 'field list'', 异常时间='Thu Jul 23 17:31:47 CST 2020', 堆栈信息='java.lang.reflect.InvocationTargetException
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.base/java.lang.reflect.Method.invoke(Method.java:566)
	at org.apache.ibatis.plugin.Invocation.proceed(Invocation.java:49)
	at com.sky.common.exception.Interceptor.SqlExceptionInterceptor.intercept(SqlExceptionInterceptor.java:46)
	at org.apache.ibatis.plugin.Plugin.invoke(Plugin.java:61)
	at com.sun.proxy.$Proxy98.update(Unknown Source)
	at org.apache.ibatis.session.defaults.DefaultSqlSession.update(DefaultSqlSession.java:197)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.base/java.lang.reflect.Method.invoke(Method.java:566)
	at org.mybatis.spring.SqlSessionTemplate$SqlSessionInterceptor.invoke(SqlSessionTemplate.java:426)
	at com.sun.proxy.$Proxy81.update(Unknown Source)
	at org.mybatis.spring.SqlSessionTemplate.update(SqlSessionTemplate.java:287)
	at com.baomidou.mybatisplus.core.override.MybatisMapperMethod.execute(MybatisMapperMethod.java:65)
	at com.baomidou.mybatisplus.core.override.MybatisMapperProxy.invoke(MybatisMapperProxy.java:96)
	at com.sun.proxy.$Proxy86.updateById(Unknown Source)
	at com.sky.server.user.listen.infrastructure.repository.PlatformRiskForbidRepository.updateForbidStatus(PlatformRiskForbidRepository.java:29)
	at com.sky.server.user.listen.infrastructure.repository.PlatformRiskForbidRepository$$FastClassBySpringCGLIB$$31443c6e.invoke(<generated>)
	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:218)
	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:771)
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163)
	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:749)
	at org.springframework.dao.support.PersistenceExceptionTranslationInterceptor.invoke(PersistenceExceptionTranslationInterceptor.java:139)
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)
	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:749)
	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:691)
	at com.sky.server.user.listen.infrastructure.repository.PlatformRiskForbidRepository$$EnhancerBySpringCGLIB$$727fdd7f.updateForbidStatus(<generated>)
	at com.sky.server.user.listen.domain.service.MonitorForbidUserService.updateForBidStatus(MonitorForbidUserService.java:36)
	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
	at java.base/java.util.stream.ReferencePipeline$2$1.accept(ReferencePipeline.java:177)
	at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1654)
	at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:484)
	at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
	at java.base/java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:150)
	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:173)
	at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
	at java.base/java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:497)
	at com.sky.server.user.listen.domain.service.MonitorForbidUserService.monitorForbid(MonitorForbidUserService.java:31)
	at com.sky.server.user.listen.application.timer.DefaultTimer.run(DefaultTimer.java:20)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.base/java.lang.reflect.Method.invoke(Method.java:566)
	at org.springframework.scheduling.support.ScheduledMethodRunnable.run(ScheduledMethodRunnable.java:84)
	at org.springframework.scheduling.support.DelegatingErrorHandlingRunnable.run(DelegatingErrorHandlingRunnable.java:54)
	at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:515)
	at java.base/java.util.concurrent.FutureTask.runAndReset(FutureTask.java:305)
	at java.base/java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:305)
	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1128)
	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:628)
	at java.base/java.lang.Thread.run(Thread.java:834)
Caused by: java.sql.SQLSyntaxErrorException: Unknown column 'forbid_type' in 'field list'
	at com.mysql.cj.jdbc.exceptions.SQLError.createSQLException(SQLError.java:120)
	at com.mysql.cj.jdbc.exceptions.SQLError.createSQLException(SQLError.java:97)
	at com.mysql.cj.jdbc.exceptions.SQLExceptionsMapping.translateException(SQLExceptionsMapping.java:122)
	at com.mysql.cj.jdbc.ClientPreparedStatement.executeInternal(ClientPreparedStatement.java:953)
	at com.mysql.cj.jdbc.ClientPreparedStatement.execute(ClientPreparedStatement.java:370)
	at com.zaxxer.hikari.pool.ProxyPreparedStatement.execute(ProxyPreparedStatement.java:44)
	at com.zaxxer.hikari.pool.HikariProxyPreparedStatement.execute(HikariProxyPreparedStatement.java)
	at org.apache.ibatis.executor.statement.PreparedStatementHandler.update(PreparedStatementHandler.java:47)
	at org.apache.ibatis.executor.statement.RoutingStatementHandler.update(RoutingStatementHandler.java:74)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.base/java.lang.reflect.Method.invoke(Method.java:566)
	at org.apache.ibatis.plugin.Plugin.invoke(Plugin.java:63)
	at com.sun.proxy.$Proxy100.update(Unknown Source)
	at com.baomidou.mybatisplus.core.executor.MybatisSimpleExecutor.doUpdate(MybatisSimpleExecutor.java:54)
	at org.apache.ibatis.executor.BaseExecutor.update(BaseExecutor.java:117)
	at com.baomidou.mybatisplus.core.executor.MybatisCachingExecutor.update(MybatisCachingExecutor.java:83)
	... 54 more
}
```


​    
