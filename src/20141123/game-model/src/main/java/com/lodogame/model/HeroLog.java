package com.lodogame.model;

public class HeroLog implements ILog {

	private String userId;

	private String userHeroId;

	private int systemHeroId;

	private int useType;

	private int heroExp;

	private int heroLevel;

	private int flag;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserHeroId() {
		return userHeroId;
	}

	public void setUserHeroId(String userHeroId) {
		this.userHeroId = userHeroId;
	}

	public int getSystemHeroId() {
		return systemHeroId;
	}

	public void setSystemHeroId(int systemHeroId) {
		this.systemHeroId = systemHeroId;
	}

	public int getUseType() {
		return useType;
	}

	public void setUseType(int useType) {
		this.useType = useType;
	}

	public int getHeroExp() {
		return heroExp;
	}

	public void setHeroExp(int heroExp) {
		this.heroExp = heroExp;
	}

	public int getHeroLevel() {
		return heroLevel;
	}

	public void setHeroLevel(int heroLevel) {
		this.heroLevel = heroLevel;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

}
