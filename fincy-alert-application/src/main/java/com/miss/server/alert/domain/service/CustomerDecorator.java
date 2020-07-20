package com.miss.server.alert.domain.service;


import com.miss.server.alert.domain.model.GroupSubscription;
import com.miss.server.alert.sdk.bean.AlertParamExtend;

/**
 * 订阅者装饰者
 * 
 * @author liq
 * @Date: Jan 26, 2019 2:47:09 PM
 */
public class CustomerDecorator implements ICustomer {

	private ICustomer customer;

	public CustomerDecorator(ICustomer customer) {
		this.customer = customer;
	}

	@Override
	public GroupSubscription getSubRecord() {
		return this.customer.getSubRecord();
	}

	@Override
	public void preSend(AlertParamExtend alertParamExtend) {
		this.customer.preSend(alertParamExtend);
	}

	@Override
	public boolean equalsSub(GroupSubscription sub) {
		return this.customer.equalsSub(sub);
	}

	@Override
	public void updateGroupSubscription(GroupSubscription sub) {
		this.customer.updateGroupSubscription(sub);
	}

	@Override
	public String PLACE_HOLDER() {
		return this.customer.PLACE_HOLDER();
	}

}
