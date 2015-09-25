package com.lodogame.game.server.vo;


/**
 * 用户session值对象，用于保存用户UID和SessionId，以及对应的服务器信息
 * 
 * @author CJ
 * 
 */
public class UserSession {
	private int userId;
	private String sessionId;
	private String host;
	private int port;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
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

	@Override
	public String toString() {
		return "UserSession [userId=" + userId + ", sessionId=" + sessionId + ", host=" + host + ", port=" + port + "]";
	}

}
