package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.SystemTotalPayReward;

public interface SystemTotalPayRewardDao {

	public SystemTotalPayReward getById(int rid);

	/**
	 * 根据累计充值天数查询累计充值奖励
	 * 
	 * @return
	 */
	public List<SystemTotalPayReward> getByPayment(int userTotalDay);

	/**
	 * 获取所有的充值奖励类型
	 * 
	 * @return
	 */
	public List<SystemTotalPayReward> getAll();

}
