package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.SystemRecivePower;

public interface SystemRecivePowerDao {

	public List<SystemRecivePower> getList();

	public SystemRecivePower get(int type);
}
