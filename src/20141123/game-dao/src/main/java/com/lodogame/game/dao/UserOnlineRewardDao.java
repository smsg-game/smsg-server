package com.lodogame.game.dao;

import com.lodogame.model.UserOnlineReward;

public interface UserOnlineRewardDao {

	/**
	 * 获取用户在线奖励信息
	 * 
	 * @param userId
	 * @return
	 */
	public UserOnlineReward get(String userId);

	/**
	 * 添加用户在线奖励信息
	 * 
	 * @param userOnlineReward
	 * @return
	 */
	public boolean add(UserOnlineReward userOnlineReward);

	/**
	 * 更新用户在线奖励信息
	 * 
	 * @param userId
	 * @param subType
	 * @return
	 */
	public boolean update(String userId, int subType);

}
