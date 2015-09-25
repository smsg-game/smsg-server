package com.lodogame.game.dao;

import com.lodogame.model.UserMallInfo;

public interface UserMallInfoDao {

	/**
	 * 添加
	 * 
	 * @param userId
	 * @param mallId
	 * @param totalBuyNum
	 * @param dayBuyNum
	 * @return
	 */
	public boolean add(String userId, int mallId, int totalBuyNum, int dayBuyNum);

	/**
	 * 获取
	 * 
	 * @param userId
	 * @param systemMallId
	 * @return
	 */
	public UserMallInfo get(String userId, int mallId);

	/**
	 * 添加
	 * 
	 * @param userMallInfo
	 * @return
	 */
	public boolean add(UserMallInfo userMallInfo);

}
