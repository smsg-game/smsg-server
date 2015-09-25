package com.lodogame.game.dao;

import com.lodogame.model.RankScoreCfg;

public interface RankScoreCfgDao {
	
	/**
	 * 获取所有数据
	 * @return
	 */
	public RankScoreCfg getByRank(int rank);
}
