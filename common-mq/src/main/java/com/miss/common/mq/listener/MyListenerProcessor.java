package com.miss.common.mq.listener;

import com.miss.common.mq.bean.ConsumerConfig;
import com.miss.common.mq.config.QueuesPool;
import com.miss.common.mq.enums.ConfirmType;
import com.miss.common.mq.util.spring.MqSpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import com.miss.common.mq.annotation.MqConsumer;
import com.miss.common.mq.manage.MqManage;
import com.miss.common.mq.consumer.AbstractConsumer;

@Slf4j
@Component
public class MyListenerProcessor implements BeanPostProcessor {

    @Autowired
    private MqManage defaultMqManage;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        //查询消费者注解
        MqConsumer mqConsumer = AnnotationUtils.findAnnotation(bean.getClass(), MqConsumer.class);
        if (mqConsumer != null) {
            try {
                AbstractConsumer consumer = MqSpringUtil.getBean(beanName);
                ConsumerConfig queueBean = QueuesPool.getConsumerConfig(mqConsumer.tag());
                ConfirmType confirmType = StringUtils.isEmpty(queueBean.getConfirmType()) ? ConfirmType.AUTO : (queueBean.getConfirmType().equals("0") ? ConfirmType.AUTO : ConfirmType.MANUAL);
                //加入管理器
                defaultMqManage.add(queueBean, consumer, confirmType);
            } catch (Exception e) {
                log.error("common-mq",null,e,"消费者bean处理失败:{}", e);
            }
        }

        return bean;
    }

}
