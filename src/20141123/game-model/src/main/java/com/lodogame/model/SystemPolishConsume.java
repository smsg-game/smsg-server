package com.lodogame.model;

public class SystemPolishConsume {
	private int id;
	
	/**
	 * 洗练类型
	 */
	private int polishType;
	
	/**
	 * 消耗的洗练石
	 */
	private int polishStone;
	
	/**
	 * 消耗的银币
	 */
	private int copper;
	
	/**
	 * 消耗的金币
	 */
	private int gold;
	
	/**
	 * 每次洗练攻击增加的下限 
	 */
	private double attackFloor;
	
	/**
	 * 每次洗练攻击增加的上限 
	 */
	private double attackCeiling;
	
	/**
	 * 每次洗练生命增加的下限 
	 */
	private double lifeFloor;
	
	/**
	 * 每次洗练生命增加的上限 
	 */
	private double lifeCeiling;
	
	/**
	 * 每次洗练防御增加的下限 
	 */
	private double defenseFloor;
	
	/**
	 * 每次洗练防御增加的上限 
	 */
	private double defenseCeiling;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPolishType() {
		return polishType;
	}

	public void setPolishType(int polishType) {
		this.polishType = polishType;
	}

	public int getPolishStone() {
		return polishStone;
	}

	public void setPolishStone(int polishStone) {
		this.polishStone = polishStone;
	}

	public int getCopper() {
		return copper;
	}

	public void setCopper(int copper) {
		this.copper = copper;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public double getAttackFloor() {
		return attackFloor;
	}

	public void setAttackFloor(double attackFloor) {
		this.attackFloor = attackFloor;
	}

	public double getAttackCeiling() {
		return attackCeiling;
	}

	public void setAttackCeiling(double attackCeiling) {
		this.attackCeiling = attackCeiling;
	}

	public double getLifeFloor() {
		return lifeFloor;
	}

	public void setLifeFloor(double lifeFloor) {
		this.lifeFloor = lifeFloor;
	}

	public double getLifeCeiling() {
		return lifeCeiling;
	}

	public void setLifeCeiling(double lifeCeiling) {
		this.lifeCeiling = lifeCeiling;
	}

	public double getDefenseFloor() {
		return defenseFloor;
	}

	public void setDefenseFloor(double defenseFloor) {
		this.defenseFloor = defenseFloor;
	}

	public double getDefenseCeiling() {
		return defenseCeiling;
	}

	public void setDefenseCeiling(double defenseCeiling) {
		this.defenseCeiling = defenseCeiling;
	}
	
}
