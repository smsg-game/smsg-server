package com.lodogame.model;

public class SystemSkillUpgrade {

	/**
	 * 要升级的技能组ID
	 */
	private int skillGroupId;

	/**
	 * 升级后的技能组ID
	 */
	private int upgradeSkillGroupId;

	/**
	 * 需要的技能书ID
	 */
	private int needToolId;

	public int getSkillGroupId() {
		return skillGroupId;
	}

	public void setSkillGroupId(int skillGroupId) {
		this.skillGroupId = skillGroupId;
	}

	public int getUpgradeSkillGroupId() {
		return upgradeSkillGroupId;
	}

	public void setUpgradeSkillGroupId(int upgradeSkillGroupId) {
		this.upgradeSkillGroupId = upgradeSkillGroupId;
	}

	public int getNeedToolId() {
		return needToolId;
	}

	public void setNeedToolId(int needToolId) {
		this.needToolId = needToolId;
	}

}
