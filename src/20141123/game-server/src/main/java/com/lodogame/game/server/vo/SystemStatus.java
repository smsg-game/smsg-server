package com.lodogame.game.server.vo;

import java.io.Serializable;

public class SystemStatus implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//总连接数
	public int connCount;
	
	//服务器连接数
	public int serverConnCount;
	
	//服务器连接结构
	public String serverStruts;


	public String getServerStruts() {
		return serverStruts;
	}

	public void setServerStruts(String serverStruts) {
		this.serverStruts = serverStruts;
	}

	public int getConnCount() {
		return connCount;
	}

	public void setConnCount(int connCount) {
		this.connCount = connCount;
	}

	public int getServerConnCount() {
		return serverConnCount;
	}

	public void setServerConnCount(int serverConnCount) {
		this.serverConnCount = serverConnCount;
	}
	
}
