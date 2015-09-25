package com.lodogame.game.dao;

import com.lodogame.model.RankLog;

public interface RankLogDao {

	public boolean add(RankLog rankLog);

	public RankLog getRankLog(String date, String rankKey);

	public boolean delete(String date, String rankKey);

}
