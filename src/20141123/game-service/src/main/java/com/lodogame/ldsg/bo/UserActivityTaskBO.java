package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class UserActivityTaskBO {

	@Mapper(name = "atid")
	private int activityTaskId;

	@Mapper(name = "ft")
	private int finishTimes;

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

}
