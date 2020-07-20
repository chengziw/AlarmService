package com.miss.server.alert.application.exception;

import com.miss.server.alert.sdk.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

@Aspect
@Slf4j
@Component
public class MyExceptionHandler {

    @Around("execution(public * com.miss.server.*.application.service..*.*(..))")
    public Result<Object> serviceAOP(ProceedingJoinPoint pjp) throws Throwable {
        try {
            return (Result) pjp.proceed();
        } catch (Exception e) {
            e.printStackTrace();

            log.error("报警服务",null,e,e.getMessage());

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PrintStream pout = new PrintStream(out);
            e.printStackTrace(pout);
            String ret = new String(out.toByteArray());
            pout.close();
            try {
                out.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.error(999, ret);
        }

    }

}
