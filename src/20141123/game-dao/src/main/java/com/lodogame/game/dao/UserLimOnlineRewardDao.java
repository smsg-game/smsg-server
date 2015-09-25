package com.lodogame.game.dao;

import com.lodogame.model.UserLimOnlineReward;

public interface UserLimOnlineRewardDao {

	/**
	 * 获取最近的记录
	 * @param uid
	 * @return
	 */
	public UserLimOnlineReward getLatestLog(String uid);

	public boolean add(UserLimOnlineReward reward);

}
