package com.miss.common.mq.send.impl;

import com.miss.common.mq.manage.MqManage;
import com.miss.common.mq.send.ISender;
import com.miss.common.mq.bean.ProducerConfig;
import com.miss.common.mq.config.QueuesPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

@Slf4j
public class Sender implements ISender {

    private AmqpTemplate amqpTemplate;
    private MqManage mqManage;

    public Sender(AmqpTemplate amqpTemplate, MqManage mqManage) {
        this.amqpTemplate = amqpTemplate;
        this.mqManage = mqManage;
    }

    @Override
    public boolean send(String exchangeName,String routingKey, String msgContent) throws Exception {
        ProducerConfig producerConfig = QueuesPool.getProducerConfig(exchangeName+"."+routingKey);
        if(producerConfig == null){
            log.warn("common-mq",null,"【MQ 发送消息】 发送失败 未获取到发送者配置");
            return false;
        }
        this.send(producerConfig.getExchangeName(), producerConfig.getQueueName(), msgContent, producerConfig.getRoutingKey(), 0, 0);
        return true;
    }

    private void send(String exchangeName, String queueName,
                        String msgContent, String routingKey, int expiration, int priority) throws Exception {

        if (StringUtils.isEmpty(queueName) || StringUtils.isEmpty(exchangeName) || StringUtils.isEmpty(routingKey)) {
            throw new Exception("queueName or exchangeName or routingKey can not be empty.");
        }

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
        messageProperties.setContentEncoding("UTF-8");
        if (expiration > 0) { // 过期时间
            messageProperties.setExpiration(String.valueOf(expiration));
        }

        if (priority > 0) { // 消息优先级
            messageProperties.setPriority(Integer.valueOf(priority));
        }

        Message message = new Message(msgContent.getBytes(), messageProperties);
        try {
            amqpTemplate.send(exchangeName, routingKey, message);
        } catch (AmqpException e) {
            log.error("common-mq",null,e,"send event fail. Event Message : [" + msgContent + "]", e);
            throw new Exception("send event fail", e);
        }
    }

}
