package com.lodogame.game.dao.impl.cache;

import java.util.List;
import java.util.Map;

import com.lodogame.game.dao.UserMallLogDao;
import com.lodogame.game.dao.clearcache.ClearCacheOnLoginOut;
import com.lodogame.game.dao.impl.mysql.UserMallLogDaoMysqlImpl;
import com.lodogame.game.dao.impl.redis.UserMallLogDaoRedisImpl;
import com.lodogame.model.UserMallLog;

public class UserMallLogDaoCacheImpl implements UserMallLogDao, ClearCacheOnLoginOut {

	private UserMallLogDaoMysqlImpl userMallLogDaoMysqlImpl;

	private UserMallLogDaoRedisImpl userMallLogDaoRedisImpl;

	@Override
	public boolean add(UserMallLog userMallLog) {

		boolean result = userMallLogDaoMysqlImpl.add(userMallLog);
		if (result) {
			userMallLogDaoRedisImpl.add(userMallLog);
		}

		return result;
	}

	@Override
	public Map<Integer, Integer> getUserTodayPurchaseNum(String userId) {

		if (userMallLogDaoRedisImpl.existUserId(userId)) {
			return userMallLogDaoRedisImpl.getUserTodayPurchaseNum(userId);
		}

		List<UserMallLog> list = userMallLogDaoMysqlImpl.getUserTodayMallLogList(userId);
		userMallLogDaoRedisImpl.initUserMallLog(userId, list);

		return userMallLogDaoRedisImpl.getUserTodayPurchaseNum(userId);
	}

	public void setUserMallLogDaoMysqlImpl(UserMallLogDaoMysqlImpl userMallLogDaoMysqlImpl) {
		this.userMallLogDaoMysqlImpl = userMallLogDaoMysqlImpl;
	}

	public void setUserMallLogDaoRedisImpl(UserMallLogDaoRedisImpl userMallLogDaoRedisImpl) {
		this.userMallLogDaoRedisImpl = userMallLogDaoRedisImpl;
	}

	@Override
	public void clearOnLoginOut(String userId) throws Exception {
		userMallLogDaoRedisImpl.delEntry(userId);
	}

}
