package com.lodogame.model;

import java.util.Date;

public class UserActivityRewardLog {

	private String userId;

	private int activityTaskRewardId;

	private Date createdTime;

	public int getActivityTaskRewardId() {
		return activityTaskRewardId;
	}

	public void setActivityTaskRewardId(int activityTaskRewardId) {
		this.activityTaskRewardId = activityTaskRewardId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

}
