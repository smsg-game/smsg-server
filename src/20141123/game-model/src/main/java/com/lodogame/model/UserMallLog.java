package com.lodogame.model;

import java.util.Date;

public class UserMallLog {

	private String userId;

	private int mallId;

	private int num;

	private Date createdTime;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getMallId() {
		return mallId;
	}

	public void setMallId(int mallId) {
		this.mallId = mallId;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

}
