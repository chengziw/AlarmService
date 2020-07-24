package org.nom.mds.creator;

import org.nom.mds.spring.boot.autoconfigure.DataSourceProperty;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;

import static org.nom.mds.support.DdConstants.HIKARI_DATASOURCE;

/**
 * 数据源创建器
 */
@Slf4j
@Setter
public class DataSourceCreator {

    /**
     * 是否存在hikari
     */
    private static boolean hikariExists = false;

    static {
        try {
            Class.forName(HIKARI_DATASOURCE);
            hikariExists = true;
        } catch (ClassNotFoundException ignored) {
            // no op
        }
    }

    private BasicDataSourceCreator basicDataSourceCreator;
    private HikariDataSourceCreator hikariDataSourceCreator;

    /**
     * 创建数据源
     *
     * @param dataSourceProperty 数据源信息
     * @return 数据源
     */
    public DataSource createDataSource(DataSourceProperty dataSourceProperty) {
        DataSource dataSource;

        Class<? extends DataSource> type = dataSourceProperty.getType();
        if (type == null) {
            if (hikariExists) {
                dataSource = createHikariDataSource(dataSourceProperty);
            } else {
                dataSource = createBasicDataSource(dataSourceProperty);
            }
        } else if (HIKARI_DATASOURCE.equals(type.getName())) {
            dataSource = createHikariDataSource(dataSourceProperty);
        } else {
            dataSource = createBasicDataSource(dataSourceProperty);
        }

        return dataSource;
    }

    /**
     * 创建基础数据源
     *
     * @param dataSourceProperty 数据源参数
     * @return 数据源
     */
    public DataSource createBasicDataSource(DataSourceProperty dataSourceProperty) {
        return basicDataSourceCreator.createDataSource(dataSourceProperty);
    }

    /**
     * 创建Hikari数据源
     *
     * @param dataSourceProperty 数据源参数
     * @return 数据源
     */
    public DataSource createHikariDataSource(DataSourceProperty dataSourceProperty) {
        return hikariDataSourceCreator.createDataSource(dataSourceProperty);
    }
}
