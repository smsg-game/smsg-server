package com.lodogame.ldsg.bo;

import java.io.Serializable;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

/**
 * 用户道具
 * 
 * @author jacky
 * 
 */
@Compress
public class UserToolBO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 道具ID
	 */
	@Mapper(name = "tid")
	private int toolId;

	/**
	 * 道具数量
	 */
	@Mapper(name = "cnt")
	private int toolNum;

	/**
	 * 道具类型
	 */
	@Mapper(name = "tlt")
	private int toolType;
	
	/**
	 * 强化概率（神器强化用）
	 */
	@Mapper(name = "pro")
	private int probability;
	
	public int getProbability() {
		return probability;
	}

	public void setProbability(int probability) {
		this.probability = probability;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public int getToolType() {
		return toolType;
	}

	public void setToolType(int toolType) {
		this.toolType = toolType;
	}

}
