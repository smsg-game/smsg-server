package com.lodogame.model;

/**
 * 系统技能分组
 * 
 * @author jacky
 * 
 */
public class SystemSkillGroup {

	/**
	 * 技能组ID
	 */
	private int skillGroupId;

	/**
	 * 技能类型
	 */
	private int type;

	/**
	 * 道具ID(技能书)
	 */
	private int toolId;

	/**
	 * 说明
	 */
	private String remark;

	/**
	 * 品质
	 */
	private int color;

	/**
	 * 训练花费
	 */
	private int trainPrice;

	/**
	 * 锁定花费
	 */
	private int lockPrice;

	/**
	 * 技能图标ID
	 */
	private int imgId;

	public int getSkillGroupId() {
		return skillGroupId;
	}

	public void setSkillGroupId(int skillGroupId) {
		this.skillGroupId = skillGroupId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getToolId() {
		return toolId;
	}

	public void setToolId(int toolId) {
		this.toolId = toolId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getTrainPrice() {
		return trainPrice;
	}

	public void setTrainPrice(int trainPrice) {
		this.trainPrice = trainPrice;
	}

	public int getLockPrice() {
		return lockPrice;
	}

	public void setLockPrice(int lockPrice) {
		this.lockPrice = lockPrice;
	}

	public int getImgId() {
		return imgId;
	}

	public void setImgId(int imgId) {
		this.imgId = imgId;
	}

}
