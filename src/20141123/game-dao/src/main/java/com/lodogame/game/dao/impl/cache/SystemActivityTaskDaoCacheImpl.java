package com.lodogame.game.dao.impl.cache;

import java.util.ArrayList;
import java.util.List;

import com.lodogame.game.dao.SystemActivityTaskDao;
import com.lodogame.game.dao.impl.mysql.SystemActivityTaskDaoMysqlImpl;
import com.lodogame.game.dao.reload.ReloadAbleBase;
import com.lodogame.model.ActivityTask;
import com.lodogame.model.ActivityTaskReward;

public class SystemActivityTaskDaoCacheImpl extends ReloadAbleBase implements SystemActivityTaskDao {

	private SystemActivityTaskDaoMysqlImpl systemActivityTaskDaoMysqlImpl;

	private List<ActivityTaskReward> rewardListCache = new ArrayList<ActivityTaskReward>();

	private List<ActivityTask> taskListCache = new ArrayList<ActivityTask>();

	@Override
	public void reload() {
		rewardListCache.clear();
		taskListCache.clear();
	}

	@Override
	public List<ActivityTaskReward> getActivityRewardList() {
		if (!rewardListCache.isEmpty()) {
			return rewardListCache;
		} else {
			rewardListCache = systemActivityTaskDaoMysqlImpl.getActivityRewardList();
		}
		return rewardListCache;
	}

	@Override
	public ActivityTaskReward getActivityReward(int activityTaskRewardId) {

		if (rewardListCache.isEmpty()) {
			rewardListCache = getActivityRewardList();
		}
		for (ActivityTaskReward activetyTaskReward : rewardListCache) {
			if (activetyTaskReward.getActivityTaskRewardId() == activityTaskRewardId) {
				return activetyTaskReward;
			}
		}
		return null;
	}

	@Override
	public List<ActivityTask> getActivityTaskList() {

		if (!taskListCache.isEmpty()) {
			return taskListCache;
		} else {
			taskListCache = systemActivityTaskDaoMysqlImpl.getActivityTaskList();
		}
		return taskListCache;
	}

	@Override
	public ActivityTask getActivityTask(int activityTaskId) {

		if (taskListCache.isEmpty()) {
			getActivityTaskList();
		}
		for (ActivityTask activityTask : taskListCache) {
			if (activityTask.getActivityTaskId() == activityTaskId) {
				return activityTask;
			}
		}
		return null;
	}

	@Override
	public ActivityTask getActivityTaskByTarget(int targetType) {

		if (taskListCache.isEmpty()) {
			getActivityTaskList();
		}
		for (ActivityTask activityTask : taskListCache) {
			if (activityTask.getTargetType() == targetType) {
				return activityTask;
			}
		}
		return null;
	}

	public void setSystemActivityTaskDaoMysqlImpl(SystemActivityTaskDaoMysqlImpl systemActivityTaskDaoMysqlImpl) {
		this.systemActivityTaskDaoMysqlImpl = systemActivityTaskDaoMysqlImpl;
	}

}
