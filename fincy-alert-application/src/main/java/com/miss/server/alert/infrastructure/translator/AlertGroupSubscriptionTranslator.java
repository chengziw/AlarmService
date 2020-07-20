package com.miss.server.alert.infrastructure.translator;

import com.miss.server.alert.domain.model.GroupSubscription;
import com.miss.server.alert.infrastructure.model.AlertGroupSubscription;
import com.miss.server.alert.sdk.enums.AlertLevelEnum;
import com.miss.server.alert.sdk.enums.AlertTypeEnum;
import com.miss.server.alert.sdk.enums.PriorityLevelEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class AlertGroupSubscriptionTranslator {

    public GroupSubscription convert(AlertGroupSubscription sub) {
        if (null == sub) {
            return null;
        }

        GroupSubscription bean = GroupSubscription.builder()
                .id(sub.getId())
                .groupName(sub.getGroupName())
                .appId(sub.getAppId())
                .appName(sub.getAppName())
                .moduleName(sub.getModuleName())
                .eventName(sub.getEventName())
                .intervalInt(sub.getIntervalInt())
                .build();

        if (StringUtils.isNoneBlank(sub.getAlertType())) {
            bean.setAlertType(AlertTypeEnum.getEnum(sub.getAlertType()));
        }
        if (StringUtils.isNoneBlank(sub.getAlertLevel())) {
            bean.setAlertLevel(AlertLevelEnum.getEnum(sub.getAlertLevel()));
        }
        if (StringUtils.isNoneBlank(sub.getPriorityLevel())) {
            bean.setPriorityLevel(PriorityLevelEnum.getEnum(sub.getPriorityLevel()));
        }

        return bean;
    }

    public List<GroupSubscription> convert(List<AlertGroupSubscription> subList) {
        if (subList != null && subList.size() > 0) {
            List<GroupSubscription> list = new ArrayList<>();
            subList.parallelStream().forEach(sub -> {
                list.add(convert(sub));
            });
            return list;
        }
        return null;
    }

}
