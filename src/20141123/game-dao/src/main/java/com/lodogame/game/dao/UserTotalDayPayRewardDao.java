package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.UserTotalDayPayRewardLog;


public interface UserTotalDayPayRewardDao {

	/**
	 * 新增用户奖励可领取记录
	 * @param userPayReward
	 * @return
	 */
	boolean add(UserTotalDayPayRewardLog userTotalPayReward);
	
	/**
	 * 获取用户领取记录
	 * @param userId
	 * @param day
	 * @return
	 */
	public UserTotalDayPayRewardLog  getUserTotalDayPayReward(String userId, int day);
	
	/**
	 * 获取用户领取记录
	 * @param userId
	 * @return
	 */
	public UserTotalDayPayRewardLog  getLastUserTotalDayPayRewardLog(String userId);
	
	/**
	 * 添加用户领取记录
	 * @param userId
	 * @param day
	 * @return
	 */
	public boolean  addReceiveTotalDayPayRewardLog(String userId, int day);
	
	/**
	 * 获取用户的积天充值信息
	 * @param userId
	 * @return
	 */
	public List<UserTotalDayPayRewardLog> getUserAllTotalDayPayRewardLog(String userId);

}
