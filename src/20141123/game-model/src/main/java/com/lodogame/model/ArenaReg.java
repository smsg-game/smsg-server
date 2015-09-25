package com.lodogame.model;

import java.util.Date;

public class ArenaReg {

	private String userId;

	private String username;

	private int maxWinCount;

	private int status;

	private int winCount;

	private int winTimes;

	private int loseTimes;

	private int encourageTimes;

	private int ability;

	private Date createdTime;

	private Date updatedTime;

	private long lastPushRankTime;

	private long lastPushBattleRecordTime;

	/**
	 * 玩家等级
	 */
	private int userLevel;

	/**
	 * 用户系统武将ID
	 */
	private int systemHeroId;

	/**
	 * 获得的所有奖励
	 */
	private int totalCopper;

	private Object statusLock = new Object();

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getMaxWinCount() {
		return maxWinCount;
	}

	public void setMaxWinCount(int maxWinCount) {
		this.maxWinCount = maxWinCount;
	}

	public int getStatus() {
		synchronized (statusLock) {
			return status;
		}
	}

	public void setStatus(int status) {
		synchronized (statusLock) {
			this.status = status;
		}
	}

	public int getWinCount() {
		return winCount;
	}

	public void setWinCount(int winCount) {
		this.winCount = winCount;
	}

	public int getLoseTimes() {
		return loseTimes;
	}

	public void setLoseTimes(int loseTimes) {
		this.loseTimes = loseTimes;
	}

	public int getEncourageTimes() {
		return encourageTimes;
	}

	public void setEncourageTimes(int encourageTimes) {
		this.encourageTimes = encourageTimes;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getUpdatedTime() {
		synchronized (statusLock) {
			return updatedTime;
		}
	}

	public void setUpdatedTime(Date updatedTime) {
		synchronized (statusLock) {
			this.updatedTime = updatedTime;
		}
	}

	public int getAbility() {
		return ability;
	}

	public void setAbility(int ability) {
		this.ability = ability;
	}

	public int getSystemHeroId() {
		return systemHeroId;
	}

	public void setSystemHeroId(int systemHeroId) {
		this.systemHeroId = systemHeroId;
	}

	public Object getStatusLock() {
		return statusLock;
	}

	public void setStatusLock(Object statusLock) {
		this.statusLock = statusLock;
	}

	public int getWinTimes() {
		return winTimes;
	}

	public void setWinTimes(int winTimes) {
		this.winTimes = winTimes;
	}

	public int getTotalCopper() {
		return totalCopper;
	}

	public void setTotalCopper(int totalCopper) {
		this.totalCopper = totalCopper;
	}

	public int getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(int userLevel) {
		this.userLevel = userLevel;
	}

	public long getLastPushRankTime() {
		return lastPushRankTime;
	}

	public void setLastPushRankTime(long lastPushRankTime) {
		this.lastPushRankTime = lastPushRankTime;
	}

	public long getLastPushBattleRecordTime() {
		return lastPushBattleRecordTime;
	}

	public void setLastPushBattleRecordTime(long lastPushBattleRecordTime) {
		this.lastPushBattleRecordTime = lastPushBattleRecordTime;
	}

}
