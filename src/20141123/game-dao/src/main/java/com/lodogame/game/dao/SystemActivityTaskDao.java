package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.ActivityTask;
import com.lodogame.model.ActivityTaskReward;

public interface SystemActivityTaskDao {

	public List<ActivityTaskReward> getActivityRewardList();

	public ActivityTaskReward getActivityReward(int activityTaskRewardId);

	public List<ActivityTask> getActivityTaskList();

	public ActivityTask getActivityTask(int activityTaskId);

	public ActivityTask getActivityTaskByTarget(int targetType);

}
