package com.lodogame.model;

import java.io.Serializable;
import java.util.Date;

public class UserHero implements Serializable {

	private static final long serialVersionUID = 75800777885412119L;

	/**
	 * 用户武将ID
	 */
	private String userHeroId;

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 系统武将ID
	 */
	private int systemHeroId;

	/**
	 * 武将经验
	 */
	private int heroExp;

	/**
	 * 武将等级
	 */
	private int heroLevel;

	/**
	 * 武将站
	 */
	private int pos;

	/**
	 * 创建时间
	 */
	private Date createdTime;

	/**
	 * 更新时间
	 */
	private Date updatedTime;
	
	/**
	 *  血祭的等级
	 */
	private int bloodSacrificeStage;
	
	/**
	 *  锁定状态
	 */
	private int lockStatus;
	
	/**
	 *  化神等级
	 */
	private int DeifyNodeLevel;
	
	
	public String getUserHeroId() {
		return userHeroId;
	}

	public void setUserHeroId(String userHeroId) {
		this.userHeroId = userHeroId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getSystemHeroId() {
		return systemHeroId;
	}

	public void setSystemHeroId(int systemHeroId) {
		this.systemHeroId = systemHeroId;
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

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public int getBloodSacrificeStage() {
		return bloodSacrificeStage;
	}

	public void setBloodSacrificeStage(int bloodSacrificeStage) {
		this.bloodSacrificeStage = bloodSacrificeStage;
	}

	public int getLockStatus() {
		return lockStatus;
	}

	public void setLockStatus(int lockStatus) {
		this.lockStatus = lockStatus;
	}

	public int getDeifyNodeLevel() {
		return DeifyNodeLevel;
	}

	public void setDeifyNodeLevel(int deifyNodeLevel) {
		DeifyNodeLevel = deifyNodeLevel;
	}


}
