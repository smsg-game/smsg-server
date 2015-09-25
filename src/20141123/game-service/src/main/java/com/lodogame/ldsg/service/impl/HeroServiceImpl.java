package com.lodogame.ldsg.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.SystemBloodSacrificeDao;
import com.lodogame.game.dao.SystemDeifyAttrDao;
import com.lodogame.game.dao.SystemDeifyDefineDao;
import com.lodogame.game.dao.SystemDeifyNodeDao;
import com.lodogame.game.dao.SystemEquipDao;
import com.lodogame.game.dao.SystemEquipSuitDao;
import com.lodogame.game.dao.SystemHeroAttrDao;
import com.lodogame.game.dao.SystemHeroDao;
import com.lodogame.game.dao.SystemHeroSkillDao;
import com.lodogame.game.dao.SystemHeroUpgradeDao;
import com.lodogame.game.dao.SystemLevelExpDao;
import com.lodogame.game.dao.SystemPassiveSkillDao;
import com.lodogame.game.dao.SystemSkillGroupDao;
import com.lodogame.game.dao.SystemSkillUpgradeDao;
import com.lodogame.game.dao.SystemUserLevelDao;
import com.lodogame.game.dao.UserEquipDao;
import com.lodogame.game.dao.UserExtinfoDao;
import com.lodogame.game.dao.UserHeroDao;
import com.lodogame.game.dao.UserHeroRegenerateDao;
import com.lodogame.game.dao.UserHeroSkillDao;
import com.lodogame.game.dao.UserHeroSkillTrainDao;
import com.lodogame.game.dao.UserOnlineLogDao;
import com.lodogame.game.dao.UserToolDao;
import com.lodogame.game.utils.json.Json;
import com.lodogame.ldsg.bo.BattleHeroBO;
import com.lodogame.ldsg.bo.BloodSacrificeBO;
import com.lodogame.ldsg.bo.DeifyBo;
import com.lodogame.ldsg.bo.DropToolBO;
import com.lodogame.ldsg.bo.MergeSkillBO;
import com.lodogame.ldsg.bo.SystemBloodSacrificeEquipBO;
import com.lodogame.ldsg.bo.SystemBloodSacrificeHeroBO;
import com.lodogame.ldsg.bo.SystemBloodSacrificeLooseBO;
import com.lodogame.ldsg.bo.SystemBloodSacrificeToolBO;
import com.lodogame.ldsg.bo.UserEquipBO;
import com.lodogame.ldsg.bo.UserEquipPolishBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.bo.UserHeroSkillBO;
import com.lodogame.ldsg.bo.UserHeroSkillTrainBO;
import com.lodogame.ldsg.bo.UserToolBO;
import com.lodogame.ldsg.checker.TaskChecker;
import com.lodogame.ldsg.constants.ActivityTargetType;
import com.lodogame.ldsg.constants.LogTypes;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.constants.TaskTargetType;
import com.lodogame.ldsg.constants.ToolId;
import com.lodogame.ldsg.constants.ToolType;
import com.lodogame.ldsg.constants.ToolUseType;
import com.lodogame.ldsg.event.BaseEvent;
import com.lodogame.ldsg.event.CopperUpdateEvent;
import com.lodogame.ldsg.event.EquipUpdateEvent;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.HeroTowerEvent;
import com.lodogame.ldsg.event.HeroUpdateEvent;
import com.lodogame.ldsg.event.ToolUpdateEvent;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.helper.BOHelper;
import com.lodogame.ldsg.helper.DropToolHelper;
import com.lodogame.ldsg.helper.EquipHelper;
import com.lodogame.ldsg.helper.HeroHelper;
import com.lodogame.ldsg.service.ActivityTaskService;
import com.lodogame.ldsg.service.EquipService;
import com.lodogame.ldsg.service.EventServcie;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.TaskService;
import com.lodogame.ldsg.service.ToolService;
import com.lodogame.ldsg.service.UnSynLogService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.SystemBloodSacrifice;
import com.lodogame.model.SystemDeifyAttr;
import com.lodogame.model.SystemDeifyDefine;
import com.lodogame.model.SystemDeifyNode;
import com.lodogame.model.SystemEquip;
import com.lodogame.model.SystemEquipSuit;
import com.lodogame.model.SystemHero;
import com.lodogame.model.SystemHeroAttr;
import com.lodogame.model.SystemHeroSkill;
import com.lodogame.model.SystemHeroUpgrade;
import com.lodogame.model.SystemLevelExp;
import com.lodogame.model.SystemPassiveSkill;
import com.lodogame.model.SystemSkillGroup;
import com.lodogame.model.SystemSkillUpgrade;
import com.lodogame.model.SystemSuitDetail;
import com.lodogame.model.SystemUserLevel;
import com.lodogame.model.User;
import com.lodogame.model.UserEquip;
import com.lodogame.model.UserHero;
import com.lodogame.model.UserHeroSkill;
import com.lodogame.model.UserHeroSkillTrain;
import com.lodogame.model.UserTool;

public class HeroServiceImpl implements HeroService {

	/**
	 * 转生-转生丹的 tool_id
	 */
	private static int TOOL_PILL_ID = 11051;

	/**
	 * 转生-鬼之契约的 tool_id
	 */
	private static int TOOL_CONTRACT_ID = 11050;

	private static final Logger LOG = Logger.getLogger(HeroServiceImpl.class);

	private static final Logger statLogger = Logger.getLogger("stat");

	@Autowired
	private UserToolDao userToolDao;

	@Autowired
	private UserHeroRegenerateDao userHeroRegenerateDao;

	@Autowired
	private UserHeroDao userHeroDao;

	@Autowired
	private UserHeroDao userHeroDaoMysqlImpl;

	@Autowired
	private SystemHeroDao systemHeroDao;

	@Autowired
	private UserEquipDao userEquipDao;

	@Autowired
	private SystemHeroUpgradeDao systemHeroUpgradeDao;

	@Autowired
	private SystemLevelExpDao systemLevelExpDao;

	@Autowired
	private UserService userService;

	@Autowired
	private SystemUserLevelDao systemUserLevelDao;

	@Autowired
	private EquipService equipService;

	@Autowired
	private SystemHeroSkillDao systemHeroSkillDao;

	@Autowired
	private SystemHeroAttrDao systemHeroAttrDao;

	@Autowired
	private TaskService taskService;

	@Autowired
	private UserExtinfoDao userExtinfoDao;

	@Autowired
	private UserOnlineLogDao userOnlineLogDao;

	@Autowired
	private ActivityTaskService activityTaskService;

	@Autowired
	private SystemSkillGroupDao systemSkillGroupDao;

	@Autowired
	private SystemPassiveSkillDao systemPassiveSkillDao;

	@Autowired
	private UserHeroSkillTrainDao userHeroSkillTrainDao;

	@Autowired
	private SystemSkillUpgradeDao systemSkillUpgradeDao;

	@Autowired
	private ToolService toolService;

	@Autowired
	private SystemEquipDao systemEquipDao;

	@Autowired
	private UnSynLogService unSynLogService;

	@Autowired
	private EventServcie eventServcie;

	@Autowired
	private SystemEquipSuitDao systemEquipSuitDao;

	@Autowired
	private SystemBloodSacrificeDao systemBloodSacrificeDao;

	@Autowired
	private SystemDeifyNodeDao systemDeifyNodeDao;

	@Autowired
	private SystemDeifyDefineDao systemDeifyDefineDao;

	@Autowired
	private SystemDeifyAttrDao systemDeifyAttrDao;

	public List<UserHeroBO> getUserHeroList(String userId, int type) {

		List<UserHeroBO> userHeroBOList = new ArrayList<UserHeroBO>();

		List<UserHero> userHeroList = this.userHeroDao.getUserHeroList(userId);

		for (UserHero userHero : userHeroList) {
			if (userHero.getPos() > 0 || type == 0) {
				UserHeroBO userHeroBO = this.createUserHeroBO(userHero);
				userHeroBOList.add(userHeroBO);
			}
		}

		return userHeroBOList;
	}

	public UserHeroBO getUserHeroBO(String userId, String userHeroId) {
		UserHero userHero = this.userHeroDao.get(userId, userHeroId);
		if (userHero != null) {
			UserHeroBO userHeroBO = this.createUserHeroBO(userHero);
			return userHeroBO;
		}
		return null;
	}

	/**
	 * 判断上场武将个数有没有超过限制
	 * 
	 * @param userId
	 */
	private void checkBattleHeroLimit(String userId) {

		User user = this.userService.get(userId);
		SystemUserLevel systemUserLevel = this.systemUserLevelDao.get(user.getLevel());
		if (systemUserLevel == null) {
			throw new ServiceException(ServiceReturnCode.FAILD, "数据异常，武将等级配置数据不存在.level[" + user.getLevel() + "]");
		}

		// 用户当前上场武将个数
		int userBattleCount = this.userHeroDao.getBattleHeroCount(userId);

		// 人数已经达到上阵限制
		if (userBattleCount >= systemUserLevel.getBattleNum()) {
			throw new ServiceException(CHANGE_POS_HERO_NUM_LIMIT, "布阵失败，上场武将个数超过限制.userBattleCount[" + userBattleCount + "]");
		}

	}

	/**
	 * 判断在阵上的武将是不是至少有一个
	 * 
	 * @param userId
	 */
	private void checkBattleHeroIsZero(String userId) {

		// 用户当前上场武将个数
		int userBattleCount = this.userHeroDao.getBattleHeroCount(userId);

		// 人数已经达到上阵限制
		if (userBattleCount <= 1) {
			throw new ServiceException(CHANGE_POS_HERO_NUM_IS_ZERO, "武将下阵失败，至少有一个武将在阵上.userBattleCount[" + userBattleCount + "]");
		}
	}

	/**
	 * 判断有没有相同的武将在阵上
	 * 
	 * @param userId
	 * @param userHeroId
	 */
	public void checkSameHeroOnEmbattle(String userId, String userHeroId, int pos) {

		UserHeroBO userHeroBO = this.getUserHeroBO(userId, userHeroId);

		List<UserHeroBO> userHeroBOList = this.getUserHeroList(userId, 1);

		for (UserHeroBO bo : userHeroBOList) {
			if (!userHeroBO.getUserHeroId().equals(bo.getUserHeroId()) && userHeroBO.getHeroId() == bo.getHeroId() && bo.getPos() != pos) {// 如果是同一人物开将，且不是换掉它，则是不允许的(如:同一个张飞只允许上一个)
				String message = "开将布阵失败，已经有相同的开将在阵上.userHeroId[" + userHeroId + "], userHeroId2[" + bo.getUserHeroId() + "]";
				throw new ServiceException(CHANGE_POS_SAME_HERO_EXIST, message);
			}
		}

	}

	public boolean changePos(String userId, String userHeroId, int pos, EventHandle handle) {

		LOG.debug("修改武将战斗站位.userId[" + userId + "], userHeroId[" + userHeroId + "], pos[" + pos + "]");

		UserHero userHero = this.get(userId, userHeroId);

		int oldPos = userHero.getPos();

		UserHero targetUserHero = null;
		if (pos > 0) {// 上阵
			targetUserHero = this.userHeroDaoMysqlImpl.getUserHeroByPos(userId, pos);
			if (oldPos == 0 && targetUserHero == null) {// 原来不在阵 上，判断人数有没有超
				this.checkBattleHeroLimit(userId);
			}

			// 判断是不是有相同的武将在阵上
			this.checkSameHeroOnEmbattle(userId, userHeroId, pos);

		} else {// 下阵，一定要最少有一个武将在阵上
			this.checkBattleHeroIsZero(userId);
		}
		// 修改的位置等于自己的位置的话,直接返回true
		if (oldPos == pos) {
			return true;
		}

		boolean success = this.userHeroDao.changePos(userId, userHeroId, pos);
		if (!success) {
			return success;
		}

		// 卸下装备
		if (pos == 0) {
			List<UserEquip> userHeroEquipList = this.userEquipDao.getHeroEquipList(userId, userHeroId);
			for (UserEquip userEquip : userHeroEquipList) {
				this.userEquipDao.updateEquipHero(userId, userEquip.getUserEquipId(), null);
				EquipUpdateEvent equipUpdateEvent = new EquipUpdateEvent(userId, userEquip.getUserEquipId());
				handle.handle(equipUpdateEvent);
			}
		}

		if (targetUserHero != null) {

			if (oldPos > 0) {// 如果武将原来是阵上的，则是两个人换位置
				this.userHeroDao.changePos(userId, targetUserHero.getUserHeroId(), oldPos);
			} else {// 否则将目标武将下阵
				List<UserEquip> userHeroEquipList = this.userEquipDao.getHeroEquipList(userId, targetUserHero.getUserHeroId());
				this.userHeroDao.changePos(userId, targetUserHero.getUserHeroId(), 0);
				// 查询替换者英雄职业
				SystemHero systemHero = systemHeroDao.get(userHero.getSystemHeroId());
				boolean isSendStCode = false;
				for (UserEquip userEquip : userHeroEquipList) {
					String changeTempUserHeroId = null;
					SystemDeifyDefine systemDeifyDefine = this.systemDeifyDefineDao.get(userEquip.getEquipId());
					if (systemDeifyDefine != null) {
						if (userHero.getSystemHeroId() == targetUserHero.getSystemHeroId()) {
							this.userEquipDao.updateEquipHero(userId, userEquip.getUserEquipId(), userHero.getUserHeroId());
							changeTempUserHeroId = userHero.getUserHeroId();
						} else {
							this.userEquipDao.updateEquipHero(userId, userEquip.getUserEquipId(), null);
							isSendStCode = true;
						}
					} else {
						SystemEquip systemEquip = systemEquipDao.get(userEquip.getEquipId());
						if (systemEquip.getCareer() == 100 || systemEquip.getCareer() == systemHero.getCareer()) {
							this.userEquipDao.updateEquipHero(userId, userEquip.getUserEquipId(), userHero.getUserHeroId());
							changeTempUserHeroId = userHero.getUserHeroId();
						} else {
							this.userEquipDao.updateEquipHero(userId, userEquip.getUserEquipId(), null);
							isSendStCode = true;
						}
					}
					EquipUpdateEvent equipUpdateEvent = new EquipUpdateEvent(userId, userEquip.getUserEquipId(), changeTempUserHeroId);
					handle.handle(equipUpdateEvent);
				}
				if (isSendStCode) {
					handle.handle(new BaseEvent());
				}
			}
			HeroUpdateEvent targetHeroUpdateEvent = new HeroUpdateEvent(userId, targetUserHero.getUserHeroId());
			handle.handle(targetHeroUpdateEvent);
		}

		HeroUpdateEvent heroUpdateEvent = new HeroUpdateEvent(userId, userHeroId);
		handle.handle(heroUpdateEvent);

		return success;
	}

	public UserHeroBO upgradePre(String userId, String userHeroId) {

		UserHero userHero = this.get(userId, userHeroId);

		int systemHeroId = userHero.getSystemHeroId();

		SystemHeroUpgrade systemHeroUpgrade = this.systemHeroUpgradeDao.get(systemHeroId);
		if (systemHeroUpgrade != null) {
			userHero.setSystemHeroId(systemHeroUpgrade.getUpgradeHeroId());
		}

		return this.createUserHeroBO(userHero);
	}

	public boolean upgrade(String userId, String userHeroId, List<String> userHeroIdList, boolean force, EventHandle handle) {

		UserHero userHero = this.get(userId, userHeroId);

		int systemHeroId = userHero.getSystemHeroId();

		SystemHero systemHero = this.getSysHero(systemHeroId);

		SystemHeroUpgrade systemHeroUpgrade = this.systemHeroUpgradeDao.get(systemHeroId);
		if (systemHeroUpgrade == null) {
			String message = "武将进阶失败,武将已经到达最高等级.userId[" + userId + "], userHeroId[" + userHeroId + "], systemHeroId[" + systemHeroId + "]";
			LOG.info(message);
			throw new ServiceException(UPGRADE_HERO_IS_MAX_GRADE, message);
		}

		int upgradeHeroId = systemHeroUpgrade.getUpgradeHeroId();

		// 等级不足
		if (userHero.getHeroLevel() < systemHero.getMaxLevel()) {
			String message = "武将进阶失败,武将等级不足.userId[" + userId + "], userHeroId[" + userHeroId + "]";
			LOG.info(message);
			throw new ServiceException(UPGRADE_HERO_LEVEL_NOT_ENOUGH, message);
		}

		// 判断是所需要的道具是否满足
		// 被进阶的英雄列表
		List<UserHero> beStepUpHeroList = new ArrayList<UserHero>();
		this.checkHeroEnough(userId, userHeroId, userHeroIdList, beStepUpHeroList);

		// 统计
		User user = this.userService.get(userId);
		long online = this.userOnlineLogDao.getUserOnline(userId);

		int needMoney = systemHeroUpgrade.getForceUpgradeGold();

		// 如果100%进阶，判断金币够不够
		if (user.getGoldNum() < needMoney) {
			String message = "100%进阶失败，金币不足.userId[" + userId + "], needMoney[" + needMoney + "]";
			throw new ServiceException(UPGRADE_HERO_NOT_ENOUGH_MONEY, message);
		}

		// 扣卡牌
		this.delete(userId, beStepUpHeroList, ToolUseType.REDUCE_UPGRADE_HERO);

		/*
		 * if (force) {// 100进阶
		 * 
		 * if (!this.userService.reduceGold(userId, needMoney,
		 * GoldUseType.TYPE_FORCE_UPGRADE_HERO)) { String message =
		 * "100%进阶失败，金币不足.userId[" + userId + "], needMoney[" + needMoney + "]";
		 * throw new ServiceException(UPGRADE_HERO_NOT_ENOUGH_MONEY, message); }
		 * } else { if (RandomUtils.nextInt(100) >
		 * systemHeroUpgrade.getUpgradeRate()) { String message =
		 * "100%进阶失败，你运气太背"; throw new
		 * ServiceException(UPGRADE_HERO_RATE_FAILURE, message); } }
		 */

		boolean success = this.userHeroDao.changeSystemHeroId(userId, userHeroId, upgradeHeroId);
		if (success) {
			// 武将变更通知
			HeroUpdateEvent heroUpdateEvent = new HeroUpdateEvent(userId, userHeroId);
			handle.handle(heroUpdateEvent);

			// 统计
			statLogger.info("action[upgradeHero], userId[" + userId + "], userLevel[" + user.getLevel() + "], online[" + online + "], systemHeroId[" + systemHeroUpgrade.getUpgradeHeroId() + "]");
			// 英雄进阶日志
			SystemHero systemTempHero = this.getSysHero(upgradeHeroId);
			String heroNameTemp = (systemTempHero != null ? systemTempHero.getHeroName() : "NoHeroName");
			unSynLogService.userHeroOperatorLog(userId, userHero.getUserHeroId(), upgradeHeroId, LogTypes.HERO_STEP_UP, userHero.getHeroExp(), userHero.getHeroLevel(), userHero.getPos(), 0,
					new Date(), heroNameTemp);
			// 英雄被进阶日志
			for (UserHero userHeroTemp : beStepUpHeroList) {
				SystemHero systemBeStepHero = this.getSysHero(userHeroTemp.getSystemHeroId());
				String heroBeStepName = (systemBeStepHero != null ? systemBeStepHero.getHeroName() : "NoHeroName");
				unSynLogService.userHeroOperatorLog(userId, userHeroTemp.getUserHeroId(), userHeroTemp.getSystemHeroId(), LogTypes.HERO_BE_STEP_UP, userHeroTemp.getHeroExp(),
						userHeroTemp.getHeroLevel(), userHeroTemp.getPos(), 0, new Date(), heroBeStepName);
			}
		} else {
			String message = "武将进阶失败,未知异常.userId[" + userId + "], userHeroId[" + userHeroId + "], upgradeHeroId[" + upgradeHeroId + "]";
			LOG.info(message);
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		return true;
	}

	/**
	 * 判断所需要的道具是否满足
	 * 
	 * @param systemHeroId
	 * @param upgradeHeroId
	 * @return
	 */
	private void checkHeroEnough(String userId, String userHeroId, List<String> userHeroIdList, List<UserHero> beStepUpHeroList) {

		UserHero userHero = this.get(userId, userHeroId);
		SystemHero systemHero = this.getSysHero(userHero.getSystemHeroId());

		for (String deleteUserHeroId : userHeroIdList) {

			if (StringUtils.equalsIgnoreCase(userHeroId, deleteUserHeroId)) {
				String message = "数据异常，消耗的武将中包贪进阶的武将.userId[" + userId + "], userHeroId[" + userHeroId + "], deleteUserHeroId[" + deleteUserHeroId + "]";
				LOG.info(message);
				throw new ServiceException(UPGRADE_HERO_TOOL_NOT_ENOUGH, message);
			}

			UserHero deleteUserHero = this.get(userId, deleteUserHeroId);

			SystemHero deleteSystemHero = this.getSysHero(deleteUserHero.getSystemHeroId());

			if (deleteSystemHero.getHeroColor() != systemHero.getHeroColor()) {
				String message = "武将进阶失败,所消耗的武将品质与升级武 将的品质不一样.userId[" + userId + "], systemHero.color[" + systemHero.getHeroColor() + "], deleteSystemHero.color[" + deleteSystemHero.getHeroColor()
						+ "]";
				LOG.info(message);
				throw new ServiceException(UPGRADE_HERO_TOOL_NOT_ENOUGH, message);
			}

			if (deleteSystemHero.getHeroStar() <= 1) {
				String message = "武将进阶失败,所消耗的武将品星级不够.userId[" + userId + "], deleteUserHero.star[" + deleteSystemHero.getHeroStar() + "]";
				LOG.info(message);
				throw new ServiceException(UPGRADE_HERO_TOOL_NOT_ENOUGH, message);
			}

			if (deleteUserHero.getPos() > 0) {// 武将在阵上
				String message = "武将进阶失败,所消耗的武将在阵上.userId[" + userId + "]";
				LOG.info(message);
				throw new ServiceException(UPGRADE_HERO_TOOL_NOT_ENOUGH, message);
			}

			if (deleteUserHero.getLockStatus() > 0) {
				String message = "武将进阶失败,所消耗的武将中包含被锁定的英雄.userId[" + userId + "]";
				LOG.info(message);
				throw new ServiceException(HERO_HAS_LOCKED, message);
			}

			if (beStepUpHeroList != null) {
				beStepUpHeroList.add(deleteUserHero);
			}
		}
	}

	public boolean sell(String userId, List<String> userHeroIdList, EventHandle handle) {

		if (userHeroIdList == null || userHeroIdList.size() == 0) {
			String message = "数据异常，至少选择一名武将.userId[" + userId + "]";
			LOG.error(message);
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		// 可删除验证
		List<UserHero> userHeroList = new ArrayList<UserHero>();
		for (String userHeroId : userHeroIdList) {
			UserHero userHero = this.checkDeleteAble(userId, userHeroId);
			userHeroList.add(userHero);
		}

		// 出售
		int totalPrice = 0;
		List<UserHero> beSellList = new ArrayList<UserHero>();
		for (String userHeroId : userHeroIdList) {
			totalPrice += this.getSellPrice(userId, userHeroId, beSellList);
		}

		int rowCount = this.delete(userId, userHeroList, ToolUseType.REDUCE_SELL_HERO);

		if (rowCount == userHeroIdList.size()) {
			this.userService.addCopper(userId, totalPrice, ToolUseType.ADD_SELL_HERO);
			CopperUpdateEvent copperUpdateEvent = new CopperUpdateEvent(userId);
			handle.handle(copperUpdateEvent);
			// 写英雄出售日志
			for (UserHero userHero : beSellList) {
				SystemHero systemHero = this.getSysHero(userHero.getSystemHeroId());
				String heroName = ((systemHero != null) ? systemHero.getHeroName() : "NOHeroName");
				unSynLogService.userHeroOperatorLog(userId, userHero.getUserHeroId(), userHero.getSystemHeroId(), LogTypes.HERO_SALES, userHero.getHeroExp(), userHero.getHeroLevel(),
						userHero.getPos(), 0, new Date(), heroName);
			}
		} else {
			String message = "数据异常，出售武将失败.userId[" + userId + "], rowCount[" + rowCount + "], userHeroIdList.size[" + userHeroIdList.size() + "]";
			LOG.error(message);
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		return true;
	}

	public int delete(String userId, List<UserHero> userHeroList, int useType) {

		List<String> userHeroIdList = new ArrayList<String>();
		for (UserHero userHero : userHeroList) {
			userHeroIdList.add(userHero.getUserHeroId());
			this.unSynLogService.heroLog(userId, userHero.getUserHeroId(), userHero.getSystemHeroId(), useType, -1, userHero.getHeroExp(), userHero.getHeroLevel());
		}

		int count = 0;
		try {
			count = this.userHeroDao.delete(userId, userHeroIdList);
		} finally {
			unSynLogService.toolLog(userId, ToolType.HERO, ToolType.HERO, userHeroIdList.size(), useType, -1, Json.toJson(userHeroIdList), count == userHeroIdList.size());
		}

		return count;
	}

	public boolean devour(String userId, String userHeroId, List<String> targetUserHeroIdList, EventHandle handle) {

		if (targetUserHeroIdList == null || targetUserHeroIdList.size() == 0) {
			String message = "数据异常，至少选择一名武将.userId[" + userId + "], userHeroId[" + userHeroId + "]";
			LOG.error(message);
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		// 可删除验证
		List<UserHero> userHeroList = new ArrayList<UserHero>();
		for (String targetUserHeroId : targetUserHeroIdList) {

			if (StringUtils.equalsIgnoreCase(userHeroId, targetUserHeroId)) {
				String message = "数据异常，不可以吞掉自己.userId[" + userId + "], userHeroId[" + userHeroId + "]";
				LOG.error(message);
				throw new ServiceException(ServiceReturnCode.FAILD, message);
			}

			UserHero userHero = this.checkDeleteAble(userId, targetUserHeroId);
			userHeroList.add(userHero);
		}

		// 吞噬
		int exp = 0;
		// 被吞噬英雄对象列表
		List<UserHero> beDevourHeroList = new ArrayList<UserHero>();
		for (String targetUserHeroId : targetUserHeroIdList) {
			exp += this.getDevourExp(userId, targetUserHeroId, beDevourHeroList);
		}

		UserHero userHero = this.get(userId, userHeroId);
		User user = this.userService.get(userId);

		if (userHero.getHeroLevel() >= user.getLevel()) {
			throw new ServiceException(DEVOUR_HERO_HERO_LEVEL_OVER_USER_LEVEL, "吞噬武将失败，武将等级超过主将等级.userId[" + userId + "], userLevel[" + user.getLevel() + "], heroLevel[" + userHero.getHeroLevel()
					+ "]");
		}

		SystemHero systemHero = this.getSysHero(userHero.getSystemHeroId());
		if (userHero.getHeroLevel() >= systemHero.getMaxLevel()) {
			throw new ServiceException(DEVOUR_HERO_HERO_LEVEL_OVER, "吞噬武将失败，武将等级已经达到最高等级.userId[" + userId + "]");
		}

		// 加上吞噬获得的 经验
		exp += userHero.getHeroExp();

		SystemLevelExp systemLevelExp = this.systemLevelExpDao.getHeroLevel(exp);

		if (systemLevelExp == null) {
			String message = "数据异常，武将经验等级配置不存在.userId[" + userId + "], userHeroId[" + userHeroId + "], exp[" + exp + "]";
			LOG.error(message);
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		int level = systemLevelExp.getLevel();

		if (level >= systemHero.getMaxLevel()) {
			level = systemHero.getMaxLevel();
			systemLevelExp = this.systemLevelExpDao.getHeroExp(level);
			exp = systemLevelExp.getExp();// 经验截断
		}

		// 不能超过主将等级
		if (level >= user.getLevel()) {
			level = user.getLevel();
			systemLevelExp = this.systemLevelExpDao.getHeroExp(level);
			exp = systemLevelExp.getExp();// 经验截断
		}

		// 扣银币
		// （0.035*武将当前等级+0.5）*武将此次升级操作所得经验值
		// int needCopper = targetUserHeroIdList.size() * 13 *
		// userHero.getHeroLevel();
		// int needCopper = (int) ((0.035 * userHero.getHeroLevel() + 0.5) *
		// (exp - userHero.getHeroExp()));
		int needCopper = (int) (0.4 * (exp - userHero.getHeroExp()));
		if (needCopper < 1) {
			needCopper = 1;
		}
		boolean success = this.userService.reduceCopper(userId, needCopper, ToolUseType.REDUCE_DEVOUR_HERO);
		if (!success) {
			throw new ServiceException(DEVOUR_HERO_COPPER_NOT_ENOUGH, "吞噬武将失败，用户银币不足.userId[" + userId + "], needCopper[" + needCopper + "]");
		}
		CopperUpdateEvent copperUpdateEvent = new CopperUpdateEvent(userId);
		handle.handle(copperUpdateEvent);

		LOG.info("吞噬武将,批量删除武将,userId[" + userId + "], userHeroId[" + userHeroId + "], userHeroIdList[" + StringUtils.join(targetUserHeroIdList, ",") + "]");

		int rowCount = this.delete(userId, userHeroList, ToolUseType.REDUCE_DEVOUR_HERO);

		// 活跃度任务
		this.activityTaskService.updateActvityTask(userId, ActivityTargetType.DEVOUR_HERO, 1);

		LOG.info("吞噬武将，共删除武将数量.rowCount[" + rowCount + "]");

		if (rowCount == targetUserHeroIdList.size()) {
			LOG.info("更新武将经验等级.userId[" + userId + "], userHeroId[" + userHeroId + "exp[" + exp + "], level[" + level + "]");
			this.userHeroDao.updateExpLevel(userId, userHeroId, exp, level);
			// 写入英雄吞噬操作日志
			String heroName = (systemHero != null ? systemHero.getHeroName() : "NoHeroName");
			unSynLogService.userHeroOperatorLog(userId, userHero.getUserHeroId(), userHero.getSystemHeroId(), LogTypes.HERO_DEVOUR, exp, level, userHero.getPos(), exp - userHero.getHeroExp(),
					new Date(), heroName);
			// 写入被吞噬操作日志
			for (UserHero userTargetHero : beDevourHeroList) {
				// 写入英雄操作日志表
				SystemHero systemTempHero = this.getSysHero(userTargetHero.getSystemHeroId());
				String heroNameTemp = (systemTempHero != null ? systemTempHero.getHeroName() : "NoHeroName");
				unSynLogService.userHeroOperatorLog(userId, userTargetHero.getUserHeroId(), userTargetHero.getSystemHeroId(), LogTypes.HERO_BE_DEVOUR, userTargetHero.getHeroExp(),
						userTargetHero.getHeroLevel(), userTargetHero.getPos(), 0, new Date(), heroNameTemp);
			}
		} else {
			String message = "数据异常，吞噬删除武将失败.userId[" + userId + "], userHeroId[" + userHeroId + "], rowCount[" + rowCount + "], targetUserHeroIdList.size[" + targetUserHeroIdList.size() + "]";
			LOG.error(message);
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		return true;
	}

	@Override
	public Map<String, Object> devourPre(String userId, String userHeroId, List<String> targetUserHeroIdList) {

		// 吞噬
		int exp = 0;
		for (String targetUserHeroId : targetUserHeroIdList) {
			exp += this.getDevourExp(userId, targetUserHeroId, null);
		}

		UserHero userHero = this.get(userId, userHeroId);

		// int needCopper = targetUserHeroIdList.size() * 13 *
		// userHero.getHeroLevel();
		LOG.debug("吞噬数量：" + targetUserHeroIdList.size() + "武将等级：" + userHero.getHeroLevel());
		// 加上吞噬获得的 经验
		exp += userHero.getHeroExp();

		SystemLevelExp systemLevelExp = this.systemLevelExpDao.getHeroLevel(exp);

		if (systemLevelExp == null) {
			String message = "数据异常，武将经验等级配置不存在.userHeroId[" + userHeroId + "], exp[" + exp + "]";
			LOG.error(message);
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		int level = systemLevelExp.getLevel();

		SystemHero systemHero = this.getSysHero(userHero.getSystemHeroId());
		if (level >= systemHero.getMaxLevel()) {
			level = systemHero.getMaxLevel();
			systemLevelExp = this.systemLevelExpDao.getHeroExp(level);
			exp = systemLevelExp.getExp();
		}

		// 不能超过用户等级
		User user = this.userService.get(userId);
		if (level >= user.getLevel()) {
			level = user.getLevel();
			systemLevelExp = this.systemLevelExpDao.getHeroExp(level);
			exp = systemLevelExp.getExp();
		}

		// 2013-06-13修改
		// int needCopper = (int) ((0.035 * userHero.getHeroLevel() + 0.5) *
		// (exp - userHero.getHeroExp()));
		int needCopper = (int) (0.4 * (exp - userHero.getHeroExp()));
		if (needCopper < 1) {
			needCopper = 1;
		}
		LOG.debug("升级武将所需银币：" + needCopper);

		userHero.setHeroExp(exp);
		userHero.setHeroLevel(level);

		UserHeroBO userHeroBO = this.createUserHeroBO(userHero);

		Map<String, Object> retVal = new HashMap<String, Object>();
		retVal.put("userHeroBO", userHeroBO);
		retVal.put("copper", needCopper);

		return retVal;
	}

	private int getSellPrice(String userId, String userHeroId, List<UserHero> beSellList) {

		UserHero userHero = this.get(userId, userHeroId);

		int systemHeroId = userHero.getSystemHeroId();
		int heroLevel = userHero.getHeroLevel();

		SystemHeroAttr systemHeroAttr = this.systemHeroAttrDao.getHeroAttr(systemHeroId, heroLevel);

		int price = systemHeroAttr.getRecyclePrice();

		if (beSellList != null) {
			beSellList.add(userHero);
		}
		return price;
	}

	/**
	 * 判断武将是否可以删除(没有上阵，没有装备，没有兵符)
	 * 
	 * @param userId
	 */
	private UserHero checkDeleteAble(String userId, String userHeroId) {

		UserHero userHero = this.get(userId, userHeroId);

		if (!StringUtils.equalsIgnoreCase(userHero.getUserId(), userId)) {
			String message = "武将不可删除,武将所有人不符.userId[" + userId + "], userHeroId[" + userHeroId + "]";
			LOG.info(message);
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		if (userHero.getPos() > 0) {
			String message = "武将不可删除,武将在阵上.userId[" + userId + "], userHeroId[" + userHeroId + "]";
			LOG.info(message);
			throw new ServiceException(DELETE_HERO_IS_IN_EMBATTLE, message);
		}

		List<UserEquip> userHeroEquipList = this.userEquipDao.getHeroEquipList(userId, userHeroId);
		if (userHeroEquipList.size() > 0) {
			String message = "武将不可删除,武将身上佩戴装备.userId[" + userId + "], userHeroId[" + userHeroId + "]";
			LOG.info(message);
			throw new ServiceException(DELETE_HERO_IS_INSTALL_EQUIP, message);
		}

		if (userHero.getLockStatus() > 0) {
			String message = "武将不可删除,所消耗的武将已被锁定.userId[" + userId + "]";
			LOG.info(message);
			throw new ServiceException(HERO_HAS_LOCKED, message);
		}

		return userHero;
	}

	/**
	 * 获取吞噬经验
	 * 
	 * @param userHeroId
	 * @return
	 */
	private int getDevourExp(String userId, String userHeroId, List<UserHero> heroList) {

		UserHero userHero = this.get(userId, userHeroId);

		SystemHeroAttr systemHeroAttr = this.systemHeroAttrDao.getHeroAttr(userHero.getSystemHeroId(), userHero.getHeroLevel());

		if (systemHeroAttr == null) {
			LOG.error("武将等级配置不存在.systemHeroId[" + userHero.getSystemHeroId() + "], heroLevel[" + userHero.getHeroLevel() + "]");
		}
		if (heroList != null) {
			heroList.add(userHero);
		}
		return systemHeroAttr.getMixExp();
	}

	/**
	 * 获取系统武将
	 * 
	 * @param systemHeroId
	 * @return
	 */
	public SystemHero getSysHero(int systemHeroId) {

		SystemHero systemHero = this.systemHeroDao.get(systemHeroId);
		if (systemHero == null) {
			String message = "获取系统武将失败,系统武将不存在.systemHeroId[" + systemHeroId + "]";
			LOG.error(message);
			throw new ServiceException(HERO_NOT_EXIST, message);
		}
		return systemHero;
	}

	public List<SystemHero> getAllSystemHero() {
		return systemHeroDao.getSysHeroList();
	}

	private UserHeroBO createUserHeroBO(UserHero userHero) {

		UserHeroBO userHeroBO = new UserHeroBO();
		userHeroBO.setUserId(userHero.getUserId());
		userHeroBO.setSystemHeroId(userHero.getSystemHeroId());
		userHeroBO.setUserHeroId(userHero.getUserHeroId());
		userHeroBO.setLevel(userHero.getHeroLevel());
		userHeroBO.setExp(userHero.getHeroExp());
		userHeroBO.setPos(userHero.getPos());
		userHeroBO.setLockStatus(userHero.getLockStatus());

		int level = userHero.getHeroLevel();

		SystemHero systemHero = this.systemHeroDao.get(userHero.getSystemHeroId());
		userHeroBO.setHeroId(systemHero.getHeroId());

		SystemHeroAttr systemHeroAttr = this.systemHeroAttrDao.getHeroAttr(userHero.getSystemHeroId(), level);

		if (systemHero != null) {

			userHeroBO.setName(systemHero.getHeroName());
			userHeroBO.setImgId(systemHero.getImgId());
			userHeroBO.setCardId(systemHero.getCardId());

			// 技能
			userHeroBO.setPlan(systemHero.getPlan());
			userHeroBO.setNormalPlan(systemHero.getNormalPlan());
			userHeroBO.setSkill1(systemHero.getSkill1());
			userHeroBO.setSkill2(systemHero.getSkill2());
			userHeroBO.setSkill3(systemHero.getSkill3());
			userHeroBO.setSkill4(systemHero.getSkill4());

			// 职业
			userHeroBO.setCareer(systemHero.getCareer());

			if (systemHeroAttr != null) {
				userHeroBO.setDexp(systemHeroAttr.getMixExp());
				userHeroBO.setHeroId(systemHero.getHeroId());

				// 计算属性值
				userHeroBO.setCrit(systemHeroAttr.getCrit());
				userHeroBO.setDodge(systemHeroAttr.getDuck());
				userHeroBO.setHit(systemHeroAttr.getHit());
				userHeroBO.setLife(systemHeroAttr.getLife());
				userHeroBO.setParry(systemHeroAttr.getParry());
				userHeroBO.setPhysicalDefense(systemHeroAttr.getPhysicalDefense());
				userHeroBO.setPhysicalAttack(systemHeroAttr.getPhysicalAttack());
				userHeroBO.setPrice(systemHeroAttr.getRecyclePrice());
			} else {// 容错
				LOG.error("武将等级配置不存在.userId[" + userHero.getUserId() + "], userHeroId[" + userHero.getUserHeroId() + "], systemHeroId[" + userHero.getSystemHeroId() + "], heroLevel["
						+ userHero.getHeroLevel() + "]");
				userHeroBO.setLife(1);
				userHeroBO.setPhysicalDefense(1);
				userHeroBO.setPhysicalAttack(1);

			}

		}

		// 计算装备的加成
		if (userHero.getPos() > 0) {
			List<UserEquipBO> userEquipBOList = this.equipService.getUserHeroEquipList(userHero.getUserId(), userHero.getUserHeroId());

			for (UserEquipBO userEquipBO : userEquipBOList) {
				userHeroBO.setPhysicalAttack(userHeroBO.getPhysicalAttack() + userEquipBO.getPhysicsAttack());
				userHeroBO.setPhysicalDefense(userHeroBO.getPhysicalDefense() + userEquipBO.getPhysicsDefense());
				userHeroBO.setLife(userHeroBO.getLife() + userEquipBO.getLife());

				// 计算装备加成属性
				if (userEquipBO.getUserEquipPolishBO() != null) {
					UserEquipPolishBO userEquipPolishBO = userEquipBO.getUserEquipPolishBO();
					userHeroBO.setPhysicalAttack(userHeroBO.getPhysicalAttack() + userEquipPolishBO.getAttack());
					userHeroBO.setPhysicalDefense(userHeroBO.getPhysicalDefense() + userEquipPolishBO.getDefense());
					userHeroBO.setLife(userHeroBO.getLife() + userEquipPolishBO.getLife());
				}
			}

			// 判断套装
			SystemEquipSuit systemEquipSuit = this.systemEquipSuitDao.getHeroEquipSuit(systemHero.getHeroId());
			if (systemEquipSuit != null) {

				List<SystemSuitDetail> systemSuitDetailList = this.systemEquipSuitDao.getSuitDetailList(systemEquipSuit.getSuitId());
				// 是否拥有所有套装装备
				boolean getAllEquip = true;
				for (SystemSuitDetail systemSuitDetail : systemSuitDetailList) {
					if (!EquipHelper.hasEquip(userEquipBOList, systemSuitDetail)) {
						getAllEquip = false;
						break;
					}
				}

				if (getAllEquip) {
					userHeroBO.setSuitSkillId(systemEquipSuit.getSkillId());
				}

			}
		}

		// 计算血祭加成
		if (userHero.getBloodSacrificeStage() > 0) {
			SystemHero iSystemHero = systemHeroDao.get(userHero.getSystemHeroId());
			SystemBloodSacrifice systemBloodSacrificeNow = this.systemBloodSacrificeDao.get(iSystemHero.getHeroId(), userHero.getBloodSacrificeStage());
			userHeroBO.setPhysicalAttack((int) (userHeroBO.getPhysicalAttack() + systemHeroAttr.getPhysicalAttack() * (systemBloodSacrificeNow.getAddPhysicalAttackRatio())));
			userHeroBO.setPhysicalDefense((int) (userHeroBO.getPhysicalDefense() + systemHeroAttr.getPhysicalDefense() * (systemBloodSacrificeNow.getAddPhysicalDefenseRatio())));
			userHeroBO.setLife((int) (userHeroBO.getLife() + systemHeroAttr.getLife() * (systemBloodSacrificeNow.getAddLifeRatio())));
			userHeroBO.setSkill5(systemBloodSacrificeNow.getBsSkill());
			userHeroBO.setStage(systemBloodSacrificeNow.getStage());
			userHeroBO.setOutLine(systemBloodSacrificeNow.getOutline());
			userHeroBO.setBloodSacrificeSkillID(systemBloodSacrificeNow.getBsSkill());
		}

		// 增加化神加成
		int deifyNodeLevel = userHero.getDeifyNodeLevel();
		userHeroBO.setDeifyNodeLevel(userHero.getDeifyNodeLevel());
		if (deifyNodeLevel > 0 && systemHero.getHeroColor() == 6) {
			List<SystemDeifyNode> systemDeifyNodeList = this.systemDeifyNodeDao.getSystemDeifyNode(systemHero.getHeroId());
			for (SystemDeifyNode systemDeifyNode : systemDeifyNodeList) {
				if (systemDeifyNode.getDeifyNodeLevel() <= deifyNodeLevel) {
					userHeroBO.setLife(userHeroBO.getLife() + systemDeifyNode.getLife());
					userHeroBO.setPhysicalAttack(userHeroBO.getPhysicalAttack() + systemDeifyNode.getPhysicalAttack());
					userHeroBO.setPhysicalDefense(userHeroBO.getPhysicalDefense() + systemDeifyNode.getPhysicalDefense());
				}
			}
		}

		// 专属神器
		if (systemHero.getHeroColor() == 7 && userHero.getPos() > 0) {
			// List<UserEquip> list =
			// this.userEquipDao.getUserEquipList(userHero.getUserId(),
			// systemDeifyDefine.getDeifyId());
			List<UserEquip> list = this.userEquipDao.getHeroEquipList(userHero.getUserId(), userHero.getUserHeroId());
			if (list != null && list.size() > 0) {
				for (UserEquip userEquip : list) {
					SystemDeifyDefine systemDeifyDefines = this.systemDeifyDefineDao.get(userEquip.getEquipId());
					if (systemDeifyDefines != null) {
						userHeroBO.setIsArms(1);
					}
				}
			}
		}
		return userHeroBO;
	}

	public UserHero get(String userId, String userHeroId) {
		UserHero userHero = this.userHeroDao.get(userId, userHeroId);
		if (userHero == null) {
			String message = "获取用户武将失败,用户武将不存在.userHeroId[" + userHeroId + "]";
			LOG.error(message);
			throw new ServiceException(HERO_NOT_EXIST, message);
		}
		return userHero;
	}

	public List<BattleHeroBO> getUserBattleHeroBOList(String userId) {

		List<BattleHeroBO> battleHeroBOList = new ArrayList<BattleHeroBO>();

		List<UserHeroBO> userHeroBOList = this.getUserHeroList(userId, 1);

		// 武将被动技能(学习的)
		List<UserHeroSkill> userHeroSkillList = this.userHeroSkillDao.getList(userId);
		Map<String, Set<Integer>> userHeroSkillIdMap = new HashMap<String, Set<Integer>>();
		for (UserHeroSkill userHeroSkill : userHeroSkillList) {
			Set<Integer> userHeroSkillIds = null;
			String userHeroId = userHeroSkill.getUserHeroId();
			if (userHeroSkillIdMap.containsKey(userHeroId)) {
				userHeroSkillIds = userHeroSkillIdMap.get(userHeroId);
			} else {
				userHeroSkillIds = new HashSet<Integer>();
			}

			userHeroSkillIds.add(userHeroSkill.getSkillId());
			userHeroSkillIdMap.put(userHeroId, userHeroSkillIds);
		}

		// 阵上武将ID列表
		Map<Integer, UserHeroBO> battleHeroIdMap = new HashMap<Integer, UserHeroBO>();

		for (UserHeroBO userHeroBO : userHeroBOList) {
			BattleHeroBO battleHeroBO = BOHelper.createBattleHeroBO(userHeroBO);
			battleHeroBOList.add(battleHeroBO);

			SystemHero systemHero = this.getSysHero(userHeroBO.getSystemHeroId());
			battleHeroIdMap.put(systemHero.getHeroId(), userHeroBO);

			if (userHeroSkillIdMap.containsKey(userHeroBO.getUserHeroId())) {
				battleHeroBO.getSkillList().addAll(userHeroSkillIdMap.get(userHeroBO.getUserHeroId()));
			}

			if (userHeroBO.getSuitSkillId() > 0) {
				battleHeroBO.getSkillList().add(userHeroBO.getSuitSkillId());
			}
		}

		// 设置组合技
		for (BattleHeroBO battleHeroBO : battleHeroBOList) {

			SystemHero systemHero = this.systemHeroDao.get(battleHeroBO.getSystemHeroId());
			List<SystemHeroSkill> heroSkillList = this.systemHeroSkillDao.getHeroSkillList(systemHero.getHeroId());
			for (SystemHeroSkill systemHeroSkill : heroSkillList) {

				MergeSkillBO mergeSkillBO = new MergeSkillBO();

				List<Integer> posList = new ArrayList<Integer>();

				List<Integer> needHeroIdList = new ArrayList<Integer>();
				needHeroIdList.add(systemHeroSkill.getNeedHeroId1());
				needHeroIdList.add(systemHeroSkill.getNeedHeroId2());
				needHeroIdList.add(systemHeroSkill.getNeedHeroId3());
				needHeroIdList.add(systemHeroSkill.getNeedHeroId4());
				needHeroIdList.add(systemHeroSkill.getNeedHeroId5());

				boolean checkPass = true;

				for (int needHeroId : needHeroIdList) {

					if (needHeroId > 0) {

						if (!battleHeroIdMap.containsKey(needHeroId)) {
							checkPass = false;
							break;
						} else {
							posList.add(battleHeroIdMap.get(needHeroId).getPos());
						}
					}
				}

				if (!checkPass) {
					continue;
				}

				mergeSkillBO.setPosList(posList);
				mergeSkillBO.setSkillId(systemHeroSkill.getSkillId());

				LOG.debug("添加组合技.heroId[" + systemHeroSkill.getHeroId() + "], skillId[" + systemHeroSkill.getSkillId() + "]");
				battleHeroBO.addMergeSkill(mergeSkillBO);
			}
		}

		return battleHeroBOList;
	}

	public boolean addUserHero(String userId, String userHeroId, final int systemHeroId, int pos, int useType) {

		Date now = new Date();

		UserHero userHero = new UserHero();
		userHero.setCreatedTime(now);
		userHero.setHeroExp(0);
		userHero.setHeroLevel(1);
		userHero.setSystemHeroId(systemHeroId);
		userHero.setPos(pos);
		userHero.setUpdatedTime(now);
		userHero.setUserId(userId);
		userHero.setUserHeroId(userHeroId);

		// 任务处理
		this.taskService.updateTaskFinish(userId, 1, new EventHandle() {

			@Override
			public boolean handle(Event event) {
				return false;
			}
		}, new TaskChecker() {

			@Override
			public boolean isFinish(int systemTaskId, int taskTarget, Map<String, String> params) {

				if (taskTarget == TaskTargetType.TASK_TYPE_GET_HERO) {
					int needSystemHeroId = NumberUtils.toInt(params.get("system_hero_id"));
					return systemHeroId == needSystemHeroId;
				}
				return false;
			}
		});

		boolean success = this.userHeroDao.addUserHero(userHero);

		unSynLogService.heroLog(userId, userHeroId, systemHeroId, useType, 1, 0, 1);

		// 写入英雄操作日志表
		SystemHero systemHero = this.getSysHero(systemHeroId);
		String heroName = systemHero != null ? systemHero.getHeroName() : "NoHeroName";
		unSynLogService.userHeroOperatorLog(userId, userHeroId, systemHeroId, LogTypes.HERO_GET, userHero.getHeroExp(), userHero.getHeroLevel(), pos, 0, new Date(), heroName);
		return success;
	}

	@Override
	public void amendEmbattle(String userId, Map<Integer, String> posMap) {

		if (posMap.isEmpty()) {// 空阵法，不做处理
			return;
		}

		// 服务端上阵武将列表
		List<UserHeroBO> userHeroBOList = this.getUserHeroList(userId, 1);

		// 需要更新的武将列表
		Map<String, Integer> upateUserHeroIdMap = new HashMap<String, Integer>();

		// 当前服务端的阵法
		Map<Integer, String> serverPosMap = new HashMap<Integer, String>();

		// 看下当前服务端的阵法中有没有要下阵的
		for (UserHeroBO userHeroBO : userHeroBOList) {
			String userHeroId = userHeroBO.getUserHeroId();

			serverPosMap.put(userHeroBO.getPos(), userHeroId);

			if (!posMap.values().contains(userHeroId)) {// 有人下阵，不允许
				return;
			}
		}

		// 看下有没有要新上阵的
		for (Map.Entry<Integer, String> entry : posMap.entrySet()) {
			Integer pos = entry.getKey();
			String userHeroId = entry.getValue();

			if (!serverPosMap.values().contains(userHeroId)) {
				// 有新人上阵，不允许
				return;
			}

			if (serverPosMap.containsKey(pos) && userHeroId.equalsIgnoreCase(serverPosMap.get(pos))) {// 传上来的版本和服务端的一至
				continue;
			}

			upateUserHeroIdMap.put(userHeroId, pos);
		}

		if (upateUserHeroIdMap.isEmpty()) {
			LOG.debug("阵法一致，不做处理");
			return;
		}

		for (Map.Entry<String, Integer> entry : upateUserHeroIdMap.entrySet()) {
			String userHeroId = entry.getKey();
			int pos = entry.getValue();
			LOG.debug("修正用户阵法.userId[" + userId + "], userHeroId[" + userHeroId + "], pos[" + pos + "]");
			this.userHeroDao.changePos(userId, userHeroId, pos);
		}

	}

	@Override
	public boolean addUserHero(String userId, Map<String, Integer> heroIdMap, int useType) {

		List<UserHero> userHeroList = new ArrayList<UserHero>();

		Set<Integer> gainSystemHeroIdSet = new HashSet<Integer>();

		for (Map.Entry<String, Integer> entry : heroIdMap.entrySet()) {

			final int systemHeroId = entry.getValue();
			String userHeroId = entry.getKey();

			Date now = new Date();

			UserHero userHero = new UserHero();
			userHero.setCreatedTime(now);
			userHero.setHeroExp(0);
			userHero.setHeroLevel(1);
			userHero.setSystemHeroId(systemHeroId);
			userHero.setPos(0);
			userHero.setUpdatedTime(now);
			userHero.setUserId(userId);
			userHero.setUserHeroId(userHeroId);

			gainSystemHeroIdSet.add(systemHeroId);

			userHeroList.add(userHero);

			this.unSynLogService.heroLog(userId, userHeroId, systemHeroId, useType, 1, 0, 1);

		}

		boolean success = this.userHeroDao.addUserHero(userHeroList);
		/*
		 * for (UserHero userHero : userHeroList) { // 写入英雄操作日志表 SystemHero
		 * systemHero = this.getSysHero(userHero.getSystemHeroId()); String
		 * heroName = systemHero != null ? systemHero.getHeroName() :
		 * "NoHeroName"; unSynLogService .userHeroOperatorLog(userId,
		 * userHero.getUserHeroId(), userHero.getSystemHeroId(),
		 * LogTypes.HERO_GET, userHero.getHeroExp(), userHero.getHeroLevel(),
		 * userHero.getPos(), 0, new Date(), heroName);
		 * unSynLogService.userTreasureLog(userId, userHero.getSystemHeroId(),
		 * ToolType.HERO, 1, useType + "", 1, "", new Date()); }
		 */
		// unSynLogService.toolLog(userId, ToolType.HERO, 0, heroIdMap.size(),
		// useType, 1, Json.toJson(heroIdMap), success);
		for (final Integer systemHeroId : gainSystemHeroIdSet) {
			// 任务处理
			this.taskService.updateTaskFinish(userId, 1, new EventHandle() {

				@Override
				public boolean handle(Event event) {
					return false;
				}
			}, new TaskChecker() {

				@Override
				public boolean isFinish(int systemTaskId, int taskTarget, Map<String, String> params) {

					if (taskTarget == TaskTargetType.TASK_TYPE_GET_HERO) {
						int needSystemHeroId = NumberUtils.toInt(params.get("system_hero_id"));
						return systemHeroId == needSystemHeroId;
					}
					return false;
				}
			});
		}

		return success;

	}

	@Override
	public int getUserHeroCount(String userId) {
		return this.userHeroDao.getUserHeroCount(userId);
	}

	@Override
	public List<UserHeroBO> listByHeroLevelDesc(int offset, int size) {
		List<UserHero> userHeros = this.userHeroDao.listByHeroLevelDesc(offset, size);
		List<UserHeroBO> ret = new ArrayList<UserHeroBO>();
		for (UserHero userHero : userHeros) {
			UserHeroBO userHeroBO = this.createUserHeroBO(userHero);
			ret.add(userHeroBO);
		}

		return ret;
	}

	@Autowired
	private UserHeroSkillDao userHeroSkillDao;

	@Override
	public boolean studySkill(String userId, String userHeroId, int toolId) {

		// 拿到用户当前的
		List<UserHeroSkill> userHeroSkillList = userHeroSkillDao.getList(userId, userHeroId);

		SystemSkillGroup systemSkillGroup = systemSkillGroupDao.getByToolId(toolId);
		if (systemSkillGroup == null) {
			String message = "学习技能异常,技能书对应的技能分组不存在.userId[" + userId + "], toolId[" + toolId + "]";
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		// 判断是否学过
		for (UserHeroSkill userHeroSkill : userHeroSkillList) {

			SystemPassiveSkill existGroup = systemPassiveSkillDao.get(userHeroSkill.getSkillGroupId(), userHeroSkill.getSkillId());
			if (existGroup.getType() == systemSkillGroup.getType()) {
				String message = "已经学习过相同的技能.userId[" + userId + "], type[" + existGroup.getType() + "]";
				throw new ServiceException(HERO_SKILL_STUDY_HAD_STUDY_SAME_SKILL, message);//
			}

		}

		UserHero userHero = this.userHeroDao.get(userId, userHeroId);
		int studyCount = HeroHelper.getStudyHeroCount(userHero.getHeroLevel());

		if (userHeroSkillList.size() >= studyCount) {
			String message = "可学的技能次数超过限制.userId[" + userId + "], studyCount[" + studyCount + "]";
			throw new ServiceException(HERO_SKILL_STUDY_COUNT_OVER, message);
		}

		if (!this.toolService.reduceTool(userId, ToolType.SKILL_BOOK, toolId, 1, ToolUseType.REDUCE_STUDY_SKILL)) {
			String message = "没有相应的技能书.userId[" + userId + "], toolId[" + toolId + "]";
			throw new ServiceException(HERO_SKILL_STUDY_TOOL_NOT_ENOUGH, message);
		}

		Date now = new Date();

		SystemPassiveSkill systemPassiveSkill = this.systemPassiveSkillDao.getFirst(systemSkillGroup.getSkillGroupId());

		UserHeroSkill userHeroSkill = new UserHeroSkill();
		userHeroSkill.setSkillGroupId(systemPassiveSkill.getSkillGroupId());
		userHeroSkill.setSkillId(systemPassiveSkill.getSkillId());
		userHeroSkill.setUserId(userId);
		userHeroSkill.setUserHeroId(userHeroId);
		userHeroSkill.setCreatedTime(now);
		userHeroSkill.setUpdatedTime(now);

		this.userHeroSkillDao.add(userHeroSkill);

		return true;
	}

	@Override
	public boolean trainSkill(String userId, String userHeroId, List<Integer> lockHeroSkillIdList) {

		this.userHeroSkillTrainDao.delete(userId, userHeroId);

		List<UserHeroSkill> userHeroSkillList = userHeroSkillDao.getList(userId, userHeroId);

		Date now = new Date();

		List<UserHeroSkillTrain> userHeroSkillTrainList = new ArrayList<UserHeroSkillTrain>();

		// 需要的银币
		int needCopper = 0;

		for (UserHeroSkill userHeroSkill : userHeroSkillList) {

			int userHeroSkillId = userHeroSkill.getUserHeroSkillId();
			int skillGroupId = userHeroSkill.getSkillGroupId();
			int skillId = userHeroSkill.getSkillId();

			int lock = 0;

			SystemSkillGroup systemSkillGroup = this.systemSkillGroupDao.get(skillGroupId);

			if (lockHeroSkillIdList.contains(userHeroSkillId)) {// 锁定的不动
				LOG.debug("锁定的属性，不做更新.userId[" + userId + "], userHeroSkillId[" + userHeroSkillId + "]");
				needCopper += systemSkillGroup.getLockPrice();
				lock = 1;
			} else {

				List<SystemPassiveSkill> systemPassiveSkillList = this.systemPassiveSkillDao.getList(skillGroupId);
				SystemPassiveSkill systemPassiveSkill = randGetSystemPassiveSkill(systemPassiveSkillList);

				if (systemPassiveSkill == null) {
					LOG.warn("随机获取技能出错");
					continue;
				}

				needCopper += systemSkillGroup.getTrainPrice();

				skillId = systemPassiveSkill.getSkillId();
			}

			UserHeroSkillTrain userHeroSkillTrain = new UserHeroSkillTrain();
			userHeroSkillTrain.setUserId(userId);
			userHeroSkillTrain.setUserHeroId(userHeroId);
			userHeroSkillTrain.setSkillId(skillId);
			userHeroSkillTrain.setSkillGroupId(skillGroupId);
			userHeroSkillTrain.setLock(lock);
			userHeroSkillTrain.setCreatedTime(now);
			userHeroSkillTrain.setUpdatedTime(now);

			userHeroSkillTrainList.add(userHeroSkillTrain);

		}

		if (!this.userService.reduceCopper(userId, needCopper, ToolUseType.REDUCE_TRAIN_SKILL)) {
			String message = "技能训练失败，银币不足.userId[" + userId + "]";
			throw new ServiceException(HERO_SKILL_TRAIN_COPPER_NOT_ENOUGH, message);
		}

		// 活跃度奖励
		activityTaskService.updateActvityTask(userId, ActivityTargetType.DRILL_SKILL, 1);

		return this.userHeroSkillTrainDao.add(userHeroSkillTrainList);
	}

	/**
	 * 随机得到一个技能
	 * 
	 * @param systemPassiveSkillList
	 * @return
	 */
	private SystemPassiveSkill randGetSystemPassiveSkill(List<SystemPassiveSkill> systemPassiveSkillList) {

		int rand = RandomUtils.nextInt(10000) + 1;
		for (SystemPassiveSkill systemPassiveSkill : systemPassiveSkillList) {
			if (systemPassiveSkill.getLowerNum() <= rand && rand <= systemPassiveSkill.getUpperNum()) {
				return systemPassiveSkill;
			}
		}

		return null;
	}

	@Override
	public boolean trainSkillSave(String userId, String userHeroId) {

		List<UserHeroSkillTrain> userHeroSkillTrainList = this.userHeroSkillTrainDao.getList(userId, userHeroId);
		if (userHeroSkillTrainList.isEmpty()) {
			String message = "保存用户武将技能训练结果出错，没有可保存的项.userId[" + userId + "], userHeroId[" + userHeroId + "]";
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		for (UserHeroSkillTrain userHeroSkillTrain : userHeroSkillTrainList) {

			int skillGroupId = userHeroSkillTrain.getSkillGroupId();
			int skillId = userHeroSkillTrain.getSkillId();

			LOG.debug("更新用户武将.userId[" + userId + "], userHeroId[" + userHeroId + "], skillGroupId[" + skillGroupId + "], skillId[" + skillId + "]");
			this.userHeroSkillDao.udpate(userId, userHeroId, skillGroupId, skillId);
		}

		this.userHeroSkillTrainDao.delete(userId, userHeroId);

		return true;
	}

	@Override
	public List<UserHeroSkillBO> getUserHeroSkillBOList(String userId) {

		List<UserHeroSkill> userHeroSKillList = this.userHeroSkillDao.getList(userId);
		return this.userHeroSkillListToBOList(userHeroSKillList);
	}

	private List<UserHeroSkillBO> userHeroSkillListToBOList(List<UserHeroSkill> userHeroSKillList) {

		List<UserHeroSkillBO> userHeroSkillBOList = new ArrayList<UserHeroSkillBO>();

		for (UserHeroSkill userHeroSkill : userHeroSKillList) {

			UserHeroSkillBO userHeroSkillBO = new UserHeroSkillBO();
			userHeroSkillBO.setUserHeroSkillId(userHeroSkill.getUserHeroSkillId());
			userHeroSkillBO.setSkillId(userHeroSkill.getSkillId());
			userHeroSkillBO.setSkillGroupId(userHeroSkill.getSkillGroupId());
			userHeroSkillBO.setUserHeroId(userHeroSkill.getUserHeroId());
			userHeroSkillBOList.add(userHeroSkillBO);
		}

		return userHeroSkillBOList;

	}

	@Override
	public List<UserHeroSkillBO> getUserHeroSkillBOList(String userId, String userHeroId) {
		List<UserHeroSkill> userHeroSKillList = this.userHeroSkillDao.getList(userId, userHeroId);
		return this.userHeroSkillListToBOList(userHeroSKillList);
	}

	@Override
	public List<UserHeroSkillTrainBO> getUserHeroSkillTrainBOList(String userId, String userHeroId) {

		List<UserHeroSkillTrainBO> userHeroSkillTrainBOList = new ArrayList<UserHeroSkillTrainBO>();

		List<UserHeroSkillTrain> userHeroSkillTrainList = this.userHeroSkillTrainDao.getList(userId, userHeroId);

		for (UserHeroSkillTrain userHeroSkillTrain : userHeroSkillTrainList) {

			UserHeroSkillTrainBO userHeroSkillTrainBO = new UserHeroSkillTrainBO();

			userHeroSkillTrainBO.setSkillGroupId(userHeroSkillTrain.getSkillGroupId());
			userHeroSkillTrainBO.setSkillId(userHeroSkillTrain.getSkillId());
			userHeroSkillTrainBO.setLock(userHeroSkillTrain.getLock());

			userHeroSkillTrainBOList.add(userHeroSkillTrainBO);

		}

		return userHeroSkillTrainBOList;
	}

	@Override
	public boolean upgradeSkill(String userId, String userHeroId, int userHeroSkillId) {

		UserHeroSkill userHeroSkill = this.userHeroSkillDao.get(userId, userHeroSkillId);
		if (userHeroSkill == null) {
			String message = "进阶用户武将技能失败，用户武将技能不存在.userId[" + userId + "], userHeroSkillId[" + userHeroSkillId + "]";
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		int skillGroupId = userHeroSkill.getSkillGroupId();
		SystemSkillUpgrade systemSkillUpgrade = this.systemSkillUpgradeDao.get(skillGroupId);
		if (systemSkillUpgrade == null) {
			String message = "进阶用户武将技能失败，已经达到最高级.userId[" + userId + "], userHeroSkillId[" + userHeroSkillId + "]";
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		int needToolId = systemSkillUpgrade.getNeedToolId();
		if (!this.toolService.reduceTool(userId, ToolType.SKILL_BOOK, needToolId, 1, ToolUseType.REDUCE_UPGRADE_SKILL)) {
			String message = "进阶用户武将技能失败，没有对应的技能书.userId[" + userId + "], needToolId[" + needToolId + "]";
			throw new ServiceException(HERO_SKILL_UPGRADE_TOOL_NOT_ENOUGH, message);
		}

		int upgradeSkillGroupId = systemSkillUpgrade.getUpgradeSkillGroupId();

		SystemPassiveSkill systemPassiveSkill = this.systemPassiveSkillDao.getFirst(upgradeSkillGroupId);

		// 更新
		this.userHeroSkillDao.update(userId, userHeroSkillId, upgradeSkillGroupId, systemPassiveSkill.getSkillId());

		// 删除掉的训练
		this.userHeroSkillTrainDao.delete(userId, userHeroId, skillGroupId);

		return true;
	}

	@Override
	public boolean exchangeShard(String userId, String userHeroId, EventHandle handle) {

		UserHero userHero = this.userHeroDao.get(userId, userHeroId);
		if (userHero == null) {
			String message = "武将兑换碎片失败,用户武将不存在.userId[" + userId + "], userHeroId[" + userHeroId + "]";
			throw new ServiceException(ServiceReturnCode.FAILD, message);

		}

		if (userHero.getLockStatus() > 0) {
			String message = "该武将已被锁定.userId[" + userId + "], userHeroId[" + userHeroId + "]";
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		int systemHeroId = userHero.getSystemHeroId();

		SystemHero systemHero = this.getSysHero(userHero.getSystemHeroId());
		if (systemHero == null || systemHero.getShardNum() == 0) {
			String message = "武将兑换碎片失败,该武将不可以兑换兵符.userId[" + userId + "], userHeroId[" + userHeroId + "], systemHeroId[" + systemHeroId + "]";
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		List<UserHero> userHeroList = new ArrayList<UserHero>();
		userHeroList.add(userHero);

		int count = this.delete(userId, userHeroList, ToolUseType.REDUCE_EXCHANGE_SHARD);
		if (count != 1) {
			String message = "武将兑换碎片失败,删除武将异常.userId[" + userId + "], userHeroId[" + userHeroId + "], systemHeroId[" + systemHeroId + "]";
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		int toolNum = systemHero.getShardNum() * (userHero.getBloodSacrificeStage() + 1);

		boolean success = this.toolService.addTool(userId, ToolType.HERO_SHARD, ToolId.TOOL_ID_HERO_SHARD, toolNum, ToolUseType.ADD_HERO_EXCHANGE_SHARD);
		if (!success) {
			String message = "武将兑换碎片发放碎片异常，userId[" + userId + "], userHeroId[" + userHeroId + "]";
			LOG.warn(message);
		}

		UserToolBO userToolBO = this.toolService.getUserToolBO(userId, ToolId.TOOL_ID_HERO_SHARD);

		ToolUpdateEvent event = new ToolUpdateEvent(userId, userToolBO);
		handle.handle(event);
		// 武将操作兑换日志
		unSynLogService.userHeroOperatorLog(userId, userHeroId, systemHeroId, LogTypes.HERO_EXCHANGE, userHero.getHeroExp(), userHero.getHeroLevel(), userHero.getPos(), 0, new Date(),
				systemHero.getHeroName());
		return success;
	}

	@Override
	public boolean forgetSkill(String userId, String userHeroId, int userHeroSkillId) {

		UserHeroSkill userHeroSkill = this.userHeroSkillDao.get(userId, userHeroSkillId);
		if (userHeroSkill == null) {
			return true;
		}

		boolean success = this.userHeroSkillDao.delete(userId, userHeroId, userHeroSkillId);

		int skillGroupId = userHeroSkill.getSkillGroupId();
		if (success) {
			if (!this.userHeroSkillTrainDao.delete(userId, userHeroId, skillGroupId)) {
				LOG.warn("删除用户武将技能训练失败.userId[" + userId + "], userHeroId[" + userHeroId + "], skillGroupId[" + skillGroupId + "]");
			}
		}

		return success;
	}

	@Override
	public boolean trainSkillCancel(String userId, String userHeroId) {
		return this.userHeroSkillTrainDao.delete(userId, userHeroId);
	}

	@Override
	public int getHeroId(int systemHeroId) {
		return systemHeroDao.get(systemHeroId).getHeroId();
	}

	/**
	 * 检验武将是否满足：1）品质为5，等级达到100级，3）五星武将。
	 */
	private void checkBasic(UserHero userHero) {
		int systemHeroId = userHero.getSystemHeroId();

		SystemHero systemHero = this.systemHeroDao.get(systemHeroId);

		/**
		 * 查询出武将品质、武将等级和武将星级，如果没有达到转生要求，向客户端发出2001状态码
		 */
		int heroColor = systemHero.getHeroColor();
		int heroLevel = userHero.getHeroLevel();
		int heroStar = systemHero.getHeroStar();

		if (heroColor < 5 || heroLevel < 100 || heroStar < 5) {
			String message = "武将不符合转生条件.userId[" + userHero.getUserId() + "], systemHeroId[" + systemHeroId + "], heroColor[" + heroColor + "], heroLevel[" + heroLevel + "], heroStar[" + heroStar
					+ "]";
			throw new ServiceException(CONDITION_NOT_SATISFIED, message);
		}
	}

	/**
	 * 判断是否有转生数据
	 * 
	 * @return 如果转生成功，转生后武将的 system_hero_id
	 */
	private int checkRegenerateInfo(UserHero userHero) {
		// 查询武将转生后的 ID，如果为0表示该武将没有转生数据
		int posHeroid = userHeroRegenerateDao.getPostHeroId(userHero.getSystemHeroId());
		if (posHeroid == 0) {
			String message = "无转生数据.userId[" + userHero.getUserId() + "], systemHeroId[" + userHero.getSystemHeroId() + "]";
			throw new ServiceException(HERO_NO_REGENERATE_INFO, message);
		}

		return posHeroid;
	}

	/**
	 * 判断是否有足够的转生丹
	 * 
	 * @param userHero
	 * @return 如果转生成功，将消耗多少转生丹
	 */
	private int checkPill(UserHero userHero) {
		// 查询用户已有的转生丹的数量
		// UserToolBO userToolBO;
		int userPillNum = userToolDao.getUserToolNum(userHero.getUserId(), TOOL_PILL_ID);
		// try {
		// userToolBO = toolService.getUserToolBO(userHero.getUserId(),
		// TOOL_PILL_ID);
		// } catch (Exception e) {
		// String message = "用户没有转生丹.userId[" + userHero.getUserId() +
		// "], tool_id[" + TOOL_PILL_ID + "]";
		// throw new ServiceException(HERO_PILL_NOT_ENOUGH, message);
		// }

		// 查询转生需要的转生丹的数量
		int consumePillNum = userHeroRegenerateDao.getPillNum(userHero.getSystemHeroId());

		if (userPillNum < consumePillNum) {
			String message = "转生丹数量不足.userPillNum[" + userPillNum + "]";
			throw new ServiceException(HERO_PILL_NOT_ENOUGH, message);
		}

		return consumePillNum;
	}

	/**
	 * 判断是否有足够的鬼之合约
	 * 
	 * @return 如果转生成功，将消耗多少鬼之合约
	 */
	private int checkContract(UserHero userHero) {
		// 用户已有的 鬼之合约 数量
		// UserToolBO userToolBO;
		int userContractNum = userToolDao.getUserToolNum(userHero.getUserId(), TOOL_CONTRACT_ID);
		// try {
		// userToolBO = toolService.getUserToolBO(userHero.getUserId(),
		// TOOL_CONTRACT_ID);
		// } catch (Exception e) {
		// String message = "用户没有鬼之合约.userId[" + userHero.getUserId() +
		// "], tool_id[" + TOOL_CONTRACT_ID + "]";
		// throw new ServiceException(HERO_CONTRACT_NOT_ENOUGH, message);
		// }
		// int userContractNum = userToolBO.getToolNum();

		// 转生所需要的 鬼之合约 数量
		int consumeContractNum = userHeroRegenerateDao.getContractNum(userHero.getSystemHeroId());

		if (userContractNum < consumeContractNum) {
			String message = "鬼之合约数量不足.userId[" + userHero.getUserId() + "], userContractNum[" + userContractNum + "]";
			throw new ServiceException(HERO_CONTRACT_NOT_ENOUGH, message);
		}

		return consumeContractNum;
	}

	/**
	 * 判断是否有足够的相同武将
	 * 
	 * @return 如果转生成功，将剩余多少相同武将
	 */
	private void checkHeroNum(String userId, UserHero userHero, List<String> userHeroIdList) {

		// 转生所需的相同武将数
		int consumeHeroNum = userHeroRegenerateDao.getHeroNum(userHero.getSystemHeroId());

		if (userHeroIdList.size() != consumeHeroNum) {
			String message = "武将数量不足.consumeHeroNum[" + consumeHeroNum + "]";
			throw new ServiceException(HERO_NUM_NOT_ENOUGH, message);
		}

		int heroId = this.getHeroId(userHero.getSystemHeroId());

		for (String userHeroId : userHeroIdList) {

			if (StringUtils.equalsIgnoreCase(userHeroId, userHero.getUserHeroId())) {
				String message = "转生异常,不能用转生武将本来做为材料.userId[" + userId + "], userHeroId[" + userHeroId + "]";
				throw new ServiceException(ServiceReturnCode.FAILD, message);
			}

			UserHero u = this.userHeroDao.get(userId, userHeroId);
			if (u == null) {
				String message = "转生异常,用户武将不存在.userId[" + userId + "], userHeroId[" + userHeroId + "]";
				throw new ServiceException(ServiceReturnCode.FAILD, message);
			}

			if (u.getLockStatus() > 0) {
				String message = "转生异常,用户武将被锁定.userId[" + userId + "], userHeroId[" + userHeroId + "]";
				throw new ServiceException(ServiceReturnCode.FAILD, message);
			}
			SystemHero systemHero = this.systemHeroDao.get(u.getSystemHeroId());

			if (systemHero == null || systemHero.getHeroId() != heroId) {
				String message = "转生异常,不是相同类弄卡牌. userId[" + userId + "], userHeroId[" + userHeroId + "]";
				throw new ServiceException(ServiceReturnCode.FAILD, message);
			}
		}

	}

	@Override
	public boolean heroTransform(String userId, String userHeroId, List<String> userHeroIdList) {
		int consumePillNum = 0;
		int postHeroId = 0;

		// if (userHeroIdList.size() != 3) {
		// String message = "必须选择3个武将";
		// throw new ServiceException(HERO_NUM_NOT_ENOUGH, message);
		// }

		if (userHeroIdList == null || userHeroIdList.isEmpty()) {
			String message = "武将数量不足,userId[" + userId + "]";
			throw new ServiceException(HERO_NUM_NOT_ENOUGH, message);
		}

		List<UserHero> userHeroList = new ArrayList<UserHero>();
		for (String uhid : userHeroIdList) {
			UserHero userHero = this.get(userId, uhid);
			userHeroList.add(userHero);
		}

		UserHero userHero = this.userHeroDao.get(userId, userHeroId);
		SystemHero systemHero = systemHeroDao.get(userHero.getSystemHeroId());

		checkBasic(userHero);
		postHeroId = checkRegenerateInfo(userHero);
		consumePillNum = checkPill(userHero);
		checkHeroNum(userId, userHero, userHeroIdList);

		/**
		 * 转生条件满足，减去转生所需的转生丹数量，删除转生所消耗的相同武将
		 */
		toolService.reduceTool(userId, ToolType.HERO, TOOL_PILL_ID, consumePillNum, ToolUseType.REDUCE_REGENERATE);
		int deleteCount = delete(userId, userHeroList, ToolUseType.REDUCE_REGENERATE);
		if (deleteCount <= 0) {
			String message = "武将数量不足,userId[" + userId + "]";
			throw new ServiceException(HERO_NUM_NOT_ENOUGH, message);
		}

		// 更新转生后武将数据
		this.userHeroDao.update(userId, userHeroId, postHeroId, 1, 0);
		HeroTowerEvent event = new HeroTowerEvent(userId, systemHero.getHeroName());
		eventServcie.dispatchEvent(event);
		return true;
	}

	@Override
	public boolean contractTransform(String userId, String userHeroId) {
		int consumePillNum = 0;
		int consumeContractNum = 0;
		int postHeroId = 0;

		UserHero userHero = this.userHeroDao.get(userId, userHeroId);
		SystemHero systemHero = systemHeroDao.get(userHero.getSystemHeroId());
		/**
		 * 检验是否满足所有的转生条件
		 */
		checkBasic(userHero);
		postHeroId = checkRegenerateInfo(userHero);
		consumePillNum = checkPill(userHero);
		consumeContractNum = checkContract(userHero);

		/**
		 * 转生条件满足，减去转生所需的转生丹、鬼之合约和数量，
		 */
		toolService.reduceTool(userId, ToolType.HERO, TOOL_PILL_ID, consumePillNum, ToolUseType.REDUCE_REGENERATE);
		toolService.reduceTool(userId, ToolType.HERO, TOOL_CONTRACT_ID, consumeContractNum, ToolUseType.REDUCE_REGENERATE);

		// 更新转生后武将数据
		this.userHeroDao.update(userId, userHeroId, postHeroId, 1, 0);
		HeroTowerEvent event = new HeroTowerEvent(userId, systemHero.getHeroName());
		eventServcie.dispatchEvent(event);
		return true;
	}

	/**
	 * 转生预览，两种转生方法共用这一个预览方法
	 */
	public UserHeroBO transformPre(String userId, String userHeroId) {
		UserHero userHero = this.userHeroDao.get(userId, userHeroId);
		int postHeroId = userHeroRegenerateDao.getPostHeroId(userHero.getSystemHeroId());

		userHero.setSystemHeroId(postHeroId);
		userHero.setHeroLevel(1);
		UserHeroBO userHeroBo = createUserHeroBO(userHero);

		return userHeroBo;
	}

	@Override
	public List<UserHeroBO> createUserHeroBOList(String userId, List<DropToolBO> boList) {
		List<UserHeroBO> userHeroBOList = new ArrayList<UserHeroBO>();
		for (DropToolBO dropToolBO : boList) {
			UserHeroBO userHeroBO = getUserHeroBO(userId, dropToolBO.getValue());
			userHeroBOList.add(userHeroBO);
		}
		return userHeroBOList;
	}

	@Override
	public Map<String, Object> inherit(String uid, String givingHeroId, String inheritHeroId, int type) {

		Map<String, Object> retVal = new HashMap<String, Object>();
		retVal.put("contract", 0);

		User user = userService.get(uid);

		UserHero givingHero = this.userHeroDao.get(uid, givingHeroId); // 传承武将

		if (givingHero == null) {
			String message = "传承武将不存在.givingHero.uhid[" + givingHeroId + "]";
			LOG.info(message);
			throw new ServiceException(INHERIT_HERO_NOT_EXIST, message);
		}

		UserHero inheritHero = this.userHeroDao.get(uid, inheritHeroId); // 被传承武将

		if (inheritHero == null) {
			String message = "被传承武将不存在.inheritHero.uhid[" + inheritHeroId + "]";
			LOG.info(message);
			throw new ServiceException(INHERIT_HERO_NOT_EXIST, message);
		}

		checkInherit(user, givingHero, inheritHero, type); // 检查是否满足传承条件

		SystemHero gSystemHero = systemHeroDao.get(givingHero.getSystemHeroId());
		SystemHero iSystemHero = systemHeroDao.get(inheritHero.getSystemHeroId());

		/*
		 * 根据是元宝成传承还是普通传承计算传承后被传承武将的经验，并扣除对应的金币
		 */
		int gExp = givingHero.getHeroExp(); // 传承武将的经验
		int iExp = inheritHero.getHeroExp(); // 被传承武将的经验
		int iExpAfter = 0; // 传承结束后，被传承武将可以达到的最大经验

		if (type == 0) {
			iExpAfter = iExp + (int) Math.ceil(gExp * 0.5);
		} else if (type == 1) {
			iExpAfter = iExp + gExp;
		}
		// 非鬼将给鬼将
		if (gSystemHero.getHeroColor() < 6) {
			int goldNum = HeroHelper.getGoldNum(gSystemHero.getHeroColor());

			if (type == 1) {
				boolean success = userService.reduceGold(uid, goldNum, ToolUseType.REDUCE_INHERIT_GOLDEN, user.getLevel());
				if (success == false) {
					String message = "用户元宝不足.goldNum[" + user.getGoldNum() + "]";
					LOG.info(message);
					throw new ServiceException(INHERIT_GOLD_NOT_ENOUGH, message);
				}
			}
		} else if (gSystemHero.getHeroColor() == 6) {

			// 判读被传承的武将是否可鬼话，不可鬼话，则返回传承的英雄不可鬼画
			int iSystemHeroId = systemHeroDao.getSystemHeroId(iSystemHero.getHeroId(), 6);
			if (iSystemHeroId == 0) {
				String message = "用户鬼之合约失败.传承武将不存在";
				LOG.info(message);
				throw new ServiceException(INHERIT_HERO_CAN_NOT_CONTRACT, message);
			}

			// 鬼将传承给非鬼将，元宝传承，需要花费【鬼之合约+2500元宝】，鬼将恢复到普通1级状态，被传承武将直接成为鬼将，并获得当前鬼将经验100%的等级
			if (iSystemHero.getHeroColor() < 6 && type == 1) {
				UserTool userTool = this.userToolDao.get(uid, TOOL_CONTRACT_ID);
				retVal.put("contract", 1);
				if (userTool == null || userTool.getToolNum() < 1) {
					String message = "用户鬼之合约不足. userId[" + uid + "]";
					LOG.info(message);
					throw new ServiceException(INHERIT_HERO_NOT_ENOUGH_CONTRACT, message);
				}

				boolean success = userService.reduceGold(uid, 2500, ToolUseType.REDUCE_INHERIT_GOLDEN, user.getLevel());
				if (success == false) {
					String message = "用户元宝不足.userId[" + uid + "],goldNum[" + 2500 + "]";
					LOG.info(message);
					throw new ServiceException(INHERIT_GOLD_NOT_ENOUGH, message);
				}

				toolService.reduceTool(uid, ToolType.HERO, TOOL_CONTRACT_ID, 1, ToolUseType.REDUCE_REGENERATE);
			} else if (iSystemHero.getHeroColor() < 6 && type == 0) {
				// 鬼将传承给非鬼将，普通传承，需要花费【鬼之合约】，鬼将恢复到普通1级状态，被传承武将直接成为鬼将，并获得当前鬼将经验/2的等级
				UserTool userTool = this.userToolDao.get(uid, TOOL_CONTRACT_ID);
				retVal.put("contract", 1);
				if (userTool == null || userTool.getToolNum() < 1) {
					String message = "用户鬼之合约不足..userId[" + uid + "],goldNum[" + 200 + "]";
					LOG.info(message);
					throw new ServiceException(INHERIT_HERO_NOT_ENOUGH_CONTRACT, message);
				}
				toolService.reduceTool(uid, ToolType.HERO, TOOL_CONTRACT_ID, 1, ToolUseType.REDUCE_REGENERATE);
			} else if (iSystemHero.getHeroColor() == 6 && type == 1) {
				// 鬼将传承给鬼将，元宝传承，需要花费【2000元宝】，鬼将恢复到普通1级状态，被传承武将成为当前鬼将等级经验100%的等级
				boolean success = userService.reduceGold(uid, 2000, ToolUseType.REDUCE_INHERIT_GOLDEN, user.getLevel());
				if (success == false) {
					String message = "用户元宝不足.goldNum[" + 2000 + "]";
					LOG.info(message);
					throw new ServiceException(INHERIT_GOLD_NOT_ENOUGH, message);
				}
			}
		}

		Map<String, Integer> map = new HashMap<String, Integer>();
		map = computerColorLevelExp(gSystemHero, iSystemHero, iExpAfter, user.getLevel());

		/*
		 * 传承结束后，更新被传承武将的信息。传承的过程实际是更新武将的 systemHeroId，即升级武将品质
		 * 和增加武将的等级和经验，因此可以使用武将转成的 update 方法
		 */
		userHeroDao.update(uid, inheritHeroId, map.get("iSystemHeroIdAfter"), map.get("iLevelAfter"), map.get("iExpAfter"));

		// 更新传承武将信息， 等级：1，经验：0
		userHeroDao.update(uid, givingHeroId, map.get("gSystemHeroIdAfter"), 1, 0);

		return retVal;
	}

	private Map<String, Integer> computerColorLevelExp(SystemHero gSystemHero, SystemHero iSystemHero, int iExpAfter, int userLevel) {
		Map<String, Integer> map = new HashMap<String, Integer>();

		int gColor = gSystemHero.getHeroColor();
		int iColor = iSystemHero.getHeroColor();
		int iColorAfter = 0;

		// 如果传承的是鬼将，而被传承武将的星级小于5，则品质只能升到5（红色）而不能升到6（鬼将）
		// if (gColor == 6 && (gSystemHero.getHeroStar()) < 5) {
		// iColorAfter = 5;
		// } else {
		// iColorAfter = (gColor > iColor) ? gColor : iColor;
		// }

		iColorAfter = (gColor > iColor) ? gColor : iColor;

		// 根据传承结束后的品质和 heroId 获取对应的 systemheroId
		int iSystemHeroIdAfter = systemHeroDao.getSystemHeroId(iSystemHero.getHeroId(), iColorAfter);
		int gSystemHeroIdAfter = systemHeroDao.getSystemHeroId(gSystemHero.getHeroId(), 0);

		/*
		 * 计算传承后被传承武将的等级
		 */
		int iLevelAfter = systemLevelExpDao.getHeroLevel(iExpAfter).getLevel(); // 吸取了经验后可以升到的等级
		int maxLevel = (gColor > iColor) ? gSystemHero.getMaxLevel() : iSystemHero.getMaxLevel(); // 该品质的武将可达到的最大等级

		if (iLevelAfter >= maxLevel) {
			iLevelAfter = maxLevel;
			iExpAfter = systemLevelExpDao.getHeroExp(iLevelAfter).getExp(); // 这个等级可以达到的最大经验
		}

		if (iLevelAfter >= userLevel) {
			iLevelAfter = userLevel;
			iExpAfter = systemLevelExpDao.getHeroExp(iLevelAfter).getExp();
		}

		map.put("iSystemHeroIdAfter", iSystemHeroIdAfter);
		map.put("gSystemHeroIdAfter", gSystemHeroIdAfter);
		map.put("iExpAfter", iExpAfter);
		map.put("iLevelAfter", iLevelAfter);

		return map;
	}

	@Override
	public Map<String, Object> inheritPre(String uid, String givingHeroId, String inheritHeroId, int type) {
		User user = userService.get(uid);
		Map<String, Object> retVal = new HashMap<String, Object>();

		UserHero givingHero = this.userHeroDao.get(uid, givingHeroId); // 传承武将

		if (givingHero == null) {
			String message = "传承武将不存在.givingHero.uhid[" + givingHeroId + "]";
			LOG.info(message);
			throw new ServiceException(INHERITPRE_GHERO_NOT_EXIST, message);
		}

		UserHero inheritHero = this.userHeroDao.get(uid, inheritHeroId); // 被传承武将

		if (inheritHero == null) {
			String message = "被传承武将不存在.inheritHero.uhid[" + inheritHeroId + "]";
			LOG.info(message);
			throw new ServiceException(INHERITPRE_IHERO_NOT_EXIST, message);
		}

		SystemHero gSystemHero = systemHeroDao.get(givingHero.getSystemHeroId());
		SystemHero iSystemHero = systemHeroDao.get(inheritHero.getSystemHeroId());

		if (gSystemHero.getHeroColor() == 6) {
			// 判读被传承的武将是否可鬼话，不可鬼话，则返回传承的英雄不可鬼画
			int iSystemHeroId = systemHeroDao.getSystemHeroId(iSystemHero.getHeroId(), 6);
			if (iSystemHeroId == 0) {
				String message = "用户鬼之合约失败.传承武将不存在";
				LOG.info(message);
				throw new ServiceException(INHERITPRE_HERO_CAN_NOT_CONTRACT, message);
			}
		}

		checkInherit(user, givingHero, inheritHero, type); // 检查是否满足传承条件

		/*
		 * 计算传承后被传承武将的经验
		 */
		int gExp = givingHero.getHeroExp(); // 传承武将的经验
		int iExp = inheritHero.getHeroExp(); // 被传承武将的经验
		int iExpAfter = 0; // 传承结束后，被传承武将的经验

		retVal.put("gold", 0);
		retVal.put("contract", 0);
		if (type == 0) {
			iExpAfter = iExp + (int) Math.ceil(gExp * 0.5);
		} else if (type == 1) {
			iExpAfter = iExp + gExp;
		}

		if (gSystemHero.getHeroColor() < 6 && type == 1) {
			int goldNum = HeroHelper.getGoldNum(gSystemHero.getHeroColor());
			retVal.put("gold", goldNum);
		} else if (gSystemHero.getHeroColor() == 6) {
			if (iSystemHero.getHeroColor() < 6 && type == 1) {
				retVal.put("gold", 2500);
				retVal.put("contract", 1);
			} else if (iSystemHero.getHeroColor() < 6 && type == 0) {
				retVal.put("contract", 1);
			} else if (iSystemHero.getHeroColor() == 6 && type == 1) {
				retVal.put("gold", 2000);
			}
		}

		Map<String, Integer> map = new HashMap<String, Integer>();
		map = computerColorLevelExp(gSystemHero, iSystemHero, iExpAfter, user.getLevel());

		givingHero.setSystemHeroId(map.get("gSystemHeroIdAfter"));
		givingHero.setHeroLevel(1);
		givingHero.setHeroExp(0);
		retVal.put("givingHero", this.createUserHeroBO(givingHero));

		inheritHero.setSystemHeroId(map.get("iSystemHeroIdAfter"));
		inheritHero.setHeroLevel(map.get("iLevelAfter"));
		inheritHero.setHeroExp(map.get("iExpAfter"));
		retVal.put("inheritHero", this.createUserHeroBO(inheritHero));

		return retVal;
	}

	/**
	 * 检查武将传承条件 givingHero不能是鬼将
	 */
	private void checkInherit(User user, UserHero givingHero, UserHero inheritHero, int type) {
		// 检查被传承武将条件
		SystemHero iSystemHero = systemHeroDao.get(inheritHero.getSystemHeroId());
		if (iSystemHero.getHeroStar() <= 2) {
			String message = "被传承武将不满足条件，星级小于等于2.uhid[" + inheritHero.getUserHeroId() + "]";
			LOG.info(message);
			throw new ServiceException(INHERIT_INHERIT_HERO_NOT_MATCH, message);
		}

		// 检查传承武将条件
		if (givingHero.getPos() > 0) {
			String message = "传承武将不满足条件，武将已上阵.uhid[" + givingHero.getUserHeroId() + "]";
			LOG.info(message);
			throw new ServiceException(INHERIT_GIVING_HERO_NOT_MATCH, message);
		}

		SystemHero gSystemHero = systemHeroDao.get(givingHero.getSystemHeroId());
		if (gSystemHero.getHeroStar() < 2) {
			String message = "传承武将不满足条件，武将星级小于2.uhid[" + givingHero.getUserHeroId() + "]";
			LOG.info(message);
			throw new ServiceException(INHERIT_GIVING_HERO_NOT_MATCH, message);
		}

		if (gSystemHero.getHeroColor() == 7 || iSystemHero.getHeroColor() == 7) {
			String message = "鬼将不能传承或者被传承";
			LOG.info(message);
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		if (givingHero.getHeroLevel() < 1) {
			String message = "传承武将不满足在条件，武将等级小于1.uhid[" + givingHero.getUserHeroId() + "]";
			LOG.info(message);
			throw new ServiceException(INHERIT_GIVING_HERO_NOT_MATCH, message);
		}

		if (givingHero.getLockStatus() > 0) {
			String message = "传承武将被锁定.uhid[" + givingHero.getUserHeroId() + "]";
			LOG.info(message);
			throw new ServiceException(HERO_HAS_LOCKED, message);
		}

	}

	@Override
	public BloodSacrificeBO bloodSacrificePre(String userId, String userHeroId) {

		UserHero givingHero = this.userHeroDao.get(userId, userHeroId); // 血祭的武将

		if (givingHero == null) {
			String message = "用户血祭的英雄不存在.userId[" + userId + "], userHeroId[" + userHeroId + "]";
			throw new ServiceException(BLOOD_SACRIFICE_HERO_NOT_EXIST, message);
		}

		SystemHero iSystemHero = systemHeroDao.get(givingHero.getSystemHeroId());

		checkInheroBS(givingHero, iSystemHero.getHeroStar());

		SystemBloodSacrifice systemBloodSacrificeNow = this.systemBloodSacrificeDao.get(iSystemHero.getHeroId(), givingHero.getBloodSacrificeStage());

		SystemBloodSacrifice systemBloodSacrifice = this.systemBloodSacrificeDao.get(iSystemHero.getHeroId(), givingHero.getBloodSacrificeStage() + 1);

		SystemHeroAttr systemHeroAttr = this.systemHeroAttrDao.getHeroAttr(givingHero.getSystemHeroId(), givingHero.getHeroLevel());

		BloodSacrificeBO bloodSacrificeBO = new BloodSacrificeBO();
		bloodSacrificeBO.setAddLife((int) (systemHeroAttr.getLife() * systemBloodSacrifice.getAddLifeRatio()) - (int) (systemHeroAttr.getLife() * systemBloodSacrificeNow.getAddLifeRatio()));
		bloodSacrificeBO.setAddPhysicalAttack((int) (systemHeroAttr.getPhysicalAttack() * (systemBloodSacrifice.getAddPhysicalAttackRatio()))
				- (int) (systemHeroAttr.getPhysicalAttack() * systemBloodSacrificeNow.getAddPhysicalAttackRatio()));
		bloodSacrificeBO.setAddPhysicalDefense((int) (systemHeroAttr.getPhysicalDefense() * (systemBloodSacrifice.getAddPhysicalDefenseRatio()))
				- (int) (systemHeroAttr.getPhysicalDefense() * systemBloodSacrificeNow.getAddPhysicalDefenseRatio()));
		bloodSacrificeBO.setBSSkillID(systemBloodSacrifice.getBsSkill());
		bloodSacrificeBO.setOutline(systemBloodSacrifice.getOutline());
		bloodSacrificeBO.setDropToolBO(DropToolHelper.parseDropTool(systemBloodSacrifice.getToolIds()));
		bloodSacrificeBO.setAddStage(1);
		return bloodSacrificeBO;
	}

	@Override
	public SystemBloodSacrificeLooseBO bloodSacrifice(String userId, String userHeroId, String useUserHeroId, EventHandle handle) {

		UserHero givingHero = this.userHeroDao.get(userId, userHeroId); // 血祭的武将

		SystemHero iSystemHero = systemHeroDao.get(givingHero.getSystemHeroId());

		// 判断是否有指定武将
		if (userHeroId.equalsIgnoreCase(useUserHeroId)) {
			String message = "用户用来被血祭的英雄不存在.userId[" + userId + "], useUserHeroId[" + useUserHeroId + "]";
			throw new ServiceException(BLOOD_SACRIFICE_HERO_REPETTION, message);
		}

		UserHero useUserHero = this.userHeroDao.get(userId, useUserHeroId); // 血祭的武将

		if (useUserHero == null) {
			String message = "用户用来被血祭的英雄不存在.userId[" + userId + "], useUserHeroId[" + useUserHeroId + "]";
			throw new ServiceException(BLOOD_SACRIFICE_HERO_NOT_EXIST, message);
		}

		checkInheroBS(givingHero, iSystemHero.getHeroStar());

		if (useUserHero.getLockStatus() > 0) {
			String message = "用户用来被血祭的英雄被锁定.userId[" + userId + "], useUserHeroId[" + useUserHeroId + "]";
			throw new ServiceException(HERO_HAS_LOCKED, message);
		}

		SystemHero userSystemHero = systemHeroDao.get(useUserHero.getSystemHeroId());
		if (userSystemHero.getHeroColor() >= 6) {
			String message = "鬼将不能用于被血祭.userId[" + userId + "], useUserHeroId[" + useUserHeroId + "]";
			throw new ServiceException(BLOOD_SACRIFICE_HAS_GHOST_GENERAL, message);
		}

		SystemBloodSacrifice systemBloodSacrifice = this.systemBloodSacrificeDao.get(iSystemHero.getHeroId(), givingHero.getBloodSacrificeStage() + 1);

		List<DropToolBO> dropToolBOList = DropToolHelper.parseDropTool(systemBloodSacrifice.getToolIds());

		SystemBloodSacrificeLooseBO bloodSacrificeLooseBO = this.createToolExchangeLooseBO(userId, dropToolBOList, userHeroId, useUserHeroId);

		SystemBloodSacrificeHeroBO systemBloodSacrificeHeroBO = new SystemBloodSacrificeHeroBO();
		systemBloodSacrificeHeroBO.setExchangeNum(1);
		systemBloodSacrificeHeroBO.setUserHeroId(useUserHeroId);
		bloodSacrificeLooseBO.getHeroBOList().add(systemBloodSacrificeHeroBO);

		reduceTool(userId, bloodSacrificeLooseBO);

		// 给用户血祭加一层
		this.userHeroDao.upgradeStage(userId, userHeroId);

		CopperUpdateEvent copperUpdateEvent = new CopperUpdateEvent(userId);
		handle.handle(copperUpdateEvent);

		return bloodSacrificeLooseBO;

	}

	/**
	 * 血祭-告诉客户端丢失了什么物品
	 * 
	 * @param userId
	 * @param useUserHeroId
	 * @param userHeroId
	 * @param exchangeItems
	 * @param num
	 * @return
	 */
	private SystemBloodSacrificeLooseBO createToolExchangeLooseBO(String userId, List<DropToolBO> dropToolBOList, String userHeroId, String useUserHeroId) {

		SystemBloodSacrificeLooseBO looseBO = new SystemBloodSacrificeLooseBO();
		User user = userService.get(userId);

		for (DropToolBO preExchangeItem : dropToolBOList) {
			int toolType = preExchangeItem.getToolType();
			int toolId = preExchangeItem.getToolId();
			int toolNum = preExchangeItem.getToolNum();

			switch (toolType) {
			case ToolType.GOLD:
				long goldNum = user.getGoldNum();
				if (goldNum < toolNum) {
					String message = "用户金币不足.userId[" + userId + "], gold_num[" + goldNum + "]";
					throw new ServiceException(BLOOD_SACRIFICE_TOOL_NOT_ENOUGH, message);
				} else {
					looseBO.setGold(toolNum); // 失去的金币数量
				}
				break;
			case ToolType.COPPER:
				long cooperNum = user.getCopper();
				if (cooperNum < toolNum) {
					String message = "用户银币不足.userId[" + userId + "], coopper[" + cooperNum + "]";
					throw new ServiceException(BLOOD_SACRIFICE_TOOL_NOT_ENOUGH, message);
				} else {
					looseBO.setCopper(toolNum); // 失去的银币数量
				}
				break;
			case ToolType.POWER:
				int power = user.getPower();
				if (power < toolNum) {
					String message = "用户体力不足.userId[" + userId + "], power[" + power + "]";
					throw new ServiceException(BLOOD_SACRIFICE_TOOL_NOT_ENOUGH, message);
				} else {
					looseBO.setPower(toolNum); // 失去的体力
				}
				break;
			case ToolType.EXP:
				long exp = user.getExp();
				if (exp < toolNum) {
					String message = "用户经验值不足.userId[" + userId + "], exp[" + exp + "]";
					throw new ServiceException(BLOOD_SACRIFICE_TOOL_NOT_ENOUGH, message);
				} else {
					looseBO.setExp(toolNum); // 失去的经验
				}
				break;
			case ToolType.HERO_BAG:
				// 查询武将背包上限
				int heroMax = userExtinfoDao.get(userId).getHeroMax();
				if (heroMax < toolNum) {
					String message = "武将背包空间不足.userId[" + userId + "], heroMax[" + heroMax + "]";
					throw new ServiceException(BLOOD_SACRIFICE_TOOL_NOT_ENOUGH, message);
				} else {
					looseBO.setHeroBag(toolNum);
				}
				break;
			case ToolType.EQUIP_BAG:
				// 查询装备背包
				int equipMax = userExtinfoDao.get(userId).getEquipMax();
				if (equipMax < toolNum) {
					String message = "装备背包不足.userId[" + userId + "], equipMax[" + equipMax + "]";
					throw new ServiceException(BLOOD_SACRIFICE_TOOL_NOT_ENOUGH, message);
				} else {
					looseBO.setEquipBag(toolNum);
				}
				break;
			case ToolType.HERO:
				userHeroDao.getUserHeroList(userId);
				looseBO.getHeroBOList().addAll(exchangeHero(userId, toolId, toolNum, userHeroId, useUserHeroId));
				break;
			case ToolType.EQUIP:
				looseBO.getEquipBOList().addAll(exchangeEquip(userId, toolId, toolNum));
				break;
			case ToolType.MATERIAL:
			case ToolType.GIFT_BOX:
			case ToolType.HERO_SHARD:
			case ToolType.SKILL_BOOK:
				int userToolNum = userToolDao.getUserToolNum(userId, toolId);
				if (userToolNum < toolNum) {
					String message = "道具数量不够.userId[" + userId + "], equipId[" + toolId + "]";
					throw new ServiceException(BLOOD_SACRIFICE_TOOL_NOT_ENOUGH, message);
				} else {
					SystemBloodSacrificeToolBO toolBo = new SystemBloodSacrificeToolBO();
					toolBo.setToolId(toolId);
					toolBo.setToolNum(toolNum);
					looseBO.getToolBOList().add(toolBo);
				}
				break;
			default:
				break;
			}
		}
		return looseBO;
	}

	private List<SystemBloodSacrificeHeroBO> exchangeHero(String userId, int toolId, int exchangeNum, String userHeroId, String useUserHeroId) {
		// 将用户中相同 system_hero_id 的武将按照 hero_level 从小大排列
		List<UserHero> userHeroList = userHeroDao.listUserHeroByLevelAsc(userId, toolId);
		List<SystemBloodSacrificeHeroBO> heroBoList = new ArrayList<SystemBloodSacrificeHeroBO>();

		for (UserHero uh : userHeroList) {
			SystemHero iSystemHero = systemHeroDao.get(uh.getSystemHeroId());
			if (iSystemHero.getHeroColor() == 6) {
				heroBoList.remove(uh);
			}
		}

		int userHeroNum = userHeroList.size();

		// 检查这种类型武将数量是否足够
		if (userHeroNum < (exchangeNum - 2)) {
			String message = "武将数量不够.userId[" + userId + "], systemHeroId[" + toolId + "]";
			throw new ServiceException(BLOOD_SACRIFICE_TOOL_NOT_ENOUGH, message);
		} else {

			for (int i = 0, j = 0; i < exchangeNum; j++) {
				UserHero userHero = userHeroList.get(j);
				if (userHero.getUserHeroId().endsWith(userHeroId) || userHero.getUserHeroId().endsWith(useUserHeroId) || userHero.getLockStatus() > 0) {
					continue;
				}
				SystemBloodSacrificeHeroBO heroBO = new SystemBloodSacrificeHeroBO();
				heroBO.setUserHeroId(userHero.getUserHeroId());
				heroBoList.add(heroBO);
				i++;
			}

			if (exchangeNum > heroBoList.size()) {
				String message = "用于消耗的武将中有被锁定的武将.userId[" + userId + "], systemHeroId[" + toolId + "]";
				throw new ServiceException(HERO_HAS_LOCKED, message);
			}

			return heroBoList;
		}
	}

	private List<SystemBloodSacrificeEquipBO> exchangeEquip(String userId, int toolId, int exchangeNum) {
		List<UserEquip> userEquipList = userEquipDao.listUserEquipByLevelAsc(userId, toolId);
		List<SystemBloodSacrificeEquipBO> equipBoList = new ArrayList<SystemBloodSacrificeEquipBO>();
		int userEquipNum = userEquipList.size();

		if (userEquipNum < exchangeNum) {
			String message = "装备数量不够.userId[" + userId + "], equipId[" + toolId + "]";
			throw new ServiceException(BLOOD_SACRIFICE_TOOL_NOT_ENOUGH, message);
		} else {
			for (int i = 0; i < exchangeNum; i++) {
				UserEquip userEquip = userEquipList.get(i);
				SystemBloodSacrificeEquipBO equipBo = new SystemBloodSacrificeEquipBO();
				equipBo.setUserEquipId(userEquip.getUserEquipId());
				equipBoList.add(equipBo);
			}

			return equipBoList;
		}
	}

	/**
	 * 血祭-为用户删除要兑换的物品
	 */
	private void reduceTool(String userId, SystemBloodSacrificeLooseBO looseBO) {

		User user = userService.get(userId);
		int toolUseType = ToolUseType.REDUCE_TOOL_BLOOD;

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
			List<SystemBloodSacrificeHeroBO> heroBoList = looseBO.getHeroBOList();
			List<UserHero> userHeroList = new ArrayList<UserHero>();
			for (SystemBloodSacrificeHeroBO heroBo : heroBoList) {
				String userHeroId = heroBo.getUserHeroId();
				UserHero userHero = this.get(userId, userHeroId);
				if (userHero != null) {
					userHeroList.add(userHero);
				}
			}
			this.delete(userId, userHeroList, toolUseType);
		}
		if (looseBO.getEquipBOList().size() != 0) {
			List<SystemBloodSacrificeEquipBO> equipBoList = looseBO.getEquipBOList();
			for (SystemBloodSacrificeEquipBO equipBO : equipBoList) {
				userEquipDao.delete(userId, equipBO.getUserEquipId());
			}
		}
		if (looseBO.getToolBOList().size() != 0) {
			List<SystemBloodSacrificeToolBO> toolBoList = looseBO.getToolBOList();
			for (SystemBloodSacrificeToolBO toolBo : toolBoList) {
				userToolDao.reduceUserTool(userId, toolBo.getToolId(), toolBo.getToolNum());
			}
		}
	}

	/**
	 * 该武将是否可血祭 检验武将是否满足：1）品质为5，等级达到100级，3）五星武将。 1， 武将为三星或以上武将 2，
	 * 武将血祭等级未到达上限，（3星+5 为上限、4星 +9为上限 、5星 +14为上限）
	 * 
	 * @param userId
	 */
	private void checkInheroBS(UserHero userHero, int heroStar) {

		if (heroStar <= 2) {
			String message = "被传承武将不满足条件，星级小于等于2.uhid[" + userHero.getUserHeroId() + "]";
			LOG.info(message);
			throw new ServiceException(BLOOD_SACRIFICE_HERO_NOT_MATCH, message);
		}

		if (heroStar == 3 && userHero.getBloodSacrificeStage() >= 5) {
			String message = "被传承武将血祭次数已满，3星+5 为上限.uhid[" + userHero.getUserHeroId() + "]";
			LOG.info(message);
			throw new ServiceException(BLOOD_SACRIFICE_IS_MAX_STAGE, message);
		}
		if (heroStar == 4 && userHero.getBloodSacrificeStage() >= 9) {
			String message = "被传承武将血祭次数已满，4星+9为上限.uhid[" + userHero.getUserHeroId() + "]";
			LOG.info(message);
			throw new ServiceException(BLOOD_SACRIFICE_IS_MAX_STAGE, message);
		}
		if (heroStar == 5 && userHero.getBloodSacrificeStage() >= 14) {
			String message = "被传承武将血祭次数已满，5星+14为上限.uhid[" + userHero.getUserHeroId() + "]";
			LOG.info(message);
			throw new ServiceException(BLOOD_SACRIFICE_IS_MAX_STAGE, message);
		}

	}

	@Override
	public boolean lockHero(String userId, String userHeroId) {

		UserHero userHero = this.userHeroDao.get(userId, userHeroId); // 血祭的武将

		if (userHero == null) {
			String message = "用户锁定的英雄不存在.userId[" + userId + "], userHeroId[" + userHeroId + "]";
			throw new ServiceException(HERO_NOT_EXIST, message);
		}

		SystemHero iSystemHero = systemHeroDao.get(userHero.getSystemHeroId());

		if (iSystemHero.getHeroStar() < 4 && iSystemHero.getHeroId() != 100021 && iSystemHero.getHeroId() != 136) {
			String message = "用户锁定的英雄的不能被.userId[" + userId + "], userHeroId[" + userHeroId + "]";
			throw new ServiceException(LOCK_HERO_STAR_NOT_ENOUGH, message);
		}

		if (userHero.getLockStatus() != 0) {
			String message = "用户锁定的英雄已经被锁定了.userId[" + userId + "], userHeroId[" + userHeroId + "]";
			throw new ServiceException(LOCK_HERO_HAS_LOCKED, message);
		}

		return this.userHeroDao.lockHero(userId, userHeroId);
	}

	@Override
	public boolean unlockHero(String userId, String userHeroId) {

		UserHero userHero = this.userHeroDao.get(userId, userHeroId); // 血祭的武将

		if (userHero == null) {
			String message = "用户锁定的英雄不存在.userId[" + userId + "], userHeroId[" + userHeroId + "]";
			throw new ServiceException(HERO_NOT_EXIST, message);
		}

		if (userHero.getLockStatus() != 1) {
			String message = "用户锁定的英雄未被锁定了.userId[" + userId + "], userHeroId[" + userHeroId + "]";
			throw new ServiceException(LOCK_HERO_WAS_UNLOCKED, message);
		}

		return this.userHeroDao.unlockHero(userId, userHeroId);
	}

	@Override
	public List<UserHeroBO> getDeifyPeopleList(String uid) {
		List<UserHero> list = this.userHeroDao.getDeifyPeopleList(uid);
		List<UserHeroBO> userHeroBOs = new ArrayList<UserHeroBO>();
		for (UserHero userHero : list) {
			UserHeroBO userHeroBO = this.createUserHeroBO(userHero);
			userHeroBOs.add(userHeroBO);
		}
		return userHeroBOs;
	}

	@Override
	public BattleHeroBO getUserBattleHeroBO(String uid, String userHeroId) {

		UserHeroBO userHeroBO = this.getUserHeroBO(uid, userHeroId);

		// 武将被动技能(学习的)
		List<UserHeroSkill> userHeroSkillList = this.userHeroSkillDao.getList(uid, userHeroId);
		Map<String, Set<Integer>> userHeroSkillIdMap = new HashMap<String, Set<Integer>>();
		for (UserHeroSkill userHeroSkill : userHeroSkillList) {
			Set<Integer> userHeroSkillIds = null;
			if (userHeroSkillIdMap.containsKey(userHeroId)) {
				userHeroSkillIds = userHeroSkillIdMap.get(userHeroId);
			} else {
				userHeroSkillIds = new HashSet<Integer>();
			}

			userHeroSkillIds.add(userHeroSkill.getSkillId());
			userHeroSkillIdMap.put(userHeroId, userHeroSkillIds);
		}

		BattleHeroBO battleHeroBO = BOHelper.createBattleHeroBO(userHeroBO);

		if (userHeroSkillIdMap.containsKey(userHeroBO.getUserHeroId())) {
			battleHeroBO.getSkillList().addAll(userHeroSkillIdMap.get(userHeroBO.getUserHeroId()));
		}

		if (userHeroBO.getSuitSkillId() > 0) {
			battleHeroBO.getSkillList().add(userHeroBO.getSuitSkillId());
		}

		return battleHeroBO;
	}
}
