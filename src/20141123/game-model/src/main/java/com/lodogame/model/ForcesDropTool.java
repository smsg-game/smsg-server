package com.lodogame.model;

import java.io.Serializable;

/**
 * 怪物部队掉落数据表
 * 
 * @author jacky
 * 
 */
public class ForcesDropTool implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 部队ID
	 */
	private int foresId;

	/**
	 * 道具类型
	 */
	private int toolType;

	/**
	 * 道具ID
	 */
	private int toolId;

	/**
	 * 道具名称
	 */
	private String toolName;

	/**
	 * 道具掉落概率下限
	 */
	private int lowerNum;

	/**
	 * 怪物掉落概率上限
	 */
	private int upperNum;

	/**
	 * 道具数量
	 */
	private int toolNum;

	public int getForesId() {
		return foresId;
	}

	public void setForesId(int foresId) {
		this.foresId = foresId;
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

	public String getToolName() {
		return toolName;
	}

	public void setToolName(String toolName) {
		this.toolName = toolName;
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

	public int getToolNum() {
		return toolNum;
	}

	public void setToolNum(int toolNum) {
		this.toolNum = toolNum;
	}

}
