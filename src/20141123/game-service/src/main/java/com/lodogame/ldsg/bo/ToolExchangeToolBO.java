package com.lodogame.ldsg.bo;

import java.io.Serializable;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class ToolExchangeToolBO implements Serializable {

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
	private int exchangeNum;

	public int getToolId() {
		return toolId;
	}

	public void setToolId(int toolId) {
		this.toolId = toolId;
	}

	public int getExchangeNum() {
		return exchangeNum;
	}

	public void setExchangeNum(int exchangeNum) {
		this.exchangeNum = exchangeNum;
	}
	
	

}
