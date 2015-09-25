package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.SystemOncePayReward;

public interface SystemOncePayRewardDao {

	/**
	 * 获取所有的数据
	 * @return
	 */
	List<SystemOncePayReward> getAll();
	/**
	 * 根据ID获取奖励信息
	 * @param aid
	 * @return
	 */
	SystemOncePayReward getById(int id);

	/**
	 * 获取指定奖励的下一个阶层的数据
	 * @param rid
	 * @return
	 */
	SystemOncePayReward getNextById(int rid);

}
