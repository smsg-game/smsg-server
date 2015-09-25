package com.lodogame.game.dao;

import com.lodogame.model.SystemVipLevel;

public interface SystemVipLevelDao {

	/**
	 * 获取VIP等级配置
	 * 
	 * @param vipLevel
	 * @return
	 */
	public SystemVipLevel get(int vipLevel);

	/**
	 * 按充的钱获取VIP等级配置
	 * 
	 * @param money
	 * @return
	 */
	public SystemVipLevel getBuyMoney(int money);

}
