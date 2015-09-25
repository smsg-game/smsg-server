package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

/**
 * 活动副本BO
 * 
 * @author jacky
 * 
 */
@Compress
public class CopyActivityBO {

	private static final long serialVersionUID = 1L;

	/**
	 * 活动ID
	 */
	private int activityId;

	/**
	 * 活动类型
	 */
	private int activityType;

	/*
	 * 活动名字
	 */
	@Mapper(name = "an")
	private String activityName;

	/**
	 * 活动描述
	 */
	@Mapper(name = "as")
	private String activityDesc;

	/**
	 * 开始时间
	 */
	@Mapper(name = "opt")
	private long startTime;

	/**
	 * 活动结束时间
	 */
	@Mapper(name = "edt")
	private long endTime;

	/**
	 * 扩展 参数
	 */
	@Mapper(name = "sid")
	private int sceneId;

	public int getActivityId() {
		return activityId;
	}

	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}

	public int getActivityType() {
		return activityType;
	}

	public void setActivityType(int activityType) {
		this.activityType = activityType;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getActivityDesc() {
		return activityDesc;
	}

	public void setActivityDesc(String activityDesc) {
		this.activityDesc = activityDesc;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public int getSceneId() {
		return sceneId;
	}

	public void setSceneId(int sceneId) {
		this.sceneId = sceneId;
	}

}
