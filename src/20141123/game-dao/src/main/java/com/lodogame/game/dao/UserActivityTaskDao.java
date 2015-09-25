package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.UserActivityRewardLog;
import com.lodogame.model.UserActivityTask;

public interface UserActivityTaskDao {

	/**
	 * 获取用户活跃度任务列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserActivityTask> getUserActivityTaskList(String userId);

	/**
	 * 获取用户活跃度任务
	 * 
	 * @param userId
	 * @param activityTaskId
	 * @return
	 */
	public UserActivityTask getUserActivityTask(String userId, int activityTaskId);

	/**
	 * 更新用户活跃度任务状态
	 * 
	 * @param userId
	 * @param activityTaskId
	 * @param times
	 * @param status
	 * @return
	 */
	public boolean updateUserActivityTask(String userId, int activityTaskId, int times, int status);

	/**
	 * 获取用户活跃度奖励日志列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserActivityRewardLog> getUserActivityRewardLogList(String userId);

	/**
	 * 
	 * @param userActivityRewardLog
	 * @return
	 */
	public boolean addUserActivityRewardLog(UserActivityRewardLog userActivityRewardLog);

	/**
	 * 添加用户活跃度任务奖励
	 * 
	 * @param userActivityTaskList
	 * @return
	 */
	public boolean addUserActivityTask(List<UserActivityTask> userActivityTaskList);

}
