package com.sky.common.exception.domain;

import java.io.Serializable;
import java.util.Date;

public class SqlExceptionLog implements Serializable {

    /**
     *系统名称
     */
    private String systemName;
    /**
     *sqlId
     */
    private String sqlId;
    /**
     * SQL参数
     */
    private String sqlParameter;
    /**
     * SQL语句
     */
    private String sqlSource;
    /**
     * SQL类型
     */
    private String sqlType;
    /**
     * 异常简要信息
     */
    private String exceptionMessage;
    /**
     * 堆栈信息
     */
    private String exceptionStack;
    /**
     * 创建时间
     */
    private Date createTime;

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getSqlId() {
        return sqlId;
    }

    public void setSqlId(String sqlId) {
        this.sqlId = sqlId;
    }

    public String getSqlParameter() {
        return sqlParameter;
    }

    public void setSqlParameter(String sqlParameter) {
        this.sqlParameter = sqlParameter;
    }

    public String getSqlSource() {
        return sqlSource;
    }

    public void setSqlSource(String sqlSource) {
        this.sqlSource = sqlSource;
    }

    public String getSqlType() {
        return sqlType;
    }

    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public String getExceptionStack() {
        return exceptionStack;
    }

    public void setExceptionStack(String exceptionStack) {
        this.exceptionStack = exceptionStack;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "SQL异常详情{" +
                "系统名称='" + systemName + '\'' +
                ", 异常点='" + sqlId + '\'' +
                ", 参数='" + sqlParameter + '\'' +
                ", 来源='" + sqlSource + '\'' +
                ", 类型='" + sqlType + '\'' +
                ", 异常信息='" + exceptionMessage + '\'' +
                ", 异常时间='" + createTime + '\'' +
                ", 堆栈信息='" + exceptionStack +
                '}';
    }
}
