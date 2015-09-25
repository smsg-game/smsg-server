package com.lodogame.model;

import java.io.Serializable;
import java.util.Date;

public class UserEquip implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户装备ID(唯一ID)
	 */
	private String userEquipId;

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 系统装备ID
	 */
	private int equipId;

	/**
	 * 穿戴的武将ID
	 */
	private String userHeroId;

	/**
	 * 装备等级
	 */
	private int equipLevel;

	/**
	 * 创建时间
	 */
	private Date createdTime;

	/**
	 * 更新时间
	 */
	private Date updatedTime;

	public String getUserEquipId() {
		return userEquipId;
	}

	public void setUserEquipId(String userEquipId) {
		this.userEquipId = userEquipId;
	}

	public int getEquipId() {
		return equipId;
	}

	public void setEquipId(int equipId) {
		this.equipId = equipId;
	}

	public String getUserHeroId() {
		return userHeroId;
	}

	public void setUserHeroId(String userHeroId) {
		this.userHeroId = userHeroId;
	}

	public int getEquipLevel() {
		return equipLevel;
	}

	public void setEquipLevel(int equipLevel) {
		this.equipLevel = equipLevel;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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
