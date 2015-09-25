package com.lodogame.game.dao.impl.cache;

import java.util.ArrayList;
import java.util.List;

import com.lodogame.game.dao.UserTaskDao;
import com.lodogame.game.dao.clearcache.ClearCacheOnLoginOut;
import com.lodogame.game.dao.impl.mysql.UserTaskDaoMysqlImpl;
import com.lodogame.game.dao.impl.redis.UserTaskDaoRedisImpl;
import com.lodogame.model.UserTask;

public class UserTaskDaoCacheImpl implements UserTaskDao,ClearCacheOnLoginOut {

	private UserTaskDaoRedisImpl userTaskDaoRedisImpl;
	private UserTaskDaoMysqlImpl userTaskDaoMysqlImpl;
	@Override
	public List<UserTask> getList(String userId, int status) {
		if(userTaskDaoRedisImpl.existUserId(userId)){
			return userTaskDaoRedisImpl.getList(userId, status);
		}
		List<UserTask> list = userTaskDaoMysqlImpl.getList(userId, 100);
		if(status==100){
			userTaskDaoRedisImpl.initUserTaskCache(userId, list);
			return list;
		}
		List<UserTask> result = new ArrayList<UserTask>();
		if(list!=null&&list.size()>0){
			userTaskDaoRedisImpl.initUserTaskCache(userId, list);
			for(UserTask task:list){
				if(task.getStatus()!=4){
					if(status == task.getStatus()){
							result.add(task);
					}
				}
			
			}
		}
		return result;
	}

	@Override
	public boolean update(String userId, int systemTaskId, int finishTimes,
			int status) {
		if(userTaskDaoMysqlImpl.update(userId, systemTaskId, finishTimes, status)){
			userTaskDaoRedisImpl.update(userId, systemTaskId, finishTimes, status);
			return true;
		}	
		return false;
	}

	@Override
	public boolean update(String userId, int systemTaskId, int status) {
		if(userTaskDaoMysqlImpl.update(userId, systemTaskId, status)){
			userTaskDaoRedisImpl.update(userId, systemTaskId, status);
			return true;
		}
		return false;
	}

	@Override
	public void add(List<UserTask> userTaskList) {
        userTaskDaoMysqlImpl.add(userTaskList);
        userTaskDaoRedisImpl.add(userTaskList);	
	}

	@Override
	public UserTask get(String userId, int systemTaskId) {
		if(userTaskDaoRedisImpl.existUserId(userId)){
			return userTaskDaoRedisImpl.get(userId, systemTaskId);
		}else{
			//先load上来
			getList(userId, 100);
			return userTaskDaoRedisImpl.get(userId, systemTaskId);
		}
	}

	@Override
	public boolean delete(String userId, int systemTaskId) {
		if(userTaskDaoMysqlImpl.delete(userId, systemTaskId)){
			userTaskDaoRedisImpl.delete(userId, systemTaskId);
			return true;
		}
		return false;
	}

	public void setUserTaskDaoRedisImpl(UserTaskDaoRedisImpl userTaskDaoRedisImpl) {
		this.userTaskDaoRedisImpl = userTaskDaoRedisImpl;
	}

	public void setUserTaskDaoMysqlImpl(UserTaskDaoMysqlImpl userTaskDaoMysqlImpl) {
		this.userTaskDaoMysqlImpl = userTaskDaoMysqlImpl;
	}

	@Override
	public void clearOnLoginOut(String userId) throws Exception {
		userTaskDaoRedisImpl.delEntry(userId);
	}
}
