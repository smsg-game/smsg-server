package com.lodogame.game.dao;

import com.lodogame.model.UserGiftbag;

public interface UserGiftbagDao {

	/**
	 * 根据类型获取最新那个
	 * 
	 * @param type
	 * @return
	 */
	public UserGiftbag getLast(String userId, int type);

	/**
	 * 添加或者更新用户礼包(次数加1)
	 * 
	 * @param userId
	 * @param type
	 * @param giftBagId
	 * @return
	 */

	public boolean addOrUpdateUserGiftbag(String userId, int type, int giftBagId);
	
	
	/**
	 * 获取玩家礼包的数量
	 * 
	 * @param userId
	 * @param type
	 * @param giftBagId
	 * @return
	 */
	public int getCount(String userId, int type, int giftBagId);
}
