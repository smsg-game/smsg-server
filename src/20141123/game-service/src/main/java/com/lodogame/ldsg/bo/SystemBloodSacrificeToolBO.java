package com.lodogame.ldsg.bo;

import java.io.Serializable;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class SystemBloodSacrificeToolBO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 道具ID
	 */
	@Mapper(name = "tid")
	private int toolId;

	/**
	 * 消耗的道具数量
	 */
	@Mapper(name = "num")
	private int ToolNum;

	public int getToolId() {
		return toolId;
	}

	public void setToolId(int toolId) {
		this.toolId = toolId;
	}

	public int getToolNum() {
		return ToolNum;
	}

	public void setToolNum(int toolNum) {
		ToolNum = toolNum;
	}


}
