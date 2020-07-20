package com.miss.common.mq.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerConfig implements Serializable {
    /**
     * 交换机
     */
    private String exchangeName;

    /**
     * 队列名称
     */
    private String queueName;

    /**
     * 消息确认类型
     */
    private String confirmType;

    /**
     * 唯一标签
     */
    private String consumerTag;

    /**
     * 手动确认 异常情况消息处理
     */
    private String basicOrNack;
}
