package com.lodogame.ldsg.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.CommandDao;
import com.lodogame.game.dao.SystemTaskDao;
import com.lodogame.game.dao.UserForcesDao;
import com.lodogame.game.dao.UserMonthlyCardTaskDao;
import com.lodogame.game.dao.UserTaskDao;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.DropToolBO;
import com.lodogame.ldsg.bo.UserTaskBO;
import com.lodogame.ldsg.checker.TaskChecker;
import com.lodogame.ldsg.constants.CommandType;
import com.lodogame.ldsg.constants.ForcesStatus;
import com.lodogame.ldsg.constants.PushType;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.constants.TaskStatus;
import com.lodogame.ldsg.constants.TaskTargetType;
import com.lodogame.ldsg.constants.TaskType;
import com.lodogame.ldsg.constants.ToolType;
import com.lodogame.ldsg.constants.ToolUseType;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.TaskUpdateEvent;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.helper.BOHelper;
import com.lodogame.ldsg.helper.DropToolHelper;
import com.lodogame.ldsg.helper.TaskHelper;
import com.lodogame.ldsg.service.TaskService;
import com.lodogame.ldsg.service.ToolService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.Command;
import com.lodogame.model.SystemTask;
import com.lodogame.model.User;
import com.lodogame.model.UserForces;
import com.lodogame.model.UserMonthlyCardTask;
import com.lodogame.model.UserTask;

public class TaskServiceImpl implements TaskService {

	private static final Logger logger = Logger.getLogger(TaskServiceImpl.class);
	
	@Autowired
	private UserMonthlyCardTaskDao userMonthlyCardTaskDao;

	@Autowired
	private UserTaskDao userTaskDao;

	@Autowired
	private SystemTaskDao systemTaskDao;

	@Autowired
	private UserService userService;

	@Autowired
	private ToolService toolService;

	@Autowired
	private UserForcesDao userForcesDao;

	@Autowired
	private CommandDao commandDao;

	public boolean addInitTask(String userId) {

		// 获取初始任务列表
		List<SystemTask> systemTaskList = this.systemTaskDao.getPosTaskList(0);

		assert systemTaskList.size() > 0;

		logger.debug("添加用户初始化任务.userId[" + userId + "], taskCount[" + systemTaskList.size() + "]");

		Date now = new Date();

		List<UserTask> userTaskList = new ArrayList<UserTask>();
		for (SystemTask systemTask : systemTaskList) {
			UserTask userTask = new UserTask();
			userTask.setUserId(userId);
			userTask.setFinishTimes(0);
			userTask.setNeedFinishTimes(systemTask.getNeedFinishTimes());
			userTask.setSystemTaskId(systemTask.getSystemTaskId());
			userTask.setTaskType(systemTask.getTaskType());
			userTask.setStatus(TaskStatus.TASK_STATUS_NEW);
			userTask.setCreatedTime(now);
			userTask.setUpdatedTime(now);
			userTaskList.add(userTask);
		}

		this.userTaskDao.add(userTaskList);

		return true;

	}

	public UserTaskBO get(String userId, int taskId) {

		UserTask userTask = this.userTaskDao.get(userId, taskId);
		if (userTask == null) {
			logger.error("用户任务不存在.userId[" + userId + "], taskId[" + taskId + "]");
			return null;
		}

		SystemTask systemTask = this.systemTaskDao.get(taskId);

		return BOHelper.crateUserTaskBO(systemTask, userTask);
	}

	public List<UserTaskBO> getUserTaskList(String userId, int status) {

		List<UserTaskBO> userTaskBOList = new ArrayList<UserTaskBO>();

		List<UserTask> userTaskList = this.userTaskDao.getList(userId, status);

		for (UserTask userTask : userTaskList) {
			if (userTask.getStatus() == TaskStatus.TASK_STATUS_DRAWED && userTask.getTaskType() != TaskType.INVITED_REGISTER) {// 已经领取的不返回
				continue;
			}

			SystemTask systemTask = this.systemTaskDao.get(userTask.getSystemTaskId());

			if (systemTask == null) { // 系统任务中没有这个任务就直接跳过
				continue;
			}

			Date now = new Date();

			if (now.before(systemTask.getEffectBeginTime()) || now.after(systemTask.getEffectEndTime())) { // 不在任务失效期的不返回
				continue;
			}

			UserTaskBO userTaskBO = BOHelper.crateUserTaskBO(systemTask, userTask);
			userTaskBOList.add(userTaskBO);
		}

		return userTaskBOList;

	}

	/**
	 * 验证是否可领取
	 * 
	 * @param userId
	 * @param systemTaskId
	 */
	private void checkFinish(String userId, int systemTaskId) {

		UserTask userTask = this.userTaskDao.get(userId, systemTaskId);

		if (userTask == null) {
			String message = "领取任务失败,任务不存在.userId[" + userId + "], systemTaskId[" + systemTaskId + "]";
			logger.warn(message);
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		if (userTask.getStatus() == TaskStatus.TASK_STATUS_NEW) {
			String message = "领取任务失败,任务未完成.userId[" + userId + "], systemTaskId[" + systemTaskId + "]";
			logger.warn(message);
			throw new ServiceException(TASK_RECEIVE_NOT_FINISH, message);
		}

		if (userTask.getStatus() == TaskStatus.TASK_STATUS_DRAWED) {
			String message = "领取任务失败,任务已经领取.userId[" + userId + "], systemTaskId[" + systemTaskId + "]";
			logger.warn(message);
			throw new ServiceException(TASK_RECEIVE_HAS_RECEIVE, message);
		}

		if (userTask.getStatus() != TaskStatus.TASK_STATUS_FINISH) {
			String message = "领取任务失败,任务不是可领取状态.userId[" + userId + "], systemTaskId[" + systemTaskId + "]";
			logger.warn(message);
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}
	}

	public CommonDropBO receive(String userId, int systemTaskId, EventHandle handle) {

		// 可领取状态验证
		this.checkFinish(userId, systemTaskId);

		// 获取系统任务
		SystemTask systemTask = this.systemTaskDao.get(systemTaskId);
		if (systemTask == null) {
			String message = "领取任务失败,系统任务不存在.userId[" + userId + "], systemTaskId[" + systemTaskId + "]";
			logger.warn(message);
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		int taskType = systemTask.getTaskType();
		int flag = PushType.PUSH_TYPE_DELETE;

		boolean success = false;
		if (taskType == TaskType.NORMAL_TASK) {
			success = this.userTaskDao.delete(userId, systemTaskId);
		} else {
			success = this.userTaskDao.update(userId, systemTaskId, TaskStatus.TASK_STATUS_DRAWED);
			// flag = PushType.PUSH_TYPE_UPDATE;
		}

		TaskUpdateEvent event = new TaskUpdateEvent(userId, systemTaskId, flag);
		handle.handle(event);

		if (!success) {
			String message = "领取任务失败,任务不存在.userId[" + userId + "], systemTaskId[" + systemTaskId + "]";
			logger.warn(message);
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		// 获取奖励
		CommonDropBO commonDropBO = this.dropTask(userId, systemTask);

		// 触发新的任务
		this.addAfterTask(userId, systemTaskId, handle);

		// 推用户信息
		this.userService.pushUser(userId);

		return commonDropBO;

	}

	/**
	 * 获取道具奖励
	 * 
	 * @param userId
	 * @param systemTask
	 */
	private CommonDropBO dropTask(String userId, SystemTask systemTask) {

		CommonDropBO commonDropBO = new CommonDropBO();

		User user = this.userService.get(userId);

		// 给金币
		int gold = systemTask.getGoldNum();
		this.userService.addGold(userId, gold, ToolUseType.ADD_TASK_DROP, user.getLevel());

		// 给银币
		int copper = systemTask.getCopperNum();
		if (TaskHelper.isVIPTask(systemTask.getSystemTaskId())) {// 特殊处理,VIP每日奖励
			copper += TaskHelper.getVipTaskCopperAdd(user.getVipLevel());
		}

		this.userService.addCopper(userId, copper, ToolUseType.ADD_TASK_DROP);

		// 给经验
		int exp = systemTask.getExp();

		toolService.giveTools(userId, ToolType.EXP, ToolType.EXP, exp, ToolUseType.ADD_TASK_DROP);

		// 给道具
		String toolIds = systemTask.getToolIds();

		List<DropToolBO> dropToolBOList = DropToolHelper.parseDropTool(toolIds);
		for (DropToolBO dropToolBO : dropToolBOList) {
			int toolType = dropToolBO.getToolType();
			int toolId = dropToolBO.getToolId();
			int toolNum = dropToolBO.getToolNum();

			List<DropToolBO> dropBOList = toolService.giveTools(userId, toolType, toolId, toolNum, ToolUseType.ADD_TASK_DROP);

			for (DropToolBO dropBO : dropBOList) {
				this.toolService.appendToDropBO(userId, commonDropBO, dropBO);
			}
		}

		return commonDropBO;

	}

	/**
	 * 判断用户是否完成过这个任务
	 * 
	 * @param systemTask
	 */
	private boolean isTaskFinish(String userId, SystemTask systemTask) {

		int taskTargetType = systemTask.getTaskTarget();

		if (taskTargetType == TaskTargetType.TASK_TYPE_PASS_FORCES) {

			Map<String, String> params = TaskHelper.parse(systemTask.getParam());
			int forcesId = NumberUtils.toInt(params.get("forces_id"));
			UserForces userForces = this.userForcesDao.get(userId, forcesId);
			if (userForces != null && userForces.getStatus() == ForcesStatus.STATUS_PASS) {
				return true;
			}
		} else if (taskTargetType == TaskTargetType.TASK_TYPE_TO_SPC_LEVEL) {

			Map<String, String> params = TaskHelper.parse(systemTask.getParam());
			int needLevel = NumberUtils.toInt(params.get("lv"));

			User user = this.userService.get(userId);
			return user.getLevel() >= needLevel;

		} else if (taskTargetType == TaskTargetType.TASK_TYPE_TO_BE_VIIP) {

			Map<String, String> params = TaskHelper.parse(systemTask.getParam());
			int needVipLevel = NumberUtils.toInt(params.get("vl"));
			User user = this.userService.get(userId);
			return user.getVipLevel() >= needVipLevel;

		}

		return false;

	}

	/**
	 * 触发新的任务
	 * 
	 * @param userId
	 * @param systemTaskId
	 * @param handle
	 */
	private void addAfterTask(String userId, int systemTaskId, EventHandle handle) {

		Date now = new Date();

		// 触发新的任务
		List<SystemTask> systemTaskList = this.systemTaskDao.getPosTaskList(systemTaskId);
		List<UserTask> userTaskList = new ArrayList<UserTask>();
		for (SystemTask sysTask : systemTaskList) {

			int finishTimes = 0;
			int status = TaskStatus.TASK_STATUS_NEW;

			if (isTaskFinish(userId, sysTask)) {
				finishTimes = sysTask.getNeedFinishTimes();
				status = TaskStatus.TASK_STATUS_FINISH;
			}

			UserTask userTask = new UserTask();
			userTask.setUserId(userId);
			userTask.setCreatedTime(now);
			userTask.setUpdatedTime(now);
			userTask.setNeedFinishTimes(finishTimes);
			userTask.setSystemTaskId(sysTask.getSystemTaskId());
			userTask.setTaskType(sysTask.getTaskType());
			userTask.setStatus(status);
			userTask.setFinishTimes(finishTimes);

			userTaskList.add(userTask);
		}

		if (userTaskList.size() > 0) {
			this.userTaskDao.add(userTaskList);
			for (UserTask userTask : userTaskList) {
				TaskUpdateEvent taskUpdateEvent = new TaskUpdateEvent(userId, userTask.getSystemTaskId(), PushType.PUSH_TYPE_ADD);
				handle.handle(taskUpdateEvent);
			}
		}

	}

	@Override
	public void updateTaskFinish(String userId, int times, EventHandle handle, TaskChecker taskChecker) {

		List<UserTask> userTaskList = this.userTaskDao.getList(userId, TaskStatus.TASK_STATUS_NEW);
		for (UserTask userTask : userTaskList) {

			int systemTaskId = userTask.getSystemTaskId();
			int status = userTask.getStatus();// 当前状态

			SystemTask systemTask = this.systemTaskDao.get(systemTaskId);

			if (systemTask == null) {
				continue;
			}

			Map<String, String> params = TaskHelper.parse(systemTask.getParam());

			if (!taskChecker.isFinish(systemTask.getSystemTaskId(), systemTask.getTaskTarget(), params)) {
				continue;
			}
			
			// 判断 30 天月卡任务
			if (userTask.getSystemTaskId() == 7001) {
				UserMonthlyCardTask task = userMonthlyCardTaskDao.getByUserId(userId);

				if (task == null || task.isTaskExpired()) {
					continue;
				}
			}

			int needFinishTimes = systemTask.getNeedFinishTimes();
			int finishTimes = userTask.getFinishTimes();

			finishTimes = finishTimes + times;
			if (finishTimes >= needFinishTimes) {
				finishTimes = needFinishTimes;
				status = TaskStatus.TASK_STATUS_FINISH;
			}

			// 更新任务
			this.updateTask(userId, systemTaskId, finishTimes, status, handle);

		}

	}

	/**
	 * 更新任务
	 * 
	 * @param userId
	 * @param systemTaskId
	 * @param finishTimes
	 * @param status
	 * @param handle
	 */
	private void updateTask(String userId, int systemTaskId, int finishTimes, int status, EventHandle handle) {

		this.userTaskDao.update(userId, systemTaskId, finishTimes, status);

		TaskUpdateEvent taskUpdateEvent = new TaskUpdateEvent(userId, systemTaskId, PushType.PUSH_TYPE_UPDATE);
		boolean handled = handle.handle(taskUpdateEvent);
		if (!handled) {// 如果没处理，则要发个推送用户任务变动的命令

			Map<String, String> map = new HashMap<String, String>();
			map.put("userId", userId);
			map.put("systemTaskId", String.valueOf(systemTaskId));
			map.put("pushType", String.valueOf(PushType.PUSH_TYPE_UPDATE));

			Command command = new Command();
			command.setCommand(CommandType.COMMAND_PUSH_USER_TASK);
			command.setType(CommandType.PUSH_USER);
			command.setParams(map);

			commandDao.add(command);
		}

	}

	@Override
	public void refreshDailyTask(String userId) {

		List<UserTask> taskList = this.userTaskDao.getList(userId, 100);

		Date now = new Date();

		User user = this.userService.get(userId);

		for (UserTask userTask : taskList) {

			int systemTaskId = userTask.getSystemTaskId();
			// SystemTask systemTask =
			// this.systemTaskDao.get(userTask.getSystemTaskId());

			// 已经领取的任务
			if (userTask.getStatus() != TaskStatus.TASK_STATUS_NEW && userTask.getTaskType() == TaskType.DAILY_TASK) {

				if (DateUtils.isSameDay(now, userTask.getUpdatedTime())) {// 同一天更新过的，不处理
					continue;
				}

				int status = TaskStatus.TASK_STATUS_NEW;

				if (TaskHelper.isVIPTask(userTask.getSystemTaskId())) {// VIP任务
					if (user.getVipLevel() > 0) {
						status = TaskStatus.TASK_STATUS_FINISH;
					}
				}

				this.userTaskDao.update(userId, systemTaskId, 0, status);

			}

		}

	}
	
	private boolean addUserMonthlyCardTask(String userId) {
		SystemTask systemTask = systemTaskDao.get(7001);

		UserTask userTask = new UserTask();
		userTask.setUserId(userId);
		userTask.setFinishTimes(1);
		userTask.setNeedFinishTimes(systemTask.getNeedFinishTimes());
		userTask.setSystemTaskId(systemTask.getSystemTaskId());
		userTask.setTaskType(systemTask.getTaskType());
		userTask.setStatus(TaskStatus.TASK_STATUS_FINISH);

		Date now = new Date();
		userTask.setCreatedTime(now);
		userTask.setUpdatedTime(now);

		List<UserTask> userTaskList = new ArrayList<UserTask>();
		userTaskList.add(userTask);

		userTaskDao.add(userTaskList);

		return true;
	}
	
	@Override
	public void refreshUserMonthlyCardTask(String userId) {
		UserTask task = userTaskDao.get(userId, 7001);
		UserMonthlyCardTask monthlyTask = userMonthlyCardTaskDao.getByUserId(userId);

		Date now = new Date();

		if (monthlyTask == null) {
			return;
		}

		if (task != null) {
			if (DateUtils.isSameDay(now, task.getUpdatedTime()) == true || monthlyTask.isTaskExpired() == true) {
				return;
			}
			userTaskDao.update(userId, task.getSystemTaskId(), 1, TaskStatus.TASK_STATUS_FINISH);
		} else if (monthlyTask.isTaskExpired() == false) {
			addUserMonthlyCardTask(userId);
		}

	}

}
