package com.miss.server.alert.domain.service;


import com.miss.server.alert.domain.model.GroupSubscription;
import com.miss.server.alert.domain.repository.IAlertRepository;
import com.miss.server.alert.infrastructure.model.AlertGroupSubscriptionBlack;
import com.miss.server.alert.sdk.bean.AlertParamExtend;
import com.miss.server.alert.sdk.util.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;


/**
 * 订阅信息的例外匹配装饰者
 */
@Slf4j
public class SubscriptionBlackDecorator extends CustomerDecorator {

    private List<AlertGroupSubscriptionBlack> blackSubList = new ArrayList<>();

    private IAlertRepository alertRepository;

    public SubscriptionBlackDecorator(ICustomer customer) {
        super(customer);
        try {
            alertRepository = (IAlertRepository) SpringContextUtil.getBean("alertRepository");
            blackSubList = alertRepository.findBlackSubscription(customer.getSubRecord().getId());
            log.debug("报警服务", null, " >>> 例外订阅条数 >>> " + blackSubList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void preSend(AlertParamExtend alertParamExtend) {
        // 匹配订阅例外
        if (isBlackMatch(alertParamExtend)) {
            log.debug("报警服务", null, alertParamExtend.getSerialNumber() + " 的消息被设置了订阅例外。>>> alertType："
                    + super.getSubRecord().getAlertType() + "；groupName：" + super.getSubRecord().getGroupName());
            return;
        }
        super.preSend(alertParamExtend);
    }

    /**
     * 是否在例外订阅中
     *
     * @param param
     * @return
     */
    private boolean isBlackMatch(AlertParamExtend param) {
        if (null == blackSubList || blackSubList.size() < 1) {
            return false;
        }

        for (AlertGroupSubscriptionBlack bean : blackSubList) {
            if (!param.getAppName().equalsIgnoreCase(bean.getAppName())
                    && !super.PLACE_HOLDER().equals(bean.getAppName())) {
                continue;
            }
            if (!param.getModuleName().equalsIgnoreCase(bean.getModuleName())
                    && !super.PLACE_HOLDER().equals(bean.getModuleName())) {
                continue;
            }
            if (!param.getEventName().equalsIgnoreCase(bean.getEventName())
                    && !super.PLACE_HOLDER().equals(bean.getEventName())) {
                continue;
            }
            if (!param.getAlertLevel().equals(bean.getAlertLevel())
                    && !super.PLACE_HOLDER().equals(bean.getAlertLevel())) {
                continue;
            }
            if (!param.getPriorityLevel().equals(bean.getPriorityLevel())
                    && !super.PLACE_HOLDER().equals(bean.getPriorityLevel())) {
                continue;
            }
            return true;
        }

        return false;
    }

    @Override
    public final void updateGroupSubscription(GroupSubscription sub) {
        super.updateGroupSubscription(sub);

        if (null == sub || null == super.getSubRecord()) {
            log.debug("报警服务", null, "订阅者对象没有正常初始化。更新例外订阅失败");
            return;
        }
        if (super.getSubRecord().getId().equals(sub.getId())) {
            blackSubList = alertRepository.findBlackSubscription(super.getSubRecord().getId());
            log.debug("报警服务", null, " >>> 例外订阅条数 >>> " + blackSubList.size());
        }
    }

}
