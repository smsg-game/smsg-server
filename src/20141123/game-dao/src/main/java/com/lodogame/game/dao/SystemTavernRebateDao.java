package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.SystemTavernRebate;

public interface SystemTavernRebateDao {
	
	public SystemTavernRebate get(int id);
	
	public List<SystemTavernRebate> getAllByType(int typeA, int typeB);
	
}
