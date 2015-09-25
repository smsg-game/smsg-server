package com.lodogame.ldsg.bo;

import java.io.Serializable;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

/**
 * 帮派成员
 * 
 * @author Administrator
 * 
 */
@Compress
public class FactionMemberBO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Mapper(name = "un")
	private String userName;
	
	@Mapper(name = "lv")
	private int level;
	
	@Mapper(name = "pid")
	private Long playerId;
	
	@Mapper(name = "rk")
	private int rank;
	
	@Mapper(name = "vl")
	private int vipLevel;

	
	public Long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(Long playerId) {
		this.playerId = playerId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}

}
