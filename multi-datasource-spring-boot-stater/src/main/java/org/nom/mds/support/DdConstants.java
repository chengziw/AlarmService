package org.nom.mds.support;

/**
 * 动态数据源常量
 */
public class DdConstants {
    /**
     * 数据源：主库
     */
    public static final String MASTER = "master";
    /**
     * 数据源：从库
     */
    public static final String SLAVE = "slave";
    /**
     * HikariCp数据源
     */
    public static final String HIKARI_DATASOURCE = "com.zaxxer.hikari.HikariDataSource";

    private DdConstants() {
    }
}
