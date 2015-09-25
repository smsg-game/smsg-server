package com.lodogame.model;

import java.io.Serializable;
import java.util.Date;

public class UserPkInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 排名
	 */
	private int rank;
	/**
	 * 积分
	 */
	private int score;
	/**
	 * 挑战剩余次数
	 */
	private int pkTimes;
	/**
	 * 更新PK次数时间
	 */
	private Date updatePkTime;

	/**
	 * 购买pk次数
	 */
	private int buyPkTimes;

	/**
	 * 最后购买pk次数的时间
	 */
	private Date lastBuyTime;

	/**
	 * 可见列表类型
	 */
	private int seeType;
	
	/**
	 * 用户等级
	 */
	private int user_level;
	
	/**
	 * 组排名
	 */
	private int gRank;
	
	/**
	 * 连胜场数
	 */
	private int times;
	
	private String username;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public int getgRank() {
		return gRank;
	}

	public void setgRank(int gRank) {
		this.gRank = gRank;
	}

	public int getUser_level() {
		return user_level;
	}

	public void setUser_level(int user_level) {
		this.user_level = user_level;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getPkTimes() {
		return pkTimes;
	}

	public void setPkTimes(int pkTimes) {
		this.pkTimes = pkTimes;
	}

	public Date getUpdatePkTime() {
		return updatePkTime;
	}

	public void setUpdatePkTime(Date updatePkTime) {
		this.updatePkTime = updatePkTime;
	}

	public int getSeeType() {
		return seeType;
	}

	public void setSeeType(int seeType) {
		this.seeType = seeType;
	}

	public int getBuyPkTimes() {
		return buyPkTimes;
	}

	public void setBuyPkTimes(int buyPkTimes) {
		this.buyPkTimes = buyPkTimes;
	}

	public Date getLastBuyTime() {
		return lastBuyTime;
	}

	public void setLastBuyTime(Date lastBuyTime) {
		this.lastBuyTime = lastBuyTime;
	}

}
