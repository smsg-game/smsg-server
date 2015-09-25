package com.lodogame.ldsg.bo;

import java.io.Serializable;
import java.util.List;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

/**
 * 争霸玩家信息，包含玩家的英雄列表
 * 
 * @author Administrator
 * 
 */
@Compress
public class PkPlayerBO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Mapper(name = "nn")
	private String nickname;
	@Mapper(name = "lv")
	private int level;
	@Mapper(name = "pid")
	private long playerId;
	@Mapper(name = "rk")
	private int rank;
	@Mapper(name = "sc")
	private int score;
	@Mapper(name = "vl")
	private int vipLevel;
	@Mapper(name = "hs")
	private List<UserHeroBO> heroList;
	@Mapper(name = "grk")
	private int gRank;
	
	public int getGRank() {
		return gRank;
	}

	public void setGRank(int gRank) {
		this.gRank = gRank;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public List<UserHeroBO> getHeroList() {
		return heroList;
	}

	public void setHeroList(List<UserHeroBO> heroList) {
		this.heroList = heroList;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}
}
