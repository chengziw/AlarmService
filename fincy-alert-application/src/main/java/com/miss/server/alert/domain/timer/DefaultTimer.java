package com.miss.server.alert.domain.timer;

import com.miss.server.alert.domain.service.AlertDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class DefaultTimer {

    @Resource
    private AlertDomainService alertDomainService;

    /**
     * 同步刷新mq数据
     */
    public void run() {
        try {
            log.debug("报警服务",null,"同步订阅者列表");
            alertDomainService.init();
        } catch (Exception e) {
            log.error("报警服务",null,e,"同步订阅者列表异常");
        }
    }
}
