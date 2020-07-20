package com.miss.common.mq.manage;

import java.util.*;

import com.miss.common.mq.consumer.AbstractConsumer;
import com.miss.common.mq.bean.ConsumerConfig;
import com.miss.common.mq.bean.ProducerConfig;
import com.miss.common.mq.config.QueuesPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SerializerMessageConverter;

import com.miss.common.mq.config.MqConfig;
import com.miss.common.mq.enums.ConfirmType;
import com.miss.common.mq.error.MessageErrorHandler;
import com.miss.common.mq.send.ISender;
import com.miss.common.mq.send.impl.Sender;

@Slf4j
public class MqManage implements IMqManage {
    private static MqManage mqManage;

    private MqConfig config;

    private RabbitAdmin rabbitAdmin;

    private CachingConnectionFactory rabbitConnectionFactory;

    private static RabbitTemplate rabbitTemplate;

    private MessageConverter serializerMessageConverter = new SerializerMessageConverter();

    private Map<String, DirectExchange> directExchanges = new HashMap<>();

    private Map<String, Queue> queues = new HashMap<>();

    private Map<String, Queue> autoQueues = new HashMap<>();

    private Map<String, Queue> manualQueues = new HashMap<>();

    private ISender sender;

    private Map<String, SimpleMessageListenerContainer> autoContainerMap = new HashMap<>();
    private Map<String, SimpleMessageListenerContainer> manualContainerMap = new HashMap<>();

    public static synchronized MqManage getInstance(MqConfig config) {
        if (mqManage == null)
            mqManage = new MqManage(config);

        return mqManage;
    }

    private MqManage(MqConfig config) {
        if (config == null)
            throw new IllegalArgumentException("Mq Config can not be null");
        this.config = config;

        initRabbitConnectionFactory();

        rabbitAdmin = new RabbitAdmin(rabbitConnectionFactory);

        rabbitTemplate = new RabbitTemplate(rabbitConnectionFactory);
        rabbitTemplate.setMessageConverter(serializerMessageConverter);

        sender = new Sender(rabbitTemplate, this);
    }

    private void initRabbitConnectionFactory() {
        rabbitConnectionFactory = new CachingConnectionFactory();
        rabbitConnectionFactory.setHost(config.getHost());
        rabbitConnectionFactory.setPort(config.getPort());

        rabbitConnectionFactory.setChannelCacheSize(config.getMsgProcessNum());
        rabbitConnectionFactory.setUsername(config.getUserName());
        rabbitConnectionFactory.setPassword(config.getPassword());
        rabbitConnectionFactory.setVirtualHost(config.getVirtualHost());
    }

    public IMqManage add(ConsumerConfig queueBean, AbstractConsumer msgProcesser, ConfirmType confirmType) {
        declareBinding(queueBean.getExchangeName(), queueBean.getQueueName());
        Queue queue = queues.get(queueBean.getQueueName());
        if (confirmType.equals(ConfirmType.AUTO)) {
            autoQueues.put(queueBean.getQueueName(), queue);
            initMsgListenerAdapter(queueBean.getConsumerTag(), queueBean.getQueueName(), msgProcesser);
        } else if (confirmType.equals(ConfirmType.MANUAL)) {
            manualQueues.put(queueBean.getQueueName(), queue);
            initMsgListenerAdapterManual(queueBean.getConsumerTag(), queueBean.getQueueName(),msgProcesser);
        }
        return this;
    }


    private void initMsgListenerAdapter(String tag, String queueName, AbstractConsumer msgProcesser) {
        SimpleMessageListenerContainer container = autoContainerMap.get(tag);
        if (container == null) {
            container = new SimpleMessageListenerContainer();
        }

        containerDataFill(container,AcknowledgeMode.AUTO,autoQueues.get(queueName));

        container.setMessageListener(message -> {
            Message msg = new Message(new String(message.getBody()).getBytes(),null);
            msgProcesser.process(msg,null,ConfirmType.AUTO);
        });

        container.start();

        autoContainerMap.put(tag, container);
    }

    private void initMsgListenerAdapterManual(String tag, String queueName,AbstractConsumer consumer) {
        SimpleMessageListenerContainer container = manualContainerMap.get(tag);
        if (container == null) {
            container = new SimpleMessageListenerContainer();
        }

        containerDataFill(container,AcknowledgeMode.MANUAL,manualQueues.get(queueName));

        container.setMessageListener((ChannelAwareMessageListener) (message, channel) -> {
            consumer.process(message,channel,ConfirmType.MANUAL);
        });

        container.start();

        manualContainerMap.put(tag, container);
    }

    private void containerDataFill(SimpleMessageListenerContainer container,AcknowledgeMode acknowledgeMode,Queue queue) {
        container.setConnectionFactory(rabbitConnectionFactory);
        container.setAcknowledgeMode(acknowledgeMode);
        container.setErrorHandler(new MessageErrorHandler());
        container.setPrefetchCount(config.getPrefetchSize());
        container.setConcurrentConsumers(config.getMsgProcessNum());
        container.setTxSize(config.getPrefetchSize());
        container.setQueues(queue);
    }

    @Override
    public ISender getSender() {
        return sender;
    }



    /**
     * 生产者队列绑定处理
     */
    public void bindProducers() {
        List<ProducerConfig> producerConfigs = new ArrayList<>(QueuesPool.producerConfigMap.values());
        for (ProducerConfig config : producerConfigs) {
            declareBinding(config.getExchangeName(),config.getQueueName());
            Binding binding = BindingBuilder.bind(queues.get(config.getQueueName())).to(directExchanges.get(config.getExchangeName())).with(config.getRoutingKey());
            rabbitAdmin.declareBinding(binding);
        }
    }

    /**
     * 交换机 队列申明
     * @param exchangeName
     * @param queueName
     */
    public synchronized void declareBinding(String exchangeName, String queueName) {
        DirectExchange directExchange = directExchanges.get(exchangeName);
        if (directExchange == null) {
            directExchange = new DirectExchange(exchangeName, true, false);
            directExchanges.put(exchangeName, directExchange);
            rabbitAdmin.declareExchange(directExchange);
        }

        Queue queue = queues.get(queueName);
        if (queue == null) {
            queue = new Queue(queueName, true, false, false);
            queues.put(queueName, queue);
            rabbitAdmin.declareQueue(queue);
        }
    }

    /**
     * 停止消费
     * @param tag
     * @return
     */
    @Override
    public boolean stopConsumer(String tag) {
        log.info("【暂停消费】 消费者Tag：{}", tag);
        return consumerStatusChange(tag, "stop");
    }

    /**
     * 恢复消费
     * @param tag
     * @return
     */
    @Override
    public boolean resumeConsumer(String tag) {
        log.info("【恢复消费】 消费者Tag：{}", tag);
        return consumerStatusChange(tag, "start");
    }

    /**
     * 消费容器状态变更
     * @param tag
     * @param status
     * @return
     */
    private boolean consumerStatusChange(String tag, String status) {
        ConsumerConfig consumerConfig = QueuesPool.getConsumerConfig(tag);
        if (consumerConfig == null) {
            log.warn("【消费状态变更】 消费者Tag：{},未找到队列配置", tag);
            return false;
        }
        SimpleMessageListenerContainer container = getContainer(tag, consumerConfig.getConfirmType());
        if (container == null) {
            log.warn("【消费状态变更】 消费者Tag：{},未找到消费容器", tag);
            return false;
        }
        if (status.equalsIgnoreCase("start")) {
            container.start();
        } else if (status.equalsIgnoreCase("stop")) {
            container.stop();
        }
        return true;
    }

    /**
     * 获取容器
     * @param tag
     * @param confirmType
     * @return
     */
    private SimpleMessageListenerContainer getContainer(String tag, String confirmType) {
        if (confirmType.equalsIgnoreCase("1")) {
            return manualContainerMap.get(tag);
        } else {
            return autoContainerMap.get(tag);
        }
    }
}
