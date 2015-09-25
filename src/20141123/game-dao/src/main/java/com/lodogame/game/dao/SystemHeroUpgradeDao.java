package com.lodogame.game.dao;

import com.lodogame.model.SystemHeroUpgrade;

/**
 * 武将进阶配置Dao
 * 
 * @author jacky
 * 
 */
public interface SystemHeroUpgradeDao {

	/**
	 * 获取一个武将的进阶配置
	 * 
	 * @param systemHeroId
	 * @return
	 */
	public SystemHeroUpgrade get(int systemHeroId);

}
