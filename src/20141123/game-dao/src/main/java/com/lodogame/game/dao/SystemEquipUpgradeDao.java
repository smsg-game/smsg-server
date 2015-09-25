package com.lodogame.game.dao;

import com.lodogame.model.SystemEquipUpgrade;

public interface SystemEquipUpgradeDao {

	/**
	 * 获取装备升级配置
	 * 
	 * @param equipId
	 * @return
	 */
	public SystemEquipUpgrade get(int equipId);

}
