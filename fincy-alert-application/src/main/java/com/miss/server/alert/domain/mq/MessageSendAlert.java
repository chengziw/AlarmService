package com.miss.server.alert.domain.mq;

import com.alibaba.fastjson.JSON;
import com.miss.common.mq.annotation.MqConsumer;
import com.miss.common.mq.consumer.AbstractConsumer;
import com.miss.server.alert.domain.service.AlertDomainService;
import com.miss.server.alert.sdk.bean.AlertParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Component
@Slf4j
@MqConsumer(tag = "skyAlert")
public class MessageSendAlert extends AbstractConsumer {

    @Resource
    private AlertDomainService alertDomainService;

    @Override
    protected boolean onReceive(String message) {
        try {
            log.debug("报警服务", null, "消费message：{}", message);
            AlertParam param = JSON.parseObject(message, AlertParam.class);
            param.setCreated(new Date());

            alertDomainService.sendAlertMsg(param);
        } catch (Exception e) {
            log.error("报警服务", null, e, "接收通知消息失败。" + e.getMessage(), e);
        }
        return false;
    }
}
