package org.nom.mds.spring.boot.autoconfigure;

import org.nom.mds.spring.boot.autoconfigure.hikari.HikariCpConfig;
import org.nom.mds.toolkit.CryptoUtils;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.properties.PropertyValueEncryptionUtils;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import javax.sql.DataSource;

@Slf4j
@Data
@Accessors(chain = true)
public class DataSourceProperty {

    /**
     * 连接池名称(只是一个名称标识)</br> 默认是配置文件上的名称
     */
    private String poolName;
    /**
     * 连接池类型，如果不设置自动查找 HikariCp
     */
    private Class<? extends DataSource> type;
    /**
     * JDBC driver
     */
    private String driverClassName;
    /**
     * JDBC url 地址
     */
    private String url;
    /**
     * JDBC 用户名
     */
    private String username;
    /**
     * JDBC 密码
     */
    private String password;
    /**
     * 连接池大小
     */
    private Integer maxPoolSize;
    /**
     * HikariCp参数配置
     */
    @NestedConfigurationProperty
    private HikariCpConfig hikari = new HikariCpConfig();

    public String getUrl() {
        return decrypt(url);
    }

    public String getUsername() {
        return decrypt(username);
    }

    public String getPassword() {
        return decrypt(password);
    }

    /**
     * 字符串解密
     */
    private String decrypt(String cipherText) {
        if (!PropertyValueEncryptionUtils.isEncryptedValue(cipherText)) {
            return cipherText;
        }
        return PropertyValueEncryptionUtils.decrypt(cipherText, CryptoUtils.getEnc());
    }
}
