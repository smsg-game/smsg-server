package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class ActivityDrawToolBO {
	
	@Mapper(name = "tlt")
	private int toolType;
	
	@Mapper(name = "tid")
	private int toolId;
	
	@Mapper(name = "ifh")
	private int isFlash;
	
	@Mapper(name = "cnt")
	private int toolNum;
	
	private int outId;
	
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
	public int getIsFlash() {
		return isFlash;
	}
	public void setIsFlash(int isFlash) {
		this.isFlash = isFlash;
	}
	public int getToolNum() {
		return toolNum;
	}
	public void setToolNum(int toolNum) {
		this.toolNum = toolNum;
	}
	public int getOutId() {
		return outId;
	}
	public void setOutId(int outId) {
		this.outId = outId;
	}
	
	
}
