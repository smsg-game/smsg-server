package com.lodogame.model;

import java.io.Serializable;

public class UserRecContestRewardLog implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String userId;
	
	private int session;
	
	private int rewardId;
	
	private String description;
	

	public UserRecContestRewardLog(String userId, int session, int rewardId) {
		super();
		this.userId = userId;
		this.session = session;
		this.rewardId = rewardId;
	}

	public UserRecContestRewardLog() {
		
	}
	
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

	public int getRewardId() {
		return rewardId;
	}

	public void setRewardId(int rewardId) {
		this.rewardId = rewardId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
