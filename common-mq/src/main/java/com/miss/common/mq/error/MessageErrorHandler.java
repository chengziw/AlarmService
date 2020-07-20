package com.miss.common.mq.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ErrorHandler;

@Slf4j
public class MessageErrorHandler implements ErrorHandler {

    public void handleError(Throwable t) {
        log.error("common-mq",null,t,"RabbitMQ happen a error:{}" + t.getMessage(), t);
    }

}
