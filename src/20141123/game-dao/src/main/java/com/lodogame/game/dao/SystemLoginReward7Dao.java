package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.SystemLoginReward7;

public interface SystemLoginReward7Dao {

	/**
	 * 读取哦所有的奖励信息
	 */
	public List<SystemLoginReward7> getAll();

	/**
	 * 根据天数获取奖励
	 * @param day
	 * @return
	 */
	public SystemLoginReward7 getByDay(int day);
}
