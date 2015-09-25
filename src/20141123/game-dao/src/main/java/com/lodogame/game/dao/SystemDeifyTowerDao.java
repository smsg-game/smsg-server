package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.SystemDeifyTower;

public interface SystemDeifyTowerDao {

	public List<SystemDeifyTower> getAllSystemDeifyTower();

	public SystemDeifyTower get(int towerId);

}
