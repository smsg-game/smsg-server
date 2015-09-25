package com.lodogame.game.dao;

import com.lodogame.model.SystemHeroAttr;

public interface SystemHeroAttrDao {

	/**
	 * 获取武将攻击属性
	 * 
	 * @param heroId
	 * @return
	 */
	public SystemHeroAttr getHeroAttr(int systemHeroId, int heroLevel);

}
