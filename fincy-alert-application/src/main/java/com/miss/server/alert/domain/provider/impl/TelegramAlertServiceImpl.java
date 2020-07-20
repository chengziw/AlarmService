package com.miss.server.alert.domain.provider.impl;

import com.miss.server.alert.domain.repository.IAlertRepository;
import com.miss.server.alert.domain.model.TelegramBotInfo;
import com.miss.server.alert.domain.provider.SendAlertService;
import com.miss.server.alert.sdk.bean.AlertParamExtend;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.miss.server.alert.domain.model.TelegramGroupInfo;
import com.miss.server.alert.domain.util.TelegramAlertUtil;

import javax.annotation.Resource;

@Slf4j
@Service("telegramAlertServiceImpl")
public class TelegramAlertServiceImpl implements SendAlertService {

	@Resource
	private TelegramAlertUtil telegramAlertUtil;
	@Resource
	private IAlertRepository alertRepository;

	@Override
	public void sentAlertMsg(AlertParamExtend param, String groupName) {
		TelegramGroupInfo groupInfo = alertRepository.findByGroupName(groupName);
		if (null == groupInfo) {
			log.warn("报警服务",null,"没有找到:"+groupName,"群组配置信息");
			return;
		}

		TelegramBotInfo botInfo = alertRepository.findByBotName(groupInfo.getBotName());
		if (null == botInfo) {
			log.warn("报警服务",null,(">>>没有找到 " + groupInfo.getBotName() + " 机器人配置信息。"));
			return;
		}

		telegramAlertUtil.send(botInfo.getToken(), groupInfo.getGroupId(), formatContent(param));
	}

	@Override
	public String formatContent(AlertParamExtend param) {
		return "<" + param.getSystem() + ">【" + param.getModuleName() + "-" + param.getEventName() + "】"
				+ param.getContent();
	}

}
