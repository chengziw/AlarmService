package com.miss.server.alert;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.miss.common.mq.config.MqConfiguration;
import com.miss.server.alert.sdk.util.AnnotationBeanNameGeneratorRewrite;
import com.miss.server.alert.sdk.util.SpringContextUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;


@MapperScan(basePackages = "com.miss.server.alert.infrastructure.dao", basePackageClasses = BaseMapper.class)
@Import({MqConfiguration.class, SpringContextUtil.class})
@ComponentScan(nameGenerator = AnnotationBeanNameGeneratorRewrite.class)
@EnableAspectJAutoProxy
@SpringBootApplication
public class AlertApplication {

    public static void main(String[] args)
    {
        SpringApplication.run(AlertApplication.class, args);
        System.out.println("报警服务启动！！");
    }

}
