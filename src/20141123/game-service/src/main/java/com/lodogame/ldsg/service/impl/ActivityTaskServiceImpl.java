package com.lodogame.ldsg.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.SystemActivityTaskDao;
import com.lodogame.game.dao.UserActivityTaskDao;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.DropToolBO;
import com.lodogame.ldsg.bo.UserActivityTaskBO;
import com.lodogame.ldsg.bo.UserActivityTaskRewardBO;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.constants.ToolUseType;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.service.ActivityTaskService;
import com.lodogame.ldsg.service.ToolService;
import com.lodogame.model.ActivityTask;
import com.lodogame.model.ActivityTaskReward;
import com.lodogame.model.UserActivityRewardLog;
import com.lodogame.model.UserActivityTask;

public class ActivityTaskServiceImpl implements ActivityTaskService {

	private static final Logger logger = Logger.getLogger(ActivityServiceImpl.class);

	@Autowired
	private SystemActivityTaskDao systemActivityTaskDao;

	@Autowired
	private UserActivityTaskDao userActivityTaskDao;

	@Autowired
	private ToolService toolService;

	private Map<String, Long> reqTimes = new ConcurrentHashMap<String, Long>();

	@Override
	public int getUserActivityPoint(String userId) {

		int point = 0;

		List<UserActivityTask> userActivityTaskList = this.getUserActivityTaskList(userId);
		for (UserActivityTask userActivityTask : userActivityTaskList) {
			if (userActivityTask.getStatus() != 1) {
				continue;
			}

			ActivityTask activityTask = this.systemActivityTaskDao.getActivityTask(userActivityTask.getActivityTaskId());
			point += activityTask.getPoint();
		}

		return point;
	}

	@Override
	public CommonDropBO receive(String userId, int activityTaskRewardId) {

		ActivityTaskReward activityTaskReward = this.systemActivityTaskDao.getActivityReward(activityTaskRewardId);
		if (activityTaskReward == null) {
			String message = "";
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		long now = System.currentTimeMillis();
		String key = userId + "_" + activityTaskRewardId;
		if (reqTimes.containsKey(key)) {
			if (now - reqTimes.get(key) < 1000) {// 一秒内不能有两次请求
				String message = "领取活跃度奖励出错,重复请求.userId[" + userId + "], activityTaskRewardId[" + activityTaskRewardId + "]";
				logger.warn(message);
				throw new ServiceException(RECIVE_REWARD_ERROR_HAS_RECIVE, message);
			}
		}

		reqTimes.put(key, now);

		int needPoint = activityTaskReward.getPoint();
		int userPoint = this.getUserActivityPoint(userId);
		if (userPoint < needPoint) {
			String message = "领取活跃度奖励出错,用户活跃度积分不够.userId[" + userId + "], needPoint[" + needPoint + "], userPoint[" + userPoint + "]";
			logger.warn(message);
			throw new ServiceException(RECIVE_REWARD_ERROR_POINT_NOT_ENOUGH, message);
		}

		List<UserActivityRewardLog> userActivityRewardLogList = this.userActivityTaskDao.getUserActivityRewardLogList(userId);
		for (UserActivityRewardLog userActivityRewardLog : userActivityRewardLogList) {
			if (userActivityRewardLog.getActivityTaskRewardId() == activityTaskRewardId) {// 已经领取过
				String message = "领取活跃度奖励出错,用户活已经领取过.userId[" + userId + "], activityTaskRewardId[" + activityTaskRewardId + "]";
				logger.warn(message);
				throw new ServiceException(RECIVE_REWARD_ERROR_HAS_RECIVE, message);
			}
		}

		// 做领取日志
		UserActivityRewardLog userActivityRewardLog = new UserActivityRewardLog();
		userActivityRewardLog.setUserId(userId);
		userActivityRewardLog.setActivityTaskRewardId(activityTaskRewardId);
		userActivityRewardLog.setCreatedTime(new Date());
		this.userActivityTaskDao.addUserActivityRewardLog(userActivityRewardLog);

		CommonDropBO commonDropBO = new CommonDropBO();

		int toolType = activityTaskReward.getToolType();
		int toolId = activityTaskReward.getToolId();
		int toolNum = activityTaskReward.getToolNum();

		List<DropToolBO> dropToolBOList = this.toolService.giveTools(userId, toolType, toolId, toolNum, ToolUseType.ADD_ACTIVITY_TASK);
		for (DropToolBO dropToolBO : dropToolBOList) {
			this.toolService.appendToDropBO(userId, commonDropBO, dropToolBO);
		}

		return commonDropBO;

	}

	@Override
	public boolean updateActvityTask(String userId, int targetType, int times) {

		ActivityTask activityTask = this.systemActivityTaskDao.getActivityTaskByTarget(targetType);
		if (activityTask == null) {// 没这种活跃度任务
			return true;
		}

		int activityTaskId = activityTask.getActivityTaskId();

		UserActivityTask userActivityTask = this.getUserActivityTask(userId, activityTaskId);

		if (userActivityTask == null) {// 没有这个任务，不应该出现
			return true;
		}

		if (userActivityTask.getFinishTimes() >= activityTask.getNeedFinishTimes()) {// 次数已经超了
			return true;
		}

		int status = 0;
		int finishTimes = userActivityTask.getFinishTimes() + times;
		if (finishTimes >= activityTask.getNeedFinishTimes()) {
			finishTimes = activityTask.getNeedFinishTimes();
			status = 1;
		}

		return this.userActivityTaskDao.updateUserActivityTask(userId, activityTaskId, finishTimes, status);

	}

	@Override
	public List<UserActivityTask> getUserActivityTaskList(String userId) {

		List<UserActivityTask> userActivityTaskList = this.userActivityTaskDao.getUserActivityTaskList(userId);

		if (userActivityTaskList == null || userActivityTaskList.isEmpty()) {
			userActivityTaskList = new ArrayList<UserActivityTask>();
			List<ActivityTask> activityTaskList = this.systemActivityTaskDao.getActivityTaskList();

			Date now = new Date();

			for (ActivityTask activityTask : activityTaskList) {
				UserActivityTask userActivityTask = new UserActivityTask();
				userActivityTask.setActivityTaskId(activityTask.getActivityTaskId());
				userActivityTask.setUserId(userId);
				userActivityTask.setCreatedTime(now);
				userActivityTask.setFinishTimes(0);
				userActivityTask.setUpdatedTime(now);
				userActivityTask.setDate(DateUtils.getDate());

				userActivityTaskList.add(userActivityTask);
			}

			this.userActivityTaskDao.addUserActivityTask(userActivityTaskList);
		}

		return userActivityTaskList;
	}

	@Override
	public UserActivityTask getUserActivityTask(String userId, int activityTaskId) {

		List<UserActivityTask> userActivityTaskList = this.getUserActivityTaskList(userId);
		for (UserActivityTask userActivityTask : userActivityTaskList) {
			if (userActivityTask.getActivityTaskId() == activityTaskId) {
				return userActivityTask;
			}
		}

		return null;
	}

	@Override
	public List<UserActivityTaskBO> getUserActivityTaskListBO(String userId) {

		List<UserActivityTask> userActivityTaskList = this.getUserActivityTaskList(userId);
		List<UserActivityTaskBO> userActivityTaskBOList = new ArrayList<UserActivityTaskBO>();
		for (UserActivityTask userActivityTask : userActivityTaskList) {
			UserActivityTaskBO userActivityTaskBO = new UserActivityTaskBO();
			userActivityTaskBO.setActivityTaskId(userActivityTask.getActivityTaskId());
			userActivityTaskBO.setFinishTimes(userActivityTask.getFinishTimes());
			userActivityTaskBOList.add(userActivityTaskBO);
		}

		return userActivityTaskBOList;
	}

	@Override
	public List<UserActivityTaskRewardBO> getUserActivityTaskRewardBOList(String userId) {

		int userPoint = this.getUserActivityPoint(userId);

		List<ActivityTaskReward> activityTaskRewardList = this.systemActivityTaskDao.getActivityRewardList();
		List<UserActivityRewardLog> userActivityRewardLogList = this.userActivityTaskDao.getUserActivityRewardLogList(userId);
		Set<Integer> recivedRewardIds = new HashSet<Integer>();
		for (UserActivityRewardLog userActivityRewardLog : userActivityRewardLogList) {
			recivedRewardIds.add(userActivityRewardLog.getActivityTaskRewardId());
		}

		List<UserActivityTaskRewardBO> userActivityTaskRewardBOList = new ArrayList<UserActivityTaskRewardBO>();

		for (ActivityTaskReward activityTaskReward : activityTaskRewardList) {
			UserActivityTaskRewardBO userActivityTaskRewardBO = new UserActivityTaskRewardBO();
			userActivityTaskRewardBO.setActivityTaskRewardId(activityTaskReward.getActivityTaskRewardId());

			int status = 0;
			if (userPoint >= activityTaskReward.getPoint()) {
				if (recivedRewardIds.contains(activityTaskReward.getActivityTaskRewardId())) {// 已经领取
					status = 2;
				} else {
					status = 1;
				}
			}

			userActivityTaskRewardBO.setStatus(status);

			userActivityTaskRewardBOList.add(userActivityTaskRewardBO);
		}

		return userActivityTaskRewardBOList;
	}
}
