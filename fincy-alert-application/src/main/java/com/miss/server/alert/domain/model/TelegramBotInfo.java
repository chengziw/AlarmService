package com.miss.server.alert.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Accessors(chain = true)
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TelegramBotInfo implements Serializable {

	private String botName;

	private String token;

}
