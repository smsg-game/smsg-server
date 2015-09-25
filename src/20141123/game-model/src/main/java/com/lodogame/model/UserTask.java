package com.lodogame.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户任务
 * 
 * @author jacky
 * 
 */
public class UserTask implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 系统任务ID
	 */
	private int systemTaskId;

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 任务类型
	 */
	private int taskType;

	/**
	 * 需要完成次数
	 */
	private int needFinishTimes;

	/**
	 * 已经完成次数
	 */
	private int finishTimes;

	/**
	 * 创建时间
	 */
	private Date createdTime;

	/**
	 * 更新时间
	 */
	private Date updatedTime;

	/**
	 * 任务状态
	 */
	private int status;

	public int getSystemTaskId() {
		return systemTaskId;
	}

	public void setSystemTaskId(int systemTaskId) {
		this.systemTaskId = systemTaskId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getTaskType() {
		return taskType;
	}

	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}

	public int getNeedFinishTimes() {
		return needFinishTimes;
	}

	public void setNeedFinishTimes(int needFinishTimes) {
		this.needFinishTimes = needFinishTimes;
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

}
