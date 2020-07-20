package com.miss.server.alert.infrastructure.translator;

import com.miss.server.alert.domain.model.TelegramBotInfo;
import com.miss.server.alert.infrastructure.model.AlertTelegramBotInfo;
import org.springframework.stereotype.Component;


@Component
public class AlertTelegramBotInfoTranslator {

	public TelegramBotInfo convert(AlertTelegramBotInfo botInfo) {
		if (null == botInfo) {
			return null;
		}

		TelegramBotInfo bean = TelegramBotInfo.builder()
				.botName(botInfo.getBotName())
				.token(botInfo.getToken())
				.build();

		return bean;
	}

}
