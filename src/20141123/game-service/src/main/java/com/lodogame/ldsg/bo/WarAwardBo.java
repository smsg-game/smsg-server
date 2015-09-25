package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class WarAwardBo {
	@Mapper(name = "id")
	private int awardId;

	@Mapper(name = "an")
	private String name;

	@Mapper(name = "img")
	private String imgId;

	@Mapper(name = "nn")
	private int needNum;

	@Mapper(name = "dsc")
	private String description;

	@Mapper(name = "tp")
	private int toolType;

	@Mapper(name = "tid")
	private int toolId;

	public int getAwardId() {
		return awardId;
	}

	public void setAwardId(int awardId) {
		this.awardId = awardId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNeedNum() {
		return needNum;
	}

	public void setNeedNum(int needNum) {
		this.needNum = needNum;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImgId() {
		return imgId;
	}

	public void setImgId(String imgId) {
		this.imgId = imgId;
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

}
