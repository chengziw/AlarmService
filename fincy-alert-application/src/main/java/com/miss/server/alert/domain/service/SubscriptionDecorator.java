package com.miss.server.alert.domain.service;


import com.miss.server.alert.sdk.bean.AlertParamExtend;
import com.miss.server.alert.sdk.enums.AlertLevelEnum;
import com.miss.server.alert.sdk.enums.PriorityLevelEnum;

/**
 * 订阅信息的匹配装饰者

 */
public class SubscriptionDecorator extends CustomerDecorator {

    public SubscriptionDecorator(ICustomer customer) {
        super(customer);
    }

    @Override
    public void preSend(AlertParamExtend alertParamExtend) {
        // 匹配订阅
        if (isMatchCustomer(alertParamExtend)) {
            super.preSend(alertParamExtend);
        }
    }

    /**
     * 是否匹配订阅规则
     *
     * @param param
     * @return
     */
    private boolean isMatchCustomer(AlertParamExtend param) {
        if (!param.getAppName().equalsIgnoreCase(super.getSubRecord().getAppName())
                && !super.PLACE_HOLDER().equals(super.getSubRecord().getAppName())) {
            return false;
        }
        if (!param.getModuleName().equalsIgnoreCase(super.getSubRecord().getModuleName())
                && !super.PLACE_HOLDER().equals(super.getSubRecord().getModuleName())) {
            return false;
        }
        if (!param.getEventName().equalsIgnoreCase(super.getSubRecord().getEventName())
                && !super.PLACE_HOLDER().equals(super.getSubRecord().getEventName())) {
            return false;
        }
        if (!param.getAlertLevel().equals(super.getSubRecord().getAlertLevel())
                && !AlertLevelEnum.ALL.equals(super.getSubRecord().getAlertLevel())) {
            return false;
        }
        if (!param.getPriorityLevel().equals(super.getSubRecord().getPriorityLevel())
                && !PriorityLevelEnum.ALL.equals(super.getSubRecord().getPriorityLevel())) {
            return false;
        }
        return true;
    }

}
