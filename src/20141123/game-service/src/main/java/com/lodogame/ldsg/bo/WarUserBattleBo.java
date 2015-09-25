package com.lodogame.ldsg.bo;

import java.util.ArrayList;
import java.util.List;

import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.model.UserWarInfo;


public class WarUserBattleBo {
	private EventHandle eventHandle;
	private String userId;
	private int st;
	private List<UserWarInfo> enemyList = new ArrayList<UserWarInfo>();
	
	public int getSt() {
		return st;
	}
	public void setSt(int st) {
		this.st = st;
	}
	public EventHandle getEventHandle() {
		return eventHandle;
	}
	public void setEventHandle(EventHandle eventHandle) {
		this.eventHandle = eventHandle;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public List<UserWarInfo> getEnemyList() {
		return enemyList;
	}
	public void setEnemyList(List<UserWarInfo> enemyList) {
		this.enemyList = enemyList;
	}
	
	
}
