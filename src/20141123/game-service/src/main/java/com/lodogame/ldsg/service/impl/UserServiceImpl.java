package com.lodogame.ldsg.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import alex.zhrenjie04.wordfilter.WordFilterUtil;

import com.lodogame.game.dao.CommandDao;
import com.lodogame.game.dao.ConfigDataDao;
import com.lodogame.game.dao.LogDao;
import com.lodogame.game.dao.PaymentLogDao;
import com.lodogame.game.dao.RuntimeDataDao;
import com.lodogame.game.dao.SystemMailDao;
import com.lodogame.game.dao.SystemUserLevelDao;
import com.lodogame.game.dao.SystemVipLevelDao;
import com.lodogame.game.dao.UserDailyGainLogDao;
import com.lodogame.game.dao.UserDao;
import com.lodogame.game.dao.UserExtinfoDao;
import com.lodogame.game.dao.UserInvitedDao;
import com.lodogame.game.dao.UserMapperDao;
import com.lodogame.game.dao.UserOnlineLogDao;
import com.lodogame.game.dao.UserPkInfoDao;
import com.lodogame.game.dao.UserTavernDao;
import com.lodogame.game.dao.UserToolDao;
import com.lodogame.game.dao.clearcache.ClearCacheManager;
import com.lodogame.game.utils.Constant;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.game.utils.IDGenerator;
import com.lodogame.game.utils.IllegalWordUtills;
import com.lodogame.ldsg.bo.UserBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.checker.TaskChecker;
import com.lodogame.ldsg.constants.ActivityTargetType;
import com.lodogame.ldsg.constants.CommandType;
import com.lodogame.ldsg.constants.ConfigKey;
import com.lodogame.ldsg.constants.InitDefine;
import com.lodogame.ldsg.constants.MailTarget;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.constants.TaskTargetType;
import com.lodogame.ldsg.constants.TavernConstant;
import com.lodogame.ldsg.constants.ToolId;
import com.lodogame.ldsg.constants.ToolType;
import com.lodogame.ldsg.constants.ToolUseType;
import com.lodogame.ldsg.constants.UserDailyGainLogType;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.LoginEvent;
import com.lodogame.ldsg.event.LogoutEvent;
import com.lodogame.ldsg.event.UserInfoUpdateEvent;
import com.lodogame.ldsg.event.UserLevelUpEvent;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.helper.BOHelper;
import com.lodogame.ldsg.helper.CopperHelper;
import com.lodogame.ldsg.helper.LodoIDHelper;
import com.lodogame.ldsg.helper.PowerHelper;
import com.lodogame.ldsg.service.ActivityService;
import com.lodogame.ldsg.service.ActivityTaskService;
import com.lodogame.ldsg.service.ArenaService;
import com.lodogame.ldsg.service.BossService;
import com.lodogame.ldsg.service.EquipService;
import com.lodogame.ldsg.service.EventServcie;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.MailService;
import com.lodogame.ldsg.service.MessageService;
import com.lodogame.ldsg.service.SceneService;
import com.lodogame.ldsg.service.TaskService;
import com.lodogame.ldsg.service.UnSynLogService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.ldsg.service.VipService;
import com.lodogame.ldsg.validation.UserValid;
import com.lodogame.model.Command;
import com.lodogame.model.RuntimeData;
import com.lodogame.model.SystemUserLevel;
import com.lodogame.model.SystemVipLevel;
import com.lodogame.model.User;
import com.lodogame.model.UserExtinfo;
import com.lodogame.model.UserInvited;
import com.lodogame.model.UserMapper;
import com.lodogame.model.UserOnlineLog;
import com.lodogame.model.UserShareLog;
import com.lodogame.model.UserTavern;

public class UserServiceImpl implements UserService {

	private static final Logger logger = Logger.getLogger(UserServiceImpl.class);

	@Autowired
	private ActivityService activityService;

	@Autowired
	private UserInvitedDao userInvitedDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private RuntimeDataDao rumtimeDataDao;

	@Autowired
	private UserMapperDao userMapperDao;

	@Autowired
	private TaskService taskService;

	@Autowired
	private HeroService heroService;

	@Autowired
	private EquipService equipService;

	@Autowired
	private SceneService sceneService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private SystemUserLevelDao systemUserLevelDao;

	@Autowired
	private CommandDao commandDao;

	@Autowired
	private UserOnlineLogDao userOnlineLogDao;

	@Autowired
	private UserExtinfoDao userExtinfoDao;

	@Autowired
	private UserTavernDao userTavernDao;

	@Autowired
	private VipService vipService;

	@Autowired
	private SystemVipLevelDao systemVipLevelDao;

	@Autowired
	private PaymentLogDao paymentLogDao;

	@Autowired
	private UserToolDao userToolDao;

	@Autowired
	private ActivityTaskService activityTaskService;

	@Autowired
	private UnSynLogService unSynLogService;

	private BossService bossService;

	@Autowired
	private UserDailyGainLogDao userDailyGainLogDao;

	@Autowired
	private ConfigDataDao configDataDao;

	@Autowired
	private ArenaService arenaService;

	@Autowired
	private EventServcie eventService;

	@Autowired
	private UserPkInfoDao userPkInfoDao;

	@Autowired
	private LogDao logDao;

	@Autowired
	private MailService mailService;

	@Autowired
	private SystemMailDao systemMailDao;

	public UserBO getUserBO(String userId) {

		User user = this.get(userId);

		UserExtinfo userExtinfo = this.userExtinfoDao.get(userId);

		// this.checkPowerAdd(user);

		UserBO userBO = BOHelper.craeteUserBO(user, userExtinfo);

		int payAmount = this.paymentLogDao.getPaymentTotalGold(userId);
		userBO.setPayAmount(payAmount);

		return userBO;
	}

	public UserBO getUserBOByPlayerId(String playerId) {

		User user = this.getByPlayerId(playerId);

		UserExtinfo userExtinfo = this.userExtinfoDao.get(user.getUserId());

		// this.checkPowerAdd(user);

		return BOHelper.craeteUserBO(user, userExtinfo);
	}

	/**
	 * 检查军令增加
	 * 
	 * @param user
	 */
	public boolean checkPowerAdd(User user) {

		int maxPower = this.vipService.getPowerMax(user.getVipLevel());

		Date now = new Date();// 当前时间
		Date powerAddTime = user.getPowerAddTime();// 上次添加体力的时间
		long timestamp1 = now.getTime();
		long timestamp2 = powerAddTime.getTime();
		long sub = timestamp1 - timestamp2;
		if (sub < InitDefine.POWER_ADD_INTERVAL) {// 时间还没到
			return false;
		}

		Date newPowerAddTime;
		long addPower;
		long newPower;

		if (user.getPower() >= maxPower) {// 如果体力已经超过上限
			addPower = 0;
			newPower = user.getPower();
			newPowerAddTime = new Date();
		} else {
			addPower = sub / InitDefine.POWER_ADD_INTERVAL;// 可以加多少体力
			newPower = user.getPower() + addPower;
			if (newPower > maxPower) {
				newPower = maxPower;
				addPower = maxPower - user.getPower();
				newPowerAddTime = new Date();
			} else {
				long leaveTimes = addPower * InitDefine.POWER_ADD_INTERVAL - sub;// 要把余下的时间给用户算回去
				newPowerAddTime = DateUtils.add(now, Calendar.MILLISECOND, (int) leaveTimes);
			}
		}

		if (addPower == 0) {
			return true;
		}

		logger.debug("用户恢复体力.userId[" + user.getUserId() + "], addPower[" + addPower + "], powerAddTime[" + newPowerAddTime + "]");

		// 更改 bean
		user.setPower((int) newPower);
		user.setPowerAddTime(newPowerAddTime);

		this.addPower(user.getUserId(), (int) addPower, maxPower, ToolUseType.ADD_AUTO_RESUME, newPowerAddTime);

		// 发push命令.
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", user.getUserId());

		Command command = new Command();
		command.setCommand(CommandType.COMMAND_PUSH_USER_INFO);
		command.setType(CommandType.PUSH_USER);
		command.setParams(params);

		commandDao.add(command);

		return true;
	}

	public User get(String userId) {

		User user = this.userDao.get(userId);

		if (user == null) {
			throw new ServiceException(LOAD_USER_NOT_EXIST, "用户不存在.userId[" + userId + "]");
		}

		UserExtinfo userExtInfo = this.userExtinfoDao.get(userId);
		if (userExtInfo.getRewardVipLevel() > user.getVipLevel()) {
			user.setVipLevel(userExtInfo.getRewardVipLevel());
		}

		return user;
	}

	@Override
	public List<UserBO> listOrderByLevelDesc(int offset, int size) {
		List<User> users = this.userDao.listOrderByLevelDesc(offset, size);
		List<UserBO> userBOs = new ArrayList<UserBO>();
		for (User user : users) {
			UserExtinfo userExtinfo = this.userExtinfoDao.get(user.getUserId());
			UserBO userBO = BOHelper.craeteUserBO(user, userExtinfo);
			userBOs.add(userBO);
		}
		return userBOs;
	}

	@Override
	public List<User> listOrderByCopperDesc(int offset, int size) {
		return this.userDao.listOrderByCopperDesc(offset, size);
	}

	public User getByPlayerId(String playerId) {
		User user = this.userDao.getByPlayerId(playerId);

		if (user == null) {
			throw new ServiceException(LOAD_USER_NOT_EXIST, "用户不存在.playerId[" + playerId + "]");
		}

		return user;
	}

	@Override
	public void checkUsername(String username) {
		if (StringUtils.isBlank(username)) {
			throw new ServiceException(NICKNAME_IS_NULL, "昵称为空.username[" + username + "]");
		} else if (Constant.getBytes(username) > 12) {
			throw new ServiceException(NICKNAME_LENGTH_ILLEGAL, "昵称长度非法.username[" + username + "]");
		} else if (!IllegalWordUtills.checkWords(username)) {
			throw new ServiceException(NICKNAME_WORD_ILLEGAL, "昵称包含非法文字.username[" + username + "]");
		}
		User user = this.userDao.getByName(username);
		if (user != null) {
			throw new ServiceException(NICKNAME_EXIST, "用 户名已经存在.username[" + username + "]");
		}
	}

	@Override
	public boolean create(String userId, int systemHeroId, String username, EventHandle handle) {

		UserValid.isValidUsername(username);
		UserValid.isValidInitHeroId(systemHeroId);

		String filterUsername = WordFilterUtil.filterHtml(username, '%').getFilteredContent();
		if (!StringUtils.equalsIgnoreCase(filterUsername, username)) {
			throw new ServiceException(UserService.CREATE_USERNAME_INVAILD, "用户名有非法字符");
		}

		/*
		 * User user = this.userDao.getByName(username);
		 * 
		 * if (user != null) { throw new ServiceException(CREATE_USER_EXIST,
		 * "用 户名已经存在.username[" + username + "]"); }
		 */
		UserMapper userMapper = this.userMapperDao.get(userId);

		if (userMapper == null) {
			logger.error("用户创建失败,usermapper表没记录.userId[" + userId + "], systemHeroId[" + systemHeroId + "], username[" + username + "]");
			throw new ServiceException(ServiceReturnCode.FAILD, "用户创建失败.userId[" + userId + "]");
		}

		User user = this.userDao.get(userId);

		if (user != null) {
			throw new ServiceException(CREATE_USER_EXIST, "用 户已经存在.userId[" + userId + "]");
		}
		long[] loadIdArray = new long[1];

		boolean success = this.addUser(userMapper.getServerId(), userId, systemHeroId, username, loadIdArray);

		// 创建扩展信息
		UserExtinfo userExtinfo = new UserExtinfo();
		userExtinfo.setEquipMax(InitDefine.EQUIP_BAG_INIT);
		userExtinfo.setHeroMax(InitDefine.HERO_BAG_INIT);
		userExtinfo.setLastBuyCopperTime(new Date());
		userExtinfo.setBuyCopperTimes(0);
		userExtinfo.setUserId(userId);
		userExtinfo.setRecordGuideStep("0");
		userExtinfo.setUserNation(systemHeroId);
		this.userExtinfoDao.add(userExtinfo);

		if (!success) {
			logger.error("用户创建失败.userId[" + userId + "], systemHeroId[" + systemHeroId + "], username[" + username + "]");
			throw new ServiceException(ServiceReturnCode.FAILD, "用户创建失败.userId[" + userId + "]");
		}

		// 创建默认任务
		this.taskService.addInitTask(userId);

		// 给第一个武将
		this.heroService.addUserHero(userId, IDGenerator.getID(), systemHeroId, 1, ToolUseType.ADD_INIT_GIVE_HERO);

		// 给两个普通武将
		this.heroService.addUserHero(userId, IDGenerator.getID(), InitDefine.INIT_USER_HERO_ID1, 2, ToolUseType.ADD_INIT_GIVE_HERO);
		// this.heroService.addUserHero(userId, IDGenerator.getID(),
		// InitDefine.INIT_USER_HERO_ID2, 1,
		// ToolUseType.USE_TYPE_INIT_GIVE_HERO);

		// 开放的场景以及部队
		// this.sceneService.addOpenScene(userId, InitDefine.INIT_USER_LEVEL);
		this.sceneService.openAfterForces(userId, 0);

		// 创建酒馆信息
		Date now = new Date();

		UserTavern userTavern = new UserTavern();
		userTavern.setType(TavernConstant.DRAW_TYPE_2);
		userTavern.setUserId(userId);
		userTavern.setCreatedTime(now);
		userTavern.setUpdatedTime(DateUtils.add(now, Calendar.MILLISECOND, -1 * (TavernConstant.DRAW_TYPE_2_CD_TIME - 15 * 60 * 1000)));
		this.userTavernDao.add(userTavern);

		userTavern = new UserTavern();
		userTavern.setType(TavernConstant.DRAW_TYPE_3);
		userTavern.setUserId(userId);
		userTavern.setCreatedTime(now);
		userTavern.setUpdatedTime(DateUtils.add(now, Calendar.MILLISECOND, -1 * (TavernConstant.DRAW_TYPE_3_CD_TIME - 15 * 60 * 1000)));
		this.userTavernDao.add(userTavern);

		// 写用户创建角色日志
		unSynLogService.userCreatRoleLog(userId, username, username, 1, new Date(), loadIdArray[0]);
		return success;

	}

	/**
	 * 添加用户
	 * 
	 * @param userId
	 * @param systemHeroId
	 * @param username
	 * @return
	 */

	private boolean addUser(String serverId, String userId, int systemHeroId, String username, long[] loadIdContext) {

		User user = new User();

		Date now = new Date();

		long lodoId = this.getLdid(serverId);
		long fullLodoId = LodoIDHelper.getLodoId(serverId, lodoId);

		user.setLodoId(fullLodoId);
		user.setUserId(userId);
		user.setCopper(InitDefine.INIT_USER_COPPER);
		user.setExp(InitDefine.INIT_USER_EXP);
		user.setGoldNum(InitDefine.INIT_USER_GOLD);
		user.setLevel(InitDefine.INIT_USER_LEVEL);
		user.setOrderAddTime(now);
		user.setOrderNum(InitDefine.INIT_USER_ORDER);
		user.setPower(InitDefine.INIT_USER_POWER);
		user.setRegTime(now);
		user.setUpdatedTime(now);
		user.setUsername(username);
		user.setPowerAddTime(now);
		user.setVipLevel(0);
		user.setVipExpiredTime(now);
		// 将loadid传到上层
		loadIdContext[0] = fullLodoId;
		return this.userDao.add(user);
	}

	private synchronized long getLdid(String serverId) {

		int id = 1;
		String key = LodoIDHelper.getIdSaveKey(serverId);
		RuntimeData rumtimeData = this.rumtimeDataDao.get(key);
		if (rumtimeData == null) {
			rumtimeData = new RuntimeData();
			rumtimeData.setValueKey(key);
			rumtimeData.setValue(id);
			rumtimeData.setCreatedTime(new Date());
			this.rumtimeDataDao.add(rumtimeData);
		} else {
			id = rumtimeData.getValue() + 1;
			this.rumtimeDataDao.inc(key);
		}

		return id;
	}

	@Override
	public void returnPower(String userId, int power, int powerUseType) {
		boolean success = this.addPower(userId, power, powerUseType, null);
		logger.debug("返还体力.userId[" + userId + "], success[" + success + "]");
	}

	@Override
	public int resumePower(String userId, EventHandle handle) {

		User user = this.get(userId);

		int power = user.getPower();

		int maxPower = this.vipService.getPowerMax(user.getVipLevel());

		if (power >= maxPower) {
			String message = "恢复体力出错，体力已经达到上限.userId[" + userId + "], power[" + power + "], maxPower[" + maxPower + "]";
			logger.debug(message);
			throw new ServiceException(RESUME_POWER_POWER_IS_MAX, message);
		}

		int maxTimes = this.vipService.getBuyPowerLimit(user.getVipLevel());

		int times = 1;
		UserExtinfo userExtinfo = this.userExtinfoDao.get(userId);
		if (userExtinfo != null && DateUtils.isSameDay(userExtinfo.getLastBuyPowerTime(), new Date())) {
			times = userExtinfo.getBuyPowerTimes() + 1;
		}

		if (times > maxTimes) {
			String message = "恢复体力出错，超过恢复次数限制.userId[" + userId + "], times[" + times + "], maxTimes[" + maxTimes + "], vipLevel[" + user.getVipLevel() + "]";
			logger.debug(message);
			throw new ServiceException(RESUME_POWER_OVER_MAX_TIMES, message);
		}

		int needMoney = PowerHelper.getBuyPowerNeedMoney(times);
		if (!this.reduceGold(userId, needMoney, ToolUseType.REDUCE_RESUMT_POWER, user.getLevel())) {
			String message = "恢复体力出错,金币不足.userId[" + userId + "], power[" + power + "], needMoney[" + needMoney + "]";
			logger.debug(message);
			throw new ServiceException(RESUME_POWER_NOT_ENOUGH_GOLD, message);
		}

		int addPower = maxPower - power;

		boolean success = this.addPower(userId, addPower, ToolUseType.ADD_GOLD_RESUME, null);

		// 活跃度任务
		activityTaskService.updateActvityTask(userId, ActivityTargetType.BUY_POWER, 1);

		// 更新购买银币记录
		if (userExtinfo != null) {
			this.userExtinfoDao.updateBuyPowerTimes(userId, times);
		} else {
			userExtinfo = new UserExtinfo();
			userExtinfo.setEquipMax(InitDefine.EQUIP_BAG_INIT);
			userExtinfo.setHeroMax(InitDefine.HERO_BAG_INIT);
			userExtinfo.setBuyPowerTimes(times);
			userExtinfo.setUserId(userId);
			this.userExtinfoDao.add(userExtinfo);
		}

		UserInfoUpdateEvent userInfoUpdateEvent = new UserInfoUpdateEvent(userId);
		handle.handle(userInfoUpdateEvent);

		logger.debug("恢复体力.userId[" + userId + "], success[" + success + "]");

		return times;
	}

	public boolean addCopper(String userId, int amount, int useType) {
		logger.debug("增加银币.userId[" + userId + "], amount[" + amount + "], useType[" + useType + "]");
		boolean success = false;
		try {
			success = this.userDao.addCopper(userId, amount);
		} catch (RuntimeException rt) {
			throw rt;
		} finally {
			unSynLogService.copperLog(userId, useType, amount, 1, success);

		}

		return success;
	}

	public boolean addGold(String userId, int amount, int useType, int userLevel) {
		logger.debug("增加金币.userId[" + userId + "], amount[" + amount + "], useType[" + useType + "]");
		if (amount <= 0) {
			return false;
		}

		User user = this.get(userId);
		long beforeAmount = user.getGoldNum();

		boolean success = false;
		try {
			success = this.userDao.addGold(userId, amount);
		} catch (RuntimeException rt) {
			throw rt;
		} finally {
			unSynLogService.goldLog(userId, userLevel, useType, amount, 1, success);

			user = this.get(userId);
			long afterAmount = user.getGoldNum();

			logDao.goldLog(userId, useType, amount, 1, success, beforeAmount, afterAmount);
		}

		return success;
	}

	@Override
	public boolean buyCopper(String userId) {

		UserExtinfo userExtinfo = this.userExtinfoDao.get(userId);
		User user = this.get(userId);

		Date now = new Date();

		int times = 1;

		if (userExtinfo != null) {

			if (DateUtils.isSameDay(now, userExtinfo.getLastBuyCopperTime())) {
				times += userExtinfo.getBuyCopperTimes();
			}
		}

		Map<String, Integer> copperInfo = CopperHelper.getCopperInfo(times, user.getLevel());
		int needMoney = copperInfo.get("ncm");
		int gainCopper = copperInfo.get("nco");

		// 扣金币
		if (!this.reduceGold(userId, needMoney, ToolUseType.REDUCE_BUY_COPPER, user.getLevel())) {
			String message = "购买银币出错，用户金币不足.userId[" + userId + "], needMoney[" + needMoney + "]";
			logger.info(message);
			throw new ServiceException(BUY_COPPER_NOT_ENOUGH_GOLD, message);
		}

		// 更新购买银币记录
		if (userExtinfo != null) {
			this.userExtinfoDao.updateBuyCopperTimes(userId, times);
		} else {
			userExtinfo = new UserExtinfo();
			userExtinfo.setEquipMax(InitDefine.EQUIP_BAG_INIT);
			userExtinfo.setHeroMax(InitDefine.HERO_BAG_INIT);
			userExtinfo.setLastBuyCopperTime(now);
			userExtinfo.setBuyCopperTimes(times);
			userExtinfo.setUserId(userId);
			this.userExtinfoDao.add(userExtinfo);
		}

		// 给银币
		if (!this.addCopper(userId, gainCopper, ToolUseType.ADD_BUY_COPPER)) {
			String message = "购买银币出错，加银币时失败.userId[" + userId + "]";
			logger.info(message);
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		this.pushUser(userId);

		return true;
	}

	public boolean reduceGold(String userId, int amount, int useType, int userLevel) {
		logger.debug("消费金币.userId[" + userId + "], amount[" + amount + "], useType[" + useType + "]");
		if (amount <= 0) {
			return false;
		}

		User user = this.get(userId);
		long beforeAmount = user.getGoldNum();

		boolean success = false;
		try {
			success = this.userDao.reduceGold(userId, amount);
		} catch (RuntimeException rt) {
			throw rt;
		} finally {
			unSynLogService.goldLog(userId, userLevel, useType, amount, -1, success);

			user = this.get(userId);
			long afterAmount = user.getGoldNum();

			logDao.goldLog(userId, useType, amount, -1, success, beforeAmount, afterAmount);
		}

		return success;
	}

	public boolean addExp(String userId, int exp, int useType) {
		logger.debug("增加经验.userId[" + userId + "], exp[" + exp + "], useType[" + useType + "]");
		User user = this.userDao.get(userId);
		if (user == null) {
			String message = "更新用户经验失败,用户不存在.userId[" + userId + "], exp[" + exp + "]";
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		SystemUserLevel systemUserLevel = this.systemUserLevelDao.getUserLevel(user.getExp() + exp);

		int level = configDataDao.getInt(ConfigKey.USER_MAX_LEVEL, 150);

		if (systemUserLevel != null) {
			level = systemUserLevel.getUserLevel();
		}

		final int userLevel = level;

		int maxPower = this.vipService.getPowerMax(user.getVipLevel());

		int power = 0;
		if (level > user.getLevel()) {
			power = user.getPower() + 30 * (level - user.getLevel());
			if (power > maxPower) {
				power = maxPower;
			}

			Event event = new UserLevelUpEvent(userId, userLevel);
			eventService.dispatchEvent(event);

			userPkInfoDao.updateUserLevel(userId, userLevel);

			// 升级日志插入
			// unSynLogService.userLevelUpLog(new Date(), userId, level);

			unSynLogService.levelUpLog(userId, exp, userLevel);
		}

		boolean success = false;
		try {
			success = this.userDao.addExp(userId, exp, level, power);
		} catch (RuntimeException rt) {
			throw rt;
		} finally {
			// this.logDao.expLog(userId, useType, exp, success);
			unSynLogService.toolLog(userId, ToolType.EXP, ToolType.EXP, exp, useType, 1, "", success);
		}

		this.taskService.updateTaskFinish(userId, 1, new EventHandle() {

			@Override
			public boolean handle(Event event) {
				return false;
			}
		}, new TaskChecker() {

			@Override
			public boolean isFinish(int systemTaskId, int taskTarget, Map<String, String> params) {
				if (taskTarget == TaskTargetType.TASK_TYPE_TO_SPC_LEVEL) {
					int needLevel = NumberUtils.toInt(params.get("lv"));
					return userLevel >= needLevel;
				}
				return false;
			}
		});

		return success;
	}

	@Override
	public boolean reduceCopper(String userId, int amount, int useType) {

		logger.debug("消费银币.userId[" + userId + "], amount[" + amount + "], useType[" + useType + "]");
		boolean success = false;
		try {
			success = this.userDao.reduceCopper(userId, amount);
		} catch (RuntimeException rt) {
			throw rt;
		} finally {
			unSynLogService.copperLog(userId, useType, amount, -1, success);
		}

		return success;
	}

	@Override
	public boolean addPower(String userId, int power, int useType, Date powerAddTime) {
		User user = this.get(userId);
		int maxPower = this.vipService.getPowerMax(user.getVipLevel());
		return this.addPower(userId, power, maxPower, useType, powerAddTime);
	}

	@Override
	public boolean addPower(String userId, int power, int maxPower, int useType, Date powerAddTime) {
		/*
		 * if (power <= 0) { return false; }
		 */
		logger.debug("增加体力.userId[" + userId + "], power[" + power + "], useType[" + useType + "], powerAddTime[" + powerAddTime + "]");
		boolean success = false;
		try {
			success = this.userDao.addPower(userId, power, maxPower, powerAddTime);
		} catch (RuntimeException rt) {
			throw rt;
		} finally {
			// this.logDao.powerLog(userId, useType, power, 1, success);
			unSynLogService.toolLog(userId, ToolType.POWER, ToolType.POWER, power, useType, 1, "", success);
		}

		return success;
	}

	@Override
	public boolean reducePower(String userId, int power, int useType) {

		logger.debug("减少体力.userId[" + userId + "], power[" + power + "], useType[" + useType + "]");
		boolean success = false;
		try {
			success = this.userDao.reducePowre(userId, power);
		} catch (RuntimeException rt) {
			throw rt;
		} finally {
			// this.logDao.powerLog(userId, useType, power, -1, success);
			unSynLogService.toolLog(userId, ToolType.POWER, ToolType.POWER, power, useType, -1, "", success);
		}

		return success;
	}

	@Override
	public void pushUser(String userId) {

		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", userId);

		Command command = new Command();
		command.setCommand(CommandType.COMMAND_PUSH_USER_INFO);
		command.setType(CommandType.PUSH_USER);
		command.setParams(params);

		commandDao.add(command);

	}

	@Override
	public void login(final String userId, String userIp) {

		User user = userDao.get(userId);
		Date now = new Date();

		/*
		 * 判断用户是否已经被封号 如果还在封号时间内，返回异常，
		 */
		Date dueTime = user.getDueTime();
		if (dueTime != null && now.before(dueTime)) {
			throw new ServiceException(LOGIN_USER_BANNED, "用户已经被封号.userId[" + userId + "]");
		}

		// 设置在线
		this.userDao.setOnline(userId, true);
		// 清下以前的缓存
		this.userDao.cleanCacheData(userId);

		this.taskService.updateTaskFinish(userId, 1, new EventHandle() {

			@Override
			public boolean handle(Event event) {
				return false;
			}
		}, new TaskChecker() {

			@Override
			public boolean isFinish(int systemTaskId, int taskTarget, Map<String, String> params) {

				if (taskTarget == TaskTargetType.TASK_TYPE_DAILY_LOGIN) {
					return true;
				} else if (taskTarget == TaskTargetType.TASK_TYPE_VIP_LOGIN) {// VIP每日登录
					UserBO userBO = getUserBO(userId);
					if (userBO.getVipLevel() > 0) {
						return true;
					}
				}
				return false;
			}
		});

		this.activityTaskService.updateActvityTask(userId, ActivityTargetType.LOGIN, 1);

		// 广播用户登录事件
		Event event = new LoginEvent(userId);
		event.setObject("userIp", userIp);
		eventService.dispatchEvent(event);

	}

	@Override
	public void logout(String userId) {

		UserOnlineLog lastUserOnlineLog = this.userOnlineLogDao.getLastOnlineLog(userId);
		if (lastUserOnlineLog != null) {
			this.userOnlineLogDao.updateLogoutTime(userId, lastUserOnlineLog.getLogId(), new Date());
		}

		// 设置离线
		this.userDao.setOnline(userId, false);
		// 清除缓存数据
		this.userDao.cleanCacheData(userId);
		// 执行缓存管理器的用户登出操作
		ClearCacheManager.getInstance().executeUserLogOut(userId);
		// 删除数量为0的用户道具
		this.userToolDao.deleteZeroNumTools(userId);
		// 让封魔小队中的玩家离开
		this.bossService.exitTeam(userId);
		// 写用户登出日志
		if (lastUserOnlineLog != null) {
			Date now = new Date();
			int liveMinutes = (int) ((now.getTime() - lastUserOnlineLog.getLoginTime().getTime()) / 1000);
			liveMinutes = liveMinutes / 60;
			liveMinutes = (liveMinutes == 0 ? 1 : liveMinutes);
			unSynLogService.userLogOutLog(userId, lastUserOnlineLog.getUserIp(), now, liveMinutes);
		}

		// 百人斩退出
		this.arenaService.quit(userId);

		LogoutEvent logoutEvent = new LogoutEvent(userId);
		eventService.dispatchEvent(logoutEvent);
	}

	public void init() {

	}

	@Override
	public void checkUserHeroBagLimit(String userId) {
		UserExtinfo userExtinfo = this.userExtinfoDao.get(userId);
		int bagMax = InitDefine.HERO_BAG_INIT;
		if (userExtinfo != null) {
			bagMax = userExtinfo.getHeroMax();
		}

		int userHeroCount = this.heroService.getUserHeroCount(userId);
		if (userHeroCount >= bagMax) {
			throw new ServiceException(ServiceReturnCode.USER_HERO_BAG_LIMIT_EXCEED, "武将背包超过上限.userId[" + userId + "]");
		}
	}

	@Override
	public void checkUserEquipBagLimit(String userId) {
		UserExtinfo userExtinfo = this.userExtinfoDao.get(userId);
		int bagMax = InitDefine.EQUIP_BAG_INIT;
		if (userExtinfo != null) {
			bagMax = userExtinfo.getEquipMax();
		}

		int userEquipCount = this.equipService.getUserEquipCount(userId);
		if (userEquipCount >= bagMax) {
			throw new ServiceException(ServiceReturnCode.USER_EQUIP_BAG_LIMIT_EXCEED, "装备背包超过上限.userId[" + userId + "]");
		}
	}

	@Override
	public void updateVipLevel(String userId) {

		User user = this.get(userId);

		int payAmount = this.paymentLogDao.getPaymentTotalGold(userId);
		SystemVipLevel systemVipLevel = this.systemVipLevelDao.getBuyMoney(payAmount);

		final int vipLevel = systemVipLevel.getVipLevel();

		this.userDao.updateVIPLevel(userId, vipLevel, new Date());

		// 插入vip升级日志
		unSynLogService.userVipLevelLog(userId, vipLevel, new Date());

		// 推送VIP跑马灯
		// VipLevelEvent vipLevelEvent = new VipLevelEvent(userId);
		// eventServcie.dispatchEvent(vipLevelEvent);
		if (vipLevel >= 7 && user.getVipLevel() != vipLevel) {
//			messageService.sendVipLevelMsg(userId, user.getUsername(), vipLevel);
		}

		// 处理VIP任务
		this.taskService.updateTaskFinish(userId, 1, new EventHandle() {

			@Override
			public boolean handle(Event event) {
				return false;
			}
		}, new TaskChecker() {

			@Override
			public boolean isFinish(int systemTaskId, int taskTarget, Map<String, String> params) {

				int needVipLevel = NumberUtils.toInt(params.get("vl"));

				if (taskTarget == TaskTargetType.TASK_TYPE_TO_BE_VIIP) {
					return vipLevel >= needVipLevel;
				}

				return false;
			}

		});

	}

	@Override
	public UserMapper getUserMapper(String userId) {
		return userMapperDao.get(userId);
	}

	@Override
	public boolean saveGuideStep(String userId, int step, String ip) {
		User user = userDao.get(userId);
		if (user != null) {
			unSynLogService.userActionLog(userId, user.getLevel(), new Date(), ip, step);
		}
		return this.userExtinfoDao.updateGuideStep(userId, step);
	}

	@Override
	public boolean recordGuideStep(String userId, int guideStep, String ip) {
		UserExtinfo info = userExtinfoDao.get(userId);
		String oldStep = info.getRecordGuideStep();
		if (oldStep == null || oldStep.equals("")) {
			oldStep = "0";
		}
		String newStep = oldStep + "," + String.valueOf(guideStep);

		User user = userDao.get(userId);
		if (user != null) {
			// 因为原来定义的常量跟新手引导的数字有些有冲突 所有加个10000
			int conditionGuideStep = guideStep + 10000;
			unSynLogService.userActionLog(userId, user.getLevel(), new Date(), ip, conditionGuideStep);
		}
		return userExtinfoDao.recordGuideStep(userId, newStep);
	}

	@Override
	public boolean isOnline(String userId) {
		return userDao.isOnline(userId);
	}

	@Override
	public boolean addUserHeroBag(String userId, int equipMax) {
		return userExtinfoDao.updateHeroMax(userId, equipMax);
	}

	@Override
	public boolean addUserEquipBag(String userId, int equipMax) {
		return userExtinfoDao.updateEquipMax(userId, equipMax);
	}

	@Override
	public int getUserPower(List<UserHeroBO> userHeros) {
		int power = 0;
		if (userHeros != null) {
			for (UserHeroBO hero : userHeros) {
				power += hero.getPhysicalAttack() + hero.getPhysicalDefense() + hero.getLife();
			}
		}
		return power;
	}

	@Override
	public boolean checkUserGoldGainLimit(String userId, int goldNum) {
		int userDailyGainGold = this.userDailyGainLogDao.getUserDailyGain(userId, UserDailyGainLogType.GOLD);

		int gainLimit = this.configDataDao.getInt(ConfigKey.USER_DAILY_GAIN_GOLD_LIMIT, 30);

		// 金币掉落超过上限
		if (userDailyGainGold + goldNum >= gainLimit) {
			return false;
		} else {
			this.userDailyGainLogDao.addUserDailyGain(userId, UserDailyGainLogType.GOLD, goldNum);
			return true;
		}
	}

	@Override
	public List<String> getAllUserIds() {
		return userDao.getAllUserIds();
	}

	@Override
	public boolean reduceExp(String userId, int amount, int useType) {
		logger.debug("消费经验.userId[" + userId + "], amount[" + amount + "], useType[" + useType + "]");
		if (amount <= 0) {
			return false;
		}
		boolean success = false;
		try {
			success = this.userDao.reduceExp(userId, amount);
		} catch (RuntimeException rt) {
			throw rt;
		}

		return success;
	}

	public BossService getBossService() {
		return bossService;
	}

	public void setBossService(BossService bossService) {
		this.bossService = bossService;
	}

	@Override
	public void invited(String uid, int systemHeroId, String username, int code) {

		/*
		 * 判断是否存在邀请码。code 即是 user 中的 lodo_id
		 */
		User user = userDao.getByPlayerId(String.valueOf(code));

		// 不存在邀请码，抛出异常
		if (user == null) {
			String message = "注册码不存在 code[" + code + "]";
			logger.error(message);
			throw new ServiceException(CODE_NOT_EXIST, message);
		}

		// 存在邀请码，保存用户信息和邀请码信息到 user_invited 表中
		UserInvited userInvited = new UserInvited();
		userInvited.setCode(user.getLodoId());
		userInvited.setUserId(user.getUserId());
		userInvited.setInvitedUserId(uid);
		userInvited.setFinishTaskIds("");
		boolean success = userInvitedDao.add(userInvited);

		// 保存失败
		if (!success) {
			String message = "激活码验证失败 userId[" + user.getUserId() + "], code[" + code + "], invitedUserId[" + uid + "]";
			logger.error(message);
			throw new ServiceException(CODE_VERIFY_FAILED, message);
		}

		/*
		 * 保存成功, 调用 create 方法，创建角色
		 */
		success = create(uid, systemHeroId, username, new EventHandle() {

			public boolean handle(Event event) {
				return true;
			}

		});

		// 给被邀请用户发放奖励
		if (success) {
			activityService.pickGiftBagReward(uid, 74, ToolUseType.ADD_INVITE_TASK);
		}
	}

	@Override
	public void checkCode(int code) {
		User user = userDao.getByPlayerId(String.valueOf(code));

		// 不存在邀请码，抛出异常
		if (user == null) {
			String message = "邀请码不存在 code[" + code + "]";
			logger.error(message);
			throw new ServiceException(CODE_NOT_EXIST, message);
		}

	}

	@Override
	public boolean banUser(String userId, String dueTime) {
		Date date = DateUtils.str2Date(dueTime);
		return userDao.banUser(userId, date);
	}

	@Override
	public boolean assignVipLevel(String userId, final int vipLevel, boolean force) {

		UserExtinfo userExtinfo = this.userExtinfoDao.get(userId);
		if (userExtinfo.getRewardVipLevel() < vipLevel || force) {

			boolean success = this.userExtinfoDao.updateRewardVipLevel(userId, vipLevel);

			// 处理VIP任务
			this.taskService.updateTaskFinish(userId, 1, new EventHandle() {

				@Override
				public boolean handle(Event event) {
					return false;
				}
			}, new TaskChecker() {

				@Override
				public boolean isFinish(int systemTaskId, int taskTarget, Map<String, String> params) {

					int needVipLevel = NumberUtils.toInt(params.get("vl"));

					if (taskTarget == TaskTargetType.TASK_TYPE_TO_BE_VIIP) {
						return vipLevel >= needVipLevel;
					}

					return false;
				}

			});

			return success;
		}
		return false;
	}

	@Override
	public boolean reduceReputation(String userId, int amount, int useType) {
		logger.debug("消费声望.userId[" + userId + "], amount[" + amount + "], useType[" + useType + "]");
		if (amount <= 0) {
			return false;
		}
		boolean success = false;
		try {
			success = this.userDao.reduceReputation(userId, amount);
		} catch (RuntimeException rt) {
			throw rt;
		}

		return success;
	}

	@Override
	public boolean addReputation(String userId, int amount, int useType) {

		logger.debug("添加声望.userId[" + userId + "], amount[" + amount + "], useType[" + useType + "]");
		if (amount <= 0) {
			return false;
		}
		boolean success = false;
		try {
			success = this.userDao.addReputation(userId, amount);
		} catch (RuntimeException rt) {
			throw rt;
		}

		return success;
	}

	@Override
	public boolean updateUserLevel(String userId, int level, int exp) {
		userPkInfoDao.updateUserLevel(userId, level);
		return this.userDao.updateUserLevel(userId, level, exp);
	}

	@Override
	public void getShareGift(String userId) {

		logger.debug("用户分享游戏.userId[" + userId + "]");

		UserShareLog userLastShareLog = this.userDao.getUserLastShareLog(userId);

		if (userLastShareLog == null) { // 首次分享
			UserShareLog userShareLog = new UserShareLog();
			userShareLog.setUserId(userId);
			userShareLog.setCreatedTime(new Date());
			this.userDao.addUserShareLog(userShareLog);

			User user = this.get(userId);
			UserMapper userMapper = this.userMapperDao.get(userId);

			int goldNum = 100;
			String toolIds = ToolType.GOLD + "," + ToolId.TOOL_GLOD_ID + "," + goldNum;
			mailService.send("分享有礼", "尊敬的合伙人：\n 谢谢您对我们游戏的支持！首次分享可以获得" + goldNum + "元宝，之后分享不再获得元宝，请您注意查收，祝您游戏愉快！", toolIds, MailTarget.USERS, user.getLodoId() + "", null, new Date(),
					userMapper.getPartnerId());
		} else {

			String shareDate = DateUtils.getDate(userLastShareLog.getCreatedTime());
			if (!StringUtils.equals(DateUtils.getDate(), shareDate) && userLastShareLog.getCreatedTime().before(new Date())) { // 今天没有分享过\

				UserShareLog userShareLog = new UserShareLog();
				userShareLog.setUserId(userId);
				userShareLog.setCreatedTime(new Date());
				this.userDao.addUserShareLog(userShareLog);

				User user = this.get(userId);
				UserMapper userMapper = this.userMapperDao.get(userId);

				int copperNum = 2000;
				String toolIds = ToolType.COPPER + "," + ToolId.TOOL_COPPER_ID + "," + copperNum * user.getLevel();
				mailService.send("分享有礼", "尊敬的合伙人：\n 谢谢您对我们游戏的支持！每天首次分享可以获得您的等级*" + copperNum + "的银币，因此您今天可获得返利银币" + copperNum * user.getLevel() + "，请您注意查收，祝您游戏愉快！", toolIds, MailTarget.USERS,
						user.getLodoId() + "", null, new Date(), userMapper.getPartnerId());

			}

		}

	}

	@Override
	public boolean reduceMind(String userId, int amount, int useType) {
		logger.debug("消费神魄.userId[" + userId + "], amount[" + amount + "], useType[" + useType + "]");
		if (amount <= 0) {
			return false;
		}
		boolean success = false;
		try {
			success = this.userDao.reduceMind(userId, amount);
		} catch (RuntimeException rt) {
			throw rt;
		}
		pushUser(userId);
		return success;
	}

	@Override
	public boolean addMind(String userId, int amount, int useType) {
		logger.debug("添加神魄.userId[" + userId + "], amount[" + amount + "], useType[" + useType + "]");
		if (amount <= 0) {
			return false;
		}
		boolean success = false;
		try {
			success = this.userDao.addMind(userId, amount);
		} catch (RuntimeException rt) {
			throw rt;
		}
		pushUser(userId);
		return success;
	}

	@Override
	public User getUserByUserName(String username) {
		return this.userDao.getByName(username);
	}

	@Override
	public boolean bannedToPost(String userId) {
		return this.userDao.bannedToPost(userId);
	}

	@Override
	public boolean updateUserFactionId(String userId, int factionId) {
		return this.userDao.updateUserFactionId(userId, factionId);
	}

	@Override
	public boolean addPkScore(String userId, int pkScore, int useType) {
		logger.debug("添加争霸积分.userId[" + userId + "], pkScore[" + pkScore + "], useType[" + useType + "]");
		if (pkScore <= 0) {
			return false;
		}
		boolean success = false;
		try {
			success = this.userDao.addPkScore(userId, pkScore);
		} catch (RuntimeException rt) {
			throw rt;
		}

		return success;
	}

	@Override
	public boolean reducePkScore(String userId, int pkScore, int useType) {
		logger.debug("消耗争霸积分.userId[" + userId + "], pkScore[" + pkScore + "], useType[" + useType + "]");
		if (pkScore <= 0) {
			return false;
		}
		boolean success = false;
		try {
			success = this.userDao.reducePkScore(userId, pkScore);
		} catch (RuntimeException rt) {
			throw rt;
		}

		return success;
	}
}
