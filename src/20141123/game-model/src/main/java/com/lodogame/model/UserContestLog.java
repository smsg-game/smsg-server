package com.lodogame.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 玩家擂台赛日志 
 * @author chengevo
 */

public class UserContestLog implements Serializable{

	private static final long serialVersionUID = 1L;

	private int session;

	private int teamId;
	
	private String attUserId;
	
	private String attUserName;
	
	private String defUserId;
	
	private String defUserName;

	/**
	 * 比赛结果
	 * <li>1表示进攻方获胜，2表示防守方获胜
	 */
	private int result;
	
	private Date createdTime;

	public int getSession() {
		return session;
	}

	public void setSession(int session) {
		this.session = session;
	}

	public String getAttUserId() {
		return attUserId;
	}

	public void setAttUserId(String attUserId) {
		this.attUserId = attUserId;
	}

	public String getDefUserId() {
		return defUserId;
	}

	public void setDefUserId(String defUserId) {
		this.defUserId = defUserId;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public String getAttUserName() {
		return attUserName;
	}

	public void setAttUserName(String attUserName) {
		this.attUserName = attUserName;
	}

	public String getDefUserName() {
		return defUserName;
	}

	public void setDefUserName(String defUserName) {
		this.defUserName = defUserName;
	}
	
}
