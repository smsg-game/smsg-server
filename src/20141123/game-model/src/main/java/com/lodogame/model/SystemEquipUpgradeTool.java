package com.lodogame.model;

/*
 * 合成装备需要的材料
 */
public class SystemEquipUpgradeTool {

	/**
	 * 装备ID
	 */
	private int equipId;

	/**
	 * 道具类型
	 */
	private int toolType;

	/**
	 * 道具ID
	 */
	private int toolId;

	/**
	 * 道具数量
	 */
	private int toolNum;

	public int getEquipId() {
		return equipId;
	}

	public void setEquipId(int equipId) {
		this.equipId = equipId;
	}

	public int getToolType() {
		return toolType;
	}

	public void setToolType(int toolType) {
		this.toolType = toolType;
	}

	public int getToolId() {
		return toolId;
	}

	public void setToolId(int toolId) {
		this.toolId = toolId;
	}

	public int getToolNum() {
		return toolNum;
	}

	public void setToolNum(int toolNum) {
		this.toolNum = toolNum;
	}

}
