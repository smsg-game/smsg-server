package com.lodogame.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 记录用户的物品兑换信息
 * @author chengevo
 *
 */
public class UserToolExchangeLog implements Serializable{
	
	private String userId;
	
	/**
	 * 物品兑换 Id
	 */
	private int exchangeId;
	
	/**
	 * 已经兑换的次数
	 */
	private int times;
	private Date createdTime;
	private Date updateTime;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getExchangeId() {
		return exchangeId;
	}
	public void setExchangeId(int exchangeId) {
		this.exchangeId = exchangeId;
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
}
