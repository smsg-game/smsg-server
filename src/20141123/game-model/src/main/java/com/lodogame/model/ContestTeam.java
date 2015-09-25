package com.lodogame.model;

import java.io.Serializable;
import java.util.Date;


public class ContestTeam implements Serializable{

	private static final long serialVersionUID = 1L;
	
	
	public ContestTeam(String contestId, int session, int teamId, String teamName, Date createdTime) {
		this.contestId = contestId;
		this.session = session;
		this.teamId = teamId;
		this.teamName = teamName;
		this.createdTime = createdTime;
	}

	public ContestTeam() {
		
	}
	
	public void incrementMembCount() {
		playerNum++;
	}
	
	private int session;
	private String contestId;
	private String teamName;
	private int teamId;

	/**
	 * 可以容纳的玩家数量
	 */
	private int capacity;
	
	/**
	 * 已经进入该组的玩家数量
	 */
	private int playerNum;

	
	/**
	 * 这个小组是否已经打完比赛了
	 */
	private int isFighted = 0;

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

	public String getContestId() {
		return contestId;
	}

	public void setContestId(String contestId) {
		this.contestId = contestId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getPlayerNum() {
		return playerNum;
	}

	public void setPlayerNum(int playerNum) {
		this.playerNum = playerNum;
	}

	public int getIsFighted() {
		return isFighted;
	}

	public void setIsFighted(int isFighted) {
		this.isFighted = isFighted;
	}

	/**
	 * 这个小队是否打完了比赛
	 * @return 0 还没有打完比赛；1已经打完了比赛
	 */
	public boolean isFighted() {
		return (isFighted == 1) ? true : false;
	}
	
	public void setTeamFighted() {
		this.isFighted = 1;
	}


}
