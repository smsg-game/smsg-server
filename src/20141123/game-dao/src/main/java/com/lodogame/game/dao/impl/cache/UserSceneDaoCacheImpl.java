package com.lodogame.game.dao.impl.cache;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;

import com.lodogame.game.dao.UserSceneDao;
import com.lodogame.game.dao.clearcache.ClearCacheOnLoginOut;
import com.lodogame.game.dao.impl.mysql.UserSceneDaoMysqlImpl;
import com.lodogame.game.dao.impl.redis.UserSceneDaoRedisImpl;
import com.lodogame.model.UserScene;

public class UserSceneDaoCacheImpl implements UserSceneDao,ClearCacheOnLoginOut {

	private UserSceneDaoMysqlImpl userSceneDaoMysqlImpl;
	private UserSceneDaoRedisImpl userSceneDaoRedisImpl;
	@Override
	public List<UserScene> getUserSceneList(String userId) {
		List<UserScene> list = userSceneDaoRedisImpl.getUserSceneList(userId);
		if(list!=null&&list.size()>0){
			return list;
		}
		list = userSceneDaoMysqlImpl.getUserSceneList(userId);
		if(list!=null&&list.size()>0){
			userSceneDaoRedisImpl.initUserSceneList(userId, list);
		}
		return list;
	}

	@Override
	public int add(UserScene userScene) {
		int i = userSceneDaoMysqlImpl.add(userScene);
		userSceneDaoRedisImpl.add(userScene);
		return i;
	}

	@Override
	public UserScene getLastUserScene(String userId) {
		throw new NotImplementedException();
	}

	@Override
	public boolean updateScenePassed(String userId, int sceneId) {
		if(userSceneDaoMysqlImpl.updateScenePassed(userId, sceneId)){
			userSceneDaoRedisImpl.updateScenePassed(userId, sceneId);
			return true;
		}
		return false;
	}

	public void setUserSceneDaoMysqlImpl(UserSceneDaoMysqlImpl userSceneDaoMysqlImpl) {
		this.userSceneDaoMysqlImpl = userSceneDaoMysqlImpl;
	}

	public void setUserSceneDaoRedisImpl(UserSceneDaoRedisImpl userSceneDaoRedisImpl) {
		this.userSceneDaoRedisImpl = userSceneDaoRedisImpl;
	}

	@Override
	public void clearOnLoginOut(String userId) throws Exception {
		userSceneDaoRedisImpl.delEntry(userId);
	}

}
