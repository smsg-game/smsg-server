package com.lodogame.game.dao.impl.cache;

import java.util.List;

import com.lodogame.game.dao.UserActivityTaskDao;
import com.lodogame.game.dao.clearcache.ClearCacheOnLoginOut;
import com.lodogame.game.dao.impl.mysql.UserActivityTaskDaoMysqlImpl;
import com.lodogame.game.dao.impl.redis.UserActivityTaskDaoRedisImpl;
import com.lodogame.model.UserActivityRewardLog;
import com.lodogame.model.UserActivityTask;

public class UserActivityTaskDaoCacheImpl implements UserActivityTaskDao, ClearCacheOnLoginOut {

	private UserActivityTaskDaoMysqlImpl userActivityTaskDaoMysqlImpl;

	private UserActivityTaskDaoRedisImpl userActivityTaskDaoRedisImpl;

	@Override
	public List<UserActivityTask> getUserActivityTaskList(String userId) {
		List<UserActivityTask> list = userActivityTaskDaoRedisImpl.getUserActivityTaskList(userId);
		if (list != null && list.size() > 0) {
			return list;
		}
		list = userActivityTaskDaoMysqlImpl.getUserActivityTaskList(userId);
		if (list != null && list.size() > 0) {
			userActivityTaskDaoRedisImpl.initUserActivityTask(userId, list);
		}
		return list;
	}

	@Override
	public UserActivityTask getUserActivityTask(String userId, int activityTaskId) {
		List<UserActivityTask> list = userActivityTaskDaoRedisImpl.getUserActivityTaskList(userId);
		if (list != null && list.size() > 0) {
			for (UserActivityTask activityTask : list) {
				if (activityTask.getActivityTaskId() == activityTaskId) {
					return activityTask;
				}
			}
		} else {
			list = userActivityTaskDaoMysqlImpl.getUserActivityTaskList(userId);
			if (list != null && list.size() > 0) {
				userActivityTaskDaoRedisImpl.initUserActivityTask(userId, list);
				for (UserActivityTask activityTask : list) {
					if (activityTask.getActivityTaskId() == activityTaskId) {
						return activityTask;
					}
				}
			}

		}
		return null;
	}

	@Override
	public boolean updateUserActivityTask(String userId, int activityTaskId, int times, int status) {
		boolean success = userActivityTaskDaoMysqlImpl.updateUserActivityTask(userId, activityTaskId, times, status);
		// 更新缓存
		if (success) {
			userActivityTaskDaoRedisImpl.updateUserActivityTask(userId, activityTaskId, times, status);
		}
		return success;
	}

	@Override
	public List<UserActivityRewardLog> getUserActivityRewardLogList(String userId) {
		List<UserActivityRewardLog> list = userActivityTaskDaoRedisImpl.getUserActivityRewardLogList(userId);
		if (list != null && list.size() > 0) {
			return list;
		}
		list = userActivityTaskDaoMysqlImpl.getUserActivityRewardLogList(userId);
		if (list != null && list.size() > 0) {
			userActivityTaskDaoRedisImpl.initUserActivityRewardLog(userId, list);
		}
		return list;
	}

	@Override
	public boolean addUserActivityRewardLog(UserActivityRewardLog userActivityRewardLog) {
		boolean result = userActivityTaskDaoMysqlImpl.addUserActivityRewardLog(userActivityRewardLog);
		if (result) {
			userActivityTaskDaoRedisImpl.addUserActivityRewardLog(userActivityRewardLog);
		}
		return result;
	}

	@Override
	public boolean addUserActivityTask(List<UserActivityTask> userActivityTaskList) {
		boolean result = userActivityTaskDaoMysqlImpl.addUserActivityTask(userActivityTaskList);
		if (result) {
			String userId = userActivityTaskList.get(0).getUserId();
			userActivityTaskDaoRedisImpl.initUserActivityTask(userId, userActivityTaskList);
		}
		return result;
	}

	public void setUserActivityTaskDaoMysqlImpl(UserActivityTaskDaoMysqlImpl userActivityTaskDaoMysqlImpl) {
		this.userActivityTaskDaoMysqlImpl = userActivityTaskDaoMysqlImpl;
	}

	public void setUserActivityTaskDaoRedisImpl(UserActivityTaskDaoRedisImpl userActivityTaskDaoRedisImpl) {
		this.userActivityTaskDaoRedisImpl = userActivityTaskDaoRedisImpl;
	}

	@Override
	public void clearOnLoginOut(String userId) throws Exception {
		userActivityTaskDaoRedisImpl.delEntry(userId);
	}
}
