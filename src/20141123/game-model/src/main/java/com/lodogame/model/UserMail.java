package com.lodogame.model;

import java.util.Date;

public class UserMail {

	private int userMailId;

	private String userId;

	private String systemMailId;

	private int status;

	private int receiveStatus;

	private Date createdTime;

	private Date updatedTime;

	public int getUserMailId() {
		return userMailId;
	}

	public void setUserMailId(int userMailId) {
		this.userMailId = userMailId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSystemMailId() {
		return systemMailId;
	}

	public void setSystemMailId(String systemMailId) {
		this.systemMailId = systemMailId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getReceiveStatus() {
		return receiveStatus;
	}

	public void setReceiveStatus(int receiveStatus) {
		this.receiveStatus = receiveStatus;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

}
