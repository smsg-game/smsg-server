package com.lodogame.model;

import java.io.Serializable;

/**
 * 系统道具表
 * 
 * @author jacky
 * 
 */
public class SystemTool implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 道具ID
	 */
	private int toolId;

	/**
	 * 类型
	 */
	private int type;

	/**
	 * 道具名称
	 */
	private String name;

	/**
	 * 道具描述
	 */
	private String description;

	/**
	 * 道具价格
	 */
	private int price;

	/**
	 * 道具图标
	 */
	private String imgId;
	
	/**
	 *  需要消耗的钥匙
	 */
	private int looseToolId;
	
	/**
	 * 概率（神器强化专用）
	 */
	private int rate;

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public int getToolId() {
		return toolId;
	}

	public void setToolId(int toolId) {
		this.toolId = toolId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getImgId() {
		return imgId;
	}

	public void setImgId(String imgId) {
		this.imgId = imgId;
	}

	public int getLooseToolId() {
		return looseToolId;
	}

	public void setLooseToolId(int looseToolId) {
		this.looseToolId = looseToolId;
	}

}
