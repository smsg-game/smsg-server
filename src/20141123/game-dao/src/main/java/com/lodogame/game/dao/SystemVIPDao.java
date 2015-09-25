package com.lodogame.game.dao;

import com.lodogame.model.SystemVIP;

public interface SystemVIPDao {

	/**
	 * 根据VIP等级获取VIP信息
	 * 
	 * @param VIPLevel
	 * @return
	 */
	public SystemVIP get(int VIPLevel);

}
