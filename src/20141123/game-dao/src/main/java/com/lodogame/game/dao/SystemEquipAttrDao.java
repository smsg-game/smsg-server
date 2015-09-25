package com.lodogame.game.dao;

import com.lodogame.model.SystemEquipAttr;

public interface SystemEquipAttrDao {

	/**
	 * 获取装备属性
	 * 
	 * @param heroId
	 * @return
	 */
	public SystemEquipAttr getEquipAttr(int equipId,int equipLevel);

}
