package com.lodogame.ldsg.bo;

import java.io.Serializable;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class ContestRankBO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Mapper(name = "un")
	private String userName;
	
	@Mapper(name = "rk")
	private int rank;
	
	@Mapper(name = "ul")
	private int userLevel;
	
	@Mapper(name = "uv")
	private int userVipLevel;
	
	@Mapper(name = "uh")
	private UserHeroBO userHeroBO;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(int userLevel) {
		this.userLevel = userLevel;
	}

	public int getUserVipLevel() {
		return userVipLevel;
	}

	public void setUserVipLevel(int userVipLevel) {
		this.userVipLevel = userVipLevel;
	}

	public UserHeroBO getUserHeroBO() {
		return userHeroBO;
	}

	public void setUserHeroBO(UserHeroBO userHeroBO) {
		this.userHeroBO = userHeroBO;
	}
	
	
}
