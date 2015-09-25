package com.lodogame.ldsg.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.ForcesDropToolDao;
import com.lodogame.game.dao.SystemForcesDao;
import com.lodogame.game.dao.SystemSceneDao;
import com.lodogame.game.dao.SystemUserLevelDao;
import com.lodogame.game.dao.UserForcesDao;
import com.lodogame.game.dao.UserSceneDao;
import com.lodogame.game.dao.UserSweepInfoDao;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.ldsg.bo.BattleBO;
import com.lodogame.ldsg.bo.BattleHeroBO;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.DropToolBO;
import com.lodogame.ldsg.bo.SweepInfoBO;
import com.lodogame.ldsg.bo.UserSceneBO;
import com.lodogame.ldsg.constants.ActivityTargetType;
import com.lodogame.ldsg.constants.ForcesStatus;
import com.lodogame.ldsg.constants.ForcesType;
import com.lodogame.ldsg.constants.LogTypes;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.constants.SweepStatus;
import com.lodogame.ldsg.constants.ToolId;
import com.lodogame.ldsg.constants.ToolType;
import com.lodogame.ldsg.constants.ToolUseType;
import com.lodogame.ldsg.event.BaseEvent;
import com.lodogame.ldsg.event.BattleResponseEvent;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.SceneEvent;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.helper.DropToolHelper;
import com.lodogame.ldsg.helper.ForcesDropHelper;
import com.lodogame.ldsg.service.ActivityService;
import com.lodogame.ldsg.service.ActivityTaskService;
import com.lodogame.ldsg.service.BattleService;
import com.lodogame.ldsg.service.EventServcie;
import com.lodogame.ldsg.service.ForcesService;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.SceneService;
import com.lodogame.ldsg.service.ToolService;
import com.lodogame.ldsg.service.UnSynLogService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.ldsg.service.VipService;
import com.lodogame.model.ForcesDropTool;
import com.lodogame.model.SystemForces;
import com.lodogame.model.SystemScene;
import com.lodogame.model.SystemUserLevel;
import com.lodogame.model.User;
import com.lodogame.model.UserForces;
import com.lodogame.model.UserScene;
import com.lodogame.model.UserSweepInfo;

public class SceneServiceImpl implements SceneService {

	private static final Logger logger = Logger.getLogger(SceneServiceImpl.class);

	private static final Logger statLogger = Logger.getLogger("stat");

	// 最大扫荡次数
	private static final int MAX_SWEEP_TIMES = 10;

	// 每分钟加速扫荡所消耗的金币数
	private static final int ONE_MINUTE_SWEEP_UP_NEED_GOLD = 10;

	@Autowired
	private UserSceneDao userSceneDao;

	@Autowired
	private UserForcesDao userForcesDao;

	@Autowired
	private SystemForcesDao systemForcesDao;

	@Autowired
	private SystemSceneDao systemSceneDao;

	@Autowired
	private ForcesService forcesService;

	@Autowired
	private HeroService heroService;

	@Autowired
	private BattleService battleService;

	@Autowired
	private ForcesDropToolDao forcesDropToolDao;

	@Autowired
	private ToolService toolService;

	@Autowired
	private UserService userService;

	@Autowired
	private ActivityService activityService;

	@Autowired
	private SystemUserLevelDao systemUserLevelDao;

	@Autowired
	private UserSweepInfoDao userSweepInfoDao;

	@Autowired
	private VipService vipService;

	@Autowired
	private ActivityTaskService activityTaskService;
	@Autowired
	public UnSynLogService unSynLogService;

	@Autowired
	private EventServcie eventServcie;

	public List<UserSceneBO> getUserSceneList(String userId) {

		List<UserSceneBO> userSceneBOList = new ArrayList<UserSceneBO>();

		List<UserScene> userSceneList = this.userSceneDao.getUserSceneList(userId);

		for (UserScene userScene : userSceneList) {
			UserSceneBO userSceneBO = new UserSceneBO();
			userSceneBO.setUserId(userScene.getUserId());
			userSceneBO.setSceneId(userScene.getSceneId());
			userSceneBO.setPassFlag(userScene.getPassFlag());
			userSceneBOList.add(userSceneBO);
		}

		return userSceneBOList;
	}

	public boolean attack(final String userId, final int forcesId, final EventHandle handle) {

		BattleBO attack = new BattleBO();

		// 怪物列表
		List<BattleHeroBO> battleHeroBOList = this.forcesService.getForcesHeroBOList(forcesId);

		// 英雄列表
		List<BattleHeroBO> userBattleBattleHeroBO = this.heroService.getUserBattleHeroBOList(userId);

		attack.setBattleHeroBOList(userBattleBattleHeroBO);

		BattleBO defense = new BattleBO();
		defense.setBattleHeroBOList(battleHeroBOList);

		userService.checkUserHeroBagLimit(userId);
		userService.checkUserEquipBagLimit(userId);
		final SystemForces systemForces = this.systemForcesDao.get(forcesId);
		if (systemForces == null) {
			throw new ServiceException(ServiceReturnCode.FAILD, "攻打怪物部队失败,怪物不存在.userId[" + userId + "], forcesId[" + forcesId + "]");
		}

		SystemScene systemScene = this.systemSceneDao.get(systemForces.getSceneId());
		final int imgId = systemScene.getImgId();

		final UserForces userForces = this.userForcesDao.get(userId, forcesId);
		if (systemForces.getForcesType() != ForcesType.FORCES_TYPE_COPY) {// 副本不需要开启

			if (userForces == null) {
				throw new ServiceException(ServiceReturnCode.FAILD, "攻打怪物部队失败,怪物部队未开放.userId[" + userId + "], forcesId[" + forcesId + "]");
			}
		} else {// 副本活动要判断开不开启
			if (!this.activityService.isForcesActivityOpen(systemScene.getSceneId())) {
				throw new ServiceException(ServiceReturnCode.ACTIVITY_NOT_OPEN_EXCTPION, "攻打怪物部队失败,副本活动未开放.userId[" + userId + "], forcesId[" + forcesId + "]");
			}
		}

		if (userForces != null && DateUtils.isSameDay(userForces.getUpdatedTime(), new Date()) && userForces.getTimes() >= systemForces.getTimesLimit() && systemForces.getTimesLimit() != 0) {
			throw new ServiceException(ServiceReturnCode.FAILD, "攻打怪物部队失败,攻打次数不足.userId[" + userId + "], forcesId[" + forcesId + "]");
		}

		// 是否首个怪
		// hard code
		final boolean isFirstForces = (forcesId == 20112 && userForces != null && userForces.getStatus() == ForcesStatus.STATUS_ATTACKABLE);

		int needPower = systemForces.getNeedPower();
		final User user = this.userService.get(userId);
		if (user.getPower() < needPower) {
			String message = "攻打怪物出错，体力不足.userId[" + userId + "], forcesId[" + forcesId + "]";
			logger.error(message);
			throw new ServiceException(ATTACK_FORCES_POWER_NOT_ENOUGH, message);
		}

		if (!this.userService.reducePower(userId, needPower, systemForces.getForcesType())) {
			String message = "攻打怪物异常,更新用户体力出错.userId[" + userId + "], forcesId[" + forcesId + "]";
			logger.error(message);
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		battleService.fight(attack, defense, 1, new EventHandle() {

			public boolean handle(Event event) {

				if (event instanceof BattleResponseEvent) {

					int flag = event.getInt("flag");

					// 统计
					statLogger.info("action[attackForces], userId[" + userId + "], userLevel[" + user.getLevel() + "], forcesId[" + forcesId + "], flag[" + flag + "]");

					if (userForces != null && userForces.getStatus() != ForcesStatus.STATUS_PASS) {
						event.setObject("canPass", 0);
					} else {
						event.setObject("canPass", 1);
					}

					if (flag == 1) {// 打怪打赢了

						// 处理怪物部队通过
						Set<Integer> openForcesTypes = handleForcesPass(userId, forcesId);
						event.setObject("openForcesTypes", openForcesTypes);

						// 获取掉落
						CommonDropBO forcesDropBO = pickUpForcesDropToolList(userId, forcesId, isFirstForces);
						event.setObject("forcesDropBO", forcesDropBO);

						if (userForces != null) {
							// 活跃度任务
							if (userForces.getForcesType() == ForcesType.FORCES_TYPE_NORMAL) {
								activityTaskService.updateActvityTask(userId, ActivityTargetType.NORMAL_FORCES, 1);
							} else if (userForces.getForcesType() == ForcesType.FORCES_TYPE_ELITE) {
								activityTaskService.updateActvityTask(userId, ActivityTargetType.ELITE_FORCES, 1);
							}
						}

					} else {

						// 返还体力
						int giveBackPower = systemForces.getNeedPower();
						if (giveBackPower > 0) {
							userService.addPower(userId, giveBackPower, ToolUseType.ADD_FORCES_BACK, null);
						}
					}

					// 保存战报
					// String report = event.getString("report");
					// battleService.saveReport(userId,
					// String.valueOf(forcesId), 1, flag, report, true);
					unSynLogService.userBattleLog(userId, user.getLevel(), String.valueOf(forcesId), LogTypes.BATTLE_TYPE_SCENCE, flag, new Date());

					event.setObject("bgid", imgId);

					// 继续广播出去
					handle.handle(event);
				}

				return true;
			}
		});

		return true;
	}

	/**
	 * 给怪物部队掉落
	 * 
	 * @param userId
	 * @param forcesId
	 */
	private CommonDropBO pickUpForcesDropToolList(String userId, int forcesId, boolean isFirstForces) {

		CommonDropBO forcesDropBO = new CommonDropBO();

		User user = this.userService.get(userId);

		int oldLevel = user.getLevel();

		SystemUserLevel systemUserLevel = this.systemUserLevelDao.get(user.getLevel());
		int oldAttack = systemUserLevel.getAttack();
		int oldDefense = systemUserLevel.getDefense();

		// long online = userOnlineLogDao.getUserOnline(userId);

		int rand = RandomUtils.nextInt(10000);

		logger.debug("获取怪物部队掉落道具.userId[" + userId + "], forcesId[" + forcesId + "]");
		List<ForcesDropTool> forcesDropToolList = this.forcesDropToolDao.getForcesDropToolList(forcesId);
		for (ForcesDropTool forcesDropTool : forcesDropToolList) {

			List<DropToolBO> dropToolBOList = this.pickUpForcesDropTool(userId, forcesDropTool, rand, isFirstForces);
			if (dropToolBOList == null) {
				continue;
			}

			for (DropToolBO dropToolBO : dropToolBOList) {
				if (dropToolBO.getToolType() == ToolType.EXP) {
					forcesDropBO.setVipAddExp((int) (vipService.getExpAddRatio(user.getVipLevel()) * dropToolBO.getToolNum()));
				} else if (dropToolBO.getToolType() == ToolType.COPPER) {
					forcesDropBO.setVipAddCopper((int) (vipService.getCopperAddRatio(user.getVipLevel()) * dropToolBO.getToolNum()));
				}

				this.toolService.appendToDropBO(userId, forcesDropBO, dropToolBO);
			}

		}

		user = this.userService.get(userId);

		int newLevel = user.getLevel();
		if (newLevel > oldLevel) {
			int levelUp = newLevel - oldLevel;
			systemUserLevel = this.systemUserLevelDao.get(newLevel);
			forcesDropBO.setLevelUp(levelUp);
			forcesDropBO.setAttackAdd(systemUserLevel.getAttack() - oldAttack);
			forcesDropBO.setDefenseAdd(systemUserLevel.getDefense() - oldDefense);
		}

		return forcesDropBO;
	}

	/**
	 * 给具体的掉落，需计算概率
	 * 
	 * @param userId
	 * @param forcesDropTool
	 */
	private List<DropToolBO> pickUpForcesDropTool(String userId, ForcesDropTool forcesDropTool, int rand, boolean isFirstForces) {

		User user = this.userService.get(userId);

		List<DropToolBO> dropToolBOList = null;

		int toolType = forcesDropTool.getToolType();
		int toolId = forcesDropTool.getToolId();
		int toolNum = forcesDropTool.getToolNum();
		int lowerNum = forcesDropTool.getLowerNum();
		int upperNum = forcesDropTool.getUpperNum();

		if (isFirstForces = false || toolType != ToolType.GOLD) {// 首次打怪,如果是金币的话必定掉落
			if (!DropToolHelper.isDrop(lowerNum, upperNum, rand)) {
				logger.debug("该道具未掉落.toolType[" + toolType + "]");
				return null;
			}
		}

		// vip加成
		if (toolType == ToolType.COPPER) {
			toolNum += vipService.getCopperAddRatio(user.getVipLevel()) * toolNum;
		} else if (toolType == ToolType.EXP) {
			toolNum += vipService.getExpAddRatio(user.getVipLevel()) * toolNum;
		}

		if (toolType == ToolType.GOLD && !userService.checkUserGoldGainLimit(userId, toolNum)) {
			return null;
		}

		dropToolBOList = toolService.giveTools(userId, toolType, toolId, toolNum, ToolUseType.ADD_FORCES);

		return dropToolBOList;
	}

	/**
	 * 给具体的掉落，无需计算概率，直接给
	 * 
	 * @param userId
	 * @param forcesDropTool
	 */
	private List<DropToolBO> pickUpForcesDropTool(String userId, ForcesDropTool forcesDropTool) {

		User user = this.userService.get(userId);

		List<DropToolBO> dropToolBOs = null;

		int toolType = forcesDropTool.getToolType();
		int toolId = forcesDropTool.getToolId();
		int toolNum = forcesDropTool.getToolNum();

		// vip加成
		if (toolType == ToolType.COPPER) {
			toolNum += vipService.getExpAddRatio(user.getVipLevel()) * toolNum;
		} else if (toolType == ToolType.EXP) {
			toolNum += vipService.getExpAddRatio(user.getVipLevel()) * toolNum;
		}

		// int gainLimit =
		// this.configDataDao.getInt(ConfigKey.USER_DAILY_GAIN_GOLD_LIMIT, 50);

		if (toolType == ToolType.GOLD && !userService.checkUserGoldGainLimit(userId, toolNum)) {
			return null;
		}

		dropToolBOs = toolService.giveTools(userId, toolType, toolId, toolNum, ToolUseType.ADD_FORCES);

		return dropToolBOs;
	}

	/**
	 * 处理怪物通过
	 * 
	 * @param uid
	 * @param forcesId
	 */
	public Set<Integer> handleForcesPass(String userId, int forcesId) {

		UserForces userForces = this.userForcesDao.get(userId, forcesId);

		if (userForces != null) {// 不为空,攻打次数+1
			Date now = new Date();
			int times = userForces.getTimes();
			if (!DateUtils.isSameDay(now, userForces.getUpdatedTime())) {
				times = 1;
			} else {
				times += 1;
			}
			// 攻打次数加1
			this.userForcesDao.updateTimes(userId, forcesId, times);

		} else {// 为空，创建

			Date now = new Date();

			SystemForces systemForces = this.systemForcesDao.get(forcesId);
			userForces = new UserForces();
			userForces.setCreatedTime(now);
			userForces.setForcesId(forcesId);
			userForces.setForcesType(systemForces.getForcesType());
			userForces.setSceneId(systemForces.getSceneId());
			userForces.setStatus(ForcesStatus.STATUS_PASS);
			userForces.setTimes(1);
			userForces.setUpdatedTime(now);
			userForces.setUserId(userId);

			this.userForcesDao.add(userForces);
		}

		if (userForces != null && userForces.getStatus() != ForcesStatus.STATUS_PASS) {// 如果是已经打过,则跳过
			if (section.containsKey(forcesId)) {
				SceneEvent event = new SceneEvent(userId, section.get(forcesId));
				eventServcie.dispatchEvent(event);
			}
			// 打开后面的怪物
			return this.openAfterForces(userId, forcesId);
		}

		return null;
	}

	public Set<Integer> openAfterForces(String userId, int forcesId) {

		List<SystemForces> systemForcesList = this.systemForcesDao.getSystemForcesByPreForcesId(forcesId);
		SystemForces currentForces = this.systemForcesDao.get(forcesId);

		// 当前部队更新为通过
		if (forcesId > 0) {
			this.userForcesDao.updateStatus(userId, forcesId, ForcesStatus.STATUS_PASS, 0);
		}

		List<UserScene> userSceneList = this.userSceneDao.getUserSceneList(userId);

		Set<Integer> userSceneIds = new HashSet<Integer>();
		for (UserScene userScene : userSceneList) {
			userSceneIds.add(userScene.getSceneId());
		}

		// 后面的怪物置为可攻打
		Date now = new Date();
		Set<Integer> forcesTypeList = new HashSet<Integer>();

		for (SystemForces systemForces : systemForcesList) {

			if (systemForces.getForcesType() == ForcesType.FORCES_TYPE_TOWER) {// 爬塔怪不加
				continue;
			}

			int preForcesId = systemForces.getPreForcesId();// 前置怪物1
			int preForcesId2 = systemForces.getPreForcesIdb();// 前置怪物2

			if (preForcesId2 > 0) {
				UserForces userForcesCheck = null;
				if (preForcesId == forcesId) {
					userForcesCheck = this.userForcesDao.get(userId, preForcesId2);
				} else {
					userForcesCheck = this.userForcesDao.get(userId, preForcesId);
				}

				// 第二个前置怪条件未满足
				if (userForcesCheck == null || userForcesCheck.getStatus() != ForcesStatus.STATUS_PASS) {
					continue;
				}
			}

			int openForcesId = systemForces.getForcesId();
			int sceneId = systemForces.getSceneId();
			int forcesType = systemForces.getForcesType();

			forcesTypeList.add(systemForces.getForcesType());

			UserForces userForces = this.userForcesDao.get(userId, openForcesId);
			if (userForces == null) {
				userForces = new UserForces();
				userForces.setForcesId(openForcesId);
				userForces.setUserId(userId);
				userForces.setSceneId(sceneId);
				userForces.setStatus(ForcesStatus.STATUS_ATTACKABLE);
				userForces.setTimes(0);
				userForces.setUpdatedTime(now);
				userForces.setCreatedTime(now);
				userForces.setForcesType(forcesType);
				this.userForcesDao.add(userForces);

				if (!userSceneIds.contains(sceneId)) {// 新开放

					// 添加关卡
					UserScene userScene = new UserScene();
					userScene.setUserId(userId);
					userScene.setCreatedTime(now);
					userScene.setUpdatedTime(now);
					userScene.setPassFlag(0);
					userScene.setSceneId(sceneId);

					logger.debug("添加用 户场景.userId[" + userId + "], sceneId[" + sceneId + "]");
					this.userSceneDao.add(userScene);
					if (currentForces != null) {
						this.userSceneDao.updateScenePassed(userId, currentForces.getSceneId());
					}

					userSceneIds.add(sceneId);
				}

			} else {
				this.userForcesDao.updateStatus(userId, openForcesId, ForcesStatus.STATUS_ATTACKABLE, 0);
			}
		}

		// for (int forcesType : forcesTypeList) {
		// // 推送
		// this.forcesService.pushCurrentForces(userId, forcesType);
		// }

		return forcesTypeList;

	}

	@Override
	public UserSceneBO getCurrentScene(String uid) {
		UserScene userScene = this.userSceneDao.getLastUserScene(uid);
		UserSceneBO userSceneBO = new UserSceneBO();
		userSceneBO.setUserId(userScene.getUserId());
		userSceneBO.setSceneId(userScene.getSceneId());
		userSceneBO.setPassFlag(userScene.getPassFlag());
		return userSceneBO;
	}

	@Override
	public void sweep(String userId, int forcesId, int times, EventHandle handle) {
		User user = userService.get(userId);
		SystemForces systemForces = systemForcesDao.get(forcesId);
		UserForces userForces = userForcesDao.get(userId, forcesId);
		int needPower = systemForces.getNeedPower() * times;
		int attTimes = sweepCheck(userId, forcesId, times, user, systemForces, userForces);
		userForcesDao.updateTimes(userId, forcesId, attTimes);
		userService.reducePower(userId, needPower, systemForces.getForcesType());

		UserSweepInfo sweepInfo = new UserSweepInfo();
		sweepInfo.setUserId(userId);
		sweepInfo.setForcesId(forcesId);
		sweepInfo.setTimes(times);
		sweepInfo.setStatus(0);
		sweepInfo.setPower(needPower);
		Date now = new Date();
		sweepInfo.setCreatedTime(now);
		// 每次扫荡耗时2分钟，计算得出结束时间
		Date endTime = DateUtils.getAfterTime(now, times * 2 * 60);
		sweepInfo.setEndTime(endTime);
		sweepInfo.setUpdatedTime(now);

		if (!userSweepInfoDao.add(sweepInfo)) {
			throw new ServiceException(ServiceReturnCode.FAILD, "添加扫荡信息失败，未知错误");
		}
		BaseEvent evt = new BaseEvent();
		evt.setObject("sweepInfo", sweepInfo);

		handle.handle(evt);
	}

	/**
	 * 扫荡前检测,并返回攻打次数
	 * 
	 * @param userId
	 * @param forcesId
	 */
	private int sweepCheck(String userId, int forcesId, int times, User user, SystemForces systemForces, UserForces userForces) {
		if (times > MAX_SWEEP_TIMES) {
			throw new ServiceException(ServiceReturnCode.FAILD, "扫荡次数超出上限");
		}

		userService.checkUserHeroBagLimit(userId);
		userService.checkUserEquipBagLimit(userId);
		UserSweepInfo sweepInfo = userSweepInfoDao.getCurrentSweep(userId);
		if (sweepInfo != null && (sweepInfo.getStatus() != SweepStatus.SWEEP_RECEIVED && sweepInfo.getStatus() != SweepStatus.SWEEP_STOPED)) {
			throw new ServiceException(ADD_SWEEP_RETURN_ING, "扫荡进行中，无法再次扫荡");
		}

		if (userForces == null || userForces.getStatus() != 2) {
			throw new ServiceException(ADD_SWEEP_RETURN_FORCES_NOT_PASS, "部队没有打过，无法扫荡");
		}

		if (user.getPower() < systemForces.getNeedPower()) {
			throw new ServiceException(ADD_SWEEP_RETURN_POWER_NOT_ENOUGH, "体力不足");
		}
		int needPower = systemForces.getNeedPower() * times;
		if (user.getPower() < needPower) {
			throw new ServiceException(ServiceReturnCode.FAILD, "体力无法满足请求次数，数据异常");
		}
		int timeLimit = systemForces.getTimesLimit();
		int forcesTimes = userForces.getTimes();
		if (!DateUtils.isSameDay(userForces.getUpdatedTime(), new Date())) {
			forcesTimes = 0;
		}

		if (timeLimit - forcesTimes < times) {
			throw new ServiceException(ADD_SWEEP_RETURN_LIMIT_EXCEED, "超出攻打限制");
		}

		return forcesTimes + times;
	}

	@Override
	public void stopSweep(String userId, EventHandle handle) {
		UserSweepInfo sweepInfo = userSweepInfoDao.getCurrentSweep(userId);
		UserForces userForces = userForcesDao.get(userId, sweepInfo.getForcesId());
		if (sweepInfo == null || sweepInfo.getStatus() != SweepStatus.SWEEP_ING) {
			throw new ServiceException(STOP_SWEEP_NOT_ING, "没有扫荡进行中，无法停止");
		}
		// SystemForces systemForces =
		// systemForcesDao.get(sweepInfo.getForcesId());
		userSweepInfoDao.stopSweep(userId);
		// 表示是否恢复部队次数
		int returnForcesTimes = 0;
		// 如果扫荡开始时间和当前时间是同一天，则恢复部队次数，否则，不恢复
		if (DateUtils.isSameDay(sweepInfo.getCreatedTime(), new Date())) {
			userForcesDao.updateTimes(userId, sweepInfo.getForcesId(), userForces.getTimes() - sweepInfo.getTimes());
			returnForcesTimes = 1;
		}
		userService.returnPower(userId, sweepInfo.getPower(), ToolUseType.ADD_BY_SWEEP_STOP);
		userForcesDao.updateTimes(userId, sweepInfo.getForcesId(), userForces.getTimes() - sweepInfo.getTimes());

		BaseEvent evt = new BaseEvent();
		evt.setObject("sweepInfo", sweepInfo);
		evt.setObject("ft", returnForcesTimes);
		handle.handle(evt);
	}

	@Override
	public void speedUpSweep(String userId) {
		User user = userService.get(userId);
		UserSweepInfo sweepInfo = userSweepInfoDao.getCurrentSweep(userId);
		int needGold = checkSpeedUpSweep(user, sweepInfo);
		if (!userService.reduceGold(userId, needGold, ToolUseType.REDUCE_SPEED_UP_SWEEP, user.getLevel())) {
			throw new ServiceException(ServiceReturnCode.FAILD, "金币扣除失败");
		}
		userSweepInfoDao.updateSweepComplete(userId);
	}

	/**
	 * 加速扫荡检测，并返回所需金币
	 * 
	 * @param userId
	 */
	private int checkSpeedUpSweep(User user, UserSweepInfo sweepInfo) {
		if (sweepInfo == null || sweepInfo.getStatus() != SweepStatus.SWEEP_ING) {
			throw new ServiceException(SPEED_UP_SWEEP_NOT_ING, "当前没有进行中的扫荡，无法加速");
		}
		long nowTime = System.currentTimeMillis();
		long endTime = sweepInfo.getEndTime().getTime();
		long diff = endTime - nowTime;
		if (diff < 0) {
			throw new ServiceException(SPEED_UP_SWEEP_NOT_ING, "当前没有进行中的扫荡，无法加速");
		}
		long needGold = 0;
		if (diff < 1) {
			needGold = ONE_MINUTE_SWEEP_UP_NEED_GOLD;
		} else if (diff % (60 * 1000) == 0) {
			// 刚好能够整除1分钟时，直接用分钟数 乘以每分钟加速所需金币
			needGold = (diff / (60 * 1000)) * ONE_MINUTE_SWEEP_UP_NEED_GOLD;
		} else {
			// 不能整除时，分钟数+1
			needGold = (diff / (60 * 1000) + 1) * ONE_MINUTE_SWEEP_UP_NEED_GOLD;
		}

		if (user.getGoldNum() < needGold) {
			throw new ServiceException(SPEED_UP_SWEEP_GOLD_NOT_ENOUGH, "所需金币不足，无法加速");
		}

		return (int) needGold;
	}

	@Override
	public void receiveSweep(String userId, EventHandle handle) {

		User user = userService.get(userId);
		UserSweepInfo sweepInfo = userSweepInfoDao.getCurrentSweep(userId);
		checkReceiveSweep(user, sweepInfo);

		CommonDropBO commonDropBO = new CommonDropBO();
		int oldLevel = user.getLevel();

		SystemUserLevel systemUserLevel = this.systemUserLevelDao.get(user.getLevel());
		int oldAttack = systemUserLevel.getAttack();
		int oldDefense = systemUserLevel.getDefense();

		if (!userSweepInfoDao.updateSweepReceived(userId)) {
			throw new ServiceException(ServiceReturnCode.FAILD, "领取奖励失败");
		}

		// updateForcesTimes(userId, sweepInfo);

		List<ForcesDropTool> forcesDropToolList = this.forcesDropToolDao.getForcesDropToolList(sweepInfo.getForcesId());

		Collection<ForcesDropTool> dropTools = ForcesDropHelper.dropTools(forcesDropToolList, sweepInfo.getTimes());

		// long online = userOnlineLogDao.getUserOnline(userId);
		for (ForcesDropTool forcesDropTool : dropTools) {
			try {
				List<DropToolBO> bos = this.pickUpForcesDropTool(userId, forcesDropTool);
				if (bos == null || bos.size() == 0) {
					continue;
				}

				for (DropToolBO dropToolBO : bos) {
					this.toolService.appendToDropBO(userId, commonDropBO, dropToolBO);
				}
			} catch (Exception e) {
				// 发送奖励失败，但仍然返回成功和已发送奖品，只做输出处理
				logger.error(e.getMessage(), e);
			}

		}
		user = this.userService.get(userId);

		int newLevel = user.getLevel();
		if (newLevel > oldLevel) {
			int levelUp = newLevel - oldLevel;
			systemUserLevel = this.systemUserLevelDao.get(newLevel);
			commonDropBO.setLevelUp(levelUp);
			commonDropBO.setAttackAdd(systemUserLevel.getAttack() - oldAttack);
			commonDropBO.setDefenseAdd(systemUserLevel.getDefense() - oldDefense);
		}

		BaseEvent evt = new BaseEvent();
		evt.setObject("forcesDropBO", commonDropBO);
		evt.setObject("forcesId", sweepInfo.getForcesId());
		evt.setObject("times", sweepInfo.getTimes());
		handle.handle(evt);

		// 活跃度任务
		UserForces userForces = this.userForcesDao.get(userId, sweepInfo.getForcesId());
		if (userForces != null) {
			sweepInfo.getForcesId();
			if (userForces.getForcesType() == ForcesType.FORCES_TYPE_NORMAL) {
				activityTaskService.updateActvityTask(userId, ActivityTargetType.NORMAL_FORCES, sweepInfo.getTimes());
			} else if (userForces.getForcesType() == ForcesType.FORCES_TYPE_ELITE) {
				activityTaskService.updateActvityTask(userId, ActivityTargetType.ELITE_FORCES, sweepInfo.getTimes());
			}
		} else {
			logger.error("扫荡时计算用户活跃度奖励出错，获取不到用户的怪物部队信息.userId[" + userId + "], forcesId[" + sweepInfo.getForcesId() + "]");
		}
	}

	/**
	 * 更新攻打次数
	 * 
	 * @param userId
	 * @param sweepInfo
	 */
	// private void updateForcesTimes(String userId, UserSweepInfo sweepInfo) {
	// UserForces userForces = forcesDao.get(userId, sweepInfo.getForcesId());
	//
	// int forcesTimes = userForces.getTimes();
	// if (!DateUtils.isSameDay(userForces.getUpdatedTime(), new Date())) {
	// forcesTimes = 0;
	// }
	//
	// userForcesDao.updateTimes(userId, sweepInfo.getForcesId(), forcesTimes +
	// sweepInfo.getTimes());
	// }

	/**
	 * 领取扫荡奖励检测
	 * 
	 * @param user
	 * @param sweepInfo
	 */
	private void checkReceiveSweep(User user, UserSweepInfo sweepInfo) {
		if (sweepInfo == null || sweepInfo.getStatus() != SweepStatus.SWEEP_COMPLETE) {
			throw new ServiceException(SPEED_UP_SWEEP_NOT_ING, "当前没有可领取的扫荡奖励");
		}
	}

	@Override
	public boolean updateSweepComplete(String userId) {
		return userSweepInfoDao.updateSweepComplete(userId);
	}

	@Override
	public SweepInfoBO getUserSweepInfo(String userId) {
		UserSweepInfo sweepInfo = userSweepInfoDao.getCurrentSweep(userId);
		SweepInfoBO bo = new SweepInfoBO();
		if (sweepInfo != null) {
			sweepInfo = checkComplete(sweepInfo);
			bo.setEndTime(sweepInfo.getEndTime().getTime());
			bo.setStartTime(sweepInfo.getCreatedTime().getTime());
			bo.setStatus(sweepInfo.getStatus());
			bo.setPower(sweepInfo.getPower());
			bo.setTimes(sweepInfo.getTimes());
			bo.setForcesId(sweepInfo.getForcesId());
		} else {
			bo.setStatus(-2);
		}
		return bo;
	}

	private UserSweepInfo checkComplete(UserSweepInfo sweepInfo) {
		long now = System.currentTimeMillis();
		long endTime = sweepInfo.getEndTime().getTime();
		// 表示已完成
		if (now >= endTime) {
			if (userSweepInfoDao.updateSweepComplete(sweepInfo.getUserId())) {
				sweepInfo.setStatus(SweepStatus.SWEEP_COMPLETE);
			}
		}
		return sweepInfo;
	}

	@Override
	public void assault(String uid, Integer sid, EventHandle eventHandle) {
		List<UserForces> forcesList = userForcesDao.getUserForcesList(uid, sid);

		// 检测scene是否有可突击的部队
		checkSceneForces(uid, forcesList);

		// 减少突击令牌
		if (!toolService.reduceTool(uid, ToolType.MATERIAL, ToolId.TOOL_ID_ASSAULT_TOKEN, 1, ToolUseType.REDUCE_PASS_FORCES_BATTLE)) {
			throw new ServiceException(ASSAULT_TOKEN_NOT_ENOUGH, "突击令不足");
		}

		// 改变部队攻打次数
		int times = 0;
		int totalTimes = 0;
		int forcesType = 0;
		List<Integer> forcesIds = new ArrayList<Integer>();
		Map<Integer, Integer> timesMap = new HashMap<Integer, Integer>();
		for (UserForces userForces : forcesList) {

			if (userForces.getStatus() != ForcesStatus.STATUS_PASS) {// 没打过的不能突击
				continue;
			}

			SystemForces sf = systemForcesDao.get(userForces.getForcesId());
			// 最大次数一致，只取一次，策划改成不一致，直接暴走
			if (times == 0) {
				times = sf.getTimesLimit();
			}
			int forcesId = sf.getForcesId();

			// 将可以突击的部队以及次数加入timesMap，用于后续的奖励发放计算，部队ID加入forcesIds，用于更新次数
			// int ts = times;
			// 如果更新日期不同，则计算剩余，并加入map，否则直接将最大次数加入map
			if (DateUtils.isSameDay(userForces.getUpdatedTime(), new Date())) {
				if (userForces.getTimes() < sf.getTimesLimit()) {
					timesMap.put(forcesId, sf.getTimesLimit() - userForces.getTimes());
					forcesIds.add(forcesId);
				}
			} else {
				timesMap.put(forcesId, times);
				forcesIds.add(forcesId);
			}

			// 活跃度奖励任务用到
			totalTimes += times;
			if (forcesType == 0) {
				forcesType = userForces.getForcesType();
			}
		}
		userForcesDao.updateTimes(uid, times, forcesIds);
		// 发奖励
		User user = userService.get(uid);
		int oldLevel = user.getLevel();
		SystemUserLevel systemUserLevel = this.systemUserLevelDao.get(user.getLevel());
		int oldAttack = systemUserLevel.getAttack();
		int oldDefense = systemUserLevel.getDefense();

		CommonDropBO commonDropBO = new CommonDropBO();
		for (UserForces userForces : forcesList) {
			// forcesId不存在map中，则无需计算掉落
			if (!timesMap.containsKey(userForces.getForcesId())) {
				continue;
			}

			List<ForcesDropTool> forcesDropToolList = this.forcesDropToolDao.getForcesDropToolList(userForces.getForcesId());

			Collection<ForcesDropTool> dropTools = ForcesDropHelper.dropTools(forcesDropToolList, timesMap.get(userForces.getForcesId()));

			for (ForcesDropTool forcesDropTool : dropTools) {
				try {
					List<DropToolBO> bos = this.pickUpForcesDropTool(uid, forcesDropTool);
					if (bos == null || bos.size() == 0) {
						continue;
					}

					for (DropToolBO dropToolBO : bos) {
						this.toolService.appendToDropBO(uid, commonDropBO, dropToolBO);
					}
				} catch (Exception e) {
					// 发送奖励失败，但仍然返回成功和已发送奖品，只做输出处理
					logger.error(e.getMessage(), e);
				}

			}
		}

		user = this.userService.get(uid);

		int newLevel = user.getLevel();
		if (newLevel > oldLevel) {
			int levelUp = newLevel - oldLevel;
			systemUserLevel = this.systemUserLevelDao.get(newLevel);
			commonDropBO.setLevelUp(levelUp);
			commonDropBO.setAttackAdd(systemUserLevel.getAttack() - oldAttack);
			commonDropBO.setDefenseAdd(systemUserLevel.getDefense() - oldDefense);
		}

		BaseEvent evt = new BaseEvent();
		evt.setObject("forcesDropBO", commonDropBO);
		eventHandle.handle(evt);

		// 活跃度任务
		if (forcesType == ForcesType.FORCES_TYPE_NORMAL) {
			activityTaskService.updateActvityTask(uid, ActivityTargetType.NORMAL_FORCES, totalTimes);
		} else if (forcesType == ForcesType.FORCES_TYPE_ELITE) {
			activityTaskService.updateActvityTask(uid, ActivityTargetType.ELITE_FORCES, totalTimes);
		}

	}

	/**
	 * 检测场景部队是否打过以及是否有足够的次数可突击
	 * 
	 * @param sid
	 */
	private void checkSceneForces(String uid, List<UserForces> forcesList) {
		if (forcesList == null || forcesList.size() == 0) {
			throw new ServiceException(ASSAULT_SCENE_FORCE_CHECK_FAILED, "没有部队可突击");
		}

		if (forcesList != null && forcesList.size() > 0) {
			for (UserForces userForces : forcesList) {
				SystemForces sf = systemForcesDao.get(userForces.getForcesId());
				if (userForces.getStatus() == ForcesStatus.STATUS_PASS && !DateUtils.isSameDay(userForces.getUpdatedTime(), new Date())) {
					return;
				}
				if (userForces.getStatus() == ForcesStatus.STATUS_PASS && userForces.getTimes() < sf.getTimesLimit()) {
					return;
				}
			}
		}

		throw new ServiceException(ASSAULT_SCENE_FORCE_CHECK_FAILED, "没有部队可突击");
	}
}
