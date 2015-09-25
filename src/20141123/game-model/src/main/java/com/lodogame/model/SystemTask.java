package com.lodogame.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统任务
 * 
 * @author jacky
 * 
 */
public class SystemTask implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 系统任务ID
	 */
	private int systemTaskId;

	/**
	 * 任务类型
	 */
	private int taskType;

	/**
	 * 任务描述
	 */
	private String taskDesc;

	/**
	 * 任务排序
	 */
	private int sort;

	/**
	 * 任务图标
	 */
	private int imgId;

	/**
	 * 任务名称
	 */
	private String taskName;

	/**
	 * 任务目标
	 */
	private int taskTarget;

	/**
	 * 需要完成次数
	 */
	private int needFinishTimes;

	/**
	 * 前置任务ID
	 */
	private int preTaskId;

	/**
	 * 银币数
	 */
	private int copperNum;

	/**
	 * 金币数
	 */
	private int goldNum;

	/**
	 * 经验数
	 */
	private int exp;

	/**
	 * 道具列表
	 */
	private String toolIds;

	/**
	 * 参数列表
	 */
	private String param;
	
	/**
	 * 任务有效性的开始时间
	 */
	private Date effectBeginTime;
	
	/**
	 * 任务有效性的结束时间
	 */
	private Date effectEndTime;
	
	public int getSystemTaskId() {
		return systemTaskId;
	}

	public void setSystemTaskId(int systemTaskId) {
		this.systemTaskId = systemTaskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public int getTaskTarget() {
		return taskTarget;
	}

	public void setTaskTarget(int taskTarget) {
		this.taskTarget = taskTarget;
	}

	public int getNeedFinishTimes() {
		return needFinishTimes;
	}

	public void setNeedFinishTimes(int needFinishTimes) {
		this.needFinishTimes = needFinishTimes;
	}

	public int getPreTaskId() {
		return preTaskId;
	}

	public void setPreTaskId(int preTaskId) {
		this.preTaskId = preTaskId;
	}

	public int getCopperNum() {
		return copperNum;
	}

	public void setCopperNum(int copperNum) {
		this.copperNum = copperNum;
	}

	public String getToolIds() {
		return toolIds;
	}

	public void setToolIds(String toolIds) {
		this.toolIds = toolIds;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public int getTaskType() {
		return taskType;
	}

	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}

	public int getGoldNum() {
		return goldNum;
	}

	public void setGoldNum(int goldNum) {
		this.goldNum = goldNum;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public String getTaskDesc() {
		return taskDesc;
	}

	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public int getImgId() {
		return imgId;
	}

	public void setImgId(int imgId) {
		this.imgId = imgId;
	}

	public Date getEffectBeginTime() {
		return effectBeginTime;
	}

	public void setEffectBeginTime(Date effectBeginTime) {
		this.effectBeginTime = effectBeginTime;
	}

	public Date getEffectEndTime() {
		return effectEndTime;
	}

	public void setEffectEndTime(Date effectEndTime) {
		this.effectEndTime = effectEndTime;
	}



}
