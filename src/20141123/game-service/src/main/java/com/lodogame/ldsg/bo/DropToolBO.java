package com.lodogame.ldsg.bo;

import java.io.Serializable;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

/**
 * 掉落道具BO对象
 * 
 * @author jacky
 * 
 */
@Compress
public class DropToolBO implements Serializable {

	private static final long serialVersionUID = 1L;

	public DropToolBO(int toolType, int toolId, int toolNum) {
		this.toolType = toolType;
		this.toolId = toolId;
		this.toolNum = toolNum;
	}

	public DropToolBO(int toolType, int toolId, String value) {
		this.toolType = toolType;
		this.toolId = toolId;
		this.value = value;
	}

	/**
	 * 道具类型
	 */
	@Mapper(name = "tt")
	protected int toolType;

	/**
	 * 道具ID
	 */
	@Mapper(name = "tid")
	private int toolId;

	/**
	 * 道具数量
	 */
	@Mapper(name = "cnt")
	protected int toolNum;

	/**
	 * 保存其它信息，如用户武将，则这里保存的是用户武将ID
	 */
	protected String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
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
