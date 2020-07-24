package org.nom.mds.spring.boot.autoconfigure;

import org.nom.mds.DynamicDataSourceConfigure;
import org.nom.mds.DynamicRoutingDataSource;
import org.nom.mds.aop.DynamicDataSourceAdvisor;
import org.nom.mds.aop.DynamicDataSourceAnnotationAdvisor;
import org.nom.mds.aop.DynamicDataSourceAnnotationInterceptor;
import org.nom.mds.processor.DsProcessor;
import org.nom.mds.processor.DsSpelExpressionProcessor;
import org.nom.mds.provider.DynamicDataSourceProvider;
import org.nom.mds.provider.YmlDynamicDataSourceProvider;
import org.nom.mds.strategy.DynamicDataSourceStrategy;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import javax.sql.DataSource;
import java.util.Map;

/**
 * 动态数据源核心自动配置类
 *
 * @see DynamicDataSourceProvider
 * @see DynamicDataSourceStrategy
 * @see DynamicRoutingDataSource
 */
@Slf4j
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(DynamicDataSourceProperties.class)
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@Import(value = {DynamicDataSourceCreatorAutoConfiguration.class})
@ConditionalOnProperty(prefix = DynamicDataSourceProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class DynamicDataSourceAutoConfiguration {

    private final DynamicDataSourceProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public DynamicDataSourceProvider dynamicDataSourceProvider() {
        Map<String, DataSourceProperty> dataSourceMap = properties.getDataSource();
        return new YmlDynamicDataSourceProvider(dataSourceMap);
    }

    @Bean
    @ConditionalOnMissingBean
    public DataSource dataSource(DynamicDataSourceProvider dynamicDataSourceProvider) {
        DynamicRoutingDataSource dataSource = new DynamicRoutingDataSource();
        dataSource.setPrimary(properties.getPrimary());
        dataSource.setStrict(properties.getStrict());
        dataSource.setStrategy(properties.getStrategy());
        dataSource.setProvider(dynamicDataSourceProvider);
        return dataSource;
    }

    @Bean
    @ConditionalOnMissingBean
    public DynamicDataSourceAnnotationAdvisor dynamicDatasourceAnnotationAdvisor(DsProcessor dsProcessor) {
        DynamicDataSourceAnnotationInterceptor interceptor = new DynamicDataSourceAnnotationInterceptor();
        interceptor.setDsProcessor(dsProcessor);
        DynamicDataSourceAnnotationAdvisor advisor = new DynamicDataSourceAnnotationAdvisor(interceptor);
        advisor.setOrder(properties.getOrder());
        return advisor;
    }

    @Bean
    @ConditionalOnMissingBean
    public DsProcessor dsProcessor() {
        return new DsSpelExpressionProcessor();
    }

    @Bean
    @ConditionalOnBean(DynamicDataSourceConfigure.class)
    public DynamicDataSourceAdvisor dynamicAdvisor(DynamicDataSourceConfigure dynamicDataSourceConfigure, DsProcessor dsProcessor) {
        DynamicDataSourceAdvisor advisor = new DynamicDataSourceAdvisor(dynamicDataSourceConfigure.getMatchers());
        advisor.setDsProcessor(dsProcessor);
        advisor.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return advisor;
    }
}
