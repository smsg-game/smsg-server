package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.SystemHeroUpgradeTool;

public interface SystemHeroUpgradeToolDao {

	/**
	 * 获取武将进阶需要的材料列表
	 * 
	 * @param systemHeroId
	 * @param upgradeHeroId
	 * @return
	 */
	public List<SystemHeroUpgradeTool> getNeedToolList(int systemHeroId, int upgradeHeroId);

}
