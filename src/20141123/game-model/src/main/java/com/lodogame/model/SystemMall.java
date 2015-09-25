package com.lodogame.model;

public class SystemMall {

	/**
	 * 商城物品ID
	 */
	private int mallId;

	/**
	 * tag
	 */
	private String tag;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 描述
	 */
	private String description;

	/**
	 * 道具类型
	 */
	private int toolType;

	/**
	 * 道具ID
	 */
	private int toolId;

	/**
	 * 道具数量
	 */
	private int toolNum;

	/**
	 * 货币类型(1:元宝 2:银币)
	 */
	private int moneyType;

	/**
	 * 价格
	 */
	private int amount;

	/**
	 * 图标ID
	 */
	private int imgId;

	/**
	 * 玩家每天可以购买该道具的最大数量（数值零表示没有限制）
	 */
	private int dailyMaxNum;

	/**
	 * 可购买的最大次数
	 */
	private int maxNum;

	/**
	 * 需要的VIP等级
	 */
	private int needVipLevel;

	/**
	 * 可见的VIP等级
	 */
	private int seeVipLevel;

	public int getMallId() {
		return mallId;
	}

	public void setMallId(int mallId) {
		this.mallId = mallId;
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

	public int getMoneyType() {
		return moneyType;
	}

	public void setMoneyType(int moneyType) {
		this.moneyType = moneyType;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getImgId() {
		return imgId;
	}

	public void setImgId(int imgId) {
		this.imgId = imgId;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public int getDailyMaxNum() {
		return dailyMaxNum;
	}

	public void setDailyMaxNum(int dailyMaxNum) {
		this.dailyMaxNum = dailyMaxNum;
	}

	public int getMaxNum() {
		return maxNum;
	}

	public void setMaxNum(int maxNum) {
		this.maxNum = maxNum;
	}

	public int getNeedVipLevel() {
		return needVipLevel;
	}

	public void setNeedVipLevel(int needVipLevel) {
		this.needVipLevel = needVipLevel;
	}

	public int getSeeVipLevel() {
		return seeVipLevel;
	}

	public void setSeeVipLevel(int seeVipLevel) {
		this.seeVipLevel = seeVipLevel;
	}

}
