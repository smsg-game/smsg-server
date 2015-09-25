package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.SystemEquipSuit;
import com.lodogame.model.SystemSuitDetail;

public interface SystemEquipSuitDao {

	/**
	 * 获取
	 * 
	 * @return
	 */
	public List<SystemEquipSuit> getHeroEquipSuitList();

	/**
	 * 
	 * @param heroId
	 * @return
	 */
	public SystemEquipSuit getHeroEquipSuit(int heroId);

	/**
	 * 
	 * @param suitId
	 * @return
	 */
	public List<SystemSuitDetail> getSuitDetailList(int suitId);

}
