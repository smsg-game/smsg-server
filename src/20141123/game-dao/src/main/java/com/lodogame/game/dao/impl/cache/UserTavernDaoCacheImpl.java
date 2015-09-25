package com.lodogame.game.dao.impl.cache;

import java.util.Date;
import java.util.List;

import com.lodogame.game.dao.UserTavernDao;
import com.lodogame.game.dao.clearcache.ClearCacheOnLoginOut;
import com.lodogame.game.dao.impl.mysql.UserTavernDaoMysqlImpl;
import com.lodogame.game.dao.impl.redis.UserTavernDaoRedisImpl;
import com.lodogame.model.UserTavern;

public class UserTavernDaoCacheImpl implements UserTavernDao,ClearCacheOnLoginOut {
    private UserTavernDaoRedisImpl  userTavernDaoRedisImpl;
    private UserTavernDaoMysqlImpl  userTavernDaoMysqlImpl;
	@Override
	public boolean add(UserTavern userTavern) {
		if(userTavernDaoMysqlImpl.add(userTavern)){
			userTavernDaoRedisImpl.add(userTavern);
			return true;
		}
		return false;
	}
	@Override
	public UserTavern get(String userId, int type) {
		if(userTavernDaoRedisImpl.existUserId(userId)){
			return userTavernDaoRedisImpl.get(userId, type);
		}
		initCache(userId);
		return userTavernDaoRedisImpl.get(userId, type);
	}
	
    private List<UserTavern> initCache(String userId){
    	List<UserTavern> list = userTavernDaoMysqlImpl.getList(userId);
    	userTavernDaoRedisImpl.initUserTavernList(userId, list);
    	return list;
    }
	@Override
	public List<UserTavern> getList(String userId) {
		if(userTavernDaoRedisImpl.existUserId(userId)){
			return userTavernDaoRedisImpl.getList(userId);
		}
		return initCache(userId);
	}

	@Override
	public boolean updateTavernInfo(String userId, int type, Date updatedTime,
			int totalTimes, int amendTimes,int hadUsedMoney ) {
		if(userTavernDaoMysqlImpl.updateTavernInfo(userId, type, updatedTime, totalTimes, amendTimes, hadUsedMoney)){
			userTavernDaoRedisImpl.updateTavernInfo(userId, type, updatedTime, totalTimes, amendTimes, hadUsedMoney);
			return true;
		}
		return false;
	}

	
	public void setUserTavernDaoRedisImpl(UserTavernDaoRedisImpl userTavernDaoRedisImpl) {
		this.userTavernDaoRedisImpl = userTavernDaoRedisImpl;
	}

	public void setUserTavernDaoMysqlImpl(UserTavernDaoMysqlImpl userTavernDaoMysqlImpl) {
		this.userTavernDaoMysqlImpl = userTavernDaoMysqlImpl;
	}
	@Override
	public void clearOnLoginOut(String userId) throws Exception {
		userTavernDaoRedisImpl.delEntry(userId);
	}

}
