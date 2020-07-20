package com.miss.server.alert.domain.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@EnableAsync
public class TelegramAlertUtil {

	private final String TELEGRAM_API = "https://api.telegram.org/bot%s/sendMessage";

	@Async
	public void send(String token, String chatId, String msg) {
		String url = String.format(TELEGRAM_API, token);

		JSONObject jsonParam = new JSONObject();
		jsonParam.put("chat_id", chatId);
		jsonParam.put("text", msg);

		String name = Thread.currentThread().getName();
		log.debug("报警服务",null,">>>>>" + name + "线程 执行发送");
		String result = null;
		try {
			result = HttpHelper.post(url, jsonParam);
		} catch (IOException e) {
			e.printStackTrace();
		}
		log.info("报警服务",null,"-----" + name + "线程 发送Telegram消息结果:-----" + result);
	}

}
