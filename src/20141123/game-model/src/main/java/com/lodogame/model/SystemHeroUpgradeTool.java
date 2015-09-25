package com.lodogame.model;

import java.io.Serializable;

/**
 * 武将进阶材料
 * 
 * @author jacky
 * 
 */
public class SystemHeroUpgradeTool implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 材料类型
	 */
	private int toolTypeId;

	/**
	 * 材料ID
	 */
	private int toolId;

	/**
	 * 需要的数量
	 */
	private int toolNum;

	public int getToolTypeId() {
		return toolTypeId;
	}

	public void setToolTypeId(int toolTypeId) {
		this.toolTypeId = toolTypeId;
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
