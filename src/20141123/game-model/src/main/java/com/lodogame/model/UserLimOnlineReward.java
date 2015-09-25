package com.lodogame.model;

import java.util.Date;

/**
 * 记录用户领取期限在线奖励信息
 * @author chengevo
 *
 */

public class UserLimOnlineReward {

	private String userId;
	
	private Date createdTime;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	
	
	
}
