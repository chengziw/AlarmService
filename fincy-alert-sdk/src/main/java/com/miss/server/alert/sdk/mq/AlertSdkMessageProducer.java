package com.miss.server.alert.sdk.mq;

import com.alibaba.fastjson.JSON;
import com.miss.common.mq.manage.IMqManage;
import com.miss.common.mq.send.ISender;
import com.miss.server.alert.sdk.config.RabbitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AlertSdkMessageProducer {

    @Autowired
    private IMqManage mqManage;


    /**
     * 发送telegram消息
     *
     * @param message
     */
    public void toSendTelegramAlert(Object message) {
        String msg = JSON.toJSONString(message);
        try {
            log.info("报警服务", null, "-----发送telegram消息入队列啦-----" + msg);

            ISender sender = mqManage.getSender();
            sender.send(RabbitConfig.EXCHANGE_NAME, RabbitConfig.ROUTING_toSendAlert_KEY, msg);
        } catch (Exception e) {
            log.error("报警服务", null, e, "发送telegram消息操作异常:" + msg);
        }
    }

}
