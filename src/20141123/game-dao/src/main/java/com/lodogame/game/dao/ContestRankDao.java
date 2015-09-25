package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.UserContestRank;

public interface ContestRankDao {

	public void saveContestRank(UserContestRank userContestRank);

	public List<UserContestRank> getLastSessionRank(int currentSession);

	/**
	 * 读取最近一场比赛的冠军
	 */
	public UserContestRank getLatestSessionChamp();

}
