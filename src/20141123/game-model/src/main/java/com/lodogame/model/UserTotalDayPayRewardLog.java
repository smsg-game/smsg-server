package com.lodogame.model;

import java.util.Date;

public class UserTotalDayPayRewardLog {

	private String userId;
	
	private int day;
	
	private int rewardStatus;
	
	private Date createdTime;
	
	private String date;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getRewardStatus() {
		return rewardStatus;
	}

	public void setRewardStatus(int rewardStatus) {
		this.rewardStatus = rewardStatus;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	

}
