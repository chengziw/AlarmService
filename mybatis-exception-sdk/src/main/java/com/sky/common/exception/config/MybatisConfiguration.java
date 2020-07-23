package com.sky.common.exception.config;



import com.sky.common.exception.Interceptor.SqlExceptionInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis配置
 */
@Configuration
public class MybatisConfiguration {

    @Bean
    public SqlExceptionInterceptor sqlExceptionInterceptor() {
        return new SqlExceptionInterceptor();
    }
}
