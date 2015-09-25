package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.SystemTotalDayPayReward;

public interface SystemTotalDayPayRewardDao {

	/**
	 * 获取积天充值奖励列表
	 * 
	 * @return
	 */
	public List<SystemTotalDayPayReward> getAll();
	
	/**
	 * 获取积天充值奖励列表
	 * @param aid
	 * @return
	 */
	public SystemTotalDayPayReward getPayRewardByAid(int aid);

}
