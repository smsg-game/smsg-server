package com.lodogame.model;

import java.io.Serializable;

/**
 * 装备攻击力相关属性
 * 
 * @author jacky
 * 
 */
public class SystemEquipAttr implements Serializable {


	private static final long serialVersionUID = 3904753795842823755L;

	private int systemEquipAttrId;

	private int equipId;

	private int equipLevel;

	private int physicalAttack;

	private int physicalDefense;

	private int life;
	
	private int recyclePrice;
	
	private int upgradePrice;

	private int upgradeRate;
	
	public int getSystemEquipAttrId() {
		return systemEquipAttrId;
	}

	public void setSystemEquipAttrId(int systemEquipAttrId) {
		this.systemEquipAttrId = systemEquipAttrId;
	}

	public int getEquipId() {
		return equipId;
	}

	public void setEquipId(int equipId) {
		this.equipId = equipId;
	}

	public int getEquipLevel() {
		return equipLevel;
	}

	public void setEquipLevel(int equipLevel) {
		this.equipLevel = equipLevel;
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

	public int getRecyclePrice() {
		return recyclePrice;
	}

	public void setRecyclePrice(int recyclePrice) {
		this.recyclePrice = recyclePrice;
	}

	public int getUpgradePrice() {
		return upgradePrice;
	}

	public void setUpgradePrice(int upgradePrice) {
		this.upgradePrice = upgradePrice;
	}

	public int getUpgradeRate() {
		return upgradeRate;
	}

	public void setUpgradeRate(int upgradeRate) {
		this.upgradeRate = upgradeRate;
	}

	
}
