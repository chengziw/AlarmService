package com.miss.common.mq.config;

public class MqConfig {

	private static final int DEFAULT_PROCESS_THREAD_NUM = Runtime.getRuntime().availableProcessors() * 2;

	private static final int PREFETCH_SIZE = 1;

	private String host;

	private int port;

	private String userName;

	private String password;

	private String virtualHost;

	private int connectionTimeout;

	private int msgProcessNum;

	private int prefetchSize;

	public MqConfig(String host, int port, String username, String password, String virtualHost) {
		this(host, port, username, password, virtualHost, 0, 1, 1);
	}

	private MqConfig(String host, int port, String userName, String password, String virtualHost, int connectionTimeout,
			int msgProcessNum, int prefetchSize) {
		this.host = host;
		this.port = port;
		this.userName = userName;
		this.password = password;
		this.virtualHost = virtualHost;
		this.connectionTimeout = connectionTimeout > 0 ? connectionTimeout : 0;
		this.msgProcessNum = msgProcessNum > 0 ? msgProcessNum : DEFAULT_PROCESS_THREAD_NUM;
		this.prefetchSize = prefetchSize > 0 ? prefetchSize : PREFETCH_SIZE;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getVirtualHost() {
		return virtualHost;
	}

	public void setVirtualHost(String virtualHost) {
		this.virtualHost = virtualHost;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public int getMsgProcessNum() {
		return msgProcessNum;
	}

	public void setMsgProcessNum(int msgProcessNum) {
		this.msgProcessNum = msgProcessNum;
	}

	public int getPrefetchSize() {
		return prefetchSize;
	}

	public void setPrefetchSize(int prefetchSize) {
		this.prefetchSize = prefetchSize;
	}
}
