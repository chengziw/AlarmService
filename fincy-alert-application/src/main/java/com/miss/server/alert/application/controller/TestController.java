package com.miss.server.alert.application.controller;

import com.miss.server.alert.sdk.MyAlertManager;
import com.miss.server.alert.sdk.bean.AlertParam;
import com.miss.server.alert.sdk.enums.AlertLevelEnum;
import com.miss.server.alert.sdk.vo.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/send")
    public Result restart(HttpServletRequest request) throws Exception {
        String moduleName = request.getParameter("moduleName");
        String eventName = request.getParameter("eventName");
        String content = request.getParameter("content");

        AlertParam param = new AlertParam();
        param.setModuleName(moduleName);
        param.setEventName(eventName);
        param.setContent(content);

        param.setAlertLevel(AlertLevelEnum.INFO);

        return MyAlertManager.alert(param);
    }

}
