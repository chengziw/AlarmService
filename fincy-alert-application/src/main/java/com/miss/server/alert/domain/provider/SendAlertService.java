package com.miss.server.alert.domain.provider;


import com.miss.server.alert.sdk.bean.AlertParamExtend;

public interface SendAlertService {

	/**
	 * 发送提醒消息
	 *
	 * @param param
	 */
	void sentAlertMsg(AlertParamExtend param, String groupName);

	/**
	 * 格式化发送内容
	 *
	 * @param param
	 * @return
	 */
	String formatContent(AlertParamExtend param);

}
