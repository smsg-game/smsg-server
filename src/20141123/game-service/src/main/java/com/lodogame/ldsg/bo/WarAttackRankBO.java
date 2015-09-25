package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class WarAttackRankBO {

	private String userId;

	@Mapper(name = "un")
	private String username;

	@Mapper(name = "shid")
	private int systemHeroId;

	@Mapper(name = "vl")
	private int vipLevel;

	@Mapper(name = "ul")
	private int level;

	@Mapper(name = "atn")
	private int attackNum;

	@Mapper(name = "st")
	private int status;

	@Mapper(name = "rk")
	private int rank;

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

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

}
