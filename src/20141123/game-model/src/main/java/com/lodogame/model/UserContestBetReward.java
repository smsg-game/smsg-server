package com.lodogame.model;

public class UserContestBetReward {
	
	private String userId;
	
	/**
	 * 第几届比赛
	 */
	private int session;
	
	private int toolType;
	private int toolId;
	private int toolNum;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getSession() {
		return session;
	}
	public void setSession(int session) {
		this.session = session;
	}
	public int getToolType() {
		return toolType;
	}
	public void setToolType(int toolType) {
		this.toolType = toolType;
	}
	public int getToolId() {
		return toolId;
	}
	public void setToolId(int toolId) {
		this.toolId = toolId;
	}
	public int getToolNum() {
		return toolNum;
	}
	public void setToolNum(int toolNum) {
		this.toolNum = toolNum;
	}
}
