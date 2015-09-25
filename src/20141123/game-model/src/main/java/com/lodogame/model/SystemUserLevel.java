package com.lodogame.model;

import java.io.Serializable;

/**
 * 玩家等级经验配置
 * 
 * @author jacky
 * 
 */
public class SystemUserLevel implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 玩家等级
	 */
	private int userLevel;

	/**
	 * 玩家经验
	 */
	private int exp;

	/**
	 * 玩家攻击
	 */
	private int attack;

	/**
	 * 玩家防御
	 */
	private int defense;

	/**
	 * 玩家上阵人数
	 */
	private int battleNum;

	public int getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(int userLevel) {
		this.userLevel = userLevel;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getDefense() {
		return defense;
	}

	public void setDefense(int defense) {
		this.defense = defense;
	}

	public int getBattleNum() {
		return battleNum;
	}

	public void setBattleNum(int battleNum) {
		this.battleNum = battleNum;
	}

}
