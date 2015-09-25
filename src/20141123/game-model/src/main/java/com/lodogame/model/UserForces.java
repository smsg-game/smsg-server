package com.lodogame.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户场景军队
 * 
 * @author jacky
 * 
 */
public class UserForces implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 场景ID
	 */
	private int sceneId;

	/**
	 * 军队ID
	 */
	private int forcesId;

	/**
	 * 怪物类型
	 */
	private int forcesType;

	/**
	 * 状态(状态 0 不可攻打 1 可以攻打 2 已经战胜过)
	 */
	private int status;

	/**
	 * 当天已攻打次数
	 */
	private int times;

	/**
	 * 创建时间
	 */
	private Date createdTime;

	/**
	 * 更新时间
	 */
	private Date updatedTime;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getSceneId() {
		return sceneId;
	}

	public void setSceneId(int sceneId) {
		this.sceneId = sceneId;
	}

	public int getForcesId() {
		return forcesId;
	}

	public void setForcesId(int forcesId) {
		this.forcesId = forcesId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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

	public int getForcesType() {
		return forcesType;
	}

	public void setForcesType(int forcesType) {
		this.forcesType = forcesType;
	}

}
