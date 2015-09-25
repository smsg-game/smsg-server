package com.lodogame.ldsg.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.ActivityDrawDao;
import com.lodogame.game.dao.ConfigDataDao;
import com.lodogame.ldsg.bo.ActivityDrawScoreRankBo;
import com.lodogame.ldsg.bo.ActivityDrawToolBO;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.DropToolBO;
import com.lodogame.ldsg.bo.SystemActivityDrawShowBO;
import com.lodogame.ldsg.bo.UserToolBO;
import com.lodogame.ldsg.constants.ActivityId;
import com.lodogame.ldsg.constants.ConfigKey;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.constants.ToolId;
import com.lodogame.ldsg.constants.ToolType;
import com.lodogame.ldsg.constants.ToolUseType;
import com.lodogame.ldsg.event.ToolDropEvent;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.helper.BOHelper;
import com.lodogame.ldsg.helper.DrawHelper;
import com.lodogame.ldsg.service.ActivityDrawService;
import com.lodogame.ldsg.service.ActivityService;
import com.lodogame.ldsg.service.EventServcie;
import com.lodogame.ldsg.service.ToolService;
import com.lodogame.ldsg.service.UnSynLogService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.ActivityDrawTool;
import com.lodogame.model.SystemActivityDrawShow;
import com.lodogame.model.User;
import com.lodogame.model.UserDrawTime;

public class ActivityDrawServiceImpl implements ActivityDrawService {

	private static final Logger logger = Logger.getLogger(ActivityDrawServiceImpl.class);

	@Autowired
	private UserService userService;

	@Autowired
	private ActivityService activityService;

	@Autowired
	private ToolService toolService;

	@Autowired
	private ActivityDrawDao activityDrawDao;

	@Autowired
	private EventServcie eventServcie;

	@Autowired
	private UnSynLogService unSynLogService;
	
	@Autowired
	private ConfigDataDao configDataDao;

	@Override
	public CommonDropBO draw(String userId, int time) {

		logger.info("用户活动抽奖userId[" + userId + "]" + ":time[" + time + "]");
		ActivityDrawToolBO activityDrawToolBO = null;
		CommonDropBO commonDropBO = new CommonDropBO();
		User user = this.userService.get(userId);
		ActivityDrawTool activityDrawTool = null;

		if (!activityService.isActivityOpen(ActivityId.DRAW_ACTIVITY_ID)) {
			throw new ServiceException(ServiceReturnCode.ACTIVITY_NOT_OPEN_EXCTPION, "活动时间已经过.userId[" + userId + "]");
		}

		// 减少用户幸运珠子
		if (!toolService.reduceTool(userId, ToolType.MATERIAL, ToolId.TOOL_ID_LUCK_ORDER_NUM, time * configDataDao.getInt(ConfigKey.DRAW_REDUCE_TOOL,99), ToolUseType.REDUCE_ACTIVITY_DRAW)) {
			throw new ServiceException(NO_LUCK_ORDER_NUM, "幸运珠不足userId[" + userId + "]");
		}

		UserDrawTime userDrawTime = activityDrawDao.getActivityDrawUserData(userId);
		int drawTimes = 0;
		if (userDrawTime != null) {
			drawTimes = userDrawTime.getTimes();
		}

		for (int i = 1; i <= time; i++) {

			activityDrawTool = getActivityDrawTool(userId, drawTimes + i);

			activityDrawToolBO = new ActivityDrawToolBO();
			activityDrawToolBO.setIsFlash(activityDrawTool.getIsLight());
			activityDrawToolBO.setToolId(activityDrawTool.getToolId());
			activityDrawToolBO.setToolNum(activityDrawTool.getToolNum());
			activityDrawToolBO.setToolType(activityDrawTool.getToolType());
			activityDrawToolBO.setOutId(activityDrawTool.getOutId());

			commonDropBO.addFlashTool(activityDrawToolBO);

			// 添加抽奖活动道具数量记录
			activityDrawDao.addActivityDrowToolNumRecorde(activityDrawTool.getToolNum(), activityDrawTool.getOutId());
			// 发道具到用户背包
			List<DropToolBO> dropToolBOList = toolService.giveTools(userId, activityDrawTool.getToolType(), activityDrawTool.getToolId(), activityDrawTool.getToolNum(), ToolUseType.ADD_ACTIVITY_DRAW);
			for (DropToolBO dropToolBO : dropToolBOList) {
				toolService.appendToDropBO(userId, commonDropBO, dropToolBO);
			}

			// 跑马灯
			ToolDropEvent toolDropEvent = new ToolDropEvent(userId, activityDrawTool.getToolId(), activityDrawTool.getToolName(), ToolUseType.ADD_ACTIVITY_DRAW, activityDrawTool.getIsFlash());
			eventServcie.dispatchEvent(toolDropEvent);
		}

		// 更新抽奖次数和积分
		activityDrawDao.updateUserDrawTime(userId, user.getUsername(), time, time);

		// 抽奖日志
		unSynLogService.userDrawLog(commonDropBO.getDrawToolBOList(), userId, time);

		return commonDropBO;
	}

	// private ActivityDrawTool getActivityDrawTool(String userId, int times) {
	// int random = 10000;
	// List<ActivityDrawTool> dropToolList = null;
	//
	// ActivityDrawTool activityDrawTool = null;
	// if (times % 300 == 0 || times % 60 == 0) {
	// int getTimes = 60;
	// if (times % 300 == 0) {
	// getTimes = 300;
	// }
	// activityDrawTool =
	// activityDrawDao.getActivityDrawTool(activityDrawDao.getOutId(getTimes),
	// 1);
	//
	// } else if (times % 30 == 0) {
	// dropToolList = activityDrawDao.getThirtyDropList();
	// } else {
	// random = 100000;
	// int type = DrawHelper.getDropType(times);
	// dropToolList = activityDrawDao.getNormalDropList(type);
	// }
	//
	// if (activityDrawTool == null) {
	// activityDrawTool = DrawHelper.draw(dropToolList, random);
	// }
	//
	// // 次数达到限制后的掉落
	// int upper =
	// activityDrawDao.getDrawToolUpper(activityDrawTool.getOutId());
	// int todayNum =
	// activityDrawDao.getTodayDrowToolNum(activityDrawTool.getOutId());
	// if (upper != 0 && todayNum >= upper) {
	// dropToolList = activityDrawDao.getLimitOverDropList();
	// activityDrawTool = DrawHelper.draw(dropToolList, 10000);
	// }
	// return activityDrawTool;
	// }

	private ActivityDrawTool getActivityDrawTool(String userId, int times) {
		int random = 10000;
		List<ActivityDrawTool> dropToolList = null;

		ActivityDrawTool activityDrawTool = null;

		if (times % 300 == 0 || times % 60 == 0) {
			int getTimes = 60;
			if (times % 300 == 0) {
				getTimes = 300;
			}
			activityDrawTool = activityDrawDao.getActivityDrawTool(activityDrawDao.getOutId(getTimes), 1);

		} else if (times % 30 == 0) {
			dropToolList = activityDrawDao.getThirtyDropList();
		} else {
			random = 100000;
			int type = DrawHelper.getDropType(times);
			dropToolList = activityDrawDao.getNormalDropList(type);
		}

		if (activityDrawTool == null) {
			activityDrawTool = DrawHelper.draw(dropToolList, random);
		}

		// 次数达到限制后的掉落
		int upper = activityDrawDao.getDrawToolUpper(activityDrawTool.getOutId());

		int todayNum = activityDrawDao.getTodayDrowToolNum(activityDrawTool.getOutId());

		if (upper != 0 && todayNum >= upper) {
			dropToolList = activityDrawDao.getLimitOverDropList();
			activityDrawTool = DrawHelper.draw(dropToolList, 10000);
		}
		return activityDrawTool;
	}

	@Override
	public List<ActivityDrawScoreRankBo> getRank() {
		List<ActivityDrawScoreRankBo> list = new ArrayList<ActivityDrawScoreRankBo>();
		List<UserDrawTime> userDrawTimeList = activityDrawDao.getActivityDrawScoreRank();
		ActivityDrawScoreRankBo rank;
		int num = 1;
		for (UserDrawTime userDrawTime : userDrawTimeList) {
			rank = new ActivityDrawScoreRankBo();
			rank.setRank(num++);
			rank.setScore(userDrawTime.getScore());
			rank.setUserName(userDrawTime.getUserName());
			list.add(rank);
		}
		return list;
	}

	@Override
	public int getUserLuckOrder(String userId) {

		UserToolBO userToolBO = toolService.getUserToolBO(userId, ToolId.TOOL_ID_LUCK_ORDER_NUM);
		if (userToolBO != null) {
			return userToolBO.getToolNum();
		}
		return 0;
	}

	@Override
	public int getUserDrawScore(String userId) {

		return activityDrawDao.getUserDrawScore(userId);
	}

	@Override
	public List<SystemActivityDrawShowBO> getSystemActivityDrawShowBOList() {

		List<SystemActivityDrawShowBO> list = new ArrayList<SystemActivityDrawShowBO>();

		List<SystemActivityDrawShow> systemActivityDrawShowList = this.activityDrawDao.getActivityDrawShowList();
		for (SystemActivityDrawShow systemActivityDrawShow : systemActivityDrawShowList) {
			list.add(BOHelper.createSystemActivityDrawShowBO(systemActivityDrawShow));
		}

		return list;

	}
}
