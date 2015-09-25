package com.lodogame.ldsg.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import com.lodogame.game.dao.ConfigDataDao;
import com.lodogame.game.dao.RuntimeDataDao;
import com.lodogame.game.dao.TavernDropToolDao;
import com.lodogame.game.dao.UserExtinfoDao;
import com.lodogame.game.dao.UserTavernDao;
import com.lodogame.game.dao.UserTavernLogDao;
import com.lodogame.game.dao.UserTavernRebateLogDao;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.game.utils.IDGenerator;
import com.lodogame.game.utils.JedisUtils;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.ldsg.bo.UserTavernBO;
import com.lodogame.ldsg.checker.TaskChecker;
import com.lodogame.ldsg.constants.ActivityTargetType;
import com.lodogame.ldsg.constants.ConfigKey;
import com.lodogame.ldsg.constants.InitDefine;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.constants.TaskTargetType;
import com.lodogame.ldsg.constants.TavernConstant;
import com.lodogame.ldsg.constants.ToolUseType;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.TavernDrawEvent;
import com.lodogame.ldsg.event.DropHeroEvent;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.helper.TavernHelper;
import com.lodogame.ldsg.service.ActivityTaskService;
import com.lodogame.ldsg.service.EventServcie;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.TaskService;
import com.lodogame.ldsg.service.TavernService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.RuntimeData;
import com.lodogame.model.SystemHero;
import com.lodogame.model.TavernAmendDropTool;
import com.lodogame.model.TavernDropTool;
import com.lodogame.model.User;
import com.lodogame.model.UserExtinfo;
import com.lodogame.model.UserTavern;
import com.lodogame.model.UserTavernRebateLog;

public class TavernServiceImpl implements TavernService {

	@Autowired
	private TavernDropToolDao tavernDropToolDao;

	@Autowired
	private UserTavernDao userTavernDao;

	@Autowired
	private UserService userService;

	@Autowired
	private HeroService heroService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private UserExtinfoDao userExtinfoDao;

	@Autowired
	private UserTavernLogDao userTavernLogDao;

	@Autowired
	private ConfigDataDao configDataDao;

	@Autowired
	private RuntimeDataDao runtimeDataDao;

	@Autowired
	private ActivityTaskService activityTaskService;

	@Autowired
	private EventServcie eventServcie;

	@Autowired
	private UserTavernRebateLogDao userTavernRebateLogDaoImpl;

	/**
	 * 获取冷却时间
	 * 
	 * @param type
	 * @return
	 */

	public boolean draw(String userId, int type, int times, EventHandle handle) {

		if (times != 1 && times != 9) {
			throw new ServiceException(ServiceReturnCode.FAILD, "抽奖失败,抽奖次数不对.userId[" + userId + "], times[" + times + "]");
		}

		this.checkHeroBag(userId);

		// 用户的抽奖信息
		UserTavern userTavern = this.getUserTavern(userId, type);

		// 判断是否要花金币
		boolean isNeedMoney = this.isNeedMoney(userTavern, type, times);

		// 扣钱(银币或者金币)
		this.costMoney(userId, type, times, isNeedMoney);

		// 是否掉过四星以上武将
		boolean isLogExist = true;
		if (type == TavernConstant.DRAW_TYPE_3) {
			isLogExist = this.userTavernLogDao.isExistLog(userId);
		}

		// 是否修正
		boolean amend = false;
		int amendTimes = userTavern.getAmendTimes();
		int amendLimitTimes = this.getAmendTimesLimit(type);
		if (amendLimitTimes > 0 && amendTimes + times >= amendLimitTimes) {
			amend = true;
		}

		// 计算掉落(正常掉掉落)
		List<TavernDropTool> tavernDropTools = this.tavernDropToolDao.getTavernDropToolList(type);

		// 计算掉落(修正掉落)
		List<TavernAmendDropTool> tavernAmendDropTools = this.tavernDropToolDao.getTavernAmendDropToolList(type);

		// 全局游标变量
		long ind = this.getTavernDropToolInd(type, times);

		List<TavernDropTool> tavernDropToolList = new ArrayList<TavernDropTool>();

		// 如果第一次 元宝type=DRAW_TYPE_2,必给四星武将。 元宝type=DRAW_TYPE_3，必给五星武将
		if (userTavern.getHadUsedMoney() == 0 && isNeedMoney && type == TavernConstant.DRAW_TYPE_2) {
			// 必得四星武将
			tavernDropToolList = TavernHelper.draw(tavernDropTools, tavernAmendDropTools, times, ind, amend, isLogExist, 1);
		} else if (userTavern.getHadUsedMoney() == 0 && isNeedMoney && type == TavernConstant.DRAW_TYPE_3) {
			// 必得五星武将
			tavernDropToolList = TavernHelper.draw(tavernDropTools, tavernAmendDropTools, times, ind, amend, isLogExist, 2);
		} else {
			tavernDropToolList = TavernHelper.draw(tavernDropTools, tavernAmendDropTools, times, ind, amend, isLogExist, 0);
		}

		if (!isLogExist) {
			this.userTavernLogDao.addTavernLog(userId);
		}

		// 重置修正值
		boolean resetAmend = false;
		if (amend) {
			resetAmend = true;
		} else {
			for (TavernDropTool tavernDropTool : tavernDropToolList) {
				int systemHeroId = tavernDropTool.getSystemHeroId();
				SystemHero systemHero = this.heroService.getSysHero(systemHeroId);
				if (systemHero.getHeroStar() == 5) {
					// resetAmend = true;
				}
			}
		}

		if (userTavern.getHadUsedMoney() == 1 || isNeedMoney) {
			// 更新用户抽奖信息
			this.updateUserTavern(userId, type, times, userTavern, isNeedMoney, resetAmend, 1);
		} else {
			this.updateUserTavern(userId, type, times, userTavern, isNeedMoney, resetAmend, 0);
		}

		// 获取掉落武将
		Map<String, Integer> heroIdMap = pickDrawTool(userId, tavernDropToolList);

		UserTavernBO userTavernBO = new UserTavernBO();
		userTavernBO.setType(type);

		if (isNeedMoney) {
			userTavernBO.setCoolTime(userTavern.getUpdatedTime().getTime() + TavernHelper.getCoolTimeInterval(type));
		} else {
			userTavernBO.setCoolTime(System.currentTimeMillis() + TavernHelper.getCoolTimeInterval(type));
		}

		if (type == TavernConstant.DRAW_TYPE_1) {
			this.activityTaskService.updateActvityTask(userId, ActivityTargetType.COPPER_TAVERN_DRAW, times);
		} else {
			if (isNeedMoney) {
				this.activityTaskService.updateActvityTask(userId, ActivityTargetType.GOLD_TAVERN_DRAW, times);
			}
		}

		// 组装Tips，没有用金币DRAW_TYPE_3招过的，提示DRAW_TYPE_TIPS_FIRST，用过的话，提示DRAW_TYPE_TIPS_HERO
		UserTavern userTavern3 = this.getUserTavern(userId, TavernConstant.DRAW_TYPE_3);
		int amendTimes3 = userTavern3.getAmendTimes();
		int amendLimitTimes3 = this.getAmendTimesLimit(type);
		if (userTavern3.getHadUsedMoney() != 1) {
			userTavernBO.setTips(TavernConstant.DRAW_TYPE_TIPS_FIRST);
		} else {
			if (type != TavernConstant.DRAW_TYPE_3) {
				userTavernBO.setTips(TavernConstant.DRAW_TYPE_TIPS_HERO.replaceFirst("N", String.valueOf(amendLimitTimes3 - amendTimes3)));
			} else {
				if (resetAmend) {
					userTavernBO.setTips(TavernConstant.DRAW_TYPE_TIPS_HERO.replaceFirst("N", String.valueOf(amendLimitTimes)));
				} else {

					userTavernBO.setTips(TavernConstant.DRAW_TYPE_TIPS_HERO.replaceFirst("N", String.valueOf(amendLimitTimes3 - amendTimes3)));
				}

			}
		}

		// 武将更新事件
		TavernDrawEvent tavernDrawEvent = new TavernDrawEvent(userId, new ArrayList<String>(heroIdMap.keySet()), userTavernBO);
		handle.handle(tavernDrawEvent);

		// 处任务
		this.handleTask(userId);

		return true;
	}

	/**
	 * 获取修正值上限
	 * 
	 * @param type
	 * @return
	 */
	private int getAmendTimesLimit(int type) {

		int amendLimitTimes = 0;

		switch (type) {
		case TavernConstant.DRAW_TYPE_1:
			amendLimitTimes = this.configDataDao.getInt(ConfigKey.TAVERN_AMEND_TIMES_1 + type, 0);
			break;
		case TavernConstant.DRAW_TYPE_2:
			amendLimitTimes = this.configDataDao.getInt(ConfigKey.TAVERN_AMEND_TIMES_2 + type, 200);
			break;
		case TavernConstant.DRAW_TYPE_3:
			amendLimitTimes = this.configDataDao.getInt(ConfigKey.TAVERN_AMEND_TIMES_3 + type, 40);
			break;
		}

		return amendLimitTimes;
	}

	/**
	 * 获取当前掉落池的游标
	 * 
	 * @param type
	 * @return
	 */
	private long getTavernDropToolInd(int type, int times) {

		String key = RedisKey.getTavernDropPoolIndKey(type);
		if (!JedisUtils.exists(key)) {
			RuntimeData runtimeData = this.runtimeDataDao.get(key);
			long totalTimes = 0;
			if (runtimeData != null) {
				totalTimes = runtimeData.getValue();
				JedisUtils.incrBy(key, totalTimes);
			} else {
				runtimeData = new RuntimeData();
				runtimeData.setCreatedTime(new Date());
				runtimeData.setValue(0);
				runtimeData.setValueKey(key);
				this.runtimeDataDao.add(runtimeData);
			}
		}

		long value = JedisUtils.incrBy(key, times);

		// 更新到数据库，防止数据丢失，可以移到其它地方做
		this.runtimeDataDao.set(key, value);

		return value;
	}

	/**
	 * 获取用户抽奖记录信息
	 * 
	 * @param userId
	 * @param type
	 * @return
	 */
	private UserTavern getUserTavern(String userId, int type) {

		UserTavern userTavern = this.userTavernDao.get(userId, type);

		if (userTavern == null) {

			Date now = new Date();
			userTavern = new UserTavern();
			userTavern.setType(type);
			userTavern.setUserId(userId);
			userTavern.setCreatedTime(now);
			userTavern.setUpdatedTime(now);
			userTavern.setTotalTimes(0);
			userTavern.setAmendTimes(0);
			userTavern.setHadUsedMoney(0);
			this.userTavernDao.add(userTavern);
		}

		return userTavern;
	}

	/**
	 * 判断当前武将背包是否已经满意了
	 * 
	 * @param userId
	 */
	private void checkHeroBag(String userId) {
		int userHeroCount = this.heroService.getUserHeroCount(userId);

		UserExtinfo userExtinfo = this.userExtinfoDao.get(userId);
		int bagMax = InitDefine.HERO_BAG_INIT;
		if (userExtinfo != null) {
			bagMax = userExtinfo.getHeroMax();
		}

		if (userHeroCount + 1 > bagMax) {
			throw new ServiceException(ServiceReturnCode.USER_HERO_BAG_LIMIT_EXCEED, "抽奖失败,武将背包超过上限.userId[" + userId + "]");
		}
	}

	private void costMoney(String userId, int type, int times, boolean isNeedMoney) {
		String now = DateUtils.getDate();
		if (type == TavernConstant.DRAW_TYPE_1) {// 用银币
			int money = TavernHelper.getCostCopper(type, times);
			if (!this.userService.reduceCopper(userId, money, ToolUseType.REDUCE_TAVERN_HERO)) {
				String message = "抽奖失败，银币不足.userId[" + userId + "], type[" + type + "]";
				throw new ServiceException(DRAW_NOT_ENOUGH_COPPER, message);
			}
		} else {

			// 如果需要花钱
			if (isNeedMoney) {

				// 用金币
				int money = TavernHelper.getCostMoney(type, times);
				User user = userService.get(userId);
				if (!this.userService.reduceGold(userId, money, ToolUseType.REDUCE_TAVERN_END_CD, user.getLevel())) {
					String message = "抽奖失败，金币不足.userId[" + userId + "], type[" + type + "]";
					throw new ServiceException(DRAW_NOT_ENOUGH_GOLD, message);
				}
				userTavernRebateLogDaoImpl.setNum(userId, now, times, type);
				/*
				 * if(type == TavernConstant.TAVERN_REBATE_1){
				 * if(userTavernRebateLogDaoImpl.isRecord(userId, now, type)){
				 * userTavernRebateLogDaoImpl.setNum(userId, now, times,
				 * TavernConstant.TAVERN_REBATE_1); }else{ UserTavernRebateLog
				 * userTavernRebateLog = new UserTavernRebateLog();
				 * userTavernRebateLog.setCreatedTime(new Date());
				 * userTavernRebateLog.setTimes(times);
				 * userTavernRebateLog.setType(TavernConstant.TAVERN_REBATE_1);
				 * userTavernRebateLog.setUserId(userId);
				 * 
				 * userTavernRebateLogDaoImpl.addRecord(userTavernRebateLog); }
				 * }else{//大摆筵席 if(userTavernRebateLogDaoImpl.isRecord(userId,
				 * now, type)){ userTavernRebateLogDaoImpl.setNum(userId, now,
				 * times, TavernConstant.TAVERN_REBATE_2); }else{
				 * UserTavernRebateLog userTavernRebateLog = new
				 * UserTavernRebateLog(); userTavernRebateLog.setCreatedTime(new
				 * Date()); userTavernRebateLog.setTimes(times);
				 * userTavernRebateLog.setType(TavernConstant.TAVERN_REBATE_2);
				 * userTavernRebateLog.setUserId(userId);
				 * userTavernRebateLogDaoImpl.addRecord(userTavernRebateLog); }
				 * }
				 */
			}
		}
	}

	private Map<String, Integer> pickDrawTool(String userId, List<TavernDropTool> tavernDropToolList) {
		Map<String, Integer> heroIdMap = new HashMap<String, Integer>();
		for (TavernDropTool tavernDropTool : tavernDropToolList) {

			int systemHeroId = tavernDropTool.getSystemHeroId();

			String userHeroId = IDGenerator.getID();
			heroIdMap.put(userHeroId, systemHeroId);

			SystemHero systemHero = this.heroService.getSysHero(systemHeroId);
			// 英雄星数大于等于5星,发送跑马灯
			if (systemHero.getHeroStar() >= 5) {
				User user = this.userService.get(userId);
				DropHeroEvent toolDropEvent = new DropHeroEvent(userId, user.getUsername(), systemHero.getHeroName(), systemHero.getHeroStar(), "酒馆中");
				eventServcie.dispatchEvent(toolDropEvent);
			}

		}

		this.heroService.addUserHero(userId, heroIdMap, ToolUseType.ADD_TAVERN_DROP_HERO);
		return heroIdMap;
	}

	/**
	 * 处理抽奖任务相关
	 * 
	 * @param userId
	 */
	private void handleTask(String userId) {
		// 任务处理
		this.taskService.updateTaskFinish(userId, 1, new EventHandle() {

			@Override
			public boolean handle(Event event) {
				return false;
			}
		}, new TaskChecker() {

			@Override
			public boolean isFinish(int systemTaskId, int taskTarget, Map<String, String> params) {

				if (taskTarget == TaskTargetType.TASK_TYPE_TAVERN_DRAW) {
					return true;
				}
				return false;
			}
		});
	}

	/**
	 * 更新用户抽奖信息
	 * 
	 * @param userId
	 * @param type
	 * @param times
	 * @param userTavern
	 * @param isNeedMoney
	 * @param resetAmend
	 * @return
	 */
	private void updateUserTavern(String userId, int type, int times, UserTavern userTavern, boolean isNeedMoney, boolean resetAmend, int hadUsedMoney) {

		Date updatedTime = null;
		int amendTimes = userTavern.getAmendTimes();
		if (isNeedMoney == false) {
			updatedTime = new Date();
		} else {
			amendTimes += times;
		}

		// 修改抽奖记录
		int totalTimes = userTavern.getTotalTimes() + times;

		if (resetAmend) {
			amendTimes = 0;
		}

		this.userTavernDao.updateTavernInfo(userId, type, updatedTime, totalTimes, amendTimes, hadUsedMoney);

	}

	/**
	 * 是否需要花费金币
	 * 
	 * @param userTavern
	 * @return
	 */
	private boolean isNeedMoney(UserTavern userTavern, int type, int times) {

		boolean costMoney = false;

		if (userTavern == null) {
			return false;
		}

		User user = this.userService.get(userTavern.getUserId());
		if (user.getLevel() < 16) {
			return true;
		}

		if (type == TavernConstant.DRAW_TYPE_1) {
			return false;
		} else {

			if (times > 1) {// 不是1次的肯定要钱
				return true;
			}

			// 冷却时间
			long coolTimeInterval = TavernHelper.getCoolTimeInterval(userTavern.getType());

			if (userTavern != null) {
				Date updateTime = userTavern.getUpdatedTime();
				Date now = new Date();
				if (updateTime.getTime() + coolTimeInterval > now.getTime()) {
					// 冷却时间未到
					costMoney = true;
				}

			}
		}

		return costMoney;
	}

	@Override
	public List<UserTavernBO> getTavernCDInfo(String userId) {
		List<UserTavern> list = userTavernDao.getList(userId);

		Map<Integer, UserTavern> tavernMap = new HashMap<Integer, UserTavern>();
		for (UserTavern userTavern : list) {
			tavernMap.put(userTavern.getType(), userTavern);
		}

		List<UserTavernBO> boList = new ArrayList<UserTavernBO>();
		for (int i = 1; i < 3; i++) {
			UserTavernBO bo = new UserTavernBO();

			if (tavernMap.containsKey(i)) {
				UserTavern userTavern = tavernMap.get(i);
				bo.setType(userTavern.getType());
				long coolTimeInterval = TavernHelper.getCoolTimeInterval(userTavern.getType());
				bo.setCoolTime(userTavern.getUpdatedTime().getTime() + coolTimeInterval);
				bo.setNeedGold(TavernHelper.getCostMoney(userTavern.getType(), 1));
				bo.setDesc(TavernHelper.getTavernDesc(userTavern.getType()));
				boList.add(bo);
				// type = 3 增加tips
				if (userTavern.getType() == TavernConstant.DRAW_TYPE_3) {
					if (userTavern.getHadUsedMoney() == 0) {
						bo.setTips(TavernConstant.DRAW_TYPE_TIPS_FIRST);
					} else {
						int amendTimes = userTavern.getAmendTimes();
						int amendLimitTimes = this.getAmendTimesLimit(TavernConstant.DRAW_TYPE_3);
						bo.setTips(TavernConstant.DRAW_TYPE_TIPS_HERO.replaceFirst("N", String.valueOf(amendLimitTimes - amendTimes)));
					}
				}

			} else {
				bo.setType(i);
				bo.setCoolTime(System.currentTimeMillis());
				bo.setNeedGold(TavernHelper.getCostMoney(i, 1));
				bo.setDesc(TavernHelper.getTavernDesc(i));

				boList.add(bo);
			}
		}
		return boList;
	}
}
