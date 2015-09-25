package com.lodogame.game.dao.impl.cache;


import java.util.List;

import com.lodogame.game.dao.UserTotalDayPayRewardDao;
import com.lodogame.model.UserTotalDayPayRewardLog;

public class UserTotalDayPayRewardDaoCacheImpl implements UserTotalDayPayRewardDao {

	private UserTotalDayPayRewardDao UserTotalDayPayRewardDaoMysqlImpl;
	
	
	public void setUserTotalDayPayRewardDaoMysqlImpl(UserTotalDayPayRewardDao userTotalDayPayRewardDaoMysqlImpl) {
		UserTotalDayPayRewardDaoMysqlImpl = userTotalDayPayRewardDaoMysqlImpl;
	}

	@Override
	public boolean add(UserTotalDayPayRewardLog userTotalPayReward) {
		return this.UserTotalDayPayRewardDaoMysqlImpl.add(userTotalPayReward);
	}

	@Override
	public UserTotalDayPayRewardLog getUserTotalDayPayReward(String userId, int day) {
		return this.UserTotalDayPayRewardDaoMysqlImpl.getUserTotalDayPayReward(userId, day);
	}

	@Override
	public UserTotalDayPayRewardLog getLastUserTotalDayPayRewardLog(String userId) {
		return this.UserTotalDayPayRewardDaoMysqlImpl.getLastUserTotalDayPayRewardLog(userId);
	}

	@Override
	public boolean addReceiveTotalDayPayRewardLog(String userId, int day) {
		return this.UserTotalDayPayRewardDaoMysqlImpl.addReceiveTotalDayPayRewardLog(userId, day);
	}

	@Override
	public List<UserTotalDayPayRewardLog> getUserAllTotalDayPayRewardLog(String userId) {
		return this.UserTotalDayPayRewardDaoMysqlImpl.getUserAllTotalDayPayRewardLog(userId);
	}



}
