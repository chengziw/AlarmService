package com.miss.server.alert.sdk.bean;


import com.miss.server.alert.sdk.enums.AlertLevelEnum;
import com.miss.server.alert.sdk.enums.PriorityLevelEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;


@Accessors(chain = true)
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertParam implements Serializable {

    /**
     * 应用Id
     */
    private String appId;

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
     * 报警级别
     */
    private AlertLevelEnum alertLevel;

    /**
     * 优先级
     */
    private PriorityLevelEnum priorityLevel;

    /**
     * 事件内容
     */
    private String content;

    /**
     * 事件时间
     */
    private Date eventTime;

    /**
     * 创建时间
     */
    private Date created;

}
