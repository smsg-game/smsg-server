package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class ArenaRankBO {

	/**
	 * 玩家名
	 */
	@Mapper(name = "un")
	private String username;

	/**
	 * 玩家第一个武将ID
	 */
	@Mapper(name = "shid")
	private int systemHeroId;

	/**
	 * 玩家第一个武将等级
	 */
	@Mapper(name = "uhlv")
	private int userHeroLevel;

	/**
	 * 最高连胜次数
	 */
	@Mapper(name = "mwc")
	private int maxWinCount;

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

	public int getUserHeroLevel() {
		return userHeroLevel;
	}

	public void setUserHeroLevel(int userHeroLevel) {
		this.userHeroLevel = userHeroLevel;
	}

	public int getMaxWinCount() {
		return maxWinCount;
	}

	public void setMaxWinCount(int maxWinCount) {
		this.maxWinCount = maxWinCount;
	}

}
