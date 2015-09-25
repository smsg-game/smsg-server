package com.lodogame.ldsg.service;

import java.util.List;

import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.UserActivityTaskBO;
import com.lodogame.ldsg.bo.UserActivityTaskRewardBO;
import com.lodogame.model.UserActivityTask;

/**
 * 活路度奖励
 * 
 * @author jacky
 * 
 */
public interface ActivityTaskService {

	/**
	 * 积分不够
	 */
	public final static int RECIVE_REWARD_ERROR_POINT_NOT_ENOUGH = 2001;

	/**
	 * 已经领取过
	 */
	public final static int RECIVE_REWARD_ERROR_HAS_RECIVE = 2001;

	/**
	 * 领取活跃度任务
	 * 
	 * @param userId
	 * @param activityRewardId
	 * @return
	 */
	public CommonDropBO receive(String userId, int activityTaskRewardId);

	/**
	 * 更新活跃度任务完成次数
	 * 
	 * @param userId
	 * @param targetType
	 * @param times
	 * @return
	 */
	public boolean updateActvityTask(String userId, int targetType, int times);

	/**
	 * 获取用户活跃度任务积分
	 * 
	 * @param userId
	 * @return
	 */
	public int getUserActivityPoint(String userId);

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
	 * 获取用户活跃活跃度任务列表BO
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserActivityTaskBO> getUserActivityTaskListBO(String userId);

	/**
	 * 获取用户
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserActivityTaskRewardBO> getUserActivityTaskRewardBOList(String userId);

}
