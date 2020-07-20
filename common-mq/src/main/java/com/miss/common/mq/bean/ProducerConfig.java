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
public class ProducerConfig implements Serializable {
    private String exchangeName;

    private String queueName;

    private String routingKey;
}
