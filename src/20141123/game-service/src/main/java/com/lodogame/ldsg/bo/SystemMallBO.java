package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class SystemMallBO {

	/**
	 * 商城物品ID
	 */
	@Mapper(name = "mid")
	private int mallId;

	/**
	 * 标签
	 */
	@Mapper(name = "tag")
	private String tag;

	/**
	 * 名称
	 */
	@Mapper(name = "nm")
	private String name;

	/**
	 * 描述
	 */
	@Mapper(name = "ds")
	private String description;

	/**
	 * 道具类型
	 */
	@Mapper(name = "tp")
	private int toolType;

	/**
	 * 道具ID
	 */
	@Mapper(name = "tid")
	private int toolId;

	/**
	 * 道具数量
	 */
	@Mapper(name = "tn")
	private int toolNum;

	/**
	 * 货币类型(1:元宝 2:银币)
	 */
	@Mapper(name = "mt")
	private int moneyType;

	/**
	 * 价格
	 */
	@Mapper(name = "am")
	private int amount;

	/**
	 * 图标ID
	 */
	@Mapper(name = "img")
	private int imgId;

	/**
	 * 当天可买次数
	 */
	@Mapper(name = "rd")
	private int dailyRemainder;

	/**
	 * 需要的vip等级
	 */
	@Mapper(name = "nvl")
	private int needVipLevel;

	/**
	 * 可见的vip等级
	 */
	@Mapper(name = "svl")
	private int seeVipLevel;

	/**
	 * 总的可购买次数
	 */
	@Mapper(name = "trd")
	private int totalRemainder;

	/**
	 * 商品在打折期间的折扣，以1到100表示，例如，90 表示打九折
	 */
	@Mapper(name = "dc")
	private int discount;

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

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

	public final int getDailyRemainder() {
		return dailyRemainder;
	}

	public final void setDailyRemainder(int dailyRemainder) {
		this.dailyRemainder = dailyRemainder;
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

	public int getTotalRemainder() {
		return totalRemainder;
	}

	public void setTotalRemainder(int totalRemainder) {
		this.totalRemainder = totalRemainder;
	}

}
