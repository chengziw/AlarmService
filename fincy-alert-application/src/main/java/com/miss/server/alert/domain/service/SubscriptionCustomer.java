package com.miss.server.alert.domain.service;

import com.miss.server.alert.domain.provider.SendAlertService;
import com.miss.server.alert.domain.model.GroupSubscription;
import com.miss.server.alert.domain.util.DateUtil;
import com.miss.server.alert.sdk.bean.AlertParamExtend;
import com.miss.server.alert.sdk.enums.AlertTypeEnum;
import com.miss.server.alert.sdk.util.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 订阅者
 */
@Slf4j
public class SubscriptionCustomer implements ICustomer {

    // 默认项
    private static final AlertTypeEnum alertType = AlertTypeEnum.TELEGRAM;
    private static final String PLACE_HOLDER = "*";

    private List<AlertParamExtend> historyRecordList;

    private GroupSubscription subRecord;

    private SendAlertService sendAlertService;

    public SubscriptionCustomer(GroupSubscription data) {
        subRecord = data;

        historyRecordList = new ArrayList<>();
        try {
            sendAlertService = (SendAlertService) SpringContextUtil
                    .getBean(subRecord.getAlertType().desc().toLowerCase() + "AlertServiceImpl");
            if (null == sendAlertService) {
                sendAlertService = (SendAlertService) SpringContextUtil.getBean(alertType.desc().toLowerCase() + "AlertServiceImpl");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取订阅者的订阅信息
     *
     * @return
     */
    @Override
    public final GroupSubscription getSubRecord() {
        return subRecord;
    }

    /**
     * 预发送
     *
     * @param alertParamExtend
     */
    @Override
    public final void preSend(AlertParamExtend alertParamExtend) {
        if (!isMatchRule(alertParamExtend)) {
            log.warn("报警服务",null,alertParamExtend.getSerialNumber() + "的消息不符合发送频率。>>> alertType：" + subRecord.getAlertType()
                    + "；groupName：" + subRecord.getGroupName());
            return;
        }

        log.info("报警服务",null,"执行发送 >>> " + alertParamExtend.getSerialNumber() + "已匹配 >>> alertType："
                + subRecord.getAlertType() + "；groupName：" + subRecord.getGroupName());
        sendAlertService.sentAlertMsg(alertParamExtend, subRecord.getGroupName());
    }

    // 依据发送历史记录，是否匹配发送规则
    private boolean isMatchRule(AlertParamExtend target) {
        if (null == target) {
            return false;
        }
        if (null == historyRecordList || historyRecordList.size() < 1) {
            historyRecordList.add(target);
            return true;
        }

        log.info("报警服务",null,"发送历史记录条数 >>> " + historyRecordList.size());

        // 操作前，historyRecordList加智能锁
        List<AlertParamExtend> synhistoryRecordList = Collections.synchronizedList(historyRecordList);

        for (AlertParamExtend record : historyRecordList) {
            if (record.equals(target)) {
                if (DateUtil.compareDateMinute(target.getCreated(), record.getCreated()) > subRecord.getIntervalInt()) {
                    synchronized (historyRecordList) {
                        synhistoryRecordList.remove(record);
                        synhistoryRecordList.add(target);
                    }
                    return true;
                } else {
                    return false;
                }
            }
        }

        historyRecordList.add(target);
        return true;
    }

    /**
     * 对比订阅者的订阅信息是否为同一条
     *
     * @param sub
     * @return
     */
    @Override
    public final boolean equalsSub(GroupSubscription sub) {
        if (null == sub || null == subRecord) {
            return false;
        }

        if (subRecord.getId().equals(sub.getId())) {
            return true;
        }

        return false;
    }

    /**
     * 订阅信息Id相同时，可更新订阅者的订阅信息
     *
     * @param sub
     */
    @Override
    public final void updateGroupSubscription(GroupSubscription sub) {
        if (null == sub || null == subRecord) {
            log.warn("报警服务",null,"订阅者对象没有正常初始化。更新订阅信息失败");
            return;
        }
        if (subRecord.getId().equals(sub.getId())) {
            subRecord = sub;
        }
    }

    /**
     * 通配符
     */
    @Override
    public String PLACE_HOLDER() {
        return PLACE_HOLDER;
    }

}
