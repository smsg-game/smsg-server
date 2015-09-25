package com.lodogame.ldsg.service;

import java.util.List;

import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.UserTaskBO;
import com.lodogame.ldsg.checker.TaskChecker;
import com.lodogame.ldsg.event.EventHandle;

/**
 * 任务service
 * 
 * @author jacky
 * 
 */
public interface TaskService {

	/**
	 * 繁体版点赞后的任务 Id
	 */
	public final static int HK_ZAN_TASK_ID = 9999;
	
	/**
	 * 领任务奖励--未完成
	 */
	public final static int TASK_RECEIVE_NOT_FINISH = 2001;

	/**
	 * 领任务奖励--已经领取
	 */
	public final static int TASK_RECEIVE_HAS_RECEIVE = 2002;

	/**
	 * 添加初始化任务
	 * 
	 * @param userId
	 * @return
	 */
	public boolean addInitTask(String userId);

	/**
	 * 获取用户任务
	 * 
	 * @param userId
	 * @param status
	 *            (任务装备 100 表示所有任务)
	 * @return
	 */
	public List<UserTaskBO> getUserTaskList(String userId, int status);

	/**
	 * 领取任务
	 * 
	 * @param userId
	 * @param taskId
	 * @return
	 */
	public CommonDropBO receive(String userId, int taskId, EventHandle handle);

	/**
	 * 获取用户单个任务
	 * 
	 * @param userId
	 * @param taskId
	 * @return
	 */
	public UserTaskBO get(String userId, int taskId);

	/**
	 * 更新任务完成次数
	 * 
	 * @param userId
	 * @param times
	 * @param handle
	 * @param taskChecker
	 */
	public void updateTaskFinish(String userId, int times, EventHandle handle, TaskChecker taskChecker);

	/**
	 * 刷新每日任务
	 * 
	 * @param userId
	 */
	public void refreshDailyTask(String userId);
	
	public void refreshUserMonthlyCardTask(String userId);

}
