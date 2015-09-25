package com.lodogame.model;

import java.util.Date;

public class UserDeifyInfo {
	
	private String userId;
	private Date deifyEndTime;

	/**
	 * 用户所在的修炼塔
	 */
	private int towerId;
	
	public int getTowerId() {
		return towerId;
	}
	public void setTowerId(int towerId) {
		this.towerId = towerId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Date getDeifyEndTime() {
		return deifyEndTime;
	}
	public void setDeifyEndTime(Date deifyEndTime) {
		this.deifyEndTime = deifyEndTime;
	}
	
	
}
