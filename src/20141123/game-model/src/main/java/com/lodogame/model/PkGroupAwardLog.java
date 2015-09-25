package com.lodogame.model;

import java.util.Date;

/**
 * 争霸奖励领取记录
 * @author Candon
 *
 */
public class PkGroupAwardLog {
	private String userId;
	private int grank;
	private Date pastTime;
	private int groupId;
	private String title = "";
	private int isGet;
	private String username;
	
	public Date getPastTime() {
		return pastTime;
	}
	public void setPastTime(Date pastTime) {
		this.pastTime = pastTime;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getIsGet() {
		return isGet;
	}
	public void setIsGet(int isGet) {
		this.isGet = isGet;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getGrank() {
		return grank;
	}
	public void setGrank(int grank) {
		this.grank = grank;
	}	
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
