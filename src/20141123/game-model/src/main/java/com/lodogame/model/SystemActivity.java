package com.lodogame.model;

import java.io.Serializable;
import java.util.Date;

public class SystemActivity implements Serializable {

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
	private String activityName;

	/**
	 * 活动描述
	 */
	private String activityDesc;

	/**
	 * 开始时间
	 */
	private Date startTime;

	/**
	 * 活动结束时间
	 */
	private Date endTime;

	/**
	 * 扩展 参数
	 */
	private String param;

	/**
	 * 开放的星期
	 */
	private String openWeeks;
	
	private int display;
	private int sort;

	
	public int getDisplay() {
		return display;
	}

	public void setDisplay(int display) {
		this.display = display;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

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

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getOpenWeeks() {
		return openWeeks;
	}

	public void setOpenWeeks(String openWeeks) {
		this.openWeeks = openWeeks;
	}

}
