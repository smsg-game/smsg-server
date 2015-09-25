package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.ContestReward;
import com.lodogame.model.UserRecContestRewardLog;

public interface ContestRewardDao {
	
	/**
	 * 获取某一轮比赛的奖励
	 * @param dayOfContest 哪一轮比赛
	 * @return
	 */
	public List<ContestReward> getContestRewardListByDay(int dayOfContest);

	public UserRecContestRewardLog getUserRecRewardLog(String userId, int currentSession, int rewardId);

	public ContestReward getContestReward(int rewardId);

	public void saveUserRecRewardLog(UserRecContestRewardLog recLog);

}
