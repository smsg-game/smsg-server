package com.lodogame.game.dao;

public interface UserDailyGainLogDao {

	/**
	 * 获取用户当天掉落
	 * 
	 * @param userId
	 * @param date
	 * @return
	 */
	public int getUserDailyGain(String userId, int type);

	/**
	 * 累加用户当天掉落
	 * 
	 * @param userId
	 * @param date
	 * @param type
	 * @param amount
	 * @return
	 */
	public boolean addUserDailyGain(String userId, int type, int amount);
}
