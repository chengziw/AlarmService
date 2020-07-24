package org.nom.mds.creator;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.nom.mds.spring.boot.autoconfigure.DataSourceProperty;
import org.nom.mds.spring.boot.autoconfigure.hikari.HikariCpConfig;

import javax.sql.DataSource;

/**
 * Hikari数据源创建器
 */
@Data
@AllArgsConstructor
public class HikariDataSourceCreator {

    private HikariCpConfig hikariCpConfig;

    public DataSource createDataSource(DataSourceProperty dataSourceProperty) {
        HikariConfig config = dataSourceProperty.getHikari().toHikariConfig(hikariCpConfig);
        config.setUsername(dataSourceProperty.getUsername());
        config.setPassword(dataSourceProperty.getPassword());
        config.setJdbcUrl(dataSourceProperty.getUrl());
        config.setDriverClassName(dataSourceProperty.getDriverClassName());
        config.setPoolName(dataSourceProperty.getPoolName());
        if (dataSourceProperty.getMaxPoolSize() != null)
            config.setMaximumPoolSize(dataSourceProperty.getMaxPoolSize());
        return new HikariDataSource(config);
    }
}
