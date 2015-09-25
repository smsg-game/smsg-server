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

import com.lodogame.game.dao.SystemDeifyAttrDao;
import com.lodogame.game.dao.SystemDeifyDefineDao;
import com.lodogame.game.dao.SystemEquipAttrDao;
import com.lodogame.game.dao.SystemEquipDao;
import com.lodogame.game.dao.SystemEquipUpgradeDao;
import com.lodogame.game.dao.SystemEquipUpgradeToolDao;
import com.lodogame.game.dao.SystemPolishConsumeDao;
import com.lodogame.game.dao.SystemPolishDao;
import com.lodogame.game.dao.UserEquipDao;
import com.lodogame.game.dao.UserEquipPolishDao;
import com.lodogame.game.dao.UserEquipPolishTempDao;
import com.lodogame.game.dao.UserToolDao;
import com.lodogame.game.utils.json.Json;
import com.lodogame.ldsg.bo.DeifyBo;
import com.lodogame.ldsg.bo.DropToolBO;
import com.lodogame.ldsg.bo.UserEquipBO;
import com.lodogame.ldsg.bo.UserEquipPolishBO;
import com.lodogame.ldsg.checker.TaskChecker;
import com.lodogame.ldsg.constants.ActivityTargetType;
import com.lodogame.ldsg.constants.LogTypes;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.constants.TaskTargetType;
import com.lodogame.ldsg.constants.ToolId;
import com.lodogame.ldsg.constants.ToolType;
import com.lodogame.ldsg.constants.ToolUseType;
import com.lodogame.ldsg.event.CopperUpdateEvent;
import com.lodogame.ldsg.event.EquipUpdateEvent;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.HeroUpdateEvent;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.helper.DropToolHelper;
import com.lodogame.ldsg.helper.EquipHelper;
import com.lodogame.ldsg.service.ActivityTaskService;
import com.lodogame.ldsg.service.EquipService;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.TaskService;
import com.lodogame.ldsg.service.ToolService;
import com.lodogame.ldsg.service.UnSynLogService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.SystemDeifyAttr;
import com.lodogame.model.SystemDeifyDefine;
import com.lodogame.model.SystemEquip;
import com.lodogame.model.SystemEquipAttr;
import com.lodogame.model.SystemEquipUpgrade;
import com.lodogame.model.SystemEquipUpgradeTool;
import com.lodogame.model.SystemHero;
import com.lodogame.model.SystemPolish;
import com.lodogame.model.SystemPolishConsume;
import com.lodogame.model.User;
import com.lodogame.model.UserEquip;
import com.lodogame.model.UserEquipPolish;
import com.lodogame.model.UserEquipPolishTemp;
import com.lodogame.model.UserHero;

public class EquipServiceImpl implements EquipService {

	private static final Logger LOG = Logger.getLogger(EquipServiceImpl.class);

	@Autowired
	private TaskService taskService;

	@Autowired
	private UserEquipDao userEquipDao;

	@Autowired
	private SystemEquipDao systemEquipDao;

	@Autowired
	private UserService userService;

	@Autowired
	private SystemEquipAttrDao systemEquipAttrDao;

	@Autowired
	private SystemEquipUpgradeDao systemEquipUpgradeDao;

	@Autowired
	private SystemEquipUpgradeToolDao systemEquipUpgradeToolDao;

	@Autowired
	private UserToolDao userToolDao;

	@Autowired
	private HeroService heroService;

	@Autowired
	private ToolService toolService;

	@Autowired
	private ActivityTaskService activityTaskService;

	@Autowired
	private UnSynLogService unSynLogService;

	@Autowired
	private SystemDeifyAttrDao systemDeifyAttrDao;

	@Autowired
	private SystemPolishDao systemPolishDao;

	@Autowired
	private SystemPolishConsumeDao systemPolishConsumeDao;

	@Autowired
	private UserEquipPolishDao userEquipPolishDao;

	@Autowired
	private UserEquipPolishTempDao userEquipPolishTempDao;

	@Autowired
	private SystemDeifyDefineDao systemDeifyDefineDao;

	public List<UserEquipBO> getUserEquipList(String userId) {

		List<UserEquipBO> userEquipBOList = new ArrayList<UserEquipBO>();

		List<UserEquip> userEquipList = this.userEquipDao.getUserEquipList(userId);
		for (UserEquip userEquip : userEquipList) {
			UserEquipBO userEquipBO = this.createEquipBO(userEquip);
			userEquipBOList.add(userEquipBO);
		}

		return userEquipBOList;

	}

	@Override
	public List<UserEquipBO> getUserHeroEquipList(String userId, String userHeroId) {

		List<UserEquipBO> userEquipBOList = new ArrayList<UserEquipBO>();

		List<UserEquip> userEquipList = this.userEquipDao.getHeroEquipList(userId, userHeroId);
		for (UserEquip userEquip : userEquipList) {
			UserEquipBO userEquipBO = this.createEquipBO(userEquip);
			userEquipBOList.add(userEquipBO);
		}

		return userEquipBOList;

	}

	/**
	 * 创建用户装备BO
	 * 
	 * @param userEquip
	 * @return
	 */
	private UserEquipBO createEquipBO(UserEquip userEquip) {
		UserEquipBO userEquipBO = new UserEquipBO();

		if (this.userEquipPolishDao.getUserEquipPolishById(userEquip.getUserEquipId()) != null) {
			userEquipBO.setUserEquipPolishBO(this.createUserEquipPolishBO(this.userEquipPolishDao.getUserEquipPolishById(userEquip.getUserEquipId())));
		}

		SystemDeifyAttr systemDeifyAttr = this.systemDeifyAttrDao.get(userEquip.getEquipId(), userEquip.getEquipLevel());
		if (systemDeifyAttr != null) {
			SystemDeifyDefine systemDeifyDefine = this.systemDeifyDefineDao.get(systemDeifyAttr.getDeifyId());
			userEquipBO.setEquipId(userEquip.getEquipId());
			userEquipBO.setEquipLevel(userEquip.getEquipLevel());
			userEquipBO.setUserEquipId(userEquip.getUserEquipId());
			userEquipBO.setUserHeroId(userEquip.getUserHeroId());
			userEquipBO.setEquipName(systemDeifyDefine.getDeifyName());
			userEquipBO.setEquipType(systemDeifyDefine.getEquipType());
			userEquipBO.setPhysicsAttack(systemDeifyAttr.getAttack());
			userEquipBO.setPhysicsDefense(systemDeifyAttr.getDefense());
			userEquipBO.setLife(systemDeifyAttr.getLife());
			userEquipBO.setPrice(0);
			userEquipBO.setUpgradetRate(0);
			userEquipBO.setStart(systemDeifyAttr.getDeifyLevel());
			return userEquipBO;
		}

		SystemEquip systemEquip = this.systemEquipDao.get(userEquip.getEquipId());

		int equipLevel = userEquip.getEquipLevel();
		if (systemEquip != null && equipLevel > systemEquip.getMaxLevel()) {
			equipLevel = systemEquip.getMaxLevel();
		}

		SystemEquipAttr systemEquipAttr = this.systemEquipAttrDao.getEquipAttr(userEquip.getEquipId(), equipLevel);

		userEquipBO.setEquipId(userEquip.getEquipId());
		userEquipBO.setEquipLevel(equipLevel);
		userEquipBO.setUserEquipId(userEquip.getUserEquipId());
		userEquipBO.setUserHeroId(userEquip.getUserHeroId());

		if (systemEquip == null) {
			return userEquipBO;
		}

		userEquipBO.setEquipName(systemEquip.getEquipName());
		userEquipBO.setEquipType(systemEquip.getEquipType());

		if (systemEquipAttr == null) {
			LOG.error("装备等级配置信息不存在.equipId[" + userEquip.getEquipId() + "], equipLevel[" + equipLevel + "]");
			return userEquipBO;
		}
		userEquipBO.setPhysicsAttack(systemEquipAttr.getPhysicalAttack());
		userEquipBO.setPhysicsDefense(systemEquipAttr.getPhysicalDefense());
		userEquipBO.setLife(systemEquipAttr.getLife());
		userEquipBO.setPrice(systemEquipAttr.getRecyclePrice());
		userEquipBO.setUpgradetRate(systemEquipAttr.getUpgradeRate());

		return userEquipBO;
	}

	public boolean updateEquipHero(String userId, String userEquipId, String userHeroId, int equipType, EventHandle handle) {

		UserEquip userEquip = this.userEquipDao.get(userId, userEquipId);

		if (userEquip == null) {
			throw new ServiceException(ServiceReturnCode.FAILD, "穿载装备出错，用户装备不存在.userEquipId[" + userEquipId + "]");
		}

		if (!StringUtils.equalsIgnoreCase(userId, userEquip.getUserId())) {
			throw new ServiceException(ServiceReturnCode.FAILD, "穿载装备出错，装备所有者不符.userEquipId[" + userEquipId + "], userId[" + userId + "], userEquip.userId[" + userEquip.getUserId() + "]");
		}

		UserHero userHero = this.heroService.get(userId, userHeroId);

		SystemHero systemHero = this.heroService.getSysHero(userHero.getSystemHeroId());
		SystemEquip systemEquip = this.getSysEquip(userEquip.getEquipId());

		if (systemEquip.getColor() == 7) {
			if (!(systemHero.getHeroId() == systemEquip.getHeroId() && systemHero.getHeroColor() == 7)) {
				String message = "穿载装备出错，专属神器只能穿戴在所属神将身上";
				LOG.warn(message);
				throw new ServiceException(INSTALL_EQUIP_DEIFY_ERROR, message);
			}
		}

		if (systemEquip.getCareer() != 100 && systemEquip.getCareer() != systemHero.getCareer()) {
			String message = "穿载装备出错，武将职业不符.hero.career[" + systemHero.getCareer() + "],equip.career[" + systemEquip.getCareer() + "]";
			LOG.warn(message);
			throw new ServiceException(INSTALL_EQUIP_CAREER_ERROR, message);
		}

		UserEquip oldUserEquip = null;

		// 拿到武将当前身上的装备，看有没有替换的
		List<UserEquip> heroEquipList = this.userEquipDao.getHeroEquipList(userId, userHeroId);
		for (UserEquip heroEquip : heroEquipList) {
			int equipId = heroEquip.getEquipId();
			SystemEquip systemEquip2 = this.systemEquipDao.get(equipId);
			if (systemEquip2.getEquipType() == equipType) {
				oldUserEquip = heroEquip;
				break;
			}
		}

		String oldEquipHeroID = userEquip.getUserHeroId();

		boolean success = this.userEquipDao.updateEquipHero(userId, userEquipId, userHeroId);

		userEquip.setUserHeroId(userHeroId);

		if (success && oldUserEquip != null) {
			this.userEquipDao.updateEquipHero(userId, oldUserEquip.getUserEquipId(), null);
			oldUserEquip.setUserHeroId(null);

			EquipUpdateEvent equipUpdateEvent = new EquipUpdateEvent(userId, oldUserEquip);
			handle.handle(equipUpdateEvent);
		}

		// 穿戴装备武将更新
		HeroUpdateEvent heroUpdateEvent = new HeroUpdateEvent(userId, userHeroId);
		handle.handle(heroUpdateEvent);

		// 原来的穿戴武将
		if (StringUtils.isNotEmpty(oldEquipHeroID)) {
			// 被卸下装备武将更新
			HeroUpdateEvent heroUpdateOffEvent = new HeroUpdateEvent(userId, oldEquipHeroID);
			handle.handle(heroUpdateOffEvent);
		}

		return true;
	}

	@Override
	public int autoUpgrade(String userId, String userEquipId, List<Integer> addLevelList) {

		// 一键强化停下来的原因
		int stopResult = 0;

		LOG.debug("装备一键强化.userId[" + userId + "], userEquipId[" + userEquipId + "]");
		UserEquip userEquip = this.getUserEquip(userId, userEquipId);

		User user = this.userService.get(userId);

		if (user.getVipLevel() < 2) {
			throw new ServiceException(ServiceReturnCode.FAILD, "一键强化失败,VIP等级不足");
		}

		SystemEquip systemEquip = this.getSysEquip(userEquip.getEquipId());

		int totalNeedCopper = 0;
		int totalAddLevel = 0;

		int i = 0;
		while (i < 100) {
			i += 1;

			if (userEquip.getEquipLevel() >= systemEquip.getMaxLevel()) {
				stopResult = UPGRADE_EQUIP_HERO_LEVEL_NOT_ENOUGH;
				break;
			}

			if (userEquip.getEquipLevel() >= user.getLevel()) {
				stopResult = UPGRADE_EQUIP_LEVEL_OVER_USER_LEVEL;
				break;
			}

			SystemEquipAttr systemEquipAttr = this.systemEquipAttrDao.getEquipAttr(userEquip.getEquipId(), userEquip.getEquipLevel());

			int needCopper = systemEquipAttr.getUpgradePrice();
			if (totalNeedCopper + needCopper > user.getCopper()) {
				stopResult = UPGRADE_EQUIP_COPPER_NOT_ENOUGH;
				break;
			}

			// 上升等级
			int addLevel = this.getAddLevel();

			if (addLevel + userEquip.getEquipLevel() > systemEquip.getMaxLevel()) {
				addLevel = systemEquip.getMaxLevel() - userEquip.getEquipLevel();
			}

			if (addLevel + userEquip.getEquipLevel() > user.getLevel()) {
				addLevel = user.getLevel() - userEquip.getEquipLevel();
			}

			userEquip.setEquipLevel(userEquip.getEquipLevel() + addLevel);

			totalAddLevel += addLevel;
			totalNeedCopper += needCopper;

			addLevelList.add(addLevel);

		}

		if (totalAddLevel > 0) {
			if (this.userService.reduceCopper(userId, totalNeedCopper, ToolUseType.REDUCE_ADUO_UPGRADE_EQUIP)) {
				this.userEquipDao.updateEquipLevel(userId, userEquipId, totalAddLevel, systemEquip.getMaxLevel());
			}
		} else {
			throw new ServiceException(stopResult, "不能强化");
		}

		if (addLevelList.size() > 0) {
			// 活跃度奖励
			activityTaskService.updateActvityTask(userId, ActivityTargetType.UPDATE_EQUIP, addLevelList.size());
		}

		return stopResult;
	}

	public boolean upgrade(String userId, String userEquipId, EventHandle handle) {

		LOG.debug("装备强化.userId[" + userId + "], userEquipId[" + userEquipId + "]");
		UserEquip userEquip = this.getUserEquip(userId, userEquipId);

		User user = this.userService.get(userId);
		if (userEquip.getEquipLevel() >= user.getLevel()) {
			throw new ServiceException(UPGRADE_EQUIP_LEVEL_OVER_USER_LEVEL, "装备强化失败.装备等级超过用户等级.userId[" + userId + "], userEquipId[" + userEquipId + "]");
		}

		// 所有人验证
		this.checkIsOwner(userId, userEquip);

		SystemEquip systemEquip = this.getSysEquip(userEquip.getEquipId());

		if (userEquip.getEquipLevel() >= systemEquip.getMaxLevel()) {
			throw new ServiceException(UPGRADE_EQUIP_HERO_LEVEL_NOT_ENOUGH, "装备强化失败.装备等级超过最大等级.userId[" + userId + "], userEquipId[" + userEquipId + "]");
		}

		// 最大等级
		int maxLevel = systemEquip.getMaxLevel();
		if (maxLevel > user.getLevel()) {
			maxLevel = user.getLevel();
		}

		// 不是vip需要根据概率是否增加强化等级
		SystemEquipAttr systemEquipAttr = this.systemEquipAttrDao.getEquipAttr(userEquip.getEquipId(), userEquip.getEquipLevel());

		int needCopper = systemEquipAttr.getUpgradePrice();

		// boolean success = this.userDao.reduceCopper(userId, needCopper);
		boolean success = this.userService.reduceCopper(userId, needCopper, ToolUseType.REDUCE_UPGRADE_EQUIP);
		// 先扣除钱，再更新装备
		if (success) {

			// 上升等级
			int addLevel = this.getAddLevel();

			// 活跃度奖励
			activityTaskService.updateActvityTask(userId, ActivityTargetType.UPDATE_EQUIP, 1);

			// int randVal = new Random().nextInt(100);

			// 爆击
			// if (user.getVipLevel() == 3 || randVal <
			// systemEquipAttr.getUpgradeRate()) {
			// addLevel = 2 + RandomUtils.nextInt(4);
			// }

			if (this.userEquipDao.updateEquipLevel(userId, userEquipId, addLevel, maxLevel)) {
				// 装备更新事件
				EquipUpdateEvent equipUpdateEvent = new EquipUpdateEvent(userId, userEquipId);
				// 银币更新事件
				CopperUpdateEvent copperUpdateEvent = new CopperUpdateEvent(userId);

				if (!StringUtils.isBlank(userEquip.getUserHeroId())) {
					// 武将更新事件
					HeroUpdateEvent heroUpdateEvent = new HeroUpdateEvent(userId, userEquip.getUserHeroId());
					handle.handle(heroUpdateEvent);
				}

				handle.handle(equipUpdateEvent);
				handle.handle(copperUpdateEvent);
				// 写入装备操作日志
				int equipMentLevel = (userEquip.getEquipLevel() + addLevel) > maxLevel ? maxLevel : (userEquip.getEquipLevel() + addLevel);
				unSynLogService.userEquipMentOperatorLog(userId, userEquip.getUserEquipId(), userEquip.getEquipId(), userEquip.getUserHeroId(), equipMentLevel, LogTypes.EQUIPMENT_STRENTH, new Date());
			} else {
				throw new ServiceException(ServiceReturnCode.FAILD, "装备强化失败,数据异常. userId[" + userId + "], userEquipId[" + userEquipId + "], userEquip.userHeroId[" + userEquip.getUserHeroId() + "]");
			}

		} else {
			throw new ServiceException(UPGRADE_EQUIP_COPPER_NOT_ENOUGH, "装备强化失败.用户银币不足.userId[" + userId + "], userEquipId[" + userEquipId + "]");
		}

		return success;
	}

	/**
	 * 获取强化上升等级
	 * 
	 * @return
	 */
	private int getAddLevel() {

		int rand = RandomUtils.nextInt(10000);
		if (rand >= 7000) {
			return 5;
		} else if (rand > 5000) {
			return 4;
		} else if (rand > 3000) {
			return 3;
		} else if (rand > 1000) {
			return 2;
		} else {
			return 1;
		}

	}

	public boolean sell(String userId, List<String> userEquipIdList, EventHandle handle) {

		int amount = 0;
		// 装备列表
		List<UserEquip> equipMentList = new ArrayList<UserEquip>();
		for (String userEquipId : userEquipIdList) {
			amount += this.checkIsSellAble(userId, userEquipId, equipMentList);
		}

		// 先删除装备，后给银币
		boolean success = this.userEquipDao.delete(userId, userEquipIdList);

		// 日志
		unSynLogService.toolLog(userId, ToolType.EQUIP, 0, userEquipIdList.size(), ToolUseType.REDUCE_SELL_EQUIP, -1, StringUtils.join(userEquipIdList, ","), success);

		if (success) {
			if (this.userService.addCopper(userId, amount, ToolUseType.ADD_SELL_EQUIP)) {
				CopperUpdateEvent event = new CopperUpdateEvent(userId);
				handle.handle(event);
			} else {
				String message = "出售装备后增加银币异常.userId[" + userId + "], userEquipId[" + StringUtils.join(userEquipIdList, ",") + "], amount[" + amount + "]";
				LOG.error(message);
				throw new ServiceException(ServiceReturnCode.FAILD, message);
			}
			// 添加装备出售日志
			if (equipMentList.size() > 0) {
				Date d = new Date();
				for (UserEquip userEquip : equipMentList) {
					unSynLogService.userEquipMentOperatorLog(userId, userEquip.getUserEquipId(), userEquip.getEquipId(), userEquip.getUserHeroId(), userEquip.getEquipLevel(),
							LogTypes.EQUIPMENT_SALES, d);
				}
			}
		}

		return success;
	}

	/**
	 * 判断装备是否可以出售
	 * 
	 * @param userId
	 * @param userEquip
	 */
	private int checkIsSellAble(String userId, String userEquipId, List<UserEquip> equipMentList) {

		UserEquip userEquip = this.getUserEquip(userId, userEquipId);

		if (userEquip == null) {
			String message = "出售装备异常装备不存在.userId[" + userId + "], userEquipId[" + userEquipId + "]";
			LOG.error(message);
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		// 所有人验证
		this.checkIsOwner(userId, userEquip);

		if (!StringUtils.isBlank(userEquip.getUserHeroId())) {
			throw new ServiceException(SELL_EQUIP_IS_INSTALLED, "装备出售失败,装备已经穿戴. userId[" + userId + "], userEquipId[" + userEquipId + "], userEquip.userHeroId[" + userEquip.getUserHeroId() + "]");
		}

		SystemEquipAttr systemEquipAttr = this.systemEquipAttrDao.getEquipAttr(userEquip.getEquipId(), userEquip.getEquipLevel());

		int price = systemEquipAttr.getRecyclePrice();
		equipMentList.add(userEquip);
		return price;
	}

	/**
	 * 装备所有人验证
	 * 
	 * @param userId
	 * @param userEquip
	 */
	private void checkIsOwner(String userId, UserEquip userEquip) {

		if (!StringUtils.equalsIgnoreCase(userEquip.getUserId(), userId)) {
			throw new ServiceException(ServiceReturnCode.FAILD, "装备所有者验证失败,装备所有人不符. userId[" + userId + "], userEquipId[" + userEquip.getUserEquipId() + "], userEquip.userId[" + userEquip.getUserId()
					+ "]");
		}
	}

	public UserEquip getUserEquip(String userId, String userEquipId) {
		UserEquip userEquip = this.userEquipDao.get(userId, userEquipId);
		if (userEquip == null) {
			throw new ServiceException(ServiceReturnCode.FAILD, "装备获取失败,装备不存在. userEquipId[" + userEquipId + "]");
		}

		return userEquip;
	}

	public SystemEquip getSysEquip(int equipId) {

		SystemDeifyDefine systemDeifyDefine = this.systemDeifyDefineDao.get(equipId);
		if (null != systemDeifyDefine) {
			SystemEquip systemEquip = new SystemEquip();
			systemEquip.setCareer(systemDeifyDefine.getCareer());
			systemEquip.setColor(systemDeifyDefine.getColor());
			systemEquip.setEquipId(systemDeifyDefine.getDeifyId());
			systemEquip.setEquipName(systemDeifyDefine.getDeifyName());
			systemEquip.setEquipStar(0);
			systemEquip.setEquipType(systemDeifyDefine.getEquipType());
			systemEquip.setLife(0);
			systemEquip.setMaxLevel(0);
			systemEquip.setPhysicalAttack(0);
			systemEquip.setPhysicalDefense(0);
			systemEquip.setHeroId(systemDeifyDefine.getHeroId());
			return systemEquip;
		}

		SystemEquip systemEquip = this.systemEquipDao.get(equipId);
		if (systemEquip == null) {
			String message = "系统装备获取失败,装备不存在. equipId[" + equipId + "]";
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		return systemEquip;
	}

	public UserEquipBO getUserEquipBO(String userId, String userEquipId) {
		UserEquip userEquip = this.getUserEquip(userId, userEquipId);
		return this.createEquipBO(userEquip);
	}

	public boolean addUserEquip(String userId, String userEquipId, final int equipId, int useType) {

		UserEquip userEquip = new UserEquip();
		userEquip.setEquipId(equipId);

		int equipLevel = 1;
		if (systemDeifyDefineDao.get(equipId) != null) {
			equipLevel = 0;
		}

		userEquip.setEquipLevel(equipLevel);
		userEquip.setUserEquipId(userEquipId);
		userEquip.setUserId(userId);
		userEquip.setCreatedTime(new Date());
		userEquip.setUpdatedTime(new Date());

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
					int needEquipId = NumberUtils.toInt(params.get("equip_id"));
					return equipId == needEquipId;
				}
				return false;
			}
		});

		boolean success = this.userEquipDao.add(userEquip);

		unSynLogService.toolLog(userId, ToolType.EQUIP, equipId, 1, useType, 1, userEquipId, success);
		unSynLogService.userEquipMentOperatorLog(userId, userEquip.getUserEquipId(), userEquip.getEquipId(), userEquip.getUserHeroId(), userEquip.getEquipLevel(), LogTypes.EQUIPMENT_GET, new Date());
		return success;
	}

	@Override
	public boolean addUserEquips(String userId, Map<String, Integer> equipIdMap, int useType) {
		List<UserEquip> userEquipList = new ArrayList<UserEquip>();

		Set<Integer> gainSystemEquipIdSet = new HashSet<Integer>();

		for (Map.Entry<String, Integer> entry : equipIdMap.entrySet()) {

			final int systemEquipId = entry.getValue();

			int equipLevel = 1;
			if (systemDeifyDefineDao.get(systemEquipId) != null) {
				equipLevel = 0;
			}

			String userEquipId = entry.getKey();

			Date now = new Date();

			UserEquip userEquip = new UserEquip();
			userEquip.setEquipId(systemEquipId);
			userEquip.setUserEquipId(userEquipId);
			userEquip.setCreatedTime(now);
			userEquip.setUpdatedTime(now);
			userEquip.setUserId(userId);
			userEquip.setEquipLevel(equipLevel);

			gainSystemEquipIdSet.add(systemEquipId);

			userEquipList.add(userEquip);

		}

		boolean success = this.userEquipDao.addEquips(userEquipList);

		// 装备添加操作日志
		for (UserEquip userEquip : userEquipList) {
			unSynLogService.userEquipMentOperatorLog(userId, userEquip.getUserEquipId(), userEquip.getEquipId(), userEquip.getUserHeroId(), userEquip.getEquipLevel(), LogTypes.EQUIPMENT_GET,
					new Date());
			unSynLogService.userTreasureLog(userId, userEquip.getEquipId(), ToolType.EQUIP, 1, useType + "", 1, "", new Date());
		}
		unSynLogService.toolLog(userId, ToolType.EQUIP, 0, equipIdMap.size(), useType, 1, Json.toJson(equipIdMap), success);

		for (final Integer systemEquipId : gainSystemEquipIdSet) {
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
						int needEquipId = NumberUtils.toInt(params.get("equip_id"));
						return systemEquipId == needEquipId;
					}
					return false;
				}
			});
		}

		return success;
	}

	@Override
	public Map<String, Object> upgradePre(String userId, String userEquipId) {

		UserEquipBO userEquipBO = null;
		int status = 1;

		LOG.debug("装备强化预览 .userId[" + userId + "], userEquipId[" + userEquipId + "]");
		UserEquip userEquip = this.getUserEquip(userId, userEquipId);

		SystemEquip systemEquip = this.getSysEquip(userEquip.getEquipId());

		SystemEquipAttr systemEquipAttr = this.systemEquipAttrDao.getEquipAttr(userEquip.getEquipId(), userEquip.getEquipLevel());

		int needCopper = systemEquipAttr.getUpgradePrice();

		User user = this.userService.get(userId);
		if (user.getCopper() < needCopper) {
			status = -1;// 银币不足
		} else if (userEquip.getEquipLevel() >= systemEquip.getMaxLevel()) {
			status = -2;// 等级不足
		}

		userEquip.setEquipLevel(userEquip.getEquipLevel() + 1);
		userEquipBO = this.createEquipBO(userEquip);

		Map<String, Object> retVal = new HashMap<String, Object>();

		retVal.put("userEquipBO", userEquipBO);
		retVal.put("status", status);
		retVal.put("needCopper", needCopper);

		return retVal;
	}

	@Override
	public boolean mergeEquip(String userId, String userEquipId, boolean useMoney, EventHandle handle) {

		UserEquip userEquip = this.getUserEquip(userId, userEquipId);

		int equipId = userEquip.getEquipId();

		SystemEquip systemEquip = this.getSysEquip(equipId);
		if (userEquip.getEquipLevel() < systemEquip.getMaxLevel()) {
			String message = "装备合成异常，装备未满级.userId[" + userId + "], userEquipId[" + userEquipId + "]";
			throw new ServiceException(MERGE_EQUIP_NOT_ENOUGH_LEVEL, message);
		}

		SystemEquipUpgrade systemEquipUpgrade = this.systemEquipUpgradeDao.get(equipId);
		if (systemEquipUpgrade == null) {
			String message = "装备合成异常，装备合成配置不存在.userId[" + userId + "], userEquipId[" + userEquipId + "]";
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		List<SystemEquipUpgradeTool> toolList = this.systemEquipUpgradeToolDao.getList(equipId);

		if (!useMoney) {// 不是金币进阶
			this.checkToolEnough(userId, toolList);// 材料是否够
		}

		if (useMoney) {
			User user = this.userService.get(userId);
			if (user.getVipLevel() < 3) {
				String message = "装备合成异常(金币),vip等级不足.userId[" + userId + "], vipLevel[" + user.getVipLevel() + "]";
				throw new ServiceException(MERGE_EQUIP_NOT_ENOUGH_VIP, message);
			}

			int needMoney = EquipHelper.getGoldMergeNeedMoney(systemEquip.getColor());
			if (!this.userService.reduceGold(userId, needMoney, ToolUseType.REDUCE_GOLD_MERGE_EQUIP, user.getLevel())) {
				String message = "装备合成异常(金币)，用户金币不足.userId[" + userId + "], needMoney[" + needMoney + "]";
				throw new ServiceException(MERGE_EQUIP_NOT_ENOUGH_GOLD, message);
			}
		} else {
			// 扣材料
			this.reduceTool(userId, toolList);
		}

		boolean success = this.userEquipDao.updateEquipId(userId, userEquipId, systemEquipUpgrade.getUpgradeEquipId());
		if (!success) {
			String message = "装备合成异常，更新装备系统ID失败.userId[" + userId + "], userEquipId[" + userEquipId + "]";
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		} else {

			// 装备更新事件
			EquipUpdateEvent equipUpdateEvent = new EquipUpdateEvent(userId, userEquipId);

			if (!StringUtils.isBlank(userEquip.getUserHeroId())) {
				// 武将更新事件
				HeroUpdateEvent heroUpdateEvent = new HeroUpdateEvent(userId, userEquip.getUserHeroId());
				handle.handle(heroUpdateEvent);
			}

			handle.handle(equipUpdateEvent);

		}
		// 写入装备操作日志
		unSynLogService.userEquipMentOperatorLog(userId, userEquip.getUserEquipId(), systemEquipUpgrade.getUpgradeEquipId(), userEquip.getUserHeroId(), userEquip.getEquipLevel(),
				LogTypes.EQUIPMENT_STEPUP, new Date());
		return true;

	}

	@Override
	public UserEquipBO mergeEquipPre(String userId, String userEquipId) {

		UserEquip userEquip = this.getUserEquip(userId, userEquipId);

		int equipId = userEquip.getEquipId();

		SystemEquipUpgrade systemEquipUpgrade = this.systemEquipUpgradeDao.get(equipId);
		if (systemEquipUpgrade == null) {
			// String message = "装备合成预览异常，装备合成配置不存在.userId[" + userId +
			// "], userEquipId[" + userEquipId + "]";
			// throw new ServiceException(ServiceReturnCode.FAILD, message);
			return null;
		}

		userEquip.setEquipId(systemEquipUpgrade.getUpgradeEquipId());

		return this.createEquipBO(userEquip);

	}

	/**
	 * 扣材料
	 * 
	 * @param userId
	 * @param toolList
	 */
	private void reduceTool(String userId, List<SystemEquipUpgradeTool> toolList) {

		for (SystemEquipUpgradeTool systemEquipUpgradeTool : toolList) {

			int toolId = systemEquipUpgradeTool.getToolId();
			int toolNum = systemEquipUpgradeTool.getToolNum();
			boolean success = this.toolService.reduceTool(userId, ToolType.MATERIAL, toolId, toolNum, ToolUseType.REDUCE_MERGE_EQUIP);
			if (!success) {
				String message = "装备进阶失败，扣材料失败.userId[" + userId + "], toolId[" + toolId + "], toolNum[" + toolNum + "]";
				LOG.warn(message);
				throw new ServiceException(MERGE_EQUIP_NOT_ENOUGH_TOOL, message);
			}
		}

	}

	/**
	 * 判断用户装备是否足够
	 * 
	 * @param userId
	 * @param toolList
	 */
	public void checkToolEnough(String userId, List<SystemEquipUpgradeTool> toolList) {

		for (SystemEquipUpgradeTool systemEquipUpgradeTool : toolList) {

			int toolId = systemEquipUpgradeTool.getToolId();
			int toolNum = systemEquipUpgradeTool.getToolNum();

			int userToolNum = this.userToolDao.getUserToolNum(userId, toolId);

			if (userToolNum < toolNum) {
				String message = "装备进阶失败，材料不足.userId[" + userId + "], toolId[" + toolId + "], toolNum[" + toolNum + "], userToolNum[" + userToolNum + "]";
				LOG.warn(message);
				throw new ServiceException(MERGE_EQUIP_NOT_ENOUGH_TOOL, message);
			}

		}

	}

	@Override
	public int getUserEquipCount(String userId) {
		return this.userEquipDao.getUserEquipCount(userId);
	}

	@Override
	public List<SystemEquip> getAllSystemEquip() {
		return systemEquipDao.getSystemEquipList();
	}

	@Override
	public List<UserEquip> getUserEquipList(String userId, int equipId) {
		return userEquipDao.getUserEquipList(userId, equipId);
	}

	@Override
	public UserEquip getLowestLevel(List<UserEquip> userEquipList) {
		UserEquip minUseEquip = userEquipList.get(0);
		int minEquipLevel = minUseEquip.getEquipLevel();

		for (UserEquip userEquip : userEquipList) {
			int equipLevel = userEquip.getEquipLevel();
			if (equipLevel < minEquipLevel) {
				minEquipLevel = equipLevel;
				minUseEquip = userEquip;
			}
		}
		return minUseEquip;
	}

	@Override
	public List<UserEquipBO> createUserEquipBOList(String userId, List<DropToolBO> boList) {
		List<UserEquipBO> userEquipBoList = new ArrayList<UserEquipBO>();

		for (DropToolBO bo : boList) {
			UserEquip userEquip = getUserEquip(userId, bo.getValue());
			UserEquipBO userEquipBo = createEquipBO(userEquip);
			userEquipBoList.add(userEquipBo);
		}
		return userEquipBoList;
	}

	@Override
	public DeifyBo getUserHeroEquipDeifyList(String userId, int deifyId, int equipLevel) {
		DeifyBo deifyBo = new DeifyBo();

		SystemDeifyDefine systemDeifyDefine = this.systemDeifyDefineDao.get(deifyId);
		if (systemDeifyDefine != null) {
			SystemDeifyAttr systemDeifyAttr = this.systemDeifyAttrDao.get(systemDeifyDefine.getDeifyId(), equipLevel);
			deifyBo.setDeifyId(systemDeifyDefine.getDeifyId());
			deifyBo.setLift(systemDeifyAttr.getLife());
			deifyBo.setPhysicalAttack(systemDeifyAttr.getAttack());
			deifyBo.setPhysicalDefense(systemDeifyAttr.getDefense());
			deifyBo.setStartNum(systemDeifyAttr.getDeifyLevel());
			deifyBo.setType(systemDeifyDefine.getEquipType());
			deifyBo.setRate(systemDeifyAttr.getSuccUpper());
			deifyBo.setDropToolBOs(DropToolHelper.parseDropTool(systemDeifyDefine.getToolIds()));
		}
		return deifyBo;
	}

	@Override
	public Map<String, Object> polish(String userId, String userEquipId, int polishType) {
		UserEquip userEquip = this.userEquipDao.get(userId, userEquipId);

		User user = this.userService.get(userId);

		SystemEquip systemEquip = this.systemEquipDao.get(userEquip.getEquipId());

		if (systemEquip.getColor() < 2) {
			String message = "装备洗练失败，装备不是蓝色以上不足.userId[" + userId + "].userEquipId [" + userEquipId + "]";
			LOG.warn(message);
			throw new ServiceException(POLISH_EQUIP_COLOR_NOT_ENOUGH, message);
		}

		if (user.getLevel() < 40) {
			String message = "装备洗练失败，用户等级不足40级.userId[" + userId + "].userEquipId [" + userEquipId + "]";
			LOG.warn(message);
			throw new ServiceException(POLISH_EQUIP_USER_LEVEL_NOT_ENOUGH, message);
		}

		SystemPolishConsume systemPolishConsume = this.systemPolishConsumeDao.getSystemPolishConsume(polishType);

		// 装备洗练消耗
		ReduceConsum(userId, systemPolishConsume);

		SystemPolish systemPolish = this.systemPolishDao.getSystemPolish(userEquip.getEquipId());

		// 计算装备的加成
		Map<String, Object> map = this.CalEquipAddition(userId, systemPolishConsume, systemPolish, userEquip);

		return map;
	}

	/**
	 * 计算洗练加成
	 * 
	 * @param userId
	 * @param systemPolishConsume
	 * @param systemPolish
	 * @param userEquip
	 */
	private Map<String, Object> CalEquipAddition(String userId, SystemPolishConsume systemPolishConsume, SystemPolish systemPolish, UserEquip userEquip) {

		Map<String, Object> map = new HashMap<String, Object>();

		String userEquipId = userEquip.getUserEquipId();

		int attackRand = (int) (Math.random() * (systemPolishConsume.getAttackCeiling() + 1 - systemPolishConsume.getAttackFloor()) + systemPolishConsume.getAttackFloor());
		int lifeRand = (int) (Math.random() * (systemPolishConsume.getLifeCeiling() + 1 - systemPolishConsume.getLifeFloor()) + systemPolishConsume.getLifeFloor());
		int defenseRand = (int) (Math.random() * (systemPolishConsume.getDefenseCeiling() + 1 - systemPolishConsume.getDefenseFloor()) + systemPolishConsume.getDefenseFloor());

		UserEquipPolish userEquipPolish = this.userEquipPolishDao.getUserEquipPolishById(userEquipId);

		UserEquipPolishTemp userEquipPolishTemp = this.userEquipPolishTempDao.getUserEquipPolishTempById(userEquipId);

		if (userEquipPolishTemp == null) {
			UserEquipPolishTemp uept = new UserEquipPolishTemp();

			attackRand = attackRand > 0 ? attackRand : 0;
			defenseRand = defenseRand > 0 ? defenseRand : 0;
			lifeRand = lifeRand > 0 ? lifeRand : 0;

			attackRand = attackRand > systemPolish.getAttackLimit() ? systemPolish.getAttackLimit() : attackRand;
			defenseRand = defenseRand > systemPolish.getDefenseLimit() ? systemPolish.getDefenseLimit() : defenseRand;
			lifeRand = lifeRand > systemPolish.getLifeLimit() ? systemPolish.getLifeLimit() : lifeRand;

			map.put("attack", attackRand);
			map.put("defense", defenseRand);
			map.put("life", lifeRand);

			uept.setAttack(attackRand);
			uept.setLife(lifeRand);
			uept.setDefense(defenseRand);
			uept.setUserEquipId(userEquipId);
			uept.setUserId(userId);

			this.userEquipPolishTempDao.insertUserEquipPolishTemp(uept);
		} else {

			if (userEquipPolish == null) {
				attackRand = attackRand > 0 ? attackRand : 0;
				defenseRand = defenseRand > 0 ? defenseRand : 0;
				lifeRand = lifeRand > 0 ? lifeRand : 0;

				attackRand = attackRand > systemPolish.getAttackLimit() ? systemPolish.getAttackLimit() : attackRand;
				defenseRand = defenseRand > systemPolish.getDefenseLimit() ? systemPolish.getDefenseLimit() : defenseRand;
				lifeRand = lifeRand > systemPolish.getLifeLimit() ? systemPolish.getLifeLimit() : lifeRand;

				map.put("attack", attackRand);
				map.put("defense", defenseRand);
				map.put("life", lifeRand);
				this.userEquipPolishTempDao.updateUserEquipPollishTemp(userId, userEquipId, attackRand, lifeRand, defenseRand);
			} else {

				// 不能超过最大值
				attackRand = userEquipPolish.getAttack() + attackRand < systemPolish.getAttackLimit() ? attackRand : systemPolish.getAttackLimit() - userEquipPolish.getAttack();
				lifeRand = userEquipPolish.getLife() + lifeRand < systemPolish.getLifeLimit() ? lifeRand : systemPolish.getLifeLimit() - userEquipPolish.getLife();
				defenseRand = userEquipPolish.getDefense() + defenseRand < systemPolish.getDefenseLimit() ? defenseRand : systemPolish.getDefenseLimit() - userEquipPolish.getDefense();

				// 副属性不能为负数
				attackRand = userEquipPolish.getAttack() + attackRand > 0 ? attackRand : 0;
				lifeRand = userEquipPolish.getAttack() + lifeRand > 0 ? lifeRand : 0;
				defenseRand = userEquipPolish.getDefense() + defenseRand > 0 ? defenseRand : 0;

				map.put("attack", attackRand);
				map.put("defense", defenseRand);
				map.put("life", lifeRand);

				this.userEquipPolishTempDao.updateUserEquipPollishTemp(userId, userEquipId, attackRand, lifeRand, defenseRand);
			}
		}
		return map;
	}

	/**
	 * 扣除装备洗练消耗
	 * 
	 * @param userId
	 * @param systemPolishConsume
	 */
	private void ReduceConsum(String userId, SystemPolishConsume systemPolishConsume) {
		int psNum = this.userToolDao.getUserToolNum(userId, ToolId.POLISH_STONE_ID);
		if (psNum < systemPolishConsume.getPolishStone()) {
			String message = "装备洗练失败，洗练石不足.userId[" + userId + "] ";
			LOG.warn(message);
			throw new ServiceException(POLISH_EQUIP_POLISH_STONE_NOT_ENOUGH, message);
		}

		User user = this.userService.get(userId);

		if (user.getCopper() < systemPolishConsume.getCopper()) {
			String message = "装备洗练失败，银币不足.userId[" + userId + "] ";
			LOG.warn(message);
			throw new ServiceException(POLISH_EQUIP_COPPER_NOT_ENOUGH, message);
		}

		if (systemPolishConsume.getGold() > 0) {
			if (!this.userService.reduceGold(userId, systemPolishConsume.getGold(), ToolUseType.REDUCE_EQUIP_POLISH, user.getLevel())) {
				String message = "装备洗练失败，用户金币不足.userId[" + userId + "], needMoney[" + systemPolishConsume.getGold() + "]";
				throw new ServiceException(POLISH_EQUIP_GOLD_NOT_ENOUGH, message);
			}
		}
		if (systemPolishConsume.getCopper() > 0) {
			this.userService.reduceCopper(userId, systemPolishConsume.getCopper(), ToolUseType.REDUCE_EQUIP_POLISH);
		}

		if (systemPolishConsume.getPolishStone() > 0) {
			this.toolService.reduceTool(userId, ToolType.MATERIAL, ToolId.POLISH_STONE_ID, systemPolishConsume.getPolishStone(), ToolUseType.REDUCE_EQUIP_POLISH);
		}
	}

	public UserEquipPolishBO createUserEquipPolishBO(UserEquipPolish userEquipPolish) {
		UserEquipPolishBO userEquipPolishBO = new UserEquipPolishBO();
		userEquipPolishBO.setAttack(userEquipPolish.getAttack());
		userEquipPolishBO.setDefense(userEquipPolish.getDefense());
		userEquipPolishBO.setLife(userEquipPolish.getLife());
		return userEquipPolishBO;
	}

	@Override
	public Map<String, Object> savePolish(String uid, String userEquipId) {
		UserEquip userEquip = this.userEquipDao.get(uid, userEquipId);
		UserEquipPolishTemp userEquipPolishTemp = userEquipPolishTempDao.getUserEquipPolishTempById(userEquipId);
		if (userEquipPolishTemp == null) {
			String message = "保存装备洗练副属性失败，该装备没有副属性.userId[" + uid + "], userEquipId[" + userEquipId + "]";
			throw new ServiceException(SAVEPOLISH_EQUIP_NOT_HAVE_TEMP, message);
		}

		this.userEquipPolishDao.updateUserEquipPollish(uid, userEquipId, userEquipPolishTemp.getAttack(), userEquipPolishTemp.getLife(), userEquipPolishTemp.getDefense());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("hero", null);
		if (userEquip.getUserHeroId() != null) {
			map.put("hero", this.heroService.getUserHeroBO(uid, userEquip.getUserHeroId()));
		}

		return map;
	}
}
