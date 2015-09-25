package com.lodogame.model;

/**
 * 系统被 动技能
 * 
 * @author jacky
 * 
 */
public class SystemPassiveSkill {

	/**
	 * 唯一ID
	 */
	private int passiveSkillId;

	/**
	 * 技能分组ID
	 */
	private int skillGroupId;

	/**
	 * 技能ID
	 */
	private int skillId;

	/**
	 * 技能类型
	 */
	private int type;

	/**
	 * 属性值
	 */
	private double value;

	/**
	 * 随机下限
	 */
	private int lowerNum;

	/**
	 * 随机上限
	 */
	private int upperNum;

	public int getPassiveSkillId() {
		return passiveSkillId;
	}

	public void setPassiveSkillId(int passiveSkillId) {
		this.passiveSkillId = passiveSkillId;
	}

	public int getSkillGroupId() {
		return skillGroupId;
	}

	public void setSkillGroupId(int skillGroupId) {
		this.skillGroupId = skillGroupId;
	}

	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public int getLowerNum() {
		return lowerNum;
	}

	public void setLowerNum(int lowerNum) {
		this.lowerNum = lowerNum;
	}

	public int getUpperNum() {
		return upperNum;
	}

	public void setUpperNum(int upperNum) {
		this.upperNum = upperNum;
	}

}
