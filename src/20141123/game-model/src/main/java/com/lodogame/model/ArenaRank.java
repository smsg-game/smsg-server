package com.lodogame.model;

public class ArenaRank {

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 最大连胜
	 */
	private int maxWinCount;

	/**
	 * 时间戳
	 */
	private long timestamp;

	public int getMaxWinCount() {
		return maxWinCount;
	}

	public void setMaxWinCount(int maxWinCount) {
		this.maxWinCount = maxWinCount;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
