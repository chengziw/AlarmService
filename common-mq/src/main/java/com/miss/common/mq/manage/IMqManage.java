package com.miss.common.mq.manage;

import com.miss.common.mq.send.ISender;

public interface IMqManage {
	ISender getSender();

	boolean stopConsumer(String tag);

	boolean resumeConsumer(String tag);
}
