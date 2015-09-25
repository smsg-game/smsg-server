package com.lodogame.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 擂台赛用户下注记录
 * @author chengevo
 *
 */
public class UserContestBetLog implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public UserContestBetLog(int session, String userId, String betOnUserId, Date createdTime) {
		super();
		this.session = session;
		this.userId = userId;
		this.betOnUserId = betOnUserId;
		this.createdTime = createdTime;
	}

	public UserContestBetLog() {
		
	}
	
	private int session;
	
	/**
	 * 下注的用户 id
	 */
	private String userId;
	
	/**
	 * 被下注的用户 id
	 */
	private String betOnUserId;

	private Date createdTime;
	
	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public int getSession() {
		return session;
	}

	public void setSession(int session) {
		this.session = session;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBetOnUserId() {
		return betOnUserId;
	}

	public void setBetOnUserId(String betOnUserId) {
		this.betOnUserId = betOnUserId;
	}
	
	
}
