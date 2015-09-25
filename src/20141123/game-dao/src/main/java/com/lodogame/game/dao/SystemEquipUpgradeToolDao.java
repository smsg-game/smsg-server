package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.SystemEquipUpgradeTool;

public interface SystemEquipUpgradeToolDao {

	/**
	 * 获取合成装备需要的材料列表
	 * 
	 * @param equipId
	 * @return
	 */
	public List<SystemEquipUpgradeTool> getList(int equipId);

}
