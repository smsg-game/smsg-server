package com.lodogame.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户场景
 * 
 * @author jacky
 * 
 */
public class UserScene implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户Id
	 */
	private String userId;

	/**
	 * 场景ID
	 */
	private int sceneId;

	/**
	 * 通关标志
	 */
	private int passFlag;

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

	public int getPassFlag() {
		return passFlag;
	}

	public void setPassFlag(int passFlag) {
		this.passFlag = passFlag;
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

}
