package com.lodogame.ldsg.bo;

import java.io.Serializable;
import java.util.List;

public class BattleBO implements Serializable {

	private static final long serialVersionUID = 6662737799210758450L;

	private int userLevel;

	/**
	 * 血量、攻防等的加成比例
	 * 
	 * 在百人斩中需要设置，其他地方不需设置的话，置为0
	 */
	private double addRatio;
	
	/**
	 * 鼓舞攻击加成
	 */
	private double attackRatio;
	
	/**
	 * 鼓舞防御加成
	 */
	private double defenseRatio;

	private List<BattleHeroBO> battleHeroBOList;

	
	public double getAttackRatio() {
		return attackRatio;
	}

	public void setAttackRatio(double attackRatio) {
		this.attackRatio = attackRatio;
	}

	public double getDefenseRatio() {
		return defenseRatio;
	}

	public void setDefenseRatio(double defenseRatio) {
		this.defenseRatio = defenseRatio;
	}

	public List<BattleHeroBO> getBattleHeroBOList() {
		return battleHeroBOList;
	}

	public void setBattleHeroBOList(List<BattleHeroBO> battleHeroBOList) {
		this.battleHeroBOList = battleHeroBOList;
	}

	public int getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(int userLevel) {
		this.userLevel = userLevel;
	}

	public double getAddRatio() {
		return addRatio;
	}

	public void setAddRatio(double addRatio) {
		this.addRatio = addRatio;
	}

}
