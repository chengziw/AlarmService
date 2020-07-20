package com.miss.server.alert.sdk;

import com.miss.server.alert.sdk.bean.AlertParam;
import com.miss.server.alert.sdk.mq.AlertSdkMessageProducer;
import com.miss.server.alert.sdk.util.AnnotationBeanNameGeneratorRewrite;
import com.miss.server.alert.sdk.util.SpringContextUtil;
import com.miss.server.alert.sdk.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@Slf4j
@Import({SpringContextUtil.class})
@ComponentScan(nameGenerator = AnnotationBeanNameGeneratorRewrite.class, basePackages = {"com.miss.server.alert.sdk"})
public class MyAlertManager {

    private static AlertSdkMessageProducer alertSdkMessageProducer = null;

    static {
        try {
            alertSdkMessageProducer = SpringContextUtil.getBean(AlertSdkMessageProducer.class);
        } catch (Exception e) {
            log.error("报警服务",null,e,"没有找到MQ服务相关配置，无法发送报警信息到服务器");
        }
    }

    /**
     * 处理报警（接口调用）
     *
     * @param alertParam
     * @return
     */
    public static Result alert(AlertParam alertParam) {
        // 参数校验
        Result jsonResult = checkParam(alertParam);
        if (!jsonResult.isSuccess()) {
            return jsonResult;
        }

        alertSdkMessageProducer.toSendTelegramAlert(alertParam);
        return Result.success("ok");
    }

    private static Result checkParam(AlertParam alertParam) {
        if (null == alertParam.getAlertLevel()) {
            return Result.error("AlertLevel 不能为空");
        }

        if (StringUtils.isBlank(alertParam.getContent())) {
            return Result.error("Content 不能为空");
        } else if (alertParam.getContent().length() > 512) {
            return Result.error("Content 最长 512 位");
        }

        if (StringUtils.isNoneBlank(alertParam.getAppId())) {
            if (alertParam.getAppId().length() > 32) {
                return Result.error("AppId 最长 32 位");
            }
        }
        if (StringUtils.isNoneBlank(alertParam.getAppName())) {
            if (alertParam.getAppName().length() > 32) {
                return Result.error("AppName 最长 32 位");
            }
        }

        if (StringUtils.isNoneBlank(alertParam.getModuleName())) {
            if (alertParam.getModuleName().length() > 32) {
                return Result.error("ModuleName 最长 32 位");
            }
        }
        if (StringUtils.isNoneBlank(alertParam.getEventName())) {
            if (alertParam.getEventName().length() > 32) {
                return Result.error("EventName 最长 32 位");
            }
        }

        return Result.success("ok");
    }

}
