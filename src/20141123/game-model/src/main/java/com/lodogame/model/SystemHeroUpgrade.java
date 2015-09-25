package com.lodogame.model;

import java.io.Serializable;

/**
 * 武将合成配置
 * 
 * @author jacky
 * 
 */
public class SystemHeroUpgrade implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 进阶武将ID
	 */
	public int systemHeroId;

	/**
	 * 进阶后武将ID
	 */
	private int upgradeHeroId;

	/**
	 * 进阶概率
	 */
	private int upgradeRate;

	/**
	 * 100%进阶需要的金币
	 */
	private int forceUpgradeGold;

	/**
	 * 需要的等级
	 */
	private int needLevel;

	public int getSystemHeroId() {
		return systemHeroId;
	}

	public void setSystemHeroId(int systemHeroId) {
		this.systemHeroId = systemHeroId;
	}

	public int getUpgradeHeroId() {
		return upgradeHeroId;
	}

	public void setUpgradeHeroId(int upgradeHeroId) {
		this.upgradeHeroId = upgradeHeroId;
	}

	public int getNeedLevel() {
		return needLevel;
	}

	public void setNeedLevel(int needLevel) {
		this.needLevel = needLevel;
	}

	public int getUpgradeRate() {
		return upgradeRate;
	}

	public void setUpgradeRate(int upgradeRate) {
		this.upgradeRate = upgradeRate;
	}

	public int getForceUpgradeGold() {
		return forceUpgradeGold;
	}

	public void setForceUpgradeGold(int forceUpgradeGold) {
		this.forceUpgradeGold = forceUpgradeGold;
	}

}
