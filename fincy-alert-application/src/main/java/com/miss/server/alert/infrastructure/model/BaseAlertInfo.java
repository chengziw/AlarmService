package com.miss.server.alert.infrastructure.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 通用消息体
 *
 * @Auther: liq
 * @Date: 2019/6/12 14:25
 * @Description:
 */
@Accessors(chain = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseAlertInfo implements Serializable {

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 模块名称
     */
    private String moduleName;

    /**
     * 事件名称
     */
    private String eventName;

    /**
     * 报警等级
     */
    private String alertLevel;

    /**
     * 优先等级
     */
    private String priorityLevel;


}
