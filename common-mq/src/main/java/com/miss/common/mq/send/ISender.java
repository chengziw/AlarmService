package com.miss.common.mq.send;

public interface ISender {
	boolean send(String exchangeName,String routingKey, String msgContent) throws Exception;
}
