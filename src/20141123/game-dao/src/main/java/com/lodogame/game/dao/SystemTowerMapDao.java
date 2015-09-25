package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.SystemTowerMap;

public interface SystemTowerMapDao {
	
	/**
	 * 获得所有地图数据
	 * @return
	 */
	List<SystemTowerMap> getAll();

}
