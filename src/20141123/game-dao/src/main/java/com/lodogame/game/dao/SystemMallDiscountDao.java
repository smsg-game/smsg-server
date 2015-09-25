package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.SystemMallDiscountActivity;
import com.lodogame.model.SystemMallDiscountItems;

public interface SystemMallDiscountDao {

	/**
	 * 获取商品的折扣率
	 * @param mallId
	 * @return
	 */
	public List<SystemMallDiscountItems> getDiscountItems(String activityId);
	
	/**
	 * 获取单个打折商品
	 */
	public SystemMallDiscountItems getDiscountItem(String activityId, int mallId);
	
	public List<SystemMallDiscountItems> getAllDiscountItems();

	public List<SystemMallDiscountActivity> getAllActivity();
	
	/**
	 * 添加打折活动信息
	 */
	public boolean addDiscountActivity(SystemMallDiscountActivity activity);
	
	/**
	 * 删除打折活动信息
	 */
	public boolean delActivity(String activityId);
	
	/**
	 * 添加打折活动中有折扣的商品
	 */
	public boolean addDiscountItem(SystemMallDiscountItems item);
	
	/**
	 * 删除打折活动中有折扣的商品
	 */
	public boolean delItems(String activityId);
}
