package com.lodogame.game.dao;

import com.lodogame.model.SystemMonster;

public interface SystemMonsterDao {

	/**
	 * 获取单个怪物信息
	 * 
	 * @param monsterId
	 * @return
	 */
	public SystemMonster get(int monsterId);

	/**
	 * 
	 * @param systemMonster
	 */
	public void add(SystemMonster systemMonster);
}
