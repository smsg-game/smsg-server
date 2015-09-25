package com.lodogame.model;

import java.util.Date;

/**
 * 用户武将技能训练
 * 
 * @author jacky
 * 
 */
public class UserHeroSkillTrain {

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 用户武将ID
	 */
	private String userHeroId;

	/**
	 * 技能分组ID
	 */
	private int skillGroupId;

	/**
	 * 技能ID
	 */
	private int skillId;

	/**
	 * 是否锁定
	 */
	private int lock;

	/**
	 * 创建时间
	 */
	private Date createdTime;

	/**
	 * 更新时间
	 */
	private Date updatedTime;

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

	public int getSkillGroupId() {
		return skillGroupId;
	}

	public void setSkillGroupId(int skillGroupId) {
		this.skillGroupId = skillGroupId;
	}

	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
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

	public int getLock() {
		return lock;
	}

	public void setLock(int lock) {
		this.lock = lock;
	}

}
