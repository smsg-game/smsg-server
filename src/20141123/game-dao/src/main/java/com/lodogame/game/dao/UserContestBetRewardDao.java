package com.lodogame.game.dao;

import com.lodogame.model.UserContestBetReward;

public interface UserContestBetRewardDao {

	public UserContestBetReward get(String userId, int session);
}
