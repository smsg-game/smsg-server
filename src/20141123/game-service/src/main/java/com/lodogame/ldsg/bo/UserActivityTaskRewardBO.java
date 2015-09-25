package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class UserActivityTaskRewardBO {

	@Mapper(name = "rid")
	private int activityTaskRewardId;

	@Mapper(name = "st")
	private int status;

	public int getActivityTaskRewardId() {
		return activityTaskRewardId;
	}

	public void setActivityTaskRewardId(int activityTaskRewardId) {
		this.activityTaskRewardId = activityTaskRewardId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
