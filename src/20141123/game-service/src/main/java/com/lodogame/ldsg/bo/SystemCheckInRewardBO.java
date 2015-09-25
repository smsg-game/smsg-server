package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class SystemCheckInRewardBO {

	private int groupId;

	@Mapper(name = "day")
	private int day;

	@Mapper(name = "tt")
	private int toolType;

	@Mapper(name = "tid")
	private int toolId;

	@Mapper(name = "tn")
	private int toolNum;

	@Mapper(name = "img")
	private int imgId;

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
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

	public int getImgId() {
		return imgId;
	}

	public void setImgId(int imgId) {
		this.imgId = imgId;
	}

}
