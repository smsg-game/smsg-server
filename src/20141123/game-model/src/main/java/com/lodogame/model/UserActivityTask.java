package com.lodogame.model;

import java.util.Date;

public class UserActivityTask {

	private int userActivityTaskId;

	private String userId;

	private int activityTaskId;

	private int finishTimes;

	private Date createdTime;

	private Date updatedTime;

	private int status;

	private String date;

	public int getUserActivityTaskId() {
		return userActivityTaskId;
	}

	public void setUserActivityTaskId(int userActivityTaskId) {
		this.userActivityTaskId = userActivityTaskId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getActivityTaskId() {
		return activityTaskId;
	}

	public void setActivityTaskId(int activityTaskId) {
		this.activityTaskId = activityTaskId;
	}

	public int getFinishTimes() {
		return finishTimes;
	}

	public void setFinishTimes(int finishTimes) {
		this.finishTimes = finishTimes;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
