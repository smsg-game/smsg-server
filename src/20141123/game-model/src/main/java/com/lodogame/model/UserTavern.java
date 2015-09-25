package com.lodogame.model;

import java.io.Serializable;
import java.util.Date;

public class UserTavern implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 抽奖类型
	 */
	private int type;

	/**
	 * 总抽奖次数
	 */
	private int totalTimes;

	/**
	 * 抽奖累计修正
	 */
	private int amendTimes;

	/**
	 * 创建时间
	 */
	private Date createdTime;

	/**
	 * 更新时间
	 */
	private Date updatedTime;

	
	/**
	 * 是否用金币招过武将
	 * 
	 */
	private int hadUsedMoney;

	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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

	public int getTotalTimes() {
		return totalTimes;
	}

	public void setTotalTimes(int totalTimes) {
		this.totalTimes = totalTimes;
	}

	public int getAmendTimes() {
		return amendTimes;
	}

	public void setAmendTimes(int amendTimes) {
		this.amendTimes = amendTimes;
	}

	public int getHadUsedMoney() {
		return hadUsedMoney;
	}

	public void setHadUsedMoney(int hadUsedMoney) {
		this.hadUsedMoney = hadUsedMoney;
	}

}
