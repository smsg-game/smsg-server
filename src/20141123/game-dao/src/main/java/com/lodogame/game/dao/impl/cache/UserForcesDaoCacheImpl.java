package com.lodogame.game.dao.impl.cache;

import java.util.List;

import com.lodogame.game.dao.UserForcesDao;
import com.lodogame.game.dao.clearcache.ClearCacheOnLoginOut;
import com.lodogame.game.dao.impl.mysql.UserForcesDaoMysqlImpl;
import com.lodogame.game.dao.impl.redis.UserForcesDaoRedisImpl;
import com.lodogame.model.UserForces;
import com.lodogame.model.UserForcesCount;

public class UserForcesDaoCacheImpl implements UserForcesDao,ClearCacheOnLoginOut {

	private UserForcesDaoMysqlImpl userForcesDaoMysqlImpl;

	private UserForcesDaoRedisImpl userForcesDaoRedisImpl;

	@Override
	public List<UserForces> getUserForcesList(String userId, int sceneId) {
		List<UserForces> result = userForcesDaoRedisImpl.getUserForcesList(userId, sceneId);
		if(result!=null){
			return result;
		}
		result = userForcesDaoMysqlImpl.getUserForcesList(userId, sceneId);
		if(result!=null){
			userForcesDaoRedisImpl.initUserSceneCache(userId, sceneId, result);
		}
		return result;
	}
	@Override
	public boolean add(UserForces userForces) {
		boolean result =  this.userForcesDaoMysqlImpl.add(userForces);
		if(result){
			userForcesDaoRedisImpl.add(userForces);
		}
		return result;
	}

	@Override
	public UserForces getUserCurrentForces(String userId, int forcesType) {
		UserForces userForces = userForcesDaoRedisImpl.getUserCurrentForces(userId, forcesType);
		if(userForces!=null){
			return userForces;
		}
		userForces = this.userForcesDaoMysqlImpl.getUserCurrentForces(userId, forcesType);
		userForcesDaoRedisImpl.initUserForceTypeCache(userId, forcesType, userForces);
		return userForces;
	}

	@Override
	public UserForces get(String userId, int forcesId) {
		UserForces userForces = userForcesDaoRedisImpl.get(userId, forcesId);
		if(userForces!=null){
			return userForces;
		}
		userForces = this.userForcesDaoMysqlImpl.get(userId, forcesId);
		
		userForcesDaoRedisImpl.initUserForceCache(userId, forcesId, userForces);
		return userForces;
	}

	@Override
	public boolean updateStatus(String userId, int forcesId, int status, int times) {
		boolean result = this.userForcesDaoMysqlImpl.updateStatus(userId, forcesId, status, times);
		if(result){
			userForcesDaoRedisImpl.updateStatus(userId, forcesId, status, times);
		}
		return result;
	}


	@Override
	public boolean updateTimes(String uid, int times, List<Integer> forcesIds) {
		boolean result = this.userForcesDaoMysqlImpl.updateTimes(uid, times, forcesIds);
		if(result){
			userForcesDaoRedisImpl.updateTimes(uid, times, forcesIds);
		}
		return result;
	}
	
	@Override
	public boolean updateTimes(String userId, int forcesId, int times) {
		boolean result = this.userForcesDaoMysqlImpl.updateTimes(userId, forcesId, times);
		if(result){
			userForcesDaoRedisImpl.updateTimes(userId, forcesId, times);
		}
		return result;
	}

	@Override
	public List<UserForcesCount> listOrderByForceCntDesc(int offset, int size) {
		return this.userForcesDaoMysqlImpl.listOrderByForceCntDesc(offset, size);
	}

	@Override
	public boolean resetForcesTimes(String userId, List<Integer> sceneIdList) {
		boolean result = this.userForcesDaoMysqlImpl.resetForcesTimes(userId, sceneIdList);
		if(result){
			userForcesDaoRedisImpl.resetForcesTimes(userId, sceneIdList);
		}
		return result;
	}

	@Override
	public long getAmendEmbattleTime(String userId) {
		return this.userForcesDaoRedisImpl.getAmendEmbattleTime(userId);
	}

	@Override
	public void setAmendEmbattleTime(String userId, long timestamp) {
		this.userForcesDaoRedisImpl.setAmendEmbattleTime(userId, timestamp);
	}
	public void setUserForcesDaoMysqlImpl(UserForcesDaoMysqlImpl userForcesDaoMysqlImpl) {
		this.userForcesDaoMysqlImpl = userForcesDaoMysqlImpl;
	}
	public void setUserForcesDaoRedisImpl(UserForcesDaoRedisImpl userForcesDaoRedisImpl) {
		this.userForcesDaoRedisImpl = userForcesDaoRedisImpl;
	}
	@Override
	public void clearOnLoginOut(String userId) throws Exception {
		userForcesDaoRedisImpl.remove(userId);
	}


}
