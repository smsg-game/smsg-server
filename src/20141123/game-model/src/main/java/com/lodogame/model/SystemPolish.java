package com.lodogame.model;

public class SystemPolish {
	private int systemPolishId;
	
	/**
	 * 装备ID
	 */
	private int systemEquipId;
	
	/**
	 * 攻击上限
	 */
	private int attackLimit;
	
	/**
	 * 生命上限
	 */
	private int lifeLimit;
	
	/**
	 * 防御上限
	 */
	private int defenseLimit;

	public int getSystemPolishId() {
		return systemPolishId;
	}

	public void setSystemPolishId(int systemPolishId) {
		this.systemPolishId = systemPolishId;
	}

	public int getSystemEquipId() {
		return systemEquipId;
	}

	public void setSystemEquipId(int systemEquipId) {
		this.systemEquipId = systemEquipId;
	}

	public int getAttackLimit() {
		return attackLimit;
	}

	public void setAttackLimit(int attackLimit) {
		this.attackLimit = attackLimit;
	}

	public int getLifeLimit() {
		return lifeLimit;
	}

	public void setLifeLimit(int lifeLimit) {
		this.lifeLimit = lifeLimit;
	}

	public int getDefenseLimit() {
		return defenseLimit;
	}

	public void setDefenseLimit(int defenseLimit) {
		this.defenseLimit = defenseLimit;
	}
	
	
}
