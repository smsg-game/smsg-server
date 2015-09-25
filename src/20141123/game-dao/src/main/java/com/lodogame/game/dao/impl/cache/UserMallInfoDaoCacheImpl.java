package com.lodogame.game.dao.impl.cache;

import org.apache.commons.lang.NotImplementedException;

import com.lodogame.game.dao.UserMallInfoDao;
import com.lodogame.model.UserMallInfo;

public class UserMallInfoDaoCacheImpl implements UserMallInfoDao {

	private UserMallInfoDao userMallInfoDaoMysqlImpl;

	private UserMallInfoDao userMallInfoDaoRedisImpl;

	public void setUserMallInfoDaoMysqlImpl(UserMallInfoDao userMallInfoDaoMysqlImpl) {
		this.userMallInfoDaoMysqlImpl = userMallInfoDaoMysqlImpl;
	}

	public void setUserMallInfoDaoRedisImpl(UserMallInfoDao userMallInfoDaoRedisImpl) {
		this.userMallInfoDaoRedisImpl = userMallInfoDaoRedisImpl;
	}

	@Override
	public boolean add(String userId, int mallId, int totalBuyNum, int dayBuyNum) {

		boolean success = userMallInfoDaoMysqlImpl.add(userId, mallId, totalBuyNum, dayBuyNum);
		if (success) {
			this.userMallInfoDaoRedisImpl.add(userId, mallId, totalBuyNum, dayBuyNum);
		}
		return success;

	}

	@Override
	public UserMallInfo get(String userId, int mallId) {

		UserMallInfo userMallInfo = this.userMallInfoDaoRedisImpl.get(userId, mallId);
		if (userMallInfo == null) {
			userMallInfo = this.userMallInfoDaoMysqlImpl.get(userId, mallId);
			if (userMallInfo != null) {
				this.userMallInfoDaoRedisImpl.add(userMallInfo);
			}
		}

		return userMallInfo;
	}

	@Override
	public boolean add(UserMallInfo userMallInfo) {
		throw new NotImplementedException();
	}

}
