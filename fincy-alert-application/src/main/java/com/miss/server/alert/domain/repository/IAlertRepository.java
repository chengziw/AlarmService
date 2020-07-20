package com.miss.server.alert.domain.repository;



import com.miss.server.alert.infrastructure.model.AlertGroupSubscriptionBlack;
import com.miss.server.alert.domain.model.GroupSubscription;
import com.miss.server.alert.domain.model.TelegramBotInfo;
import com.miss.server.alert.domain.model.TelegramGroupInfo;
import com.miss.server.alert.infrastructure.model.AlertGroupSubscriptionLazy;
import com.miss.server.alert.sdk.bean.AlertParamExtend;

import java.util.List;

public interface IAlertRepository {

	/**
	 * 获取全部订阅列表
	 * 
	 * @return
	 */
	List<GroupSubscription> findAllGroupSubscriptionList();

	/**
	 * 获取群信息
	 * 
	 * @param groupName
	 * @return
	 */
	TelegramGroupInfo findByGroupName(String groupName);

	/**
	 * 获取机器人配置
	 * 
	 * @param botName
	 * @return
	 */
	TelegramBotInfo findByBotName(String botName);

	/**
	 * 保存消息
	 * 
	 * @param alertParam
	 */
	void saveAlertRecord(AlertParamExtend alertParam);

	/**
	 * 获取订阅关系的例外列表
	 * 
	 * @param subId
	 * @return
	 */
	List<AlertGroupSubscriptionBlack> findBlackSubscription(Long subId);

	/**
	 * 获取延迟发送订阅列表
	 * 
	 * @return
	 */
	List<AlertGroupSubscriptionLazy> findLazyList();

}
