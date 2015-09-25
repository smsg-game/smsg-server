package com.lodogame.model;

import java.util.Date;

public class UserHeroSkill {

	/**
	 * 表id
	 */
	private int userHeroSkillId;

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 用户武将ID
	 */
	private String userHeroId;

	/**
	 * 技能组ID
	 */
	private int skillGroupId;

	/**
	 * 技能ID
	 */
	private int skillId;

	/**
	 * 创建时间
	 */
	private Date createdTime;

	/**
	 * 更新时间
	 */
	private Date updatedTime;

	public int getUserHeroSkillId() {
		return userHeroSkillId;
	}

	public void setUserHeroSkillId(int userHeroSkillId) {
		this.userHeroSkillId = userHeroSkillId;
	}

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

}
