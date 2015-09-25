package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.UserContestBetLog;

public interface ContestBetDao {
	public void saveContestBetLog(UserContestBetLog betLog);

	public List<String> getGoodBetUserIdList(int session, String champUserId);

	public UserContestBetLog getUserBetLog(int currentSession, String userId);

	public void clearBetCache();
}
