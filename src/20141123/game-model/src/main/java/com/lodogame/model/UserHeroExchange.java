package com.lodogame.model;

import java.util.Date;

public class UserHeroExchange {

	private String userId;

	private int userWeek;

	private int systemWeek;

	private Date createdTime;

	private Date updatedTime;

	private int times;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getUserWeek() {
		return userWeek;
	}

	public void setUserWeek(int userWeek) {
		this.userWeek = userWeek;
	}

	public int getSystemWeek() {
		return systemWeek;
	}

	public void setSystemWeek(int systemWeek) {
		this.systemWeek = systemWeek;
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

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

}
