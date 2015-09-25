package com.lodogame.game.dao;

import com.lodogame.model.SystemLevelExp;

public interface SystemLevelExpDao {

	/**
	 * 根据当前经验，获取武将等级
	 * 
	 * @param exp
	 * @return
	 */
	public SystemLevelExp getHeroLevel(int exp);

	/**
	 * 根据当前等级，获取武将经验
	 * 
	 * @param level
	 * @return
	 */
	public SystemLevelExp getHeroExp(int level);

}
