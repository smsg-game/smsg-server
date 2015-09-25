package com.lodogame.game.dao.impl.cache;

import java.util.List;

import com.lodogame.game.dao.UserToolDao;
import com.lodogame.game.dao.clearcache.ClearCacheOnLoginOut;
import com.lodogame.game.dao.impl.mysql.UserToolDaoMysqlImpl;
import com.lodogame.game.dao.impl.redis.UserToolDaoRedisImpl;
import com.lodogame.model.UserTool;

public class UserToolDaoCacheImpl implements UserToolDao,ClearCacheOnLoginOut {

	private UserToolDaoMysqlImpl userToolDaoMysqlImpl;
	private UserToolDaoRedisImpl userToolDaoRedisImpl; 
	@Override
	public UserTool get(String userId, int toolId) {
		if(userToolDaoRedisImpl.existUserId(userId)){
			return userToolDaoRedisImpl.get(userId, toolId);
		}
		initCache(userId);
		return userToolDaoRedisImpl.get(userId, toolId);
	}
    private List<UserTool> initCache(String userId){
    	List<UserTool> list = userToolDaoMysqlImpl.getList(userId);
    	userToolDaoRedisImpl.initUserToolList(userId, list);
    	return list;
    }
	@Override
	public int getUserToolNum(String userId, int toolId) {
		if(userToolDaoRedisImpl.existUserId(userId)){
			return userToolDaoRedisImpl.getUserToolNum(userId, toolId);
		}
		initCache(userId);
		return userToolDaoRedisImpl.getUserToolNum(userId, toolId);
	}

	@Override
	public boolean reduceUserTool(String userId, int toolId, int num) {
		if(userToolDaoMysqlImpl.reduceUserTool(userId, toolId, num)){
			userToolDaoRedisImpl.reduceUserTool(userId, toolId, num);
			return true;
		}
		return false;
	}

	@Override
	public boolean addUserTool(String userId, int toolId, int num) {
		if(userToolDaoMysqlImpl.addUserTool(userId, toolId, num)){
			userToolDaoRedisImpl.addUserTool(userId, toolId, num);
			return true;
		}
		return false;
	}

	@Override
	public boolean add(UserTool userTool) {
		if(userToolDaoMysqlImpl.add(userTool)){
			userToolDaoRedisImpl.add(userTool);
			return true;
		}
		return false;
	}

	@Override
	public List<UserTool> getList(String userId) {
		if(userToolDaoRedisImpl.existUserId(userId)){
			return userToolDaoRedisImpl.getList(userId);
		}
		return initCache(userId);
	}

	@Override
	public boolean deleteZeroNumTools(String userId) {
		if(userToolDaoMysqlImpl.deleteZeroNumTools(userId)){
			userToolDaoRedisImpl.deleteZeroNumTools(userId);
			return true;
		}
		return false;
	}
	public void setUserToolDaoMysqlImpl(UserToolDaoMysqlImpl userToolDaoMysqlImpl) {
		this.userToolDaoMysqlImpl = userToolDaoMysqlImpl;
	}
	public void setUserToolDaoRedisImpl(UserToolDaoRedisImpl userToolDaoRedisImpl) {
		this.userToolDaoRedisImpl = userToolDaoRedisImpl;
	}
	@Override
	public void clearOnLoginOut(String userId) throws Exception {
		userToolDaoRedisImpl.delEntry(userId);
	}
	@Override
	public List<UserTool> getUserToolListByToolType(String userId, int toolType) {
		return this.userToolDaoMysqlImpl.getUserToolListByToolType(userId, toolType);
	}
}
