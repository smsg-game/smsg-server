package com.lodogame.ldsg.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.CommandDao;
import com.lodogame.game.dao.GiftCodeDao;
import com.lodogame.game.dao.PaymentLogDao;
import com.lodogame.game.dao.SystemActivityDao;
import com.lodogame.game.dao.SystemCheckInInfoDao;
import com.lodogame.game.dao.SystemCheckInRewardDao;
import com.lodogame.game.dao.SystemGiftbagDao;
import com.lodogame.game.dao.SystemHeroAttrDao;
import com.lodogame.game.dao.SystemHeroExchangeDao;
import com.lodogame.game.dao.SystemLoginReward7Dao;
import com.lodogame.game.dao.SystemLoginRewardDao;
import com.lodogame.game.dao.SystemMailDao;
import com.lodogame.game.dao.SystemOncePayRewardDao;
import com.lodogame.game.dao.SystemOnlineRewardDao;
import com.lodogame.game.dao.SystemRecivePowerDao;
import com.lodogame.game.dao.SystemReduceRebateDao;
import com.lodogame.game.dao.SystemTavernRebateDao;
import com.lodogame.game.dao.SystemTotalDayPayRewardDao;
import com.lodogame.game.dao.SystemTotalPayRewardDao;
import com.lodogame.game.dao.ToolExchangeDao;
import com.lodogame.game.dao.UserCheckInLogDao;
import com.lodogame.game.dao.UserDailyGainLogDao;
import com.lodogame.game.dao.UserEquipDao;
import com.lodogame.game.dao.UserExtinfoDao;
import com.lodogame.game.dao.UserGiftbagDao;
import com.lodogame.game.dao.UserHeroDao;
import com.lodogame.game.dao.UserHeroExchangeDao;
import com.lodogame.game.dao.UserLimOnlineRewardDao;
import com.lodogame.game.dao.UserLoginReward7InfoDao;
import com.lodogame.game.dao.UserLoginRewardInfoDao;
import com.lodogame.game.dao.UserMailDao;
import com.lodogame.game.dao.UserMapperDao;
import com.lodogame.game.dao.UserOnlineLogDao;
import com.lodogame.game.dao.UserOnlineRewardDao;
import com.lodogame.game.dao.UserPayRewardDao;
import com.lodogame.game.dao.UserReduceGoldRebateLogDao;
import com.lodogame.game.dao.UserTavernRebateLogDao;
import com.lodogame.game.dao.UserToolDao;
import com.lodogame.game.dao.UserTotalDayPayRewardDao;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.game.utils.IDGenerator;
import com.lodogame.game.utils.json.Json;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.CopyActivityBO;
import com.lodogame.ldsg.bo.DropToolBO;
import com.lodogame.ldsg.bo.System30LoginRewardBO;
import com.lodogame.ldsg.bo.SystemActivityBO;
import com.lodogame.ldsg.bo.SystemCheckInRewardBO;
import com.lodogame.ldsg.bo.SystemHeroExchangeBO;
import com.lodogame.ldsg.bo.SystemReduceRebateBo;
import com.lodogame.ldsg.bo.TavernTebatelBo;
import com.lodogame.ldsg.bo.ToolExchangeBO;
import com.lodogame.ldsg.bo.ToolExchangeCountBO;
import com.lodogame.ldsg.bo.ToolExchangeEquipBO;
import com.lodogame.ldsg.bo.ToolExchangeHeroBO;
import com.lodogame.ldsg.bo.ToolExchangeLooseBO;
import com.lodogame.ldsg.bo.ToolExchangeReceiveBO;
import com.lodogame.ldsg.bo.ToolExchangeToolBO;
import com.lodogame.ldsg.bo.UserBO;
import com.lodogame.ldsg.bo.UserCheckInLogBO;
import com.lodogame.ldsg.bo.UserEquipBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.bo.UserLimOnlineRewardBO;
import com.lodogame.ldsg.bo.UserLoginRewardInfoBO;
import com.lodogame.ldsg.bo.UserOnlineRewardBO;
import com.lodogame.ldsg.bo.UserPayRewardBO;
import com.lodogame.ldsg.bo.UserRecivePowerBO;
import com.lodogame.ldsg.bo.UserToolBO;
import com.lodogame.ldsg.bo.UserTotalDayPayRewardBO;
import com.lodogame.ldsg.constants.ActivityId;
import com.lodogame.ldsg.constants.ActivityTargetType;
import com.lodogame.ldsg.constants.ActivityType;
import com.lodogame.ldsg.constants.GiftBagType;
import com.lodogame.ldsg.constants.InitDefine;
import com.lodogame.ldsg.constants.MailStatus;
import com.lodogame.ldsg.constants.MailTarget;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.constants.ToolType;
import com.lodogame.ldsg.constants.ToolUseType;
import com.lodogame.ldsg.constants.UserPayRewardStatus;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.ToolUpdateEvent;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.helper.ActivityHelper;
import com.lodogame.ldsg.helper.BOHelper;
import com.lodogame.ldsg.helper.DropToolHelper;
import com.lodogame.ldsg.helper.UserOnlineRewardHelper;
import com.lodogame.ldsg.service.ActivityService;
import com.lodogame.ldsg.service.ActivityTaskService;
import com.lodogame.ldsg.service.EquipService;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.MailService;
import com.lodogame.ldsg.service.ToolService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.ldsg.service.VipService;
import com.lodogame.model.GiftCode;
import com.lodogame.model.GiftbagDropTool;
import com.lodogame.model.PaymentLog;
import com.lodogame.model.SystemActivity;
import com.lodogame.model.SystemCheckInInfo;
import com.lodogame.model.SystemCheckInReward;
import com.lodogame.model.SystemGiftbag;
import com.lodogame.model.SystemHeroAttr;
import com.lodogame.model.SystemHeroExchange;
import com.lodogame.model.SystemLoginReward;
import com.lodogame.model.SystemLoginReward7;
import com.lodogame.model.SystemMail;
import com.lodogame.model.SystemOncePayReward;
import com.lodogame.model.SystemOnlineReward;
import com.lodogame.model.SystemRecivePower;
import com.lodogame.model.SystemReduceRebate;
import com.lodogame.model.SystemTavernRebate;
import com.lodogame.model.SystemTotalDayPayReward;
import com.lodogame.model.SystemTotalPayReward;
import com.lodogame.model.ToolExchange;
import com.lodogame.model.User;
import com.lodogame.model.UserCheckinLog;
import com.lodogame.model.UserEquip;
import com.lodogame.model.UserExtinfo;
import com.lodogame.model.UserGiftbag;
import com.lodogame.model.UserHero;
import com.lodogame.model.UserHeroExchange;
import com.lodogame.model.UserLimOnlineReward;
import com.lodogame.model.UserLoginReward7Info;
import com.lodogame.model.UserLoginRewardInfo;
import com.lodogame.model.UserMail;
import com.lodogame.model.UserMapper;
import com.lodogame.model.UserOnlineLog;
import com.lodogame.model.UserOnlineReward;
import com.lodogame.model.UserPayReward;
import com.lodogame.model.UserToolExchangeLog;
import com.lodogame.model.UserTotalDayPayRewardLog;

public class ActivityServiceImpl implements ActivityService {

	private static final Logger logger = Logger.getLogger(ActivityServiceImpl.class);

	@Autowired
	private UserLimOnlineRewardDao userLimOnlineRewardDao;

	@Autowired
	private UserLoginReward7InfoDao userLoginReward7InfoDao;

	@Autowired
	private SystemHeroAttrDao systemHeroAttrDao;

	@Autowired
	private SystemTotalPayRewardDao systemTotalPayRewardDao;

	@Autowired
	private UserToolDao userToolDao;

	@Autowired
	private CommandDao commandDao;

	@Autowired
	private UserHeroDao userHeroDao;

	@Autowired
	private EquipService equipService;

	@Autowired
	private UserExtinfoDao userExtinfoDao;

	@Autowired
	private ToolExchangeDao toolExchangeDao;

	@Autowired
	private UserEquipDao userEquipDao;

	@Autowired
	private SystemActivityDao systemActivityDao;

	@Autowired
	private SystemCheckInInfoDao systemCheckInInfoDao;

	@Autowired
	private UserDailyGainLogDao userDailyGainLogDao;

	@Autowired
	private SystemRecivePowerDao systemRecivePowerDao;

	@Autowired
	private UserService userService;

	@Autowired
	private UserCheckInLogDao userCheckInLogDao;

	@Autowired
	private UserLoginRewardInfoDao userLoginRewardInfoDao;

	@Autowired
	private SystemCheckInRewardDao systemCheckInRewardDao;

	@Autowired
	private SystemLoginRewardDao systemLoginRewardDao;

	@Autowired
	private ToolService toolService;

	@Autowired
	private SystemHeroExchangeDao systemHeroExchangeDao;

	@Autowired
	private SystemGiftbagDao systemGiftbagDao;

	@Autowired
	private HeroService heroService;

	@Autowired
	private UserGiftbagDao userGiftbagDao;

	@Autowired
	private PaymentLogDao paymentLogDao;

	@Autowired
	private GiftCodeDao giftCodeDao;

	@Autowired
	private ActivityTaskService activityTaskService;

	@Autowired
	private UserHeroExchangeDao userHeroExchangeDao;

	@Autowired
	private UserOnlineRewardDao userOnlineRewardDao;

	@Autowired
	private UserOnlineLogDao userOnlineLogDao;

	@Autowired
	private SystemOncePayRewardDao systemOncePayRewardDao;

	@Autowired
	private UserPayRewardDao userPayRewardDao;

	@Autowired
	private SystemLoginReward7Dao systemLoginReward7Dao;

	@Autowired
	private UserMapperDao userMapperDao;

	@Autowired
	private SystemTotalDayPayRewardDao systemTotalDayPayRewardDao;

	@Autowired
	private UserTotalDayPayRewardDao userTotalDayPayRewardDao;

	@Autowired
	private SystemTavernRebateDao systemTavernRebateDao;

	@Autowired
	private UserTavernRebateLogDao userTavernRebateLogDao;

	@Autowired
	private SystemReduceRebateDao systemReduceRebateDao;

	@Autowired
	private UserReduceGoldRebateLogDao userReduceGoldRebateLogDao;

	@Autowired
	private SystemOnlineRewardDao systemOnlineRewardDao;

	@Autowired
	private VipService vipService;
	
	@Autowired
	private UserMailDao userMailDao;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private SystemMailDao systemMailDao;

	@Override
	public List<CopyActivityBO> getCopyActivityList() {

		List<SystemActivity> systemActivityList = this.systemActivityDao.getList(ActivityType.ACTIVITY_TYPE_COPY);

		List<CopyActivityBO> copyActivityBOList = new ArrayList<CopyActivityBO>();
		for (SystemActivity systemActivity : systemActivityList) {

			CopyActivityBO copyActivityBO = BOHelper.crateCopyActivityBO(systemActivity);

			Map<String, Date> startEndTime = computeActivityTime(systemActivity);
			copyActivityBO.setStartTime(startEndTime.get("startTime").getTime());
			copyActivityBO.setEndTime(startEndTime.get("endTime").getTime());

			copyActivityBOList.add(copyActivityBO);
		}

		return copyActivityBOList;

	}

	/**
	 * 根据活动在哪些天开放和现在的时间计算出活动的开始时间和结束时间
	 * 
	 * @param 活动的开放时间
	 *            ，例如：1,3,5 表示在星期一、二、五开放
	 * @return 活动的开始时间和结束时间
	 * @param opemWeeks
	 */
	@Override
	public Map<String, Date> computeActivityTime(SystemActivity systemActivity) {
		Map<String, Date> startEndTime = new HashMap<String, Date>();

		String openWeeks = systemActivity.getOpenWeeks();

		// 如果每天都开放，则将结束时间设置为无限远的时间
		if (openWeeks.equals("1,2,3,4,5,6,7")) {
			startEndTime.put("startTime", systemActivity.getStartTime());
			startEndTime.put("endTime", systemActivity.getEndTime());
			return startEndTime;
		}

		Date now = new Date();

		Date startTime = null;
		Date endTime = null;

		int dayOfWeek = DateUtils.getDayOfWeek(); // 当天这星期中的第几天

		if (openWeeks.indexOf(String.valueOf(dayOfWeek)) != -1) {// 当天有开放

			startTime = now;

			for (int i = 1; i <= 7; i++) {// 找结束时间

				Date date = DateUtils.addDays(now, i);
				int dayWeek = DateUtils.getDayOfWeek(date);
				if (openWeeks.indexOf(String.valueOf(dayWeek)) == -1) {// 不开放了
					endTime = DateUtils.getDateAtMidnight(date);// 那么那天的凌晨就是结束时间
					break;
				}
			}

		} else {// 当天没有开放

			for (int i = 1; i <= 7; i++) {// 找开始时间

				Date date = DateUtils.addDays(now, i);
				int dayWeek = DateUtils.getDayOfWeek(date);

				if (openWeeks.indexOf(String.valueOf(dayWeek)) != -1) {// 开放
					if (startTime == null) {
						startTime = DateUtils.getDateAtMidnight(date);// 那么那天的凌晨就是开始时间
					}
				} else if (startTime != null) {
					endTime = DateUtils.getDateAtMidnight(date);
				}
			}

		}

		startEndTime.put("startTime", startTime);
		if (systemActivity.getEndTime().before(endTime)) {
			startEndTime.put("endTime", systemActivity.getEndTime());
		} else {
			startEndTime.put("endTime", endTime);
		}

		return startEndTime;

	}

	public List<UserRecivePowerBO> getUserRecivePowerInfo(String userId) {

		List<SystemRecivePower> systemRecivePowerList = this.systemRecivePowerDao.getList();

		List<UserRecivePowerBO> userRecivePowerBOList = new ArrayList<UserRecivePowerBO>();

		for (SystemRecivePower systemRecivePower : systemRecivePowerList) {

			int type = systemRecivePower.getType();

			UserRecivePowerBO userRecivePowerBO = new UserRecivePowerBO();

			int times = this.userDailyGainLogDao.getUserDailyGain(userId, type);

			userRecivePowerBO.setTimes(times);
			userRecivePowerBO.setStartTime(getTime(systemRecivePower.getStartTime()));
			userRecivePowerBO.setEndTime(getTime(systemRecivePower.getEndTime()));
			userRecivePowerBO.setType(type);

			userRecivePowerBOList.add(userRecivePowerBO);
		}

		return userRecivePowerBOList;
	}

	private long getTime(String strTime) {

		try {
			Date date = DateUtils.str2Date(DateUtils.getDate() + " " + strTime);
			return date.getTime();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return System.currentTimeMillis();
	}

	@Override
	public boolean recivePower(String userId, int type) {

		SystemRecivePower systemRecivePower = systemRecivePowerDao.get(type);

		if (!ActivityHelper.isNowCanRecive(systemRecivePower)) {
			throw new ServiceException(RECIVE_POWER_NOT_IN_THE_PERIOD, "领取体力出错，当前不在领取时间段userId[" + userId + "], type[" + type + "]");
		}

		int amount = this.userDailyGainLogDao.getUserDailyGain(userId, type);

		if (amount >= 5) {
			throw new ServiceException(RECIVE_POWER_RECIVE_ALL, "领取体力出错，已经领取完.userId[" + userId + "], type[" + type + "]");
		} else {

			// 记录次数
			boolean success = this.userDailyGainLogDao.addUserDailyGain(userId, type, 1);
			if (success) {
				// 给体力,前提是没有爆管

				this.userService.addPower(userId, 10, ToolUseType.ADD_BY_ACTIVITY, null);
				// 推送用户
				this.userService.pushUser(userId);

				// 活跃度任务
				this.activityTaskService.updateActvityTask(userId, ActivityTargetType.RECIVE_POWER, 1);

			} else {
				throw new ServiceException(ServiceReturnCode.FAILD, "领取体力出错，userId[" + userId + "], type[" + type + "]");
			}
		}

		return true;
	}

	@Override
	public CommonDropBO checkIn(String userId) {

		int groupId = this.getCheckInGroupId();

		UserCheckinLog userCheckInLog = this.userCheckInLogDao.getLastUserCheckInLog(userId, groupId);

		int day = 1;

		if (userCheckInLog != null) {

			String checkInDate = userCheckInLog.getDate();
			if (StringUtils.equals(checkInDate, DateUtils.getDate())) {
				String message = "签到出错,用户当天已经签到过.userId[" + userId + "], date[" + checkInDate + "]";
				throw new ServiceException(CHECK_IN_HAS_CHECK_IN, message);
			}

			day = userCheckInLog.getDay() + 1;
		}

		// 做签到日志
		UserCheckinLog newUserCheckInLog = new UserCheckinLog();
		newUserCheckInLog.setUserId(userId);
		newUserCheckInLog.setGroupId(groupId);
		newUserCheckInLog.setDay(day);
		newUserCheckInLog.setDate(DateUtils.getDate());
		newUserCheckInLog.setCreatedTime(new Date());
		this.userCheckInLogDao.addUserCheckInLog(newUserCheckInLog);

		SystemCheckInReward systemCheckInReward = this.systemCheckInRewardDao.getSystemCheckInReward(groupId, day);
		if (systemCheckInReward == null) {
			String message = "签到出错,用户已经到达了可领取的最大天数.userId[" + userId + "], day[" + day + "]";
			throw new ServiceException(CHECK_IN_IS_MAX_DAY, message);
		}

		int toolType = systemCheckInReward.getToolType();
		int toolId = systemCheckInReward.getToolId();
		int toolNum = systemCheckInReward.getToolNum();

		CommonDropBO commonDropBO = new CommonDropBO();

		List<DropToolBO> dropToolBOList = this.toolService.giveTools(userId, toolType, toolId, toolNum, ToolUseType.ADD_CHECK_IN);

		for (DropToolBO dropToolBO : dropToolBOList) {
			// 构建道具掉落
			this.toolService.appendToDropBO(userId, commonDropBO, dropToolBO);
		}

		return commonDropBO;
	}

	@Override
	public UserCheckInLogBO getUserCheckinLog(String userId) {

		int groupId = this.getCheckInGroupId();

		UserCheckinLog userCheckInLog = this.userCheckInLogDao.getLastUserCheckInLog(userId, groupId);
		int day = 1;
		int isCheckIn = 0;
		if (userCheckInLog != null) {

			day = userCheckInLog.getDay() + 1;

			if (StringUtils.equals(DateUtils.getDate(), userCheckInLog.getDate())) {// 当天已经签到
				isCheckIn = 1;
			}
		}

		SystemCheckInInfo systemCheckInInfo = systemCheckInInfoDao.getSystemCheckInInfo();
		int finishDay = 30;
		if (systemCheckInInfo != null) {
			finishDay = DateUtils.getDayDiff(new Date(), systemCheckInInfo.getFinishTime());
		}

		UserCheckInLogBO userCheckInLogBO = new UserCheckInLogBO();
		userCheckInLogBO.setDay(day);
		userCheckInLogBO.setIsCheckIn(isCheckIn);
		userCheckInLogBO.setFinishDay(finishDay);

		return userCheckInLogBO;
	}

	@Override
	public int getCheckInGroupId() {

		int groupId = 1;

		SystemCheckInInfo systemCheckInInfo = systemCheckInInfoDao.getSystemCheckInInfo();
		if (systemCheckInInfo == null) {

			this.systemCheckInInfoDao.setSystemCheckInInfo(groupId, DateUtils.addDays(DateUtils.getDateAtMidnight(), 30));

		} else {

			Date finishTime = systemCheckInInfo.getFinishTime();
			if (finishTime.getTime() <= System.currentTimeMillis()) {

				groupId = systemCheckInInfo.getGroupId() + 1;
				SystemCheckInReward systemCheckInReward = this.systemCheckInRewardDao.getSystemCheckInReward(groupId, 1);
				if (systemCheckInReward == null) {// 已经没有下一组了，要重新开始
					groupId = 1;
				}

				// 更新当前系统符到信息
				this.systemCheckInInfoDao.setSystemCheckInInfo(groupId, DateUtils.addDays(DateUtils.getDateAtMidnight(), 30));

				this.userCheckInLogDao.cleanUserCheckInLog();// 清掉所有签到日志

			} else {
				groupId = systemCheckInInfo.getGroupId();
			}

		}

		return groupId;
	}

	@Override
	public List<SystemCheckInRewardBO> getSystemReciveReward() {

		int groupId = this.getCheckInGroupId();

		List<SystemCheckInRewardBO> systemCheckInRewardBOList = new ArrayList<SystemCheckInRewardBO>();

		List<SystemCheckInReward> systemCheckInRewardList = this.systemCheckInRewardDao.getSystemCheckInReward(groupId);
		for (SystemCheckInReward systemCheckInReward : systemCheckInRewardList) {
			SystemCheckInRewardBO systemCheckInRewardBO = new SystemCheckInRewardBO();
			systemCheckInRewardBO.setDay(systemCheckInReward.getDay());
			systemCheckInRewardBO.setGroupId(systemCheckInReward.getGroupId());
			systemCheckInRewardBO.setImgId(systemCheckInReward.getImgId());
			systemCheckInRewardBO.setToolId(systemCheckInReward.getToolId());
			systemCheckInRewardBO.setToolNum(systemCheckInReward.getToolNum());
			systemCheckInRewardBO.setToolType(systemCheckInReward.getToolType());

			systemCheckInRewardBOList.add(systemCheckInRewardBO);
		}

		return systemCheckInRewardBOList;
	}

	@Override
	public CommonDropBO receiveLoginReward(String userId, int loginDay) {

		if (loginDay > LOGIN_REWARD_MAX_DAY || loginDay < LOGIN_REWARD_MIN_DAY) {
			String message = "用户领取30天登入奖励失败,传入的参数不对.userId[" + userId + "], 第[" + loginDay + "] 天";
			throw new ServiceException(RECIVE_LOGIN_REWARD_HAS_WRONG_PARAM, message);
		}

		UserLoginRewardInfo userLoginRewardInfo = this.userLoginRewardInfoDao.getUserLoginRewardInfoByDay(userId, loginDay);

		if (userLoginRewardInfo == null) {
			String message = "用户领取30天登入奖励失败,没有达到领取条件.userId[" + userId + "], 第[" + loginDay + "] 天";
			throw new ServiceException(RECIVE_NO_LOGIN_INFO, message);
		}

		// 已领取
		if (userLoginRewardInfo.getRewardStatus() != LOGIN_REWARD_NOT_GET) {
			String message = "用户领取30天登入奖励失败,该天的礼包已领取.userId[" + userId + "], 第[" + loginDay + "] 天";
			throw new ServiceException(RECIVE_LOGIN_REWARD_HAS_RECEIVE, message);
		}
		// 更改领取日志
		int rewardStatus = LOGIN_REWARD_HAS_GET; // 设置为已领取
		this.userLoginRewardInfoDao.updateUserLoginRewardInfoByDay(userId, loginDay, DateUtils.getDate(), rewardStatus);

		SystemLoginReward systemLoginReward = this.systemLoginRewardDao.getSystemLoginRewardByDay(loginDay);
		if (systemLoginReward == null) {
			String message = "获取奖励信息出错.userId[" + userId + "], day[" + loginDay + "]";
			throw new ServiceException(GET_SYSTEM_LOGIN_REWARD_INFO_HAS_WRONG, message);
		}

		List<DropToolBO> dropToolBOList = DropToolHelper.parseDropTool(systemLoginReward.getToolIds());

		CommonDropBO commonDropBO = new CommonDropBO();
		for (DropToolBO dropToolBO : dropToolBOList) {
			int toolType = dropToolBO.getToolType();
			int toolId = dropToolBO.getToolId();
			int toolNum = dropToolBO.getToolNum();

			List<DropToolBO> dropBOList = toolService.giveTools(userId, toolType, toolId, toolNum, ToolUseType.ADD_30_LOGIN_REWARD);

			for (DropToolBO dropBO : dropBOList) {
				this.toolService.appendToDropBO(userId, commonDropBO, dropBO);
			}
		}

		return commonDropBO;

	}

	@Override
	public int checkLoginRewardHasGiven(String userID) {

		int gulri = LOGIN_REWARD_HAS_NOT_GIVEN; // 默认没有可领取的

		List<UserLoginRewardInfoBO> userLoginRewardInfoBOList = this.getUserLoginRewardInfo(userID);

		for (UserLoginRewardInfoBO userLoginRewardInfoBO : userLoginRewardInfoBOList) {
			// 有可领取的
			if (userLoginRewardInfoBO.getRewardStatus() == LOGIN_REWARD_NOT_GET) {
				gulri = LOGIN_REWARD_NOT_GIVEN;
				break;
			}
			// 三十天都领完了
			if (userLoginRewardInfoBO.getDay() == LOGIN_REWARD_MAX_DAY) {
				gulri = LOGIN_REWARD_BEYOND_MAX_DAY;
				break;
			}
		}
		return gulri;
	}

	@Override
	public List<UserLoginRewardInfoBO> getUserLoginRewardInfo(String userId) {

		List<UserLoginRewardInfoBO> UserLoginRewardInfoBOList = new ArrayList<UserLoginRewardInfoBO>();
		List<UserLoginRewardInfo> userLoginRewardInfoList = this.userLoginRewardInfoDao.getUserLoginRewardInfo(userId);
		for (UserLoginRewardInfo userLoginRewardInfo : userLoginRewardInfoList) {
			UserLoginRewardInfoBO userLoginRewardInfoBO = new UserLoginRewardInfoBO();
			userLoginRewardInfoBO.setDay(userLoginRewardInfo.getDay());
			userLoginRewardInfoBO.setRewardStatus(userLoginRewardInfo.getRewardStatus());
			UserLoginRewardInfoBOList.add(userLoginRewardInfoBO);
		}

		return UserLoginRewardInfoBOList;

	}

	@Override
	public void checkUserLoginRewardInfo(String userId) {

		// 获取最后一次登录信息
		UserLoginRewardInfo lastUserLoginRewardInfo = this.userLoginRewardInfoDao.getUserLastLoginRewardInfo(userId);

		// 第一次登录
		if (lastUserLoginRewardInfo == null) {
			UserLoginRewardInfo newUserLoginRewardInfo = new UserLoginRewardInfo();
			newUserLoginRewardInfo.setCreatedTime(new Date());
			newUserLoginRewardInfo.setRewardStatus(LOGIN_REWARD_NOT_GET);
			newUserLoginRewardInfo.setUserId(userId);
			newUserLoginRewardInfo.setDay(LOGIN_REWARD_MIN_DAY);
			this.userLoginRewardInfoDao.addUserLoginRewardInfo(newUserLoginRewardInfo);
		} else if (!StringUtils.equals(DateUtils.getDate(), DateUtils.getDate(lastUserLoginRewardInfo.getCreatedTime())) && lastUserLoginRewardInfo.getDay() < LOGIN_REWARD_MAX_DAY) {// 当天还没有加入数据库

			int day = lastUserLoginRewardInfo.getDay() + 1;

			if (userLoginRewardInfoDao.getUserLoginRewardInfoByDay(userId, day) == null) {// 容错
				UserLoginRewardInfo newUserLoginRewardInfo = new UserLoginRewardInfo();
				newUserLoginRewardInfo.setCreatedTime(new Date());
				newUserLoginRewardInfo.setRewardStatus(LOGIN_REWARD_NOT_GET);
				newUserLoginRewardInfo.setUserId(userId);
				newUserLoginRewardInfo.setDay(day);
				this.userLoginRewardInfoDao.addUserLoginRewardInfo(newUserLoginRewardInfo);
			}
		}

	}

	@Override
	public List<System30LoginRewardBO> get30LoginRewardGift() {

		List<SystemLoginReward> systemLoginRewardList = this.systemLoginRewardDao.getSystemLoginReward();

		List<System30LoginRewardBO> system30LoginRewardList = new ArrayList<System30LoginRewardBO>();
		for (SystemLoginReward systemLoginReward : systemLoginRewardList) {

			System30LoginRewardBO system30LoginRewardBO = new System30LoginRewardBO();

			system30LoginRewardBO.setDay(systemLoginReward.getDay());
			List<DropToolBO> dropToolBOList = DropToolHelper.parseDropTool(systemLoginReward.getToolIds());
			system30LoginRewardBO.setDropToolBOList(dropToolBOList);
			system30LoginRewardList.add(system30LoginRewardBO);
		}

		return system30LoginRewardList;
	}

	@Override
	public UserHeroBO heroExchange(String userId, int exchangeHeroId, EventHandle handle) {

		SystemHeroExchange systemHeroExchange = this.systemHeroExchangeDao.get(exchangeHeroId);

		if (systemHeroExchange == null) {
			String message = "武将兑换出错，兑换配置不存在.exchangeHeroId[" + exchangeHeroId + "]";
			logger.warn(message);
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		int toolId = systemHeroExchange.getToolId();
		int toolNum = systemHeroExchange.getToolNum();
		int systemHeroId = systemHeroExchange.getSystemHeroId();

		boolean success = this.toolService.reduceTool(userId, ToolType.HERO_SHARD, toolId, toolNum, ToolUseType.REDUCE_EXCHANGE_HERO);
		if (!success) {
			String message = "武将兑换出错，材料不足.exchangeHeroId[" + exchangeHeroId + "], toolId[" + toolId + "], toolNum[" + toolNum + "]";
			logger.warn(message);
			throw new ServiceException(EXCHANGE_HERO_TOOL_NOT_ENOUGH, message);
		}

		ToolUpdateEvent toolUpdateEvent = new ToolUpdateEvent(userId);
		handle.handle(toolUpdateEvent);

		String userHeroId = IDGenerator.getID();

		// 给武将
		this.heroService.addUserHero(userId, userHeroId, systemHeroId, 0, ToolUseType.ADD_EXCHANGE_HERO_GAIN_HERO);

		UserHeroBO userHeroBO = this.heroService.getUserHeroBO(userId, userHeroId);

		return userHeroBO;
	}

	@Override
	public List<SystemHeroExchangeBO> getSystemHeroExchangeBOList(int week) {

		List<SystemHeroExchange> systemHeroExchangeList = this.systemHeroExchangeDao.getList(week);

		if (systemHeroExchangeList.isEmpty()) {
			systemHeroExchangeList = this.systemHeroExchangeDao.getList(1);
		}

		List<SystemHeroExchangeBO> systemHeroExchangeBOList = new ArrayList<SystemHeroExchangeBO>();

		for (SystemHeroExchange systemHeroExchange : systemHeroExchangeList) {
			SystemHeroExchangeBO systemHeroExchangeBO = new SystemHeroExchangeBO();

			// 从 system_hero_attr
			// 中读取系统武将的血（life）、攻击（pyhsical_attack）和防御（pyhsical_defense）
			SystemHeroAttr systemHeroAttr = systemHeroAttrDao.getHeroAttr(systemHeroExchange.getSystemHeroId(), 1);
			systemHeroExchangeBO.setLife(systemHeroAttr.getLife());
			systemHeroExchangeBO.setPhysicalAttack(systemHeroAttr.getPhysicalAttack());
			systemHeroExchangeBO.setPhysicalDefense(systemHeroAttr.getPhysicalDefense());

			systemHeroExchangeBO.setSystemHeroExchangeId(systemHeroExchange.getSystemHeroExchangeId());
			systemHeroExchangeBO.setSystemHeroId(systemHeroExchange.getSystemHeroId());
			systemHeroExchangeBO.setToolId(systemHeroExchange.getToolId());
			systemHeroExchangeBO.setToolNum(systemHeroExchange.getToolNum());
			systemHeroExchangeBO.setWeek(systemHeroExchange.getWeek());

			systemHeroExchangeBOList.add(systemHeroExchangeBO);
		}

		return systemHeroExchangeBOList;

	}

	@Override
	public CommonDropBO receiveVipGiftBag(String userId, EventHandle handle) {

		int vipLevel = 0;

		User user = this.userService.get(userId);
		vipLevel = user.getVipLevel();

		if (vipLevel == 0) {// vip等级不足
			String message = "领取VIP礼包失败，不是vip.userId[" + userId + "], vipLevel[" + vipLevel + "]";
			logger.warn(message);
			throw new ServiceException(RECEIVE_VIP_GIFTBAG_VIP_NOT_ENOUGH, message);
		}

		UserGiftbag userGiftbag = this.userGiftbagDao.getLast(userId, GiftBagType.VIP_GIFTBAG);
		if (userGiftbag != null && DateUtils.isSameDay(userGiftbag.getUpdatedTime(), new Date())) {
			String message = "领取VIP礼包失败，当天已经领取.userId[" + userId + "], lastReciveTime[" + userGiftbag.getUpdatedTime() + "]";
			logger.warn(message);
			throw new ServiceException(RECEIVE_VIP_GIFTBAG_HAS_RECEIVE, message);
		}

		SystemGiftbag systemGiftbag = this.systemGiftbagDao.getVipGiftbag(vipLevel);

		// 记录日志
		this.userGiftbagDao.addOrUpdateUserGiftbag(userId, systemGiftbag.getType(), systemGiftbag.getGiftbagId());

		// 给奖励
		return this.pickGiftBagReward(userId, systemGiftbag.getGiftbagId(), ToolUseType.ADD_VIP_GIFT_BAG);
	}

	@Override
	public CommonDropBO pickGiftBagReward(String userId, int giftbagId, int useType) {

		CommonDropBO commonDropBO = new CommonDropBO();

		List<GiftbagDropTool> giftBagDropToolList = this.systemGiftbagDao.getGiftbagDropToolList(giftbagId);
		if (giftBagDropToolList.isEmpty()) {
			logger.error("礼包掉落为空.giftbagId[" + giftbagId + "]");
		}
		for (GiftbagDropTool giftbagDropTool : giftBagDropToolList) {

			int toolType = giftbagDropTool.getToolType();
			int toolId = giftbagDropTool.getToolId();
			int toolNum = giftbagDropTool.getToolNum();

			List<DropToolBO> dropToolBOList = this.toolService.giveTools(userId, toolType, toolId, toolNum, useType);
			for (DropToolBO dropToolBO : dropToolBOList) {
				this.toolService.appendToDropBO(userId, commonDropBO, dropToolBO);
			}
		}

		return commonDropBO;

	}

	@Override
	public CommonDropBO receiveGiftCodeGiftBag(String userId, String code, EventHandle handle) {

		GiftCode giftCode = this.giftCodeDao.get(code);
		if (giftCode == null || giftCode.getFlag() != 0) {
			String message = "领取礼包码礼包出错.礼包码无效.userId[" + userId + "], code[" + code + "]";
			logger.warn(message);
			throw new ServiceException(RECEIVE_GIFT_CODE_GIFTBAG_CODE_ERROR, message);
		} else {
			// 指定服务器
			String serverIds = giftCode.getServerIds();
			if (serverIds != null && !serverIds.equalsIgnoreCase("all")) {

				UserMapper userMapper = this.userMapperDao.get(userId);
				if (serverIds.indexOf(userMapper.getServerId()) == -1) {
					String message = "领取礼包码礼包出错.礼包码对该服无效.userId[" + userId + "], code[" + code + "], serverIds[" + serverIds + "], serverId[" + userMapper.getServerId() + "]";
					logger.warn(message);
					throw new ServiceException(RECEIVE_GIFT_CODE_GIFTBAG_CODE_ERROR, message);
				}

			}
		}

		int timesLimit = giftCode.getTimesLimit();
		if (timesLimit == 0) {
			timesLimit = 1;
		}

		UserGiftbag userGiftbag = this.userGiftbagDao.getLast(userId, giftCode.getGiftBagType());
		if (userGiftbag != null) {
			if (userGiftbag.getTotalNum() >= timesLimit) {
				String message = "领取激活码礼包失败，已经领取过.userId[" + userId + "]";
				logger.warn(message);
				throw new ServiceException(RECEIVE_GIFT_CODE_GIFTBAG_HAS_RECEIVE, message);
			}
		}

		// 更新为已经使用
		if (!this.giftCodeDao.update(code, userId)) {
			String message = "领取礼包码礼包出错.礼包码无效.userId[" + userId + "], code[" + code + "]";
			logger.warn(message);
			throw new ServiceException(RECEIVE_GIFT_CODE_GIFTBAG_CODE_ERROR, message);
		}

		SystemGiftbag systemGiftbag = this.systemGiftbagDao.getCodeGiftBag(giftCode.getType(), giftCode.getGiftBagType());

		// 记录日志
		this.userGiftbagDao.addOrUpdateUserGiftbag(userId, systemGiftbag.getType(), systemGiftbag.getGiftbagId());

		return this.pickGiftBagReward(userId, systemGiftbag.getGiftbagId(), ToolUseType.ADD_GIFTKEY_GIFT_BAG);
	}

	@Override
	public CommonDropBO receiveFirstPayGiftBag(String userId, EventHandle handle) {

		int payAmount = paymentLogDao.getPaymentTotalGold(userId);
		if (payAmount <= 0) {
			String message = "领取首充礼包失败，没有充过值.userId[" + userId + "], payAmount[" + payAmount + "]";
			logger.warn(message);
			throw new ServiceException(RECEIVE_FIRST_PAY_GIFTBAG_VIP_NOT_ENOUGH, message);
		}

		UserGiftbag userGiftbag = this.userGiftbagDao.getLast(userId, GiftBagType.FIRST_PAY_GIFTBAG);
		if (userGiftbag != null) {
			String message = "领取首充礼包失败，已经领取过.userId[" + userId + "]";
			logger.warn(message);
			throw new ServiceException(RECEIVE_VIP_GIFTBAG_HAS_RECEIVE, message);
		}

		SystemGiftbag systemGiftbag = this.systemGiftbagDao.getFirstPayGiftbag();

		// 记录日志
		this.userGiftbagDao.addOrUpdateUserGiftbag(userId, systemGiftbag.getType(), systemGiftbag.getGiftbagId());

		return this.pickGiftBagReward(userId, systemGiftbag.getGiftbagId(), ToolUseType.ADD_FIRST_PAY_GIFT_BAG);
	}

	@Override
	public CommonDropBO receiveRookieGuideGiftBag(String userId, int giftBagId, EventHandle handle) {
		int count = this.userGiftbagDao.getCount(userId, GiftBagType.ROOKIE_GUIDE_GIFTBAT, giftBagId);
		if (count >= 1) {
			String message = "领取新手引导礼包失败，已经领取过.userId[" + userId + "].giftBatId[" + giftBagId + "]";
			logger.warn(message);
			throw new ServiceException(PAY_REWARD_LIMIT_ERROR, message);
		}

		// 记录日志
		this.userGiftbagDao.addOrUpdateUserGiftbag(userId, GiftBagType.ROOKIE_GUIDE_GIFTBAT, giftBagId);

		return this.pickGiftBagReward(userId, giftBagId, ToolUseType.ADD_ROOKIE_GUIDE_GIFT_BAG);
	}

	@Override
	public int getGiftBagStatus(String userId, int type) {

		// 礼包状态(1 可以领取 2 已经领取 3 不是VIP 4 没有充过值)

		int status = 1;

		if (type == GiftBagType.VIP_GIFTBAG) {

			UserBO userBo = this.userService.getUserBO(userId);
			if (userBo.getVipLevel() == 0) {
				return 3;
			}

			UserGiftbag userGiftbag = this.userGiftbagDao.getLast(userId, type);
			if (userGiftbag != null && DateUtils.isSameDay(userGiftbag.getUpdatedTime(), new Date())) {
				return 2;
			}

		} else if (type == GiftBagType.FIRST_PAY_GIFTBAG) {
			int payAmount = this.paymentLogDao.getPaymentTotalGold(userId);
			if (payAmount == 0) {
				return 4;
			}

			UserGiftbag userGiftbag = this.userGiftbagDao.getLast(userId, type);
			if (userGiftbag != null) {
				return 2;
			}

		}

		return status;
	}

	@Override
	public boolean refreshHeroExchange(String userId) {

		UserHeroExchange userHeroExchange = this.userHeroExchangeDao.get(userId);

		int needMoney = 20;
		int times = 0;
		Date now = new Date();
		if (userHeroExchange != null && DateUtils.isSameDay(userHeroExchange.getUpdatedTime(), now)) {
			times = userHeroExchange.getTimes();
		}

		needMoney = needMoney + times * 5;
		User user = userService.get(userId);
		if (!this.userService.reduceGold(userId, needMoney, ToolUseType.REDUCE_REFRESH_HERO_EXCHANGE, user.getLevel())) {
			String message = "刷新可兑换武将列表出错，用户金币不足.userId[" + userId + "]";
			logger.warn(message);
			throw new ServiceException(REFRESH_HERO_EXCHANGE_GOLD_NOT_ENOUGH, message);
		}

		int week = DateUtils.getWeekOfYear();
		int userWeek = 0;
		while (true) {
			userWeek = 1 + RandomUtils.nextInt(54);
			if (userWeek != week && (userHeroExchange == null || userHeroExchange.getUserWeek() != userWeek)) {
				break;
			}
		}
		if (userHeroExchange == null) {
			userHeroExchange = new UserHeroExchange();
			userHeroExchange.setUserId(userId);
			userHeroExchange.setUserWeek(userWeek);
			userHeroExchange.setSystemWeek(week);
			userHeroExchange.setCreatedTime(now);
			userHeroExchange.setUpdatedTime(now);
			userHeroExchange.setTimes(1);
			this.userHeroExchangeDao.add(userHeroExchange);
		} else {
			this.userHeroExchangeDao.updateUserHeroExchange(userId, userWeek, week, times + 1);
		}

		return true;

	}

	@Override
	public CommonDropBO receiveOnlineReward(String userId) {

		UserOnlineRewardBO userOnlineRewardBO = this.getUserOnlineRewardBO(userId);
		if (userOnlineRewardBO.getSubType() == 0) {// 已经领取完
			String message = "领取礼包出错，没有可领取的礼包.userId[" + userId + "]";
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		if (userOnlineRewardBO.getTime() > System.currentTimeMillis()) {// 时间未到
			String message = "领取礼包出错，时间未到.userId[" + userId + "]";
			throw new ServiceException(RECIVE_ONLINE_REWARD_TIME_NOT_ENOUGH, message);
		}
		int subType = userOnlineRewardBO.getSubType();

		SystemGiftbag systemGiftbag = this.systemGiftbagDao.getOnlineGiftBag(subType);

		this.userOnlineRewardDao.update(userId, subType);

		return this.pickGiftBagReward(userId, systemGiftbag.getGiftbagId(), ToolUseType.ADD_ONLINE_GIFT_BAG);

	}

	@Override
	public UserOnlineRewardBO getUserOnlineRewardBO(String userId) {

		UserOnlineRewardBO userOnlineRewardBO = new UserOnlineRewardBO();

		int subType = 0;

		long userOnline = 0;
		long userLastOnline = 0;
		Date now = new Date();

		UserOnlineLog userOnlineLog = this.userOnlineLogDao.getLastOnlineLog(userId);

		UserOnlineReward userOnlineReward = this.userOnlineRewardDao.get(userId);
		if (userOnlineReward == null) {
			userOnlineReward = new UserOnlineReward();
			userOnlineReward.setSubType(0);
			userOnlineReward.setCreatedTime(now);
			userOnlineReward.setUpdatedTime(now);
			userOnlineReward.setUserId(userId);
			this.userOnlineRewardDao.add(userOnlineReward);
		}

		subType = userOnlineReward.getSubType() + 1;
		if (subType <= 7) {
			userOnline = this.userOnlineLogDao.getUserOnline(userId, userOnlineReward.getUpdatedTime()) * 1000;
			if (userOnlineLog == null || userOnlineReward.getUpdatedTime().after(userOnlineLog.getLoginTime())) {// 用户最后领取礼包的时间比最后登录的时间晚
				// 用最后领的时间来算
				userLastOnline = now.getTime() - userOnlineReward.getUpdatedTime().getTime();
			} else {
				// 用最后登录的时间来算
				userLastOnline = now.getTime() - userOnlineLog.getLoginTime().getTime();
			}
		} else {
			subType = 0;
		}

		userOnlineRewardBO.setSubType(subType);

		if (subType != 0) {

			long userTotalOnline = userOnline + userLastOnline;
			long needTotalOnline = UserOnlineRewardHelper.getOnlineRewaradTime(subType);
			long subTime = needTotalOnline - userTotalOnline;
			if (subTime < 0) {
				subTime = 0;
			}

			userOnlineRewardBO.setTime(System.currentTimeMillis() + subTime);
			userOnlineRewardBO.setMinute((int) (needTotalOnline / 60 / 1000));
		}

		return userOnlineRewardBO;

	}

	@Override
	public List<UserPayRewardBO> getUserOncePayRewardList(String userId) {
		List<UserPayRewardBO> ret = new ArrayList<UserPayRewardBO>();
		// 获得活动期间用户的订单以及已经领取过的订单记录
		SystemActivity systemActivity = systemActivityDao.get(ActivityId.ONCE_PAY_REWARD_ID);
		try {
			checkActivityIsOpen(systemActivity);
		} catch (ServiceException e) {
			logger.debug("非活动期间，无需返回");
			return null;
		}

		List<SystemOncePayReward> systemOncePayRewards = systemOncePayRewardDao.getAll();

		for (SystemOncePayReward reward : systemOncePayRewards) {
			UserPayRewardBO bo = getUserOncePayReward(userId, reward, systemActivity);
			ret.add(bo);
		}

		return ret;
	}

	@Override
	public UserPayRewardBO getUserOncePayRewardById(String userId, int rid) {
		SystemOncePayReward reward = this.systemOncePayRewardDao.getById(rid);
		SystemActivity systemActivity = systemActivityDao.get(ActivityId.ONCE_PAY_REWARD_ID);

		return getUserOncePayReward(userId, reward, systemActivity);
	}

	private UserPayRewardBO getUserOncePayReward(String userId, SystemOncePayReward reward, SystemActivity systemActivity) {

		UserPayRewardBO bo = new UserPayRewardBO();
		bo.setRewardId(reward.getId());
		bo.setPayMoney(reward.getPayMoney());

		// SystemGiftbag systemGiftbag =
		// this.systemGiftbagDao.getOncePayReward(ActivityId.ONCE_PAY_REWARD_ID,
		// reward.getId());
		//
		// bo.setDropToolBoList(this.getDropToolBOByGiftbagId(systemGiftbag.getGiftbagId()));
		bo.setDropToolBoList(DropToolHelper.parseDropTool(reward.getDropToolIds()));
		bo.setTimesLimit(reward.getTimesLimit());

		SystemOncePayReward nextReward = systemOncePayRewardDao.getNextById(reward.getId());

		List<PaymentLog> paymentLogs = getPaymentLogsByTimeAndMoney(userId, systemActivity, reward, nextReward);

		if (paymentLogs == null || paymentLogs.isEmpty()) {
			bo.setStatus(UserPayRewardStatus.CANNOT_RECEIVE);
			return bo;
		}

		// 取出已经领取过的订单ID
		List<UserPayReward> receivedOrderList = userPayRewardDao.getReceivedOrderIdList(userId, ActivityId.ONCE_PAY_REWARD_ID, reward.getId(), systemActivity.getStartTime(),
				systemActivity.getEndTime());

		logger.info(" ***** reward.id[" + reward.getId() + "]");
		logger.info(" ***** paymentLogs size [" + paymentLogs.size() + "]");
		logger.info(" ***** receivedOrderList size [" + receivedOrderList.size() + "]");

		// 如果领取次数已经超出限制，则表示已领取过，否则则表示可领取
		if (receivedOrderList != null && reward.getTimesLimit() <= receivedOrderList.size()) {
			bo.setTimes(receivedOrderList.size());
			bo.setStatus(UserPayRewardStatus.RECEIVED);
		} else {

			for (PaymentLog log : paymentLogs) {
				if (!contaninOrder(receivedOrderList, log.getOrderId())) {
					bo.setTimes(receivedOrderList.size());
					bo.setStatus(UserPayRewardStatus.CAN_RECEIVE);
					break;
				}
				bo.setTimes(receivedOrderList.size());
				bo.setStatus(UserPayRewardStatus.CANNOT_RECEIVE);
			}
		}

		return bo;
	}

	@Override
	public List<UserPayRewardBO> getUserTotalPayRewardList(String userId) {
		List<UserPayRewardBO> ret = new ArrayList<UserPayRewardBO>();

		SystemActivity systemActivity = systemActivityDao.get(ActivityId.TOTAL_PAY_REWARD_ID);
		try {
			checkActivityIsOpen(systemActivity);
		} catch (ServiceException e) {
			logger.debug("非活动期间，无需返回");
			return null;
		}

		List<SystemTotalPayReward> systemTotalPayRewards = systemTotalPayRewardDao.getAll();
		for (SystemTotalPayReward reward : systemTotalPayRewards) {
			UserPayRewardBO bo = getUserTotalPayReward(userId, reward, systemActivity);
			ret.add(bo);
		}
		return ret;
	}

	@Override
	public UserPayRewardBO getUserTotalPayRewardById(String userId, int rid) {
		SystemTotalPayReward reward = this.systemTotalPayRewardDao.getById(rid);
		SystemActivity systemActivity = systemActivityDao.get(ActivityId.TOTAL_PAY_REWARD_ID);

		return getUserTotalPayReward(userId, reward, systemActivity);
	}

	private UserPayRewardBO getUserTotalPayReward(String userId, SystemTotalPayReward reward, SystemActivity systemActivity) {

		UserPayRewardBO bo = new UserPayRewardBO();
		bo.setRewardId(reward.getId());
		bo.setPayMoney(reward.getPayMoney());
		bo.setTimesLimit(reward.getTimesLimit());

		// SystemGiftbag systemGiftbag =
		// this.systemGiftbagDao.getOncePayReward(ActivityId.TOTAL_PAY_REWARD_ID,
		// reward.getId());
		//
		// bo.setDropToolBoList(this.getDropToolBOByGiftbagId(systemGiftbag.getGiftbagId()));

		bo.setDropToolBoList(DropToolHelper.parseDropTool(reward.getDropToolIds()));
		bo.setTimesLimit(reward.getTimesLimit());
		bo.setTimesLimit(reward.getTimesLimit());

		// 查看充值总金额是否达到领取要求
		int totalPay = paymentLogDao.getPaymentTotalByTime(userId, systemActivity.getStartTime(), systemActivity.getEndTime());
		if (totalPay < reward.getPayMoney()) {
			bo.setStatus(UserPayRewardStatus.CANNOT_RECEIVE);
		} else {
			// 查看是否已经领取
			UserPayReward userPayReward = userPayRewardDao.getUserPayReward(userId, ActivityId.TOTAL_PAY_REWARD_ID, reward.getId(), systemActivity.getStartTime(), systemActivity.getEndTime());
			if (userPayReward != null) {
				bo.setTimes(1);
				bo.setStatus(UserPayRewardStatus.RECEIVED);
			} else {
				bo.setStatus(UserPayRewardStatus.CAN_RECEIVE);
			}
		}

		return bo;
	}

	/**
	 * 根据时间以及金额金额区间获取订单
	 * 
	 * @param userId
	 * @param systemActivity
	 * @param reward
	 * @param nextReward
	 * @return
	 */
	private List<PaymentLog> getPaymentLogsByTimeAndMoney(String userId, SystemActivity systemActivity, SystemOncePayReward reward, SystemOncePayReward nextReward) {
		List<PaymentLog> paymentLogs = null;
		if (nextReward != null) {
			if (reward.getPayMoney() < nextReward.getPayMoney()) {
				paymentLogs = paymentLogDao.getPaymenList(userId, systemActivity.getStartTime(), systemActivity.getEndTime(), reward.getPayMoney(), nextReward.getPayMoney());
			} else {
				paymentLogs = paymentLogDao.getPaymenList(userId, systemActivity.getStartTime(), systemActivity.getEndTime(), reward.getPayMoney());
			}
		} else {
			paymentLogs = paymentLogDao.getPaymenList(userId, systemActivity.getStartTime(), systemActivity.getEndTime(), reward.getPayMoney());

		}
		return paymentLogs;
	}
	
	/**
	 * 根据时间以及金额金额区间获取订单
	 * 
	 * @param userId
	 * @param systemActivity
	 * @param reward
	 * @param nextReward
	 * @return
	 */
	private List<PaymentLog> getPaymentLogsByTimeAndGold(String userId, SystemActivity systemActivity, SystemOncePayReward reward, SystemOncePayReward nextReward) {
		List<PaymentLog> paymentLogs = null;
		if (nextReward != null) {
			if (reward.getPayMoney() < nextReward.getPayMoney()) {
				paymentLogs = paymentLogDao.getPaymenListByGold(userId, systemActivity.getStartTime(), systemActivity.getEndTime(), reward.getPayMoney(), nextReward.getPayMoney());
			} else {
				paymentLogs = paymentLogDao.getPaymenListByGold(userId, systemActivity.getStartTime(), systemActivity.getEndTime(), reward.getPayMoney());
			}
		} else {
			paymentLogs = paymentLogDao.getPaymenListByGold(userId, systemActivity.getStartTime(), systemActivity.getEndTime(), reward.getPayMoney());

		}
		return paymentLogs;
	}

	@Override
	public CommonDropBO receiveOncePayReward(String userId, int rid) {
		// 获取活动
		SystemActivity systemActivity = systemActivityDao.get(ActivityId.ONCE_PAY_REWARD_ID);
		// 判断是否活动时间
		checkActivityIsOpen(systemActivity);
		// 获取领取的奖励信息
		SystemOncePayReward reward = systemOncePayRewardDao.getById(rid);

		SystemOncePayReward nextReward = systemOncePayRewardDao.getNextById(rid);

		// SystemGiftbag systemGiftbag =
		// systemGiftbagDao.getOncePayReward(ActivityId.ONCE_PAY_REWARD_ID,
		// rid);

		// 判断用户是否存在单笔充值满足的订单
		// 从数据库中取出
		List<PaymentLog> paymentLogs = getPaymentLogsByTimeAndMoney(userId, systemActivity, reward, nextReward);

		// 取出已经领取过的订单ID
		List<UserPayReward> receivedOrderList = userPayRewardDao.getReceivedOrderIdList(userId, ActivityId.ONCE_PAY_REWARD_ID, rid, systemActivity.getStartTime(), systemActivity.getEndTime());

		// 判断是否达到领取次数上限
		if (receivedOrderList != null) {
			checkReceivedPayRewardLimit(reward, receivedOrderList);
		} else {
			receivedOrderList = new ArrayList<UserPayReward>();
		}
		// 取出一个符合条件的订单，发放奖励并保存记录
		PaymentLog paymentLog = null;
		for (PaymentLog log : paymentLogs) {
			// 如果已领取订单中不包括当前订单号，则表示用户存在满足条件的订单
			if (!contaninOrder(receivedOrderList, log.getOrderId())) {
				paymentLog = log;
				break;
			}
		}

		if (paymentLog == null) {
			throw new ServiceException(ActivityService.PAY_REWARD_MONEY_NOT_ENOUGH, "没有满足条件的充值");
		}

		// 保存记录
		saveUserPayReward(userId, ActivityId.ONCE_PAY_REWARD_ID, rid, paymentLog);

		List<DropToolBO> dropToolBOList = DropToolHelper.parseDropTool(reward.getDropToolIds());

		CommonDropBO commonDropBO = new CommonDropBO();
		for (DropToolBO dropToolBO : dropToolBOList) {
			int toolType = dropToolBO.getToolType();
			int toolId = dropToolBO.getToolId();
			int toolNum = dropToolBO.getToolNum();

			List<DropToolBO> dropBOList = toolService.giveTools(userId, toolType, toolId, toolNum, ToolUseType.ADD_ONCE_PAY_GIFT_BAG);

			for (DropToolBO dropBO : dropBOList) {
				this.toolService.appendToDropBO(userId, commonDropBO, dropBO);
			}
		}

		return commonDropBO;

	}

	private boolean contaninOrder(List<UserPayReward> receivedOrderList, String orderId) {
		for (UserPayReward reward : receivedOrderList) {
			if (reward.getOrderId().equals(orderId)) {
				return true;
			}
		}
		return false;
	}

	private void checkReceivedPayRewardLimit(SystemOncePayReward reward, List<UserPayReward> receivedOrderList) {
		if (reward.getTimesLimit() <= receivedOrderList.size()) {
			throw new ServiceException(PAY_REWARD_LIMIT_ERROR, "领取次数超出活动限制");
		}
	}

	private void saveUserPayReward(String userId, int aid, int rid, PaymentLog paymentLog) {
		Date now = new Date();
		UserPayReward userPayReward = new UserPayReward();
		userPayReward.setUserId(userId);
		userPayReward.setActivityId(aid);
		userPayReward.setRewardId(rid);
		userPayReward.setCreatedTime(now);
		userPayReward.setUpdatedTime(now);

		if (paymentLog != null) {
			userPayReward.setOrderId(paymentLog.getOrderId());
		}

		userPayRewardDao.add(userPayReward);
	}

	/**
	 * 检测活动是否开放
	 * 
	 * @param systemActivity
	 */
	private void checkActivityIsOpen(SystemActivity systemActivity) {
		Date now = new Date();
		if (systemActivity == null || now.after(systemActivity.getEndTime()) || now.before(systemActivity.getStartTime())) {
			throw new ServiceException(ACTIVITY_IS_CLOSED, "充值活动已停止");
		}
	}

	@Override
	public CommonDropBO receiveTotalPayReward(String userId, int rid) {
		// 获取活动
		SystemActivity systemActivity = systemActivityDao.get(ActivityId.TOTAL_PAY_REWARD_ID);
		// 判断是否活动时间
		checkActivityIsOpen(systemActivity);
		// 获取领取的奖励信息
		SystemTotalPayReward reward = systemTotalPayRewardDao.getById(rid);
		// SystemGiftbag systemGiftbag =
		// systemGiftbagDao.getTotalPayReward(ActivityId.TOTAL_PAY_REWARD_ID,
		// rid);
		// 判断充值金额是否足够（默认每种奖励只能领取一次）
		int totalPay = paymentLogDao.getPaymentTotalByTime(userId, systemActivity.getStartTime(), systemActivity.getEndTime());
		if (totalPay < reward.getPayMoney()) {
			throw new ServiceException(PAY_REWARD_MONEY_NOT_ENOUGH, "充值金额不足");
		}

		UserPayReward userPayReward = userPayRewardDao.getUserPayReward(userId, ActivityId.TOTAL_PAY_REWARD_ID, rid, systemActivity.getStartTime(), systemActivity.getEndTime());
		if (userPayReward != null) {
			throw new ServiceException(PAY_REWARD_LIMIT_ERROR, "领取次数超出活动限制");
		}

		saveUserPayReward(userId, ActivityId.TOTAL_PAY_REWARD_ID, rid, null);

		List<DropToolBO> dropToolBOList = DropToolHelper.parseDropTool(reward.getDropToolIds());

		CommonDropBO commonDropBO = new CommonDropBO();
		for (DropToolBO dropToolBO : dropToolBOList) {
			int toolType = dropToolBO.getToolType();
			int toolId = dropToolBO.getToolId();
			int toolNum = dropToolBO.getToolNum();

			List<DropToolBO> dropBOList = toolService.giveTools(userId, toolType, toolId, toolNum, ToolUseType.ADD_TOTAL_PAY_GIFT_BAG);

			for (DropToolBO dropBO : dropBOList) {
				this.toolService.appendToDropBO(userId, commonDropBO, dropBO);
			}
		}

		return commonDropBO;

	}

	@Override
	public Map<String, Object> toolExchange(String userId, int toolExchangeId, int num) {
		// 检测是否超出兑换次数

		if (!this.isActivityOpen(ActivityId.TOOL_EXCHANGE_ID)) {
			throw new ServiceException(ServiceReturnCode.ACTIVITY_NOT_OPEN_EXCTPION, "活动未开放");
		}

		SystemActivity activity = systemActivityDao.get(ActivityId.TOOL_EXCHANGE_ID);

		int userTimes = toolExchangeDao.getUserTimes(userId, toolExchangeId, activity.getStartTime(), activity.getEndTime());
		int times = toolExchangeDao.getTimes(toolExchangeId);
		if (userTimes >= times) {
			throw new ServiceException(REACH_TOOL_EXCHANGE_LIMIT, "达到兑换次数上限");
		}

		Map<String, Object> rtMap = new HashMap<String, Object>();
		ToolExchange toolExchange = toolExchangeDao.getExchangeItems(toolExchangeId);

		List<DropToolBO> preExchangeItems = DropToolHelper.parseDropTool(toolExchange.getPreExchangeItems());
		List<DropToolBO> postExchangeItems = DropToolHelper.parseDropTool(toolExchange.getPostExchangeItems());

		ToolExchangeLooseBO looseBO = createToolExchangeLooseBO(userId, preExchangeItems, num);
		reduceToolExchange(userId, looseBO);
		saveExchangeCount(userId, toolExchangeId);
		ToolExchangeReceiveBO receiveBO = createToolExchangeReceiveBO(userId, postExchangeItems, num);

		rtMap.put("tr", receiveBO);
		rtMap.put("te", looseBO);

		return rtMap;
	}

	@Override
	public List<ToolExchangeCountBO> toolExchangeCount(String userId) {
		SystemActivity activity = systemActivityDao.get(ActivityId.TOOL_EXCHANGE_ID);
		List<ToolExchangeCountBO> boList = new ArrayList<ToolExchangeCountBO>();
		int num = toolExchangeDao.getNum();
		for (int i = 1; i <= num; i++) {
			ToolExchangeCountBO countBO = new ToolExchangeCountBO();
			countBO.setMax(toolExchangeDao.getTimes(i));
			countBO.setTimes(toolExchangeDao.getUserTimes(userId, i, activity.getStartTime(), activity.getEndTime()));
			countBO.setToolExchangeId(i);
			boList.add(countBO);
		}
		return boList;
	}

	/**
	 * 保存用户的物品兑换信息
	 * 
	 * @param userId
	 * @param toolExchangeId
	 */
	private void saveExchangeCount(String userId, int toolExchangeId) {
		SystemActivity activity = systemActivityDao.get(ActivityId.TOOL_EXCHANGE_ID);
		int userTimes = toolExchangeDao.getUserTimes(userId, toolExchangeId, activity.getStartTime(), activity.getEndTime());
		if (userTimes == 0) {
			UserToolExchangeLog userToolExchangeLog = new UserToolExchangeLog();
			Date now = new Date();
			userToolExchangeLog.setUserId(userId);
			userToolExchangeLog.setExchangeId(toolExchangeId);
			userToolExchangeLog.setTimes(1);
			userToolExchangeLog.setCreatedTime(now);
			userToolExchangeLog.setUpdateTime(now);
			toolExchangeDao.addExchangeCount(userToolExchangeLog);
		}
		toolExchangeDao.updateExchangeCount(userId, toolExchangeId, userTimes + 1);
	}

	/**
	 * 物品兑换-告诉客户端增加了什么物品
	 * 
	 * @param userId
	 * @param postExchangeItems
	 * @param num
	 * @return
	 */
	private ToolExchangeReceiveBO createToolExchangeReceiveBO(String userId, List<DropToolBO> postExchangeItems, int num) {

		ToolExchangeReceiveBO receiveBO = new ToolExchangeReceiveBO();
		User user = userService.get(userId);
		int toolUseType = ToolUseType.ADD_TOOL_EXCHANGE;

		for (DropToolBO postExchangeItem : postExchangeItems) {
			int toolType = postExchangeItem.getToolType();
			int toolId = postExchangeItem.getToolId();
			int toolNum = postExchangeItem.getToolNum();
			int exchangeNum = toolNum * num; // 兑换后得到的物品数量

			switch (toolType) {
			case ToolType.GOLD:
				userService.addGold(userId, exchangeNum, toolUseType, user.getLevel());
				receiveBO.setGold(exchangeNum);
				break;
			case ToolType.COPPER:
				userService.addCopper(userId, exchangeNum, toolUseType);
				receiveBO.setCopper(exchangeNum);
				break;
			case ToolType.POWER:
				userService.addPower(userId, exchangeNum, toolUseType, null);
				receiveBO.setPower(exchangeNum);
				break;
			case ToolType.EXP:
				userService.addExp(userId, exchangeNum, toolUseType);
				receiveBO.setExp(exchangeNum);
				break;
			case ToolType.HERO_BAG:
				userExtinfoDao.updateHeroMax(userId, exchangeNum);
				receiveBO.setHeroBag(exchangeNum);
				break;
			case ToolType.EQUIP_BAG:
				userExtinfoDao.updateEquipMax(userId, exchangeNum);
				receiveBO.setEquipBag(exchangeNum);
				break;
			case ToolType.HERO:
				List<DropToolBO> heroDropList = toolService.giveTools(userId, ToolType.HERO, toolId, exchangeNum, toolUseType);
				List<UserHeroBO> useHeroBoList = heroService.createUserHeroBOList(userId, heroDropList);
				receiveBO.setUserHeroBOList(useHeroBoList);
				break;
			case ToolType.EQUIP:
				List<DropToolBO> equipDropList = toolService.giveTools(userId, ToolType.EQUIP, toolId, exchangeNum, toolUseType);
				List<UserEquipBO> useEquipBoList = equipService.createUserEquipBOList(userId, equipDropList);
				receiveBO.setUserEquipBOList(useEquipBoList);
				break;
			case ToolType.MIND:
				userService.addMind(userId, exchangeNum, toolUseType);
				break;
			case ToolType.MATERIAL:
			case ToolType.GIFT_BOX:
			case ToolType.HERO_SHARD:
			case ToolType.SKILL_BOOK:
				toolService.giveTools(userId, toolType, toolId, exchangeNum, toolUseType);
				UserToolBO toolBo = new UserToolBO();
				toolBo.setToolId(toolId);
				toolBo.setToolNum(exchangeNum);
				receiveBO.getUserToolBOList().add(toolBo);
				break;
			default:
				break;
			}
		}
		return receiveBO;
	}

	/**
	 * 物品兑换-为用户删除要兑换的物品
	 */
	private void reduceToolExchange(String userId, ToolExchangeLooseBO looseBO) {
		User user = userService.get(userId);
		int toolUseType = ToolUseType.REDUCE_TOOL_EXCHANGE;

		if (looseBO.getGold() != 0) {
			userService.reduceGold(userId, looseBO.getGold(), toolUseType, user.getLevel());
		}
		if (looseBO.getCopper() != 0) {
			userService.reduceCopper(userId, looseBO.getCopper(), toolUseType);
		}
		if (looseBO.getPower() != 0) {
			userService.reducePower(userId, looseBO.getPower(), toolUseType);
		}
		if (looseBO.getExp() != 0) {
			userService.reduceExp(userId, looseBO.getExp(), toolUseType);
		}
		if (looseBO.getHeroBag() != 0) {
			userExtinfoDao.updateHeroMax(userId, -1 * looseBO.getHeroBag());
		}
		if (looseBO.getEquipBag() != 0) {
			userExtinfoDao.updateEquipMax(userId, -1 * looseBO.getEquipBag());
		}
		if (looseBO.getHeroBOList().size() != 0) {
			List<ToolExchangeHeroBO> heroBoList = looseBO.getHeroBOList();
			for (ToolExchangeHeroBO heroBo : heroBoList) {
				String userHeroId = heroBo.getUserHeroId();
				userHeroDao.delete(userId, userHeroId);
			}
		}
		if (looseBO.getEquipBOList().size() != 0) {
			List<ToolExchangeEquipBO> equipBoList = looseBO.getEquipBOList();
			for (ToolExchangeEquipBO equipBO : equipBoList) {
				userEquipDao.delete(userId, equipBO.getUserEquipId());
			}
		}
		if (looseBO.getToolBOList().size() != 0) {
			List<ToolExchangeToolBO> toolBoList = looseBO.getToolBOList();
			for (ToolExchangeToolBO toolBo : toolBoList) {
				userToolDao.reduceUserTool(userId, toolBo.getToolId(), toolBo.getExchangeNum());
			}
		}
	}

	/**
	 * 物品兑换-告诉客户端丢失了什么物品
	 * 
	 * @param userId
	 * @param exchangeItems
	 * @param num
	 * @return
	 */
	private ToolExchangeLooseBO createToolExchangeLooseBO(String userId, List<DropToolBO> preExchangeItems, int num) {

		ToolExchangeLooseBO looseBO = new ToolExchangeLooseBO();
		User user = userService.get(userId);

		for (DropToolBO preExchangeItem : preExchangeItems) {
			int toolType = preExchangeItem.getToolType();
			int toolId = preExchangeItem.getToolId();
			int toolNum = preExchangeItem.getToolNum();
			int exchangeNum = toolNum * num; // 兑换需要的物品数量

			switch (toolType) {
			case ToolType.GOLD:
				long goldNum = user.getGoldNum();
				if (goldNum < exchangeNum) {
					String message = "用户金币不足.userId[" + userId + "], gold_num[" + goldNum + "]";
					throw new ServiceException(TOOL_NOT_ENOUGH, message);
				} else {
					looseBO.setGold(exchangeNum); // 失去的金币数量
				}
				break;
			case ToolType.COPPER:
				long cooperNum = user.getCopper();
				if (cooperNum < exchangeNum) {
					String message = "用户银币不足.userId[" + userId + "], coopper[" + cooperNum + "]";
					throw new ServiceException(TOOL_NOT_ENOUGH, message);
				} else {
					looseBO.setCopper(exchangeNum); // 失去的银币数量
				}
				break;
			case ToolType.POWER:
				int power = user.getPower();
				if (power < exchangeNum) {
					String message = "用户体力不足.userId[" + userId + "], power[" + power + "]";
					throw new ServiceException(TOOL_NOT_ENOUGH, message);
				} else {
					looseBO.setPower(exchangeNum); // 失去的体力
				}
				break;
			case ToolType.EXP:
				long exp = user.getExp();
				if (exp < exchangeNum) {
					String message = "用户经验值不足.userId[" + userId + "], exp[" + exp + "]";
					throw new ServiceException(TOOL_NOT_ENOUGH, message);
				} else {
					looseBO.setExp(exchangeNum); // 失去的经验
				}
				break;
			case ToolType.HERO_BAG:
				// 查询武将背包上限
				int heroMax = userExtinfoDao.get(userId).getHeroMax();
				if (heroMax < exchangeNum) {
					String message = "武将背包空间不足.userId[" + userId + "], heroMax[" + heroMax + "]";
					throw new ServiceException(TOOL_NOT_ENOUGH, message);
				} else {
					looseBO.setHeroBag(exchangeNum);
				}
				break;
			case ToolType.EQUIP_BAG:
				// 查询装备背包
				int equipMax = userExtinfoDao.get(userId).getEquipMax();
				if (equipMax < exchangeNum) {
					String message = "装备背包不足.userId[" + userId + "], equipMax[" + equipMax + "]";
					throw new ServiceException(TOOL_NOT_ENOUGH, message);
				} else {
					looseBO.setEquipBag(exchangeNum);
				}
				break;
			case ToolType.HERO:
				looseBO.getHeroBOList().addAll(exchangeHero(userId, toolId, exchangeNum));
				break;
			case ToolType.EQUIP:
				looseBO.getEquipBOList().addAll(exchangeEquip(userId, toolId, exchangeNum));
				break;
			default:
				if (this.toolService.isTool(toolType)) {
					int userToolNum = userToolDao.getUserToolNum(userId, toolId);
					if (userToolNum < exchangeNum) {
						String message = "道具数量不够.userId[" + userId + "], equipId[" + toolId + "]";
						throw new ServiceException(TOOL_NOT_ENOUGH, message);
					} else {
						ToolExchangeToolBO toolBo = new ToolExchangeToolBO();
						toolBo.setToolId(toolId);
						toolBo.setExchangeNum(exchangeNum);
						looseBO.getToolBOList().add(toolBo);
					}
				}
				break;
			}
		}
		return looseBO;
	}

	private List<ToolExchangeHeroBO> exchangeHero(String userId, int toolId, int exchangeNum) {
		// 将用户中相同 system_hero_id 的武将按照 hero_level 从小大排列
		List<UserHero> userHeroList = userHeroDao.listUserHeroByLevelAsc(userId, toolId);
		List<ToolExchangeHeroBO> heroBoList = new ArrayList<ToolExchangeHeroBO>();

		int userHeroNum = userHeroList.size();

		// 检查这种类型武将数量是否足够
		if (userHeroNum < exchangeNum) {
			String message = "武将数量不够.userId[" + userId + "], systemHeroId[" + toolId + "]";
			throw new ServiceException(TOOL_NOT_ENOUGH, message);
		} else {
			for (int i = 0; i < exchangeNum; i++) {
				UserHero userHero = userHeroList.get(i);
				if (userHero.getLockStatus() < 1) {
					ToolExchangeHeroBO heroBO = new ToolExchangeHeroBO();
					heroBO.setUserHeroId(userHero.getUserHeroId());
					heroBoList.add(heroBO);
				}
			}

			if (heroBoList.size() < exchangeNum) {
				String message = "该类武将有被锁定的,导致数量不足.userId[" + userId + "], systemHeroId[" + toolId + "]";
				throw new ServiceException(HERO_HAS_LOCKED, message);
			}
			return heroBoList;
		}
	}

	private List<ToolExchangeEquipBO> exchangeEquip(String userId, int toolId, int exchangeNum) {
		List<UserEquip> userEquipList = userEquipDao.listUserEquipByLevelAsc(userId, toolId);
		List<ToolExchangeEquipBO> equipBoList = new ArrayList<ToolExchangeEquipBO>();
		int userEquipNum = userEquipList.size();

		if (userEquipNum < exchangeNum) {
			String message = "装备数量不够.userId[" + userId + "], equipId[" + toolId + "]";
			throw new ServiceException(TOOL_NOT_ENOUGH, message);
		} else {
			for (int i = 0; i < exchangeNum; i++) {
				UserEquip userEquip = userEquipList.get(i);
				ToolExchangeEquipBO equipBo = new ToolExchangeEquipBO();
				equipBo.setUserEquipId(userEquip.getUserEquipId());
				equipBoList.add(equipBo);
			}

			return equipBoList;
		}
	}

	@Override
	public List<SystemActivityBO> getDisplayActivityBOList(String uid) {

		List<SystemActivity> activityList = this.systemActivityDao.getDisplayActivityLiset();
		List<SystemActivityBO> systemActivityBOList = new ArrayList<SystemActivityBO>();

		Date now = new Date();

		for (SystemActivity systemActivity : activityList) {

			if (now.before(systemActivity.getStartTime()) || now.after(systemActivity.getEndTime())) {
				continue;
			}

			// 积天奖励领完了就不推送给前端
			if (systemActivity.getActivityId() == ActivityId.TOTAL_DAY_PAYMENT_REWARD_ID) {
				if (0 == this.checkTotalDayPayRewardIsOver(uid)) {
					continue;
				}
			}

			SystemActivityBO systemActivityBO = new SystemActivityBO();
			systemActivityBO.setDesc(systemActivity.getActivityDesc());
			systemActivityBO.setTitle(systemActivity.getActivityName());
			systemActivityBO.setType(systemActivity.getActivityType());

			systemActivityBOList.add(systemActivityBO);
		}

		return systemActivityBOList;
	}

	@Override
	public boolean checkOncePayReward(String userId) {
		boolean flg = false;

		List<UserPayRewardBO> ret = new ArrayList<UserPayRewardBO>();
		// 获得活动期间用户的订单以及已经领取过的订单记录
		SystemActivity systemActivity = systemActivityDao.get(ActivityId.ONCE_PAY_REWARD_ID);
		try {
			checkActivityIsOpen(systemActivity);
		} catch (ServiceException e) {
			return flg;
		}

		List<SystemOncePayReward> systemOncePayRewards = systemOncePayRewardDao.getAll();

		for (SystemOncePayReward reward : systemOncePayRewards) {

			SystemOncePayReward nextReward = systemOncePayRewardDao.getNextById(reward.getId());
			List<PaymentLog> paymentLogs = getPaymentLogsByTimeAndMoney(userId, systemActivity, reward, nextReward);

			// 取出已经领取过的订单ID
			List<UserPayReward> receivedOrderList = userPayRewardDao.getReceivedOrderIdList(userId, ActivityId.ONCE_PAY_REWARD_ID, reward.getId(), systemActivity.getStartTime(),
					systemActivity.getEndTime());

			// 如果领取次数超出限制
			if (receivedOrderList != null && reward.getTimesLimit() <= receivedOrderList.size()) {
				flg = false;
				continue;
			}

			for (PaymentLog log : paymentLogs) {
				// 如果已领取订单中不包括当前订单号，则表示用户存在满足条件的订单
				if (!contaninOrder(receivedOrderList, log.getOrderId())) {
					flg = true;
					return flg;
				}
			}
		}
		return flg;
	}

	@Override
	public boolean checkTotalPayReward(String userId) {
		boolean flg = false;

		SystemActivity systemActivity = systemActivityDao.get(ActivityId.TOTAL_PAY_REWARD_ID);
		try {
			checkActivityIsOpen(systemActivity);
		} catch (ServiceException e) {
			return flg;
		}

		// 计算在活动期间，用户总的充值金额
		int userTotalPay = paymentLogDao.getPaymentTotalGoldByTime(userId, systemActivity.getStartTime(), systemActivity.getEndTime());
		// 查询出所有小于用户充值总金额的奖励
		List<SystemTotalPayReward> systemTotalPayRewards = systemTotalPayRewardDao.getByPayment(userTotalPay);

		for (SystemTotalPayReward reward : systemTotalPayRewards) {
			int rid = reward.getId();
			UserPayReward userpayrePayReward = userPayRewardDao.getUserPayReward(userId, ActivityId.TOTAL_PAY_REWARD_ID, rid, systemActivity.getStartTime(), systemActivity.getEndTime());
			if (userpayrePayReward == null) {
				flg = true;
				break;
			}
		}
		return flg;
	}

	@Override
	public List<DropToolBO> activityDraw(String userId, int num) {

		return null;
	}

	@Override
	public boolean isForcesActivityOpen(int sceneId) {

		List<SystemActivity> activityList = this.systemActivityDao.getList(ActivityType.ACTIVITY_TYPE_COPY);
		Date now = new Date();
		for (SystemActivity activity : activityList) {
			String param = activity.getParam();
			int checkSceneId = NumberUtils.toInt(param, 0);
			if (sceneId == checkSceneId) {
				if (activity.getStartTime().after(now) || activity.getEndTime().before(now)) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * 系统活动是否开放
	 * 
	 * @param activityId
	 *            系统活动 id
	 * @return true 活动开放 false 活动不开放
	 */
	public boolean isActivityOpen(int activityId) {

		Date now = new Date();

		SystemActivity systemActivity = this.systemActivityDao.get(activityId);
		if (systemActivity != null) {

			if (systemActivity.getStartTime().after(now) || systemActivity.getEndTime().before(now)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public Map<String, Object> getLoginRewardList(String userId) {
		Map<String, Object> rt = new HashMap<String, Object>();
		List<DropToolBO> toolBOList = new ArrayList<DropToolBO>();
		List<Integer> statusList = new ArrayList<Integer>();
		int day = 1;

		String currentDate = DateUtils.getDate();
		Date now = new Date();

		// 读取所有的七天登陆奖励信息
		List<SystemLoginReward7> rewardList = systemLoginReward7Dao.getAll();
		for (SystemLoginReward7 reward : rewardList) {
			DropToolBO toolBO = new DropToolBO(reward.getToolType(), reward.getToolId(), reward.getToolNum());
			toolBOList.add(toolBO);
		}

		// 查询最近一次登录的信息
		UserLoginReward7Info lastLogin = userLoginReward7InfoDao.getLastLogin(userId);

		if (lastLogin != null) {
			int lastDay = lastLogin.getDay(); // 最后一次登陆领取的是哪一天的奖励
			String lastLoginDate = lastLogin.getDate();

			// int dayDiff = DateUtils.getDayDiff(lastLogin.getCreatedTime(),
			// now);

			if (lastLoginDate.equals(currentDate)) {
				day = 0;
				statusList = setReceStatus(lastDay);
			} else if (!DateUtils.isSameDay(DateUtils.addDays(lastLogin.getCreatedTime(), 1), now)) {
				day = 1;
				statusList = setReceStatus(0);
			} else if (lastDay == 7) {
				day = 1;
				statusList = setReceStatus(0);
			} else {
				day = lastDay + 1;
				statusList = setReceStatus(lastDay);
			}

		} else {
			statusList = setReceStatus(0);
		}

		// 将要返回的数据存入 map 中
		rt.put("day", day);
		rt.put("tls", toolBOList);
		rt.put("sts", statusList);

		return rt;
	}

	private List<Integer> setReceStatus(int day) {
		List<Integer> statusList = new ArrayList<Integer>();
		for (int i = 0; i < day; i++) {
			statusList.add(1);
		}

		for (int i = 0; i < 7 - day; i++) {
			statusList.add(0);
		}
		return statusList;
	}

	@Override
	public Map<String, Object> getLoginReward(String userId) {
		Map<String, Object> rt = new HashMap<String, Object>();

		// 查询最后一次领取记录
		UserLoginReward7Info loginInfo = userLoginReward7InfoDao.getLastLogin(userId);
		String currentDate = DateUtils.getDate();
		Date now = new Date();

		/*
		 * 写入领取奖励日志 1. 计算此次领取奖励是从哪一天开始领取
		 */
		int day = 1; // 此次领取奖励是从哪一天开始领取

		if (loginInfo != null) {
			String lastReceDate = loginInfo.getDate();

			// 如果日期和今天相同
			if (lastReceDate.equals(currentDate)) {
				String message = "七天登录奖励，当天奖励已经领取,userId[" + userId + "] lastReceDate[" + lastReceDate + "]";
				throw new ServiceException(USER_LOGIN_REWARD7_HAS_RECEIVED, message);
			}

			// int dayDiff = DateUtils.getDayDiff(loginInfo.getCreatedTime(),
			// now);

			if (!DateUtils.isSameDay(DateUtils.addDays(loginInfo.getCreatedTime(), 1), now)) { // 如果日期和今天日期相差的天数大于1，则从第一天开始重新计数
				day = 1;
			} else if (loginInfo.getDay() == 7) {
				day = 1;
			} else { // 否则，将这次领取的天数设置为最后领取天数加1
				day = loginInfo.getDay() + 1;
			}

		}

		UserLoginReward7Info newloginInfo = new UserLoginReward7Info();
		newloginInfo.setCreatedTime(now);
		newloginInfo.setDate(currentDate);
		newloginInfo.setDay(day);
		newloginInfo.setStatus(1);
		newloginInfo.setUserId(userId);
		userLoginReward7InfoDao.addUserLoginReward7Info(newloginInfo);

		// 领取奖励
		SystemLoginReward7 reward = systemLoginReward7Dao.getByDay(day);
		int toolId = reward.getToolId();
		int toolNum = reward.getToolNum();
		int toolType = reward.getToolType();
		List<DropToolBO> dropToolBOList = toolService.giveTools(userId, toolType, toolId, toolNum, ToolUseType.ADD_USER_LOGIN7);

		CommonDropBO commonDropBO = new CommonDropBO();

		for (DropToolBO dropToolBO : dropToolBOList) {
			// 构建道具掉落
			this.toolService.appendToDropBO(userId, commonDropBO, dropToolBO);
		}

		rt.put("dr", commonDropBO);
		rt.put("day", day);

		return rt;
	}

	@Override
	public int checkGetRewardToday(String userId) {
		// 查询最近一次登录的信息
		UserLoginReward7Info lastLogin = userLoginReward7InfoDao.getLastLogin(userId);
		String currentDate = DateUtils.getDate();

		if (lastLogin == null) {
			return 0;
		} else {

			String lastRecDate = lastLogin.getDate();

			if (lastRecDate.equals(currentDate)) {
				return 1;
			} else {
				return 0;
			}
		}
	}

	@Override
	public CommonDropBO getLimOnlineReward(String uid) {

		int status = checkLimonlineReward(uid);

		if (status == -2) {
			throw new ServiceException(ActivityService.LIM_ONLINE_REWARD_RECEIVED, "期限在线奖励-今天的奖励已经领取.uid[" + uid + "]");
		} else if (status != 0) {
			throw new ServiceException(ActivityService.LIME_ONLINE_REWARD_BEFORE_RECTIME, "期限在线奖励-没到领取时间.uid[" + uid + "]");
		}
		CommonDropBO commonDropBO = new CommonDropBO();
		SystemOnlineReward systemOnlineReward = systemOnlineRewardDao.get((int) (ActivityService.LIM_ONLINE_REWARD_COUNTDOWN / 1000));
		List<DropToolBO> dropToolBOList = DropToolHelper.parseDropTool(systemOnlineReward.getTools());

		for (DropToolBO dropToolBO : dropToolBOList) {
			int toolType = dropToolBO.getToolType();
			int toolId = dropToolBO.getToolId();
			int toolNum = dropToolBO.getToolNum();

			List<DropToolBO> dropBOList = toolService.giveTools(uid, toolType, toolId, toolNum, ToolUseType.ADD_LIM_ONLINE_REWARD);

			for (DropToolBO dropBO : dropBOList) {
				this.toolService.appendToDropBO(uid, commonDropBO, dropBO);
			}
		}

		UserLimOnlineReward reward = new UserLimOnlineReward();
		reward.setUserId(uid);
		reward.setCreatedTime(new Date());
		userLimOnlineRewardDao.add(reward);

		return commonDropBO;
	}

	@Override
	public List<DropToolBO> getLimOnlineRewardList() {

		SystemOnlineReward systemOnlineReward = systemOnlineRewardDao.get((int) (ActivityService.LIM_ONLINE_REWARD_COUNTDOWN / 1000));
		if (null == systemOnlineReward) {
			return null;
		}
		return DropToolHelper.parseDropTool(systemOnlineReward.getTools());

	}

	@Override
	public UserLimOnlineRewardBO getUserLimOnlineRewardBO(String uid) {

		UserLimOnlineRewardBO rewardBO = new UserLimOnlineRewardBO();

		int status = checkLimonlineReward(uid);

		if (status == -1 || status == -2) {
			rewardBO.setStatus(0);
			rewardBO.setRecTime(0);
		} else {
			long recTime = new Date().getTime() + status;
			rewardBO.setStatus(1);
			rewardBO.setRecTime(recTime);
		}
		return rewardBO;
	}

	/**
	 * 检查期限在线奖励领取条件 1. 是否在活动期间并且到今天的领取时间 2. 是否已经领取
	 * 
	 * @param uid
	 * @return <li>-1 不在活动期间</li> <li>-2 今天已经领取奖励了</li> <li>
	 *         大于等于0的其他数值：距离今天领取时间还有多久</li>
	 */
	private int checkLimonlineReward(String uid) {
		long countDown;
		boolean sameDay;

		boolean activityOpen = isActivityOpen(ActivityId.LIM_ONLINE_REWARD_ID);
		UserLimOnlineReward latestLog = userLimOnlineRewardDao.getLatestLog(uid);

		if (latestLog == null) {
			sameDay = false;
		} else {
			sameDay = DateUtils.isSameDay(new Date(), latestLog.getCreatedTime());
		}

		Date now = DateUtils.str2Date(DateUtils.getDate() + " 00:00:00");
		long preOnlineTime = userOnlineLogDao.getUserOnline(uid, now) * 1000;
		long lastOnlineTime = userOnlineLogDao.getLastOnline(uid) * 1000;

		countDown = (int) (ActivityService.LIM_ONLINE_REWARD_COUNTDOWN - preOnlineTime - lastOnlineTime);
		countDown = (countDown >= 0) ? countDown : 0;

		if (!activityOpen) {
			return -1;
		} else if (sameDay) {
			return -2;
		} else {
			return (int) countDown;
		}
	}

	private List<DropToolBO> getDropToolBOByGiftbagId(int giftBagId) {

		List<GiftbagDropTool> giftbagDropToolList = this.systemGiftbagDao.getGiftbagDropToolList(giftBagId);

		List<DropToolBO> dropToolBOList = new ArrayList<DropToolBO>();
		for (GiftbagDropTool giftbagDropTool : giftbagDropToolList) {
			DropToolBO dropToolBO = new DropToolBO(giftbagDropTool.getToolType(), giftbagDropTool.getToolId(), giftbagDropTool.getToolNum());
			dropToolBOList.add(dropToolBO);
		}

		return dropToolBOList;

	}

	@Override
	public List<ToolExchangeBO> getToolExchangeBoList() {

		List<ToolExchangeBO> list = new ArrayList<ToolExchangeBO>();

		List<ToolExchange> toolExchangeList = this.toolExchangeDao.getAll();
		for (ToolExchange toolExchange : toolExchangeList) {
			ToolExchangeBO toolExchangeBO = new ToolExchangeBO();

			toolExchangeBO.setId(toolExchange.getExchangeId());
			toolExchangeBO.setTimes(toolExchange.getTimes());
			toolExchangeBO.setPreDropToolBOList(DropToolHelper.parseDropTool(toolExchange.getPreExchangeItems()));
			toolExchangeBO.setPostDropToolBOList(DropToolHelper.parseDropTool(toolExchange.getPostExchangeItems()));

			list.add(toolExchangeBO);
		}

		return list;

	}

	@Override
	public int checkActivityIsOpenAdd(SystemActivity systemActivity) {
		Date now = new Date();
		if (systemActivity == null || now.after(systemActivity.getEndTime()) || now.before(systemActivity.getStartTime())) {
			return 0;
		} else {
			return 1;
		}

	}

	@Override
	public List<UserTotalDayPayRewardBO> getUserTotalDayPayRewardList(String userId) {
		List<UserTotalDayPayRewardBO> ret = new ArrayList<UserTotalDayPayRewardBO>();

		List<SystemTotalDayPayReward> systemTotalDayPayRewards = systemTotalDayPayRewardDao.getAll();

		List<UserTotalDayPayRewardLog> userTotalDayPayRewardLogList = userTotalDayPayRewardDao.getUserAllTotalDayPayRewardLog(userId);

		for (SystemTotalDayPayReward reward : systemTotalDayPayRewards) {
			UserTotalDayPayRewardBO bo = new UserTotalDayPayRewardBO();
			bo.setRewardId(reward.getId());
			bo.setDay(reward.getDay());
			bo.setDropToolBoList(DropToolHelper.parseDropTool(reward.getDropTool()));

			UserTotalDayPayRewardLog userTotalDayPayRewardLog = new UserTotalDayPayRewardLog();

			for (UserTotalDayPayRewardLog userTotalDayPayReward : userTotalDayPayRewardLogList) {
				if (userTotalDayPayReward.getDay() == reward.getDay()) {
					userTotalDayPayRewardLog = userTotalDayPayReward;
					break;
				}
			}

			bo.setStatus(userTotalDayPayRewardLog.getRewardStatus());

			ret.add(bo);
		}
		return ret;
	}

	@Override
	public CommonDropBO receiveTotalDayPayReward(String userId, int rid) {

		SystemTotalDayPayReward systemTotalDayPayReward = systemTotalDayPayRewardDao.getPayRewardByAid(rid);

		if (systemTotalDayPayReward != null) {

			// 判断是否可以领取
			UserTotalDayPayRewardLog userTotalDayPayRewardLog = userTotalDayPayRewardDao.getUserTotalDayPayReward(userId, systemTotalDayPayReward.getDay());

			if (userTotalDayPayRewardLog == null) {
				String message = "领取积天登入奖励,没有达到领取要求,userId[" + userId + "]" + " 奖励ID[" + rid + "]";
				throw new ServiceException(TOTAL_DAY_PAY_REWARD_CANNT_GIVEN, message);
			}

			if (userTotalDayPayRewardLog.getRewardStatus() != 1) {
				String message = "领取积天登入奖励,已经领取过了,userId[" + userId + "]" + " 奖励ID[" + rid + "]";
				throw new ServiceException(TOTAL_DAY_PAY_REWARD_RECEIVED, message);
			}

			this.userTotalDayPayRewardDao.addReceiveTotalDayPayRewardLog(userId, systemTotalDayPayReward.getDay());

			List<DropToolBO> dropToolBOList = DropToolHelper.parseDropTool(systemTotalDayPayReward.getDropTool());

			CommonDropBO commonDropBO = new CommonDropBO();
			for (DropToolBO dropToolBO : dropToolBOList) {
				int toolType = dropToolBO.getToolType();
				int toolId = dropToolBO.getToolId();
				int toolNum = dropToolBO.getToolNum();

				List<DropToolBO> dropBOList = toolService.giveTools(userId, toolType, toolId, toolNum, ToolUseType.ADD_TOTAL_DAY_PAY_REWARD);

				for (DropToolBO dropBO : dropBOList) {
					this.toolService.appendToDropBO(userId, commonDropBO, dropBO);
				}
			}

			return commonDropBO;

		}

		return null;
	}

	/**
	 * 用户是否将积天充值领取完
	 */
	public int checkTotalDayPayRewardIsOver(String userId) {

		List<UserTotalDayPayRewardBO> userTotalDayPayRewardBOList = this.getUserTotalDayPayRewardList(userId);

		if (userTotalDayPayRewardBOList != null) {
			return this.checkTotalDayPayRewardIsOver(userTotalDayPayRewardBOList);
		}

		return 0;

	}

	/**
	 * 用户是否将积天充值领取完 return 0:已经领完，1，还有可领的记录
	 */
	public int checkTotalDayPayRewardIsOver(List<UserTotalDayPayRewardBO> userTotalDayPayRewardBOList) {

		for (UserTotalDayPayRewardBO reward : userTotalDayPayRewardBOList) {

			if (reward.getStatus() == 0 || reward.getStatus() == 1) {
				return 1;
			}

		}
		return 0;
	}

	@Override
	public boolean addUserTotalDayPayRewardLog(String userId) {
		UserTotalDayPayRewardLog lastUserTotalDayPayRewardLog = this.userTotalDayPayRewardDao.getLastUserTotalDayPayRewardLog(userId);
		// 第一次充值
		if (lastUserTotalDayPayRewardLog == null) {
			UserTotalDayPayRewardLog userTotalDayPayRewardLogNew = new UserTotalDayPayRewardLog();
			userTotalDayPayRewardLogNew.setDay(1);
			userTotalDayPayRewardLogNew.setUserId(userId);
			userTotalDayPayRewardLogNew.setCreatedTime(new Date());
			userTotalDayPayRewardLogNew.setRewardStatus(1);
			if (this.userTotalDayPayRewardDao.add(userTotalDayPayRewardLogNew)) {
				return true;
			}
		} else if (lastUserTotalDayPayRewardLog.getDay() < TOTAL_DAY_PAY_REWARD_MAX_DAY && !StringUtils.equals(DateUtils.getDate(), DateUtils.getDate(lastUserTotalDayPayRewardLog.getCreatedTime()))) {// 当天还没有加入数据库

			int day = lastUserTotalDayPayRewardLog.getDay() + 1;
			UserTotalDayPayRewardLog userTotalDayPayRewardLogNew = new UserTotalDayPayRewardLog();
			userTotalDayPayRewardLogNew.setDay(day);
			userTotalDayPayRewardLogNew.setUserId(userId);
			userTotalDayPayRewardLogNew.setCreatedTime(new Date());
			userTotalDayPayRewardLogNew.setRewardStatus(1);
			if (this.userTotalDayPayRewardDao.add(userTotalDayPayRewardLogNew)) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean dataSync(String table, String sqls) {

		String reloadClass = null;

		if (table.equalsIgnoreCase("system_tool_exchange")) {
			// DO NOT THING
		} else if (table.equalsIgnoreCase("system_once_pay_reward")) {
			reloadClass = "SystemOncePayRewardDaoCacheImpl";
		} else if (table.equalsIgnoreCase("system_total_pay_reward")) {
			// DO NOT THING
		} else if (table.equalsIgnoreCase("system_mall_discount_activity")) {
			// DO NOT THING
		} else if (table.equalsIgnoreCase("system_mall_discount_items")) {
			reloadClass = "SystemMallDiscountDaoCacheImpl";
		} else if (table.equalsIgnoreCase("system_reduce_rebate")) {
			reloadClass = "SystemReduceRebateDaoCacheImpl";
		} else if (table.equalsIgnoreCase("system_tavern_rebate")) {
			reloadClass = "SystemTavernRebateDaoCacheImpl";
		} else if (table.equalsIgnoreCase("system_online_reward")) {
			// DO NOT THING
		} else {
			return false;
		}

		List<Map<Object, Object>> list = Json.toObject(sqls, List.class);
		for (Map<Object, Object> map : list) {
			String s = map.get("sql").toString();
			if (s.indexOf("$table") <= 0) {
				return false;
			}
			String sql = s.replaceAll("\\$table", table);
			this.systemActivityDao.execute(sql);
		}

		if (reloadClass != null) {
			this.commandDao.cacheReload(reloadClass);
		}

		return true;
	}

	/**
	 * type,1:每日，2累计
	 */
	@Override
	public List<TavernTebatelBo> getTavernRebates(String userId, int type) {
		int typeA, typeB = 0;
		SystemActivity systemActivity = null;
		if (type == 1) {
			typeA = 1;
			typeB = 2;
			systemActivity = systemActivityDao.get(ActivityId.TAVERN_RABATE_ID_1);
		} else {
			typeA = 3;
			typeB = 4;
			systemActivity = systemActivityDao.get(ActivityId.TAVERN_RABATE_ID_2);
		}
		List<SystemTavernRebate> list = systemTavernRebateDao.getAllByType(typeA, typeB);
		List<TavernTebatelBo> rlist = new ArrayList<TavernTebatelBo>();

		for (SystemTavernRebate str : list) {
			TavernTebatelBo tavernTebatelBo = new TavernTebatelBo();
			tavernTebatelBo.setId(str.getId());
			tavernTebatelBo.setTimes(str.getTimes());
			tavernTebatelBo.setType(str.getType());
			tavernTebatelBo.setIsGet(getReceiveStatus(str.getTimes(), str.getType(), userId, systemActivity));
			tavernTebatelBo.setDropToolBOs(DropToolHelper.parseDropTool(str.getTools()));
			rlist.add(tavernTebatelBo);
		}
		return rlist;
	}

	private int getReceiveStatus(int systemTimes, int type, String userId, SystemActivity systemActivity) {
		int totalNum = 0;
		boolean isAward = false;
		if (type == 1 || type == 2) {
			totalNum = userTavernRebateLogDao.getTodayNum(userId, type, DateUtils.getDate());
			isAward = userTavernRebateLogDao.isAwardRecord(userId, DateUtils.getDate(), type, systemTimes);
		} else {
			if (type == 3) {
				totalNum = userTavernRebateLogDao.getTotalNum(userId, 1, systemActivity.getStartTime(), systemActivity.getEndTime());
			} else {
				totalNum = userTavernRebateLogDao.getTotalNum(userId, 2, systemActivity.getStartTime(), systemActivity.getEndTime());
			}
			isAward = userTavernRebateLogDao.isTotalAwardRecord(userId, systemActivity.getStartTime(), systemActivity.getEndTime(), type, systemTimes);

		}
		if (totalNum < systemTimes) {
			return 3;
		}

		if (isAward) {
			return 2;
		}
		return 1;
	}

	@Override
	public Map<String, Object> receiveTavernRebates(String userId, int id) {
		Map<String, Object> map = new HashMap<String, Object>();
		SystemTavernRebate systemTavernRebate = systemTavernRebateDao.get(id);
		int type = 0;
		SystemActivity systemActivity = null;
		int tavernNum = 0;

		boolean isAward = false;

		if (systemTavernRebate.getType() == 1 || systemTavernRebate.getType() == 2) {// 每日招募
			type = 1;
			systemActivity = systemActivityDao.get(ActivityId.TAVERN_RABATE_ID_1);
			tavernNum = userTavernRebateLogDao.getTodayNum(userId, systemTavernRebate.getType(), DateUtils.getDate());
			isAward = userTavernRebateLogDao.isAwardRecord(userId, DateUtils.getDate(), systemTavernRebate.getType(), systemTavernRebate.getTimes());
		} else {// 累计招募
			type = 2;
			systemActivity = systemActivityDao.get(ActivityId.TAVERN_RABATE_ID_2);
			if (systemTavernRebate.getType() == 3) {
				tavernNum = userTavernRebateLogDao.getTotalNum(userId, 1, systemActivity.getStartTime(), systemActivity.getEndTime());
			} else {
				tavernNum = userTavernRebateLogDao.getTotalNum(userId, 2, systemActivity.getStartTime(), systemActivity.getEndTime());
			}

			isAward = userTavernRebateLogDao.isTotalAwardRecord(userId, systemActivity.getStartTime(), systemActivity.getEndTime(), systemTavernRebate.getType(), systemTavernRebate.getTimes());
		}
		boolean isOpen = isActivityOpen(systemActivity.getActivityId());
		if (!isOpen) {
			throw new ServiceException(ACTIVITY_IS_CLOSED, "活动以停止");
		}
		if (tavernNum < systemTavernRebate.getTimes()) {
			throw new ServiceException(TAVERN_REBATE_NO_TIMES, "招募次数不足");
		}

		if (isAward) {
			throw new ServiceException(TAVERN_REBATE_GET, "该奖励已经领取");
		}

		List<DropToolBO> dropToolBOs = DropToolHelper.parseDropTool(systemTavernRebate.getTools());

		// 检测用户背包
		checkBag(userId, dropToolBOs);

		CommonDropBO commonDropBO = new CommonDropBO();

		// 发道具给用户
		for (DropToolBO dtb : dropToolBOs) {
			List<DropToolBO> dropToolBOList = toolService.giveTools(userId, dtb.getToolType(), dtb.getToolId(), dtb.getToolNum(), ToolUseType.TAVERN_REBATE);
			for (DropToolBO dropToolBO : dropToolBOList) {
				toolService.appendToDropBO(userId, commonDropBO, dropToolBO);
			}
		}
		userTavernRebateLogDao.addAwardRecord(userId, new Date(), systemTavernRebate.getType(), systemTavernRebate.getTimes());

		map.put("dr", commonDropBO);
		map.put("tp", type);
		map.put("strs", getTavernRebates(userId, type));
		return map;
	}

	@Override
	public List<Integer> getTavernTimes(String userId, int type) {
		List<Integer> list = new ArrayList<Integer>();
		if (type == 1) {
			list.add(userTavernRebateLogDao.getTodayNum(userId, 1, DateUtils.getDate()));
			list.add(userTavernRebateLogDao.getTodayNum(userId, 2, DateUtils.getDate()));
			return list;
		}
		SystemActivity systemActivity = systemActivityDao.get(ActivityId.TAVERN_RABATE_ID_2);
		list.add(userTavernRebateLogDao.getTotalNum(userId, 1, systemActivity.getStartTime(), systemActivity.getEndTime()));
		list.add(userTavernRebateLogDao.getTotalNum(userId, 2, systemActivity.getStartTime(), systemActivity.getEndTime()));
		return list;

	}

	/**
	 * 兑换时检测背包
	 * 
	 * @param userId
	 * @param dropTools
	 */
	private void checkBag(String userId, List<DropToolBO> dropTools) {
		int userHeroCount = this.heroService.getUserHeroCount(userId);
		int userEquipCount = 0;
		List<UserEquipBO> userEquips = this.equipService.getUserEquipList(userId);
		if (userEquips != null) {
			userEquipCount = userEquips.size();
		}
		UserExtinfo userExtinfo = this.userExtinfoDao.get(userId);
		int heroBagMax = InitDefine.HERO_BAG_INIT;
		int equipBagMax = InitDefine.EQUIP_BAG_INIT;
		if (userExtinfo != null) {
			heroBagMax = userExtinfo.getHeroMax();
			equipBagMax = userExtinfo.getEquipMax();
		}

		int heroCount = 0;
		int equipCount = 0;
		for (DropToolBO dropToolBO : dropTools) {
			int type = dropToolBO.getToolType();
			switch (type) {
			case ToolType.HERO:
				heroCount++;
				break;
			case ToolType.EQUIP:
				equipCount++;
				break;
			}
		}

		if (heroCount != 0 && heroCount + userHeroCount > heroBagMax) {
			throw new ServiceException(EXCHANGE_HERO_BAG_NOT_ENOUGH, "武将背包不足");
		}

		if (equipCount != 0 && equipCount + userEquipCount > equipBagMax) {
			throw new ServiceException(EXCHANGE_EQUIP_BAG_NOT_ENOUGH, "装备背包不足");
		}
	}

	@Override
	public List<SystemReduceRebateBo> getReduceRebates(String userId, int type) {
		SystemActivity systemActivity = null;
		if (type == 1) {
			systemActivity = systemActivityDao.get(ActivityId.GOLD_RABATE_ID_1);
		} else {
			systemActivity = systemActivityDao.get(ActivityId.GOLD_RABATE_ID_2);
		}
		List<SystemReduceRebate> list = systemReduceRebateDao.getAllByType(type);
		List<SystemReduceRebateBo> rlist = new ArrayList<SystemReduceRebateBo>();

		for (SystemReduceRebate str : list) {
			SystemReduceRebateBo systemReduceRebateBo = new SystemReduceRebateBo();
			systemReduceRebateBo.setId(str.getId());
			systemReduceRebateBo.setGold(str.getGold());
			systemReduceRebateBo.setDropToolList(DropToolHelper.parseDropTool(str.getTools()));
			systemReduceRebateBo.setIsGet(getReduceRecviceStatus(type, systemActivity, userId, str.getGold()));
			rlist.add(systemReduceRebateBo);
		}
		return rlist;
	}

	private int getReduceRecviceStatus(int type, SystemActivity systemActivity, String userId, int systemAmount) {
		int totalNum = 0;
		boolean isAward = false;
		if (type == 1) {
			totalNum = userReduceGoldRebateLogDao.getTodayReduceGold(userId, DateUtils.getDate());
			isAward = userReduceGoldRebateLogDao.isAwardRecord(userId, DateUtils.getDate(), systemAmount, type);
		} else {
			totalNum = userReduceGoldRebateLogDao.getTotalReduceGold(userId, systemActivity.getStartTime(), systemActivity.getEndTime());
			isAward = userReduceGoldRebateLogDao.isTotalAwardRecord(userId, systemActivity.getStartTime(), systemActivity.getEndTime(), systemAmount, type);
		}
		if (totalNum < systemAmount) {
			return 3;
		}

		if (isAward) {
			return 2;
		}
		return 1;
	}

	@Override
	public Map<String, Object> receiveReduceRebates(String userId, int id) {
		Map<String, Object> map = new HashMap<String, Object>();
		SystemReduceRebate systemReduceRebate = systemReduceRebateDao.get(id);
		SystemActivity systemActivity = null;
		int gold = 0;
		boolean isAward = false;
		if (systemReduceRebate.getType() == 1) {// 每日消耗
			systemActivity = systemActivityDao.get(ActivityId.GOLD_RABATE_ID_1);
			gold = userReduceGoldRebateLogDao.getTodayReduceGold(userId, DateUtils.getDate());
			isAward = userReduceGoldRebateLogDao.isAwardRecord(userId, DateUtils.getDate(), systemReduceRebate.getType(), systemReduceRebate.getGold());
		} else {// 累计消耗
			systemActivity = systemActivityDao.get(ActivityId.GOLD_RABATE_ID_2);
			gold = userReduceGoldRebateLogDao.getTotalReduceGold(userId, systemActivity.getStartTime(), systemActivity.getEndTime());
			isAward = userReduceGoldRebateLogDao.isTotalAwardRecord(userId, systemActivity.getStartTime(), systemActivity.getEndTime(), systemReduceRebate.getType(), systemReduceRebate.getGold());
		}
		boolean isOpen = isActivityOpen(systemActivity.getActivityId());
		if (!isOpen) {
			throw new ServiceException(ACTIVITY_IS_CLOSED, "活动以停止");
		}
		if (gold < systemReduceRebate.getGold()) {
			throw new ServiceException(REDUCE_GOLD_NOT_ENOUGH, "消耗元宝不足");
		}

		if (isAward) {
			throw new ServiceException(TAVERN_REBATE_GET, "该奖励已经领取");
		}

		List<DropToolBO> dropToolBOs = DropToolHelper.parseDropTool(systemReduceRebate.getTools());

		// 检测用户背包
		checkBag(userId, dropToolBOs);

		CommonDropBO commonDropBO = new CommonDropBO();

		// 发道具给用户
		for (DropToolBO dtb : dropToolBOs) {
			List<DropToolBO> dropToolBOList = toolService.giveTools(userId, dtb.getToolType(), dtb.getToolId(), dtb.getToolNum(), ToolUseType.REDUCE_GOLD);
			for (DropToolBO dropToolBO : dropToolBOList) {
				toolService.appendToDropBO(userId, commonDropBO, dropToolBO);
			}
		}
		userReduceGoldRebateLogDao.addAwardRecord(userId, new Date(), systemReduceRebate.getType(), systemReduceRebate.getGold());

		map.put("dr", commonDropBO);
		map.put("tp", systemReduceRebate.getType());
		map.put("srrs", getReduceRebates(userId, systemReduceRebate.getType()));
		return map;
	}

	@Override
	public int getReduceGoldByType(String userId, int type) {
		if (type == 1) {
			return userReduceGoldRebateLogDao.getTodayReduceGold(userId, DateUtils.getDate());
		}
		SystemActivity systemActivity = systemActivityDao.get(ActivityId.GOLD_RABATE_ID_2);
		return userReduceGoldRebateLogDao.getTotalReduceGold(userId, systemActivity.getStartTime(), systemActivity.getEndTime());
	}
	
	@Override
	public void comment(String userId) {
		if (isSendRewardAlready(userId) == false) {
			sendRewardMail(userId);
			
			List<String> userIdList = new ArrayList<String>();
			userIdList.add(userId);
			mailService.notifyNewMail(userIdList);
		}
	}
	
	private boolean isSendRewardAlready(String userId) {
		UserMail userCommentRewardMail = userMailDao.getBySystemMailId(userId, ActivityService.COMMENT_REWARD_MAIL_ID);
		if(userCommentRewardMail!=null){
			SystemMail mail = systemMailDao.get(ActivityService.COMMENT_REWARD_MAIL_ID);
			if(mail!=null){
				return true;
			}
		}
		return userMailDao.isZaned(userId);
	}
	
	private void sendRewardMail(String userId) {
		SystemMail commentRewardMail = getCommentRewardMail();
		UserMail userMail = new UserMail();
		userMail.setUserId(userId);
		userMail.setSystemMailId(commentRewardMail.getSystemMailId());
		userMail.setStatus(MailStatus.STATUS_NEW);
		userMail.setReceiveStatus(MailStatus.STATUS_NOT_RECEIVE);
		userMail.setCreatedTime(new Date());
		userMail.setUpdatedTime(new Date());

		List<UserMail> userMailList = new ArrayList<UserMail>();
		userMailList.add(userMail);

		this.userMailDao.add(userMailList);
		//璁剧疆宸插彂濂栧姳閭欢
		this.userMailDao.setZaned(userId);
	}
	
	private SystemMail getCommentRewardMail() {
		SystemMail commentRewardMail = new SystemMail();
		String systemMailId = IDGenerator.getID();
		commentRewardMail.setSystemMailId(systemMailId);
		commentRewardMail.setTitle("评价送奖励");
		commentRewardMail.setContent("感谢您的支持与鼓励，祝您游戏愉快，希望能得到好评价的神鬼三国团队~谢谢您^^");
		commentRewardMail.setTarget(MailTarget.USERS);
		commentRewardMail.setCreatedTime(new Date());
		commentRewardMail.setToolIds("4001,40044,1");
		commentRewardMail.setSourceId(IDGenerator.getID());
		systemMailDao.add(commentRewardMail);
		return commentRewardMail;
	}
}
