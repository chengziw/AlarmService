package com.miss.common.mq.consumer;

import com.miss.common.mq.enums.ConfirmType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;

import com.rabbitmq.client.Channel;
@Slf4j
public abstract class AbstractConsumer {

	protected abstract boolean onReceive(String msg);

	public void process(Message msg, Channel channel, ConfirmType confirmType){
		if(confirmType.equals(ConfirmType.AUTO)){
			processAuto(new String(msg.getBody()));
		}else {
			processManual(msg,channel);
		}
	}

	/**
	 * 自动确认
	 * @param message
	 */
	private void processAuto(String message) {
		log.info("【消费者】 【自动确认】 接收到消息：{}",message);
		onReceive(message);
	}

	private void processManual(Message message,Channel channel) {
		try {
			String msgContent = new String(message.getBody());
			log.info("【消费者】 【手动确认】 接收到消息：{}",msgContent);
			boolean flag = onReceive(msgContent);
			if(flag) {
				//业务执行正确
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			}else {
				log.warn("【消费者】 【手动确认】 业务执行返回false");
				try {
					channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
				}catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}catch(Exception e) {
			try {
				channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
			}catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
}
