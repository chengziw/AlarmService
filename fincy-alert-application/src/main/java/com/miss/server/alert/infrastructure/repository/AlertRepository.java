package com.miss.server.alert.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.miss.server.alert.domain.model.GroupSubscription;
import com.miss.server.alert.domain.model.TelegramBotInfo;
import com.miss.server.alert.domain.model.TelegramGroupInfo;
import com.miss.server.alert.domain.repository.IAlertRepository;
import com.miss.server.alert.infrastructure.dao.*;
import com.miss.server.alert.infrastructure.model.*;
import com.miss.server.alert.infrastructure.translator.AlertGroupSubscriptionTranslator;
import com.miss.server.alert.infrastructure.translator.AlertParamRecordTranslator;
import com.miss.server.alert.infrastructure.translator.AlertTelegramBotInfoTranslator;
import com.miss.server.alert.infrastructure.translator.AlertTelegramGroupInfoTranslator;
import com.miss.server.alert.sdk.bean.AlertParamExtend;
import org.springframework.stereotype.Repository;


import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


@Repository("alertRepository")
public class AlertRepository implements IAlertRepository {

    @Resource
    private AlertGroupSubscriptionDao alertGroupSubscriptionDao;
    @Resource
    private AlertTelegramGroupInfoDao alertTelegramGroupInfoDao;
    @Resource
    private AlertTelegramBotInfoDao alertTelegramBotInfoDao;
    @Resource
    private AlertParamRecordDao alertParamRecordDao;
    @Resource
    private AlertGroupSubscriptionBlackDao alertGroupSubscriptionBlackDao;
    @Resource
    private AlertGroupSubscriptionLazyDao alertGroupSubscriptionLazyDao;

    @Resource
    private AlertGroupSubscriptionTranslator alertGroupSubscriptionTranslator;
    @Resource
    private AlertTelegramGroupInfoTranslator alertTelegramGroupInfoTranslator;
    @Resource
    private AlertTelegramBotInfoTranslator alertTelegramBotInfoTranslator;
    @Resource
    private AlertParamRecordTranslator alertParamRecordTranslator;

    @Override
    public List<GroupSubscription> findAllGroupSubscriptionList() {
        return alertGroupSubscriptionTranslator.convert(alertGroupSubscriptionDao.selectList(new QueryWrapper<AlertGroupSubscription>().lambda()));
    }

    @Override
    public TelegramGroupInfo findByGroupName(String groupName) {
        LambdaQueryWrapper<AlertTelegramGroupInfo> wrapper = new QueryWrapper<AlertTelegramGroupInfo>().lambda()
                .eq(AlertTelegramGroupInfo::getGroupName, groupName);

        return alertTelegramGroupInfoTranslator.convert(alertTelegramGroupInfoDao.selectOne(wrapper));
    }

    @Override
    public TelegramBotInfo findByBotName(String botName) {
        LambdaQueryWrapper<AlertTelegramBotInfo> wrapper = new QueryWrapper<AlertTelegramBotInfo>().lambda()
                .eq(AlertTelegramBotInfo::getBotName, botName);

        return alertTelegramBotInfoTranslator.convert(alertTelegramBotInfoDao.selectOne(wrapper));
    }

    @Override
    public void saveAlertRecord(AlertParamExtend alertParam) {
        AlertParamRecord record = alertParamRecordTranslator.convert(alertParam);
        if (null == record) {
            return;
        }

        Date currentTime = new Date();
        record.setCreateTime(currentTime);
        record.setModifyTime(currentTime);

        alertParamRecordDao.insert(record);
    }

    @Override
    public List<AlertGroupSubscriptionBlack> findBlackSubscription(Long subId) {
        LambdaQueryWrapper<AlertGroupSubscriptionBlack> wrapper = new QueryWrapper<AlertGroupSubscriptionBlack>().lambda()
                .eq(AlertGroupSubscriptionBlack::getSubId, subId);

        return alertGroupSubscriptionBlackDao.selectList(wrapper);
    }

    @Override
    public List<AlertGroupSubscriptionLazy> findLazyList() {
        return alertGroupSubscriptionLazyDao.selectList(new QueryWrapper<AlertGroupSubscriptionLazy>().lambda());
    }

}
