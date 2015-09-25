package com.lodogame.model;

import java.util.Date;

public class UserTavernRebateLog {
	
	private int id;
	
	private String userId;
	
	//次数
	private int times;
	
	//类型 (1广交豪杰2大摆筵席)
	private int type;
	
	private Date createdTime;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	
	
}
