package com.miss.server.alert.domain.service;


import com.miss.server.alert.domain.model.GroupSubscription;
import com.miss.server.alert.sdk.bean.AlertParamExtend;

/**
 * 订阅者
 * 
 * @author liq
 * @Date: Jan 26, 2019 11:50:04 AM
 */
public interface ICustomer {

	/**
	 * 获取订阅者的订阅信息
	 * 
	 * @return
	 */
	GroupSubscription getSubRecord();

	/**
	 * 预发送
	 * 
	 * @param alertParamExtend
	 */
	void preSend(AlertParamExtend alertParamExtend);

	/**
	 * 对比订阅者的订阅信息是否相同
	 * 
	 * @param sub
	 * @return
	 */
	boolean equalsSub(GroupSubscription sub);

	/**
	 * 订阅信息Id相同时，可更新订阅者的订阅信息
	 * 
	 * @param sub
	 */
	void updateGroupSubscription(GroupSubscription sub);

	/**
	 * 通配符
	 *
	 * @return
	 */
	String PLACE_HOLDER();
	
}
