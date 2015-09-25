package com.lodogame.model;

public class WarAttackRank {

	private String userId;

	private String username;

	private int systemHeroId;

	private int vipLevel;

	private int level;

	private int attackNum;

	private int status;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getSystemHeroId() {
		return systemHeroId;
	}

	public void setSystemHeroId(int systemHeroId) {
		this.systemHeroId = systemHeroId;
	}

	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getAttackNum() {
		return attackNum;
	}

	public void setAttackNum(int attackNum) {
		this.attackNum = attackNum;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
