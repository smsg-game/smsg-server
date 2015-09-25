package com.lodogame.game.connector;

public class ServerConnectorConfig {
	/**
	 * 目标服务器HOST
	 */
	private String host;
	
	/**
	 * 目标服务器端口
	 */
	private int port;
	
	/**
	 * 禁用Nagle 算法，适用于交互性强的客户端
	 */
	private boolean tcpNoDelay = true;
	
	/**
	 * 是否使用keepAlive
	 */
	private boolean keepalive = true;

	/**
	 *	连接数量
	 */
	private int connNum;
	
	public boolean isTcpNoDelay() {
		return tcpNoDelay;
	}

	public void setTcpNoDelay(boolean tcpNoDelay) {
		this.tcpNoDelay = tcpNoDelay;
	}

	public boolean isKeepalive() {
		return keepalive;
	}

	public void setKeepalive(boolean keepalive) {
		this.keepalive = keepalive;
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

	public int getConnNum() {
		return connNum;
	}

	public void setConnNum(int connNum) {
		this.connNum = connNum;
	}
}
