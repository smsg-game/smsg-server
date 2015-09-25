package com.lodogame.model;


public class SystemMallDiscountItems {
	
	/**
	 * 打折活动 id
	 */
	private String activityId;
	
	/**
	 * 商城物品ID
	 */
	private int mallId;
	/**
	 * 商品在打折期间的折扣，以1到100表示，例如，90 表示打九折
	 */
	private int discount;
	public int getMallId() {
		return mallId;
	}
	public void setMallId(int mallId) {
		this.mallId = mallId;
	}
	public int getDiscount() {
		return discount;
	}
	public void setDiscount(int discount) {
		this.discount = discount;
	}
	public String getActivityId() {
		return activityId;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	
	
}
