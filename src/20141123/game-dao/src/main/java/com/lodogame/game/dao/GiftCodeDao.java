package com.lodogame.game.dao;

import com.lodogame.model.GiftCode;

public interface GiftCodeDao {

	/**
	 * 获取礼包码
	 * 
	 * @param code
	 * @return
	 */
	public GiftCode get(String code);

	/**
	 * 更新礼包码为已经使用
	 * 
	 * @param code
	 * @param userId
	 * @return
	 */
	public boolean update(String code, String userId);

}
