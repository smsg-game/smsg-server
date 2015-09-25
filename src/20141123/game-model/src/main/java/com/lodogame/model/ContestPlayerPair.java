package com.lodogame.model;

import java.io.Serializable;
import java.util.Date;


public class ContestPlayerPair implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String contestId;

	private String playerPairId;
	
	private int session;
	
	private int teamId;
	
	/**
	 * 进攻方玩家 id 
	 */
	private String attUserId;
	
	/**
	 * 进攻方玩家名
	 */
	private String attUserName;
	
	/**
	 * 防守方玩家 id
	 */ 
	private String defUserId;
	
	/**
	 * 防守方玩家名
	 */
	private String defUserName;
	
	/**
	 * 进攻方下注人数
	 */
	private int attBetNum;
	
	/**
	 * 防守方下注人数
	 */
	private int defBetNum;
	
	/**
	 * 比赛结果
	 * <li>0表示这两个玩家还未战斗，1表示进攻方获胜，2表示防守方获胜
	 */
	private int result;
	
	private Date createdTime;
	
	public ContestPlayerPair(String contestId, String playerPairId, int session, int teamId, Date createdTime) {
		this.contestId = contestId;
		this.playerPairId = playerPairId;
		this.teamId = teamId;
		this.session = session;
		this.createdTime = createdTime;
	}
	
	public ContestPlayerPair() {
		
	}

	public boolean isFull() {
		if (this.attUserId == null || this.defUserId == null) {
			return false;
		} else {
			return true;
		}
	}
	
	
	
	public String getContestId() {
		return contestId;
	}

	public void setContestId(String contestId) {
		this.contestId = contestId;
	}

	public int getSession() {
		return session;
	}

	public void setSession(int session) {
		this.session = session;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
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

	public String getAttUserName() {
		return attUserName;
	}

	public void setAttUserName(String attUsername) {
		this.attUserName = attUsername;
	}

	public String getDefUserName() {
		return defUserName;
	}

	public void setDefUserName(String defUsername) {
		this.defUserName = defUsername;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}
	
	public int getAttBetNum() {
		return attBetNum;
	}

	public void setAttBetNum(int attBetNum) {
		this.attBetNum = attBetNum;
	}

	public int getDefBetNum() {
		return defBetNum;
	}

	public void setDefBetNum(int defBetNum) {
		this.defBetNum = defBetNum;
	}

	public void incrAttUserBetNum() {
		this.attBetNum++;
	}
	
	public void incrDefUserBetNum() {
		this.defBetNum++;
	}
	
	public String getLoseUserId() {
		if (result == 1) {
			return defUserId;
		} else {
			return attUserId;
		}
	}
	
	public String getWinnerUserId() {
		if (result == 1) {
			return attUserId;
		} else {
			return defUserId;
		}
	}

	public String getPlayerPairId() {
		return playerPairId;
	}

	public void setPlayerPairId(String playerPairId) {
		this.playerPairId = playerPairId;
	}

	
	
}
