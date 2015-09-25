package com.lodogame.model;

import java.io.Serializable;

/**
 * 武将攻击力相关属性
 * 
 * @author jacky
 * 
 */
public class SystemHeroAttr implements Serializable {

	private static final long serialVersionUID = 6389571214435477902L;

	private int systemHeroAttrId;

	private int systemHeroId;

	private int heroLevel;

	private int life;

	private int physicalAttack;

	private int physicalDefense;

	private int speed;

	private int hit;

	private int duck;

	private int crit;

	private int parry;

	/**
	 * 出售价格
	 */
	private int recyclePrice;

	/**
	 * 吞噬经验
	 */
	private int mixExp;

	public int getSystemHeroAttrId() {
		return systemHeroAttrId;
	}

	public void setSystemHeroAttrId(int systemHeroAttrId) {
		this.systemHeroAttrId = systemHeroAttrId;
	}

	public int getSystemHeroId() {
		return systemHeroId;
	}

	public void setSystemHeroId(int systemHeroId) {
		this.systemHeroId = systemHeroId;
	}

	public int getHeroLevel() {
		return heroLevel;
	}

	public void setHeroLevel(int heroLevel) {
		this.heroLevel = heroLevel;
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
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

	public int getCrit() {
		return crit;
	}

	public void setCrit(int crit) {
		this.crit = crit;
	}

	public int getParry() {
		return parry;
	}

	public void setParry(int parry) {
		this.parry = parry;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getRecyclePrice() {
		return recyclePrice;
	}

	public void setRecyclePrice(int recyclePrice) {
		this.recyclePrice = recyclePrice;
	}

	public int getMixExp() {
		return mixExp;
	}

	public void setMixExp(int mixExp) {
		this.mixExp = mixExp;
	}

}
