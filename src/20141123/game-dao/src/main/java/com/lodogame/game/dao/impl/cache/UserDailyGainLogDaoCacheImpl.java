package com.lodogame.game.dao.impl.cache;

import java.util.List;

import com.lodogame.game.dao.UserDailyGainLogDao;
import com.lodogame.game.dao.clearcache.ClearCacheOnLoginOut;
import com.lodogame.game.dao.impl.mysql.UserDailyGainLogDaoMysqlImpl;
import com.lodogame.game.dao.impl.redis.UserDailyGainLogDaoRedisImpl;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.model.UserDailyGainLog;

public class UserDailyGainLogDaoCacheImpl implements UserDailyGainLogDao, ReloadAble,ClearCacheOnLoginOut {

	private UserDailyGainLogDaoMysqlImpl userDailyGainLogDaoMysqlImpl;
	private UserDailyGainLogDaoRedisImpl userDailyGainLogDaoRedisImpl;
	
	public void setUserDailyGainLogDaoMysqlImpl(UserDailyGainLogDaoMysqlImpl userDailyGainLogDaoMysqlImpl) {
		this.userDailyGainLogDaoMysqlImpl = userDailyGainLogDaoMysqlImpl;
	}

	public void setUserDailyGainLogDaoRedisImpl(UserDailyGainLogDaoRedisImpl userDailyGainLogDaoRedisImpl) {
		this.userDailyGainLogDaoRedisImpl = userDailyGainLogDaoRedisImpl;
	}

	@Override
	public int getUserDailyGain(String userId, int type) {
		if(userDailyGainLogDaoRedisImpl.existUserId(userId)){
			return userDailyGainLogDaoRedisImpl.getUserDailyGain(userId, type);
		}
		initCache(userId);
		return userDailyGainLogDaoRedisImpl.getUserDailyGain(userId, type);
	}

	private void initCache(String userId){
		List<UserDailyGainLog> list = userDailyGainLogDaoMysqlImpl.getUserTodayAllGainList(userId);
		userDailyGainLogDaoRedisImpl.initUserDailyGainList(userId, list);
	}
	@Override
	public boolean addUserDailyGain(String userId, int type, int amount) {
		if(this.userDailyGainLogDaoMysqlImpl.addUserDailyGain(userId, type, amount)){
			userDailyGainLogDaoRedisImpl.addUserDailyGain(userId, type, amount);
			return true;
		}
		return false;
	}

	@Override
	public void reload() {

	}

	@Override
	public void init() {

	}

	@Override
	public void clearOnLoginOut(String userId) throws Exception {
		userDailyGainLogDaoRedisImpl.delEntry(userId);
	}

}
