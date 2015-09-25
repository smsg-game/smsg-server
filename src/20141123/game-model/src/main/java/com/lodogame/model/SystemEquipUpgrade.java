package com.lodogame.model;

public class SystemEquipUpgrade {

	/**
	 * 要升级的装备
	 */
	private int equipId;

	/**
	 * 升级后的装备
	 */
	private int upgradeEquipId;

	public int getEquipId() {
		return equipId;
	}

	public void setEquipId(int equipId) {
		this.equipId = equipId;
	}

	public int getUpgradeEquipId() {
		return upgradeEquipId;
	}

	public void setUpgradeEquipId(int upgradeEquipId) {
		this.upgradeEquipId = upgradeEquipId;
	}

}
