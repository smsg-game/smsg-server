package com.lodogame.game.dao;

import java.util.Date;
import java.util.List;

import com.lodogame.model.UserPayReward;

public interface UserPayRewardDao {

	/**
	 * 新增用户奖励领取记录
	 * @param userPayReward
	 * @return
	 */
	boolean add(UserPayReward userPayReward);
	
	/**
	 * 获取用户支付奖励列表
	 * @param userId
	 * @return
	 */
	List<UserPayReward> getList(String userId);

	/**
	 * 获取用户已经领取的订单ID列表
	 * @param userId
	 * @param aid
	 * @param rid
	 * @return
	 */
	List<UserPayReward> getReceivedOrderIdList(String userId, int aid, int rid, Date startTime, Date endTime);

	/**
	 * 获取用户已经领取的订单
	 * @param userId
	 * @param aid
	 * @param rid
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public UserPayReward  getUserPayReward(String userId, int aid, int rid, Date startTime, Date endTime);
}
