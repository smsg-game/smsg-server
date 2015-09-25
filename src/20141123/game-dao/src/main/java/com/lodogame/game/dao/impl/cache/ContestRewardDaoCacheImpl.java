package com.lodogame.game.dao.impl.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lodogame.game.dao.ContestRewardDao;
import com.lodogame.game.dao.impl.mysql.ContestRewardDaoMysqlImpl;
import com.lodogame.model.ContestReward;
import com.lodogame.model.UserRecContestRewardLog;

public class ContestRewardDaoCacheImpl implements ContestRewardDao{
	
	private ContestRewardDaoMysqlImpl contestRewardDaoMysqlImpl;

	public void setContestRewardDaoMysqlImpl(ContestRewardDaoMysqlImpl contestRewardDaoMysqlImpl) {
		this.contestRewardDaoMysqlImpl = contestRewardDaoMysqlImpl;
	}

	private Map<Integer, List<ContestReward>> rewardMap = new HashMap<Integer, List<ContestReward>>();
	
	private List<ContestReward> rewardList = new ArrayList<ContestReward>();
	
	
	@Override
	public List<ContestReward> getContestRewardListByDay(int dayOfContest) {
		List<ContestReward> rewardListByDayOfContest = rewardMap.get(dayOfContest);
		
		if (rewardListByDayOfContest == null) {
			rewardListByDayOfContest = contestRewardDaoMysqlImpl.getContestRewardListByDay(dayOfContest);
			rewardMap.put(dayOfContest, rewardListByDayOfContest);
		}
			
		return rewardListByDayOfContest;
	}
	
	@Override
	public UserRecContestRewardLog getUserRecRewardLog(String userId, int session, int rewardId) {
		return contestRewardDaoMysqlImpl.getUserRecRewardLog(userId, session, rewardId);
	}


	@Override
	public ContestReward getContestReward(int rewardId) {
		for (ContestReward reward : rewardList) {
			if (reward.getRewardId() == rewardId) {
				return reward;
			}
		}
		
		ContestReward contestReward = contestRewardDaoMysqlImpl.getContestReward(rewardId);
		rewardList.add(contestReward);
		return contestReward;
	}
	
	@Override
	public void saveUserRecRewardLog(UserRecContestRewardLog recLog) {
		contestRewardDaoMysqlImpl.saveUserRecRewardLog(recLog);
	}
}
