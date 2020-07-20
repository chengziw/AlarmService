package com.miss.server.alert.infrastructure.translator;

import com.miss.server.alert.domain.model.TelegramGroupInfo;
import com.miss.server.alert.infrastructure.model.AlertTelegramGroupInfo;
import org.springframework.stereotype.Component;

@Component
public class AlertTelegramGroupInfoTranslator {

    public TelegramGroupInfo convert(AlertTelegramGroupInfo groupInfo) {
        if (null == groupInfo) {
            return null;
        }

        TelegramGroupInfo bean = TelegramGroupInfo.builder()
                .groupName(groupInfo.getGroupName())
                .botName(groupInfo.getBotName())
                .groupId(groupInfo.getGroupId())
                .build();

        return bean;
    }

}
