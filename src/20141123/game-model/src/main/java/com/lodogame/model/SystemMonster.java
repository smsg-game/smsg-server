package com.lodogame.model;

import java.io.Serializable;

public class SystemMonster implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 怪物ID
	 */
	private int monsterId;

	/**
	 * 怪物名称
	 */
	private String monsterName;

	/**
	 * 怪物等级
	 */
	private int monsterLevel;

	/**
	 * 英雄模型
	 */
	private int heroModel;

	/**
	 * 生命
	 */
	private long life;

	/**
	 * 物攻
	 */
	private int physicalAttack;

	/**
	 * 物防
	 */
	private int physicalDefense;

	/**
	 * 策攻
	 */
	private int strategyAttack;

	/**
	 * 策防
	 */
	private int strategyDefense;

	/**
	 * 速度
	 */
	private int speed;

	/**
	 * 命中
	 */
	private int hit;

	/**
	 * 闪避
	 */
	private int duck;

	/**
	 * 暴击
	 */
	private int cri;

	/**
	 * 格挡
	 */
	private int parry;

	/**
	 * 兵符
	 */
	private int soliderTokenId;

	/**
	 * 主动技能
	 */
	private int plan;

	/**
	 * 天赋技能1
	 */
	private int skill1;

	/**
	 * 天赋技能2
	 */
	private int skill2;

	/**
	 * 天赋技能3
	 */
	private int skill3;

	/**
	 * 天赋技能4
	 */
	private int skill4;

	/**
	 * 普通攻击
	 */
	private int normalPlan;

	/**
	 * 怪物职业
	 */
	private int career;

	public int getMonsterId() {
		return monsterId;
	}

	public void setMonsterId(int monsterId) {
		this.monsterId = monsterId;
	}

	public String getMonsterName() {
		return monsterName;
	}

	public void setMonsterName(String monsterName) {
		this.monsterName = monsterName;
	}

	public int getMonsterLevel() {
		return monsterLevel;
	}

	public void setMonsterLevel(int monsterLevel) {
		this.monsterLevel = monsterLevel;
	}

	public int getHeroModel() {
		return heroModel;
	}

	public void setHeroModel(int heroModel) {
		this.heroModel = heroModel;
	}

	public long getLife() {
		return life;
	}

	public void setLife(long life) {
		this.life = life;
	}

	public int getPhysicalAttack() {
		return physicalAttack;
	}

	public void setPhysicalAttack(int physicalAttack) {
		this.physicalAttack = physicalAttack;
	}

	public int getPhysicalDefense() {
		return physicalDefense;
	}

	public void setPhysicalDefense(int physicalDefense) {
		this.physicalDefense = physicalDefense;
	}

	public int getStrategyAttack() {
		return strategyAttack;
	}

	public void setStrategyAttack(int strategyAttack) {
		this.strategyAttack = strategyAttack;
	}

	public int getStrategyDefense() {
		return strategyDefense;
	}

	public void setStrategyDefense(int strategyDefense) {
		this.strategyDefense = strategyDefense;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getHit() {
		return hit;
	}

	public void setHit(int hit) {
		this.hit = hit;
	}

	public int getDuck() {
		return duck;
	}

	public void setDuck(int duck) {
		this.duck = duck;
	}

	public int getCri() {
		return cri;
	}

	public void setCri(int cri) {
		this.cri = cri;
	}

	public int getParry() {
		return parry;
	}

	public void setParry(int parry) {
		this.parry = parry;
	}

	public int getSoliderTokenId() {
		return soliderTokenId;
	}

	public void setSoliderTokenId(int soliderTokenId) {
		this.soliderTokenId = soliderTokenId;
	}

	public int getPlan() {
		return plan;
	}

	public void setPlan(int plan) {
		this.plan = plan;
	}

	public int getSkill1() {
		return skill1;
	}

	public void setSkill1(int skill1) {
		this.skill1 = skill1;
	}

	public int getSkill2() {
		return skill2;
	}

	public void setSkill2(int skill2) {
		this.skill2 = skill2;
	}

	public int getSkill3() {
		return skill3;
	}

	public void setSkill3(int skill3) {
		this.skill3 = skill3;
	}

	public int getSkill4() {
		return skill4;
	}

	public void setSkill4(int skill4) {
		this.skill4 = skill4;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getCareer() {
		return career;
	}

	public void setCareer(int career) {
		this.career = career;
	}

	public int getNormalPlan() {
		return normalPlan;
	}

	public void setNormalPlan(int normalPlan) {
		this.normalPlan = normalPlan;
	}

}
