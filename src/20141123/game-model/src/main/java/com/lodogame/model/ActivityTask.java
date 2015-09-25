package com.lodogame.model;

public class ActivityTask {

	private int activityTaskId;

	private int targetType;

	private int needFinishTimes;

	private int point;

	public int getActivityTaskId() {
		return activityTaskId;
	}

	public void setActivityTaskId(int activityTaskId) {
		this.activityTaskId = activityTaskId;
	}

	public int getTargetType() {
		return targetType;
	}

	public void setTargetType(int targetType) {
		this.targetType = targetType;
	}

	public int getNeedFinishTimes() {
		return needFinishTimes;
	}

	public void setNeedFinishTimes(int needFinishTimes) {
		this.needFinishTimes = needFinishTimes;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

}
