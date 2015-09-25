package com.lodogame.model;

import java.io.Serializable;

/**
 * 记录用户通关次数
 * @author chenjian
 *
 */
public class UserForcesCount implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String userId;
	
	private int forcesCount;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getForcesCount() {
		return forcesCount;
	}

	public void setForcesCount(int forcesCount) {
		this.forcesCount = forcesCount;
	}

}
