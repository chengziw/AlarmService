package com.miss.common.mq.config;

import com.miss.common.mq.bean.ConsumerConfig;
import com.miss.common.mq.bean.ProducerConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 配置初始化
 */
public class QueuesPool {
    public static Map<String, ConsumerConfig> consumerConfigMap = new ConcurrentHashMap<>();
    public static Map<String, ProducerConfig> producerConfigMap = new ConcurrentHashMap<>();

    public static void setConsumerConfig(Map<String, ConsumerConfig> consumerConfig) {
        consumerConfigMap.putAll(consumerConfig);
    }

    public static void setProducerConfig(Map<String, ProducerConfig> producerConfig) {
        producerConfigMap.putAll(producerConfig);
    }

    public static ConsumerConfig getConsumerConfig(String tag) {
        return consumerConfigMap.get(tag);
    }

    public static ProducerConfig getProducerConfig(String key) {
        return producerConfigMap.get(key);
    }

}
