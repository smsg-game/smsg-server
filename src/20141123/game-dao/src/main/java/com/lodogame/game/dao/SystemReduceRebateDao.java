package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.SystemReduceRebate;

public interface SystemReduceRebateDao {
	public SystemReduceRebate get(int id);
	
	public List<SystemReduceRebate> getAllByType(int type);
}
