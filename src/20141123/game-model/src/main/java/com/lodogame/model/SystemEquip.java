package com.lodogame.model;

import java.io.Serializable;

public class SystemEquip implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 装备ID
	 */
	private int equipId;

	/**
	 * 装备名称
	 */
	private String equipName;

	/**
	 * 装备类型
	 */
	private int equipType;

	/**
	 * 物攻
	 */
	private int physicalAttack;

	/**
	 * 物防
	 */
	private int physicalDefense;

	/**
	 * 生命
	 */
	private int life;

	/**
	 * 最大生命
	 */
	private int maxLevel;

	/**
	 * 品质颜色
	 */
	private int color;

	/**
	 * 星级
	 */
	private int equipStar;

	/**
	 * 职业
	 */
	private int career;
	
	/*
	 * 神器专用
	 */
	private int heroId = 0;

	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}

	public int getEquipStar() {
		return equipStar;
	}

	public void setEquipStar(int equipStar) {
		this.equipStar = equipStar;
	}

	public int getEquipId() {
		return equipId;
	}

	public void setEquipId(int equipId) {
		this.equipId = equipId;
	}

	public String getEquipName() {
		return equipName;
	}

	public void setEquipName(String equipName) {
		this.equipName = equipName;
	}

	public int getEquipType() {
		return equipType;
	}

	public void setEquipType(int equipType) {
		this.equipType = equipType;
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

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getCareer() {
		return career;
	}

	public void setCareer(int career) {
		this.career = career;
	}

}
