package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.ContestPlayerPair;

public interface ContestPlayerPairDao {

	public List<ContestPlayerPair> getPlayerPairsByContestId(String contestId);

	public ContestPlayerPair getLastPlayerPair(String contestId);

	public void savePlayerPair(ContestPlayerPair playerPair);

	public boolean updatePlayerPairFightResult(ContestPlayerPair playerPair);

	public void saveDefUser(ContestPlayerPair playerPair);

	public void incrAttUserBetNum(ContestPlayerPair playerPair);

	public void incrDefUserBetNum(ContestPlayerPair playerPair);
}
