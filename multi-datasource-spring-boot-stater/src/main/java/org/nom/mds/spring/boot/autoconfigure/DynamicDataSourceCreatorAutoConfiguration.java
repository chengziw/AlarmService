package org.nom.mds.spring.boot.autoconfigure;

import org.nom.mds.creator.BasicDataSourceCreator;
import org.nom.mds.creator.DataSourceCreator;
import org.nom.mds.creator.HikariDataSourceCreator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(DynamicDataSourceProperties.class)
public class DynamicDataSourceCreatorAutoConfiguration {

    private final DynamicDataSourceProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public DataSourceCreator dataSourceCreator() {
        DataSourceCreator dataSourceCreator = new DataSourceCreator();
        dataSourceCreator.setBasicDataSourceCreator(basicDataSourceCreator());
        dataSourceCreator.setHikariDataSourceCreator(hikariDataSourceCreator());
        return dataSourceCreator;
    }

    @Bean
    @ConditionalOnMissingBean
    public BasicDataSourceCreator basicDataSourceCreator() {
        return new BasicDataSourceCreator();
    }

    @Bean
    @ConditionalOnMissingBean
    public HikariDataSourceCreator hikariDataSourceCreator() {
        return new HikariDataSourceCreator(properties.getHikari());
    }
}
