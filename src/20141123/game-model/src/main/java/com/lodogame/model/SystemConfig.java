package com.lodogame.model;

/**
 * 系统配置
 * 
 * @author chenjian
 * 
 */
public class SystemConfig {
	/**
	 * 是否开启ack
	 */
	private int ack;
	/**
	 * 是否开启request time
	 */
	private int requestTime;

	public int getAck() {
		return ack;
	}

	public void setAck(int ack) {
		this.ack = ack;
	}

	public int getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(int requestTime) {
		this.requestTime = requestTime;
	}

}
