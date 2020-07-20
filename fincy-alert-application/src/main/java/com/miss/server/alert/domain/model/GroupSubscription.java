package com.miss.server.alert.domain.model;

import com.miss.server.alert.sdk.enums.AlertLevelEnum;
import com.miss.server.alert.sdk.enums.AlertTypeEnum;
import com.miss.server.alert.sdk.enums.PriorityLevelEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Accessors(chain = true)
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupSubscription implements Serializable {

    private Long id;

    private AlertTypeEnum alertType;

    private String groupName;

    private String appId;

    private String appName;

    private String moduleName;

    private String eventName;

    private AlertLevelEnum alertLevel;

    private PriorityLevelEnum priorityLevel;

    private Integer intervalInt;

}
