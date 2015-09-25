package com.lodogame.game.dao;

import com.lodogame.model.UserHeroExchange;

public interface UserHeroExchangeDao {

	/**
	 * 获取用户的刷将信息
	 * 
	 * @param userId
	 * @return
	 */
	public UserHeroExchange get(String userId);

	/**
	 * 添加用户武将兑换信息
	 * 
	 * @param userHeroExchange
	 * @return
	 */
	public boolean add(UserHeroExchange userHeroExchange);

	/**
	 * 更新用户武将兑换信息
	 * 
	 * @param userId
	 * @param userWeek
	 * @param systemWeek
	 * @return
	 */
	public boolean updateUserHeroExchange(String userId, int userWeek, int systemWeek, int times);

}
