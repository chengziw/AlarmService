package com.miss.server.alert.sdk.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class RabbitConfig {

    public static final String EXCHANGE_NAME = "exchangeToSky";

    @Bean
    public DirectExchange defaultExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    /////////////////////////////////////////////////////////////////////////////////////

    public static final String QUEUE_toSendAlert_NAME = "toSendAlert";

    public static final String ROUTING_toSendAlert_KEY = "toSendAlertKey";

    /**
     * QueueToSendAlert，构建队列，名称，是否持久化之类
     */
    @Bean
    public Queue queueToSendAlert() {
        return new Queue(QUEUE_toSendAlert_NAME, true);
    }

    /**
     * BindingToSendAlert，将DirectExchange与Queue进行绑定
     */
    @Bean
    public Binding bindingToSendAlert() {
        return BindingBuilder.bind(queueToSendAlert()).to(defaultExchange()).with(ROUTING_toSendAlert_KEY);
    }

}
