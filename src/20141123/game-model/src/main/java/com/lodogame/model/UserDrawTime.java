package com.lodogame.model;

import java.util.Date;

public class UserDrawTime {
	/**
	 * 用戶ID
	 */
	private String userId;

	/**
	 * 用戶名
	 */
	private String userName;

	/**
	 * 用户抽奖次数
	 */
	private int times;

	/**
	 * 用戶抽獎積分
	 */
	private int score;

	/**
	 * 創建日期
	 */
	private Date createTime;

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
