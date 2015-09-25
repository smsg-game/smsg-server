package com.lodogame.game.dao;

import com.lodogame.model.SystemUserLevel;

public interface SystemUserLevelDao {

	/**
	 * 根据用户经验，获取用户等级配置
	 * 
	 * @param exp
	 * @return
	 */
	public SystemUserLevel getUserLevel(long exp);

	/**
	 * 根据用户等级，获取用户等级配置
	 * 
	 * @param level
	 * @return
	 */
	public SystemUserLevel get(int level);

	/**
	 * 添加用户等级配置(到缓存)
	 * 
	 * @param systemUserLevel
	 */
	public void add(SystemUserLevel systemUserLevel);
}
