package com.lodogame.game.dao.impl.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.UserTavernLogDao;
import com.lodogame.game.dao.clearcache.ClearCacheOnLoginOut;
import com.lodogame.game.dao.reload.ReloadAble;

public class UserTavernLogDaoCacheImpl implements UserTavernLogDao, ReloadAble,ClearCacheOnLoginOut{

	private UserTavernLogDao userTavernLogDaoMysqlImpl;

	private Map<String,Boolean> userTavernLogCache = new ConcurrentHashMap<String, Boolean>();
	
	public void setUserTavernLogDaoMysqlImpl(UserTavernLogDao userTavernLogDaoMysqlImpl) {
		this.userTavernLogDaoMysqlImpl = userTavernLogDaoMysqlImpl;
	}

	@Override
	public boolean isExistLog(String userId) {
		if(userTavernLogCache.containsKey(userId)){
			return userTavernLogCache.get(userId);
		}
		Boolean result = this.userTavernLogDaoMysqlImpl.isExistLog(userId);
		userTavernLogCache.put(userId, result);
		return result;
	}

	@Override
	public boolean addTavernLog(String userId) {
		boolean result = this.userTavernLogDaoMysqlImpl.addTavernLog(userId);
		userTavernLogCache.put(userId, result);
		return result;
	}

	@Override
	public void reload() {

	}

	@Override
	public void init() {

	}

	@Override
	public void clearOnLoginOut(String userId) throws Exception {
		userTavernLogCache.remove(userId);
	}
}
