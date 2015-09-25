package com.lodogame.game.dao;

import com.lodogame.model.ContestFightResult;

public interface ContestFightResultDao {

	public boolean save(ContestFightResult fightResult);

	public ContestFightResult get(String contestId);

}
