package com.lodogame.game.dao.impl.cache;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.UserOnlineRewardDao;
import com.lodogame.game.dao.clearcache.ClearCacheOnLoginOut;
import com.lodogame.game.dao.impl.mysql.UserOnlineRewardDaoMysqlImpl;
import com.lodogame.model.UserOnlineReward;

public class UserOnlineRewardDaoCacheImpl implements UserOnlineRewardDao,ClearCacheOnLoginOut {
	private UserOnlineRewardDaoMysqlImpl userOnlineRewardDaoMysqlImpl;
    private Map<String,UserOnlineReward> cacheMap = new ConcurrentHashMap<String,UserOnlineReward>();
	@Override
	public UserOnlineReward get(String userId) {
		if(cacheMap.containsKey(userId)){
			return cacheMap.get(userId);
		}
		UserOnlineReward onlineReward = userOnlineRewardDaoMysqlImpl.get(userId);
		if(onlineReward!=null){
			cacheMap.put(userId, onlineReward);
		}
		return onlineReward;
	}

	@Override
	public boolean add(UserOnlineReward userOnlineReward) {
		if(userOnlineRewardDaoMysqlImpl.add(userOnlineReward)){
			cacheMap.put(userOnlineReward.getUserId(), userOnlineReward);
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean update(String userId, int subType) {
		boolean result = userOnlineRewardDaoMysqlImpl.update(userId, subType);
		if(result){
			if(cacheMap.containsKey(userId)){
				UserOnlineReward onlineReward = cacheMap.get(userId);
				onlineReward.setSubType(subType);
				onlineReward.setUpdatedTime(new Date());
			}
		}
		return result;
	}

	public void setUserOnlineRewardDaoMysqlImpl(UserOnlineRewardDaoMysqlImpl userOnlineRewardDaoMysqlImpl) {
		this.userOnlineRewardDaoMysqlImpl = userOnlineRewardDaoMysqlImpl;
	}

	@Override
	public void clearOnLoginOut(String userId) throws Exception {
		cacheMap.remove(userId);
	}

}
