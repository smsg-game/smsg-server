package com.lodogame.model;

import java.io.Serializable;
import java.util.Date;

public class UserContestRank implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String userId;
	
	private int rank;
	
	private int session;

	private Date createdTime;
	
	
	
	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
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

	public int getSession() {
		return session;
	}

	public void setSession(int session) {
		this.session = session;
	}
	
	
}
