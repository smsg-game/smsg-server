package com.lodogame.model;

import java.util.Date;

public class UserWarInfo {

	private int id;

	private String userId;

	private int systemHeroId;

	private int vipLevel;

	private int level;

	private int totalAttackNum;

	// 用户名
	private String userName;

	// 所在城池坐标
	private Integer point;

	// 国家ID
	private int countryId;

	// 攻击胜利次数
	private int attackNum;

	// 防守胜利次数
	private int defenseNum;

	// 银币清除行动CD次数消耗
	private int clearActionCopper;

	// 鼓舞次数
	private int inspireNum;

	// 复活时间
	private Date liftTime;

	// 领奖时间
	private Date drawTime;

	// 行动时间
	private Date actionTime;

	// 鼓舞时间（弃用）
	private Date inspireTime;

	// 战斗力
	private int ability;

	// 排行奖励状态
	private int status;

	// 正在被打
	private boolean beAttack = false;

	public int getAbility() {
		return ability;
	}

	public void setAbility(int ability) {
		this.ability = ability;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getInspireTime() {
		return inspireTime;
	}

	public void setInspireTime(Date inspireTime) {
		this.inspireTime = inspireTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getPoint() {
		return point;
	}

	public void setPoint(Integer point) {
		this.point = point;
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public int getAttackNum() {
		return attackNum;
	}

	public void setAttackNum(int attackNum) {
		this.attackNum = attackNum;
	}

	public int getDefenseNum() {
		return defenseNum;
	}

	public void setDefenseNum(int defenseNum) {
		this.defenseNum = defenseNum;
	}

	public int getInspireNum() {
		return inspireNum;
	}

	public void setInspireNum(int inspireNum) {
		this.inspireNum = inspireNum;
	}

	public Date getLiftTime() {
		return liftTime;
	}

	public void setLiftTime(Date liftTime) {
		this.liftTime = liftTime;
	}

	public Date getDrawTime() {
		return drawTime;
	}

	public void setDrawTime(Date drawTime) {
		this.drawTime = drawTime;
	}

	public Date getActionTime() {
		return actionTime;
	}

	public void setActionTime(Date actionTime) {
		this.actionTime = actionTime;
	}

	public int getClearActionCopper() {
		return clearActionCopper;
	}

	public void setClearActionCopper(int clearActionCopper) {
		this.clearActionCopper = clearActionCopper;
	}

	public boolean isBeAttack() {
		return beAttack;
	}

	public void setBeAttack(boolean beAttack) {
		this.beAttack = beAttack;
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

	public int getTotalAttackNum() {
		return totalAttackNum;
	}

	public void setTotalAttackNum(int totalAttackNum) {
		this.totalAttackNum = totalAttackNum;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
