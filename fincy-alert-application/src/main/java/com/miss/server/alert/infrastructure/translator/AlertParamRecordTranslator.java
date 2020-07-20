package com.miss.server.alert.infrastructure.translator;

import com.miss.server.alert.infrastructure.model.AlertParamRecord;
import com.miss.server.alert.sdk.bean.AlertParamExtend;
import org.springframework.stereotype.Component;


@Component
public class AlertParamRecordTranslator {

    public AlertParamRecord convert(AlertParamExtend paramExtend) {
        if (null == paramExtend) {
            return null;
        }

        AlertParamRecord record = AlertParamRecord.builder()
                .serialNumber(paramExtend.getSerialNumber())
                .appId(paramExtend.getAppId())
                .content(paramExtend.getContent())
                .contentKey(paramExtend.getContentKey())
                .eventTime(paramExtend.getEventTime())
                .build();

        record.setAppName(paramExtend.getAppName());
        record.setModuleName(paramExtend.getModuleName());
        record.setEventName(paramExtend.getEventName());

        if (null != paramExtend.getAlertLevel()) {
            record.setAlertLevel(paramExtend.getAlertLevel().desc());
        }
        if (null != paramExtend.getPriorityLevel()) {
            record.setPriorityLevel(paramExtend.getPriorityLevel().desc());
        }

        return record;
    }

}
