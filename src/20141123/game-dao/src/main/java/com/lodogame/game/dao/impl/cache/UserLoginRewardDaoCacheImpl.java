package com.lodogame.game.dao.impl.cache;

import java.util.List;

import com.lodogame.game.dao.UserLoginRewardInfoDao;
import com.lodogame.model.UserLoginRewardInfo;

public class UserLoginRewardDaoCacheImpl implements UserLoginRewardInfoDao {

	private UserLoginRewardInfoDao userLoginRewardDaoMysqlImpl;
	
	public void setUserLoginRewardDaoMysqlImpl(UserLoginRewardInfoDao userLoginRewardDaoMysqlImpl) {
		this.userLoginRewardDaoMysqlImpl = userLoginRewardDaoMysqlImpl;
	}

	@Override
	public boolean addUserLoginRewardInfo(UserLoginRewardInfo userLoginRewardInfo) {
		return this.userLoginRewardDaoMysqlImpl.addUserLoginRewardInfo(userLoginRewardInfo);
	}

	@Override
	public UserLoginRewardInfo getUserLoginRewardInfoByDay(String userId, int day) {
		return this.userLoginRewardDaoMysqlImpl.getUserLoginRewardInfoByDay(userId, day);
	}

	@Override
	public int updateUserLoginRewardInfoByDay(String userId, int day, String date, int rewardStatus) {
		return this.userLoginRewardDaoMysqlImpl.updateUserLoginRewardInfoByDay(userId, day, date, rewardStatus);
	}

	@Override
	public UserLoginRewardInfo getUserLastLoginRewardInfo(String userId) {
		return this.userLoginRewardDaoMysqlImpl.getUserLastLoginRewardInfo(userId);
	}

	@Override
	public List<UserLoginRewardInfo> getUserLoginRewardInfo(String userId) {
		return this.userLoginRewardDaoMysqlImpl.getUserLoginRewardInfo(userId);
	}

}
