package com.lodogame.model;

import java.io.Serializable;

public class SystemForcesMonster implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	private int forcesArmyId;

	/**
	 * 部队ID
	 */
	private int forcesId;

	/**
	 * 怪物ID
	 */
	private int monsterId;

	/**
	 * 怪物名字
	 */
	private String monsterName;

	/**
	 * 怪物站位
	 */
	private int pos;

	/**
	 * 怪物类型
	 */
	private int forcesType;

	public int getForcesArmyId() {
		return forcesArmyId;
	}

	public void setForcesArmyId(int forcesArmyId) {
		this.forcesArmyId = forcesArmyId;
	}

	public int getForcesId() {
		return forcesId;
	}

	public void setForcesId(int forcesId) {
		this.forcesId = forcesId;
	}

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

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public int getForcesType() {
		return forcesType;
	}

	public void setForcesType(int forcesType) {
		this.forcesType = forcesType;
	}

}
