package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class SystemActivityDrawShowBO {

	@Mapper(name = "nm")
	private String toolName;

	@Mapper(name = "tt")
	private int toolType;

	@Mapper(name = "tid")
	private int toolId;

	@Mapper(name = "isl")
	private int isLight;

	@Mapper(name = "pos")
	private int pos;

	public String getToolName() {
		return toolName;
	}

	public void setToolName(String toolName) {
		this.toolName = toolName;
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

	public int getIsLight() {
		return isLight;
	}

	public void setIsLight(int isLight) {
		this.isLight = isLight;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

}
