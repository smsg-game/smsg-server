package com.lodogame.ldsg.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.CommandDao;
import com.lodogame.game.dao.LogDao;
import com.lodogame.game.dao.SystemWarCityDao;
import com.lodogame.game.dao.UserExtinfoDao;
import com.lodogame.game.dao.UserWarInfoDao;
import com.lodogame.game.dao.WarAwardDao;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.ldsg.bo.BattleBO;
import com.lodogame.ldsg.bo.BattleHeroBO;
import com.lodogame.ldsg.bo.ChooseResultBO;
import com.lodogame.ldsg.bo.CityBo;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.DropToolBO;
import com.lodogame.ldsg.bo.UserEquipBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.bo.UserWarInfoBo;
import com.lodogame.ldsg.bo.WarAllCDBO;
import com.lodogame.ldsg.bo.WarAttackRankBO;
import com.lodogame.ldsg.bo.WarAwardBo;
import com.lodogame.ldsg.bo.WarEnterBo;
import com.lodogame.ldsg.config.Config;
import com.lodogame.ldsg.constants.CommandType;
import com.lodogame.ldsg.constants.InitDefine;
import com.lodogame.ldsg.constants.Priority;
import com.lodogame.ldsg.constants.ToolId;
import com.lodogame.ldsg.constants.ToolType;
import com.lodogame.ldsg.constants.ToolUseType;
import com.lodogame.ldsg.constants.WarStatus;
import com.lodogame.ldsg.event.EnterCityEvent;
import com.lodogame.ldsg.event.EnterCityResponseEvent;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.WarEvent;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.helper.DropToolHelper;
import com.lodogame.ldsg.helper.WarHelper;
import com.lodogame.ldsg.service.BattleService;
import com.lodogame.ldsg.service.EquipService;
import com.lodogame.ldsg.service.EventServcie;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.MessageService;
import com.lodogame.ldsg.service.ToolService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.ldsg.service.WarService;
import com.lodogame.model.Command;
import com.lodogame.model.SystemWarCity;
import com.lodogame.model.User;
import com.lodogame.model.UserExtinfo;
import com.lodogame.model.UserWarInfo;
import com.lodogame.model.WarAttackRank;
import com.lodogame.model.WarAttackRankReward;
import com.lodogame.model.WarAward;
import com.lodogame.model.WarCity;

public class WarServiceImpl implements WarService {

	private static final Logger logger = Logger.getLogger(WarServiceImpl.class);

	private Map<Integer, BlockingDeque<EnterCityEvent>> queueMap = new ConcurrentHashMap<Integer, BlockingDeque<EnterCityEvent>>();

	@Autowired
	private UserWarInfoDao userWarInfoDao;

	@Autowired
	private UserService userService;

	@Autowired
	private ToolService toolService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private SystemWarCityDao systemWarCityDao;

	@Autowired
	private CommandDao commandDao;

	@Autowired
	private BattleService battleService;

	@Autowired
	private HeroService heroService;

	@Autowired
	private EventServcie eventServcie;

	@Autowired
	private WarAwardDao warAwardDao;

	@Autowired
	private EquipService equipService;

	@Autowired
	private UserExtinfoDao userExtinfoDao;

	@Autowired
	private LogDao logDao;

	/**
	 * 开始时间
	 */
	private Date startTime = null;

	/**
	 * 结束时间
	 */
	private Date endTime = null;

	// 状态
	private int status = 1;

	@Override
	public WarEnterBo enter(String userId) {

		User user = userService.get(userId);
		if (user.getLevel() < 20) {
			throw new ServiceException(LEVEL_NOT_ENOUGH, "等级不够!");
		}

		WarEnterBo warEnterBo = new WarEnterBo();

		Date startTime = getStartTime();
		Date endTime = getEndTime();

		warEnterBo.setStartTime(startTime.getTime());
		warEnterBo.setEndTime(endTime.getTime());

		// 上次用户国战信息
		UserWarInfo userWarInfo = userWarInfoDao.getUserWarInfo(userId);
		if (userWarInfo != null) {

			UserWarInfoBo userWarInfoBo = new UserWarInfoBo();
			userWarInfoBo.setPoint(userWarInfo.getPoint());
			userWarInfoBo.setMyDefenseNum(userWarInfo.getDefenseNum());
			userWarInfoBo.setMyAttackNum(userWarInfo.getAttackNum());
			userWarInfoBo.setReputationNum(user.getReputation());
			userWarInfoBo.setInspireNum(userWarInfo.getInspireNum());
			warEnterBo.setPreBo(userWarInfoBo);

		}

		// 上次国战城池状态
		List<WarCity> warCities = userWarInfoDao.getCityStatus();
		for (WarCity wc : warCities) {

			CityBo cityBo = new CityBo();
			cityBo.setCountryID(wc.getCountryId());
			cityBo.setPeopleNum(wc.getNum());
			cityBo.setPoint(wc.getPoint());
			warEnterBo.getPreCity().add(cityBo);

		}

		if (this.status == WarStatus.STARTED) {

			if (!userWarInfoDao.isWarLog(DateUtils.getDate())) {
				userWarInfoDao.addWarLog(DateUtils.getDate());
			}
			UserWarInfo currUserWarInfo = userWarInfoDao.getUserWarInfo(userId);
			if (null != currUserWarInfo) {
				userWarInfoDao.setUserCity(userId, WarHelper.getInitialPoint(currUserWarInfo.getCountryId()));
				List<UserHeroBO> userHeros = heroService.getUserHeroList(user.getUserId(), 1);
				userWarInfoDao.setPower(userId, power(userId, userHeros));
				warEnterBo.setCurrBo(getUserWarInfoBo(userId));
			}

			List<WarCity> currWarCities = userWarInfoDao.getCityStatus();
			for (WarCity wc : currWarCities) {
				CityBo cityBo = new CityBo();
				cityBo.setCountryID(wc.getCountryId());
				cityBo.setPeopleNum(wc.getNum());
				cityBo.setPoint(wc.getPoint());
				warEnterBo.getCurrCity().add(cityBo);
			}
			List<WarCity> countryPeopleNums = userWarInfoDao.getCountryPeopleNum();
			warEnterBo.setHideList(WarHelper.getChooseCountryList(countryPeopleNums));
		}

		return warEnterBo;
	}

	private int power(String userId, List<UserHeroBO> userHeros) {
		int power = 0;
		for (UserHeroBO hero : userHeros) {
			power += hero.getPhysicalAttack() + hero.getPhysicalDefense() + hero.getLife();
		}
		return power;
	}

	// 距离结束时间
	public Date getEndTime() {
		return this.endTime;
	}

	// 距离开始时间
	public Date getStartTime() {

		Date now = new Date();

		if (this.startTime != null) {
			return startTime;
		}

		if (Config.ins().isDebug()) {
			startTime = DateUtils.add(now, Calendar.SECOND, 30);
			endTime = DateUtils.add(startTime, Calendar.MINUTE, 30);
			return startTime;
		}

		for (int i = 0; i < 7; i++) {
			Date date = DateUtils.addDays(now, i);
			int week = DateUtils.getDayOfWeek(date);
			if (WarHelper.isMatchDay(week)) {

				startTime = DateUtils.str2Date(DateUtils.getDate(date) + " 19:00:00");
				endTime = DateUtils.add(startTime, Calendar.MINUTE, 30);
				if (now.before(endTime)) {// 还没打完
					break;
				}

			}
		}

		return startTime;

	}

	@Override
	public CommonDropBO drawStay(String userId, Integer point) {
		CommonDropBO commonDropBO = new CommonDropBO();
		User user = userService.get(userId);
		UserWarInfo userWarInfo = userWarInfoDao.getUserWarInfo(userId);
		Date date = new Date();

		synchronized (userWarInfo) {
			if (date.before(this.getStartTime())) {
				throw new ServiceException(NO_START, "时间没到!");
			}
			if (userWarInfo != null && date.before(userWarInfo.getDrawTime())) {
				throw new ServiceException(DRAW_TIME_OUT, "时间没到!");
			}
	
			// 重新设置领奖时间
			Date lastDrawTime = DateUtils.getAfterTime(new Date(), 3 * 60);
			userWarInfoDao.setDrawTime(userId, lastDrawTime);
	
			int toolNum = getReputation(point, user.getLevel());
			
			if (userService.addReputation(userId, toolNum, ToolUseType.WAR_REPUTATION)) {
				
				logDao.toolLog(userId, ToolType.WAR_REPUTATION, ToolId.TOOL_REPUTATION_ID, toolNum, ToolUseType.WAR_REPUTATION, 1, "", true);
			} else {
				return null;
			}
			commonDropBO.setReputation(toolNum);

		}
		return commonDropBO;
	}

	// 声望
	private int getReputation(Integer point, int level) {
		SystemWarCity systemWarCity = systemWarCityDao.get(point);
		int peopleNum = userWarInfoDao.getCityPeopleNum(point);
		double crowd = 0;
		if (peopleNum <= systemWarCity.getCityFull() * 0.1) {
			crowd = 1.2;
		} else if (peopleNum <= systemWarCity.getCityFull() * 0.5) {
			crowd = 1.0;
		} else if (peopleNum <= systemWarCity.getCityFull()) {
			crowd = 0.8;
		} else {
			crowd = 0.5;
		}
		int num = (int) (level * systemWarCity.getRatio() * systemWarCity.getMultiple() * crowd);
		return num;
	}

	@Override
	public ChooseResultBO choose(String userId, int cid) {
		ChooseResultBO chooseResultBO = new ChooseResultBO();
		List<Integer> list = WarHelper.getChooseCountryList(userWarInfoDao.getCountryPeopleNum());
		if (list.indexOf(cid) >= 0) {
			chooseResultBO.setRf(2002);
			chooseResultBO.setList(list);
			return chooseResultBO;
		}
		chooseResultBO.setRf(1000);
		User user = userService.get(userId);
		UserWarInfo userWarInfo = this.userWarInfoDao.getUserWarInfo(userId);
		if (userWarInfo != null) {
			return chooseResultBO;
		}
		Date date = new Date();

		userWarInfo = new UserWarInfo();
		userWarInfo.setUserName(user.getUsername());
		userWarInfo.setUserId(userId);
		userWarInfo.setPoint(WarHelper.getInitialPoint(cid));
		userWarInfo.setCountryId(cid);
		userWarInfo.setActionTime(date);
		userWarInfo.setLiftTime(date);
		userWarInfo.setInspireTime(date);
		List<UserHeroBO> userHeros = heroService.getUserHeroList(user.getUserId(), 1);
		userWarInfo.setAbility(power(userId, userHeros));
		userWarInfo.setTotalAttackNum(0);
		userWarInfo.setLevel(user.getLevel());
		userWarInfo.setVipLevel(user.getVipLevel());
		userWarInfo.setSystemHeroId(userHeros.size() > 0 ? userHeros.get(0).getSystemHeroId() : 0);
		userWarInfoDao.add(userWarInfo);
		return chooseResultBO;
	}

	@Override
	public Map<String, Object> attackCity(String userId, Integer point, EventHandle handle) {
		Date date = new Date();
		UserWarInfo userWarInfo = userWarInfoDao.getUserWarInfo(userId);
		SystemWarCity systemWarCity = systemWarCityDao.get(userWarInfo.getPoint());
		if (systemWarCity.getAttackCity().indexOf(String.valueOf(point)) < 0) {
			throw new ServiceException(CITY_ERROR, "城池选择错误，你不能选择这个城池!");
		}
		if (date.before(userWarInfo.getActionTime())) {
			throw new ServiceException(ACTION_TIME_ERROR, "在行动CD中!");
		}
		userWarInfoDao.setActionTime(userId, DateUtils.getAfterTime(new Date(), 1 * 60));
		userWarInfoDao.setCopperClearTime(userId, 0);
		EnterCityEvent event = new EnterCityEvent();
		event.setCityId(point);
		event.setCallack(handle);
		event.setUserId(userId);
		addTask(event);

		return null;
	}

	private void addTask(EnterCityEvent event) {

		if (this.queueMap.containsKey(event.getCityId())) {

			BlockingDeque<EnterCityEvent> queue = this.queueMap.get(event.getCityId());
			queue.add(event);

		} else {

			final BlockingDeque<EnterCityEvent> queue = new LinkedBlockingDeque<EnterCityEvent>();
			this.queueMap.put(event.getCityId(), queue);
			queue.add(event);

			new Thread(new Runnable() {

				public void run() {
					while (true) {
						try {
							EnterCityEvent event = queue.take();
							handelEvent(event);
						} catch (Throwable t) {
							logger.error(t.getMessage(), t);
						}

						try {
							Thread.sleep(1000);
						} catch (InterruptedException ie) {
							logger.error(ie.getMessage(), ie);
						}

					}
				}

			}).start();
		}

	}

	private void handelEvent(EnterCityEvent enterCityEvent) {

		String userId = enterCityEvent.getUserId();
		int cityId = enterCityEvent.getCityId();
		int status = 0;

		SystemWarCity systemWarCity = systemWarCityDao.get(enterCityEvent.getCityId());
		UserWarInfo userWarInfo = userWarInfoDao.getUserWarInfo(userId);

		// Map<String, Object> rcMap = new HashMap<String, Object>();
		List<UserWarInfo> list = getAllUserByPoint(cityId);
		if (list.size() == 0) { // 如果是空城，则进入城池
			userWarInfoDao.setUserCity(userId, enterCityEvent.getCityId());
			this.pushAllCity();

			if (null == userWarInfo.getDrawTime()) {
				userWarInfoDao.setDrawTime(userId, DateUtils.getAfterTime(new Date(), 3 * 60));
			}

			if (enterCityEvent.getCityId() == 33) {
				WarEvent warEvent = new WarEvent(WarHelper.getCountryName(enterCityEvent.getCityId()), userWarInfo.getUserName(), systemWarCity.getCityName(), 1);
				eventServcie.dispatchEvent(warEvent);
			}

			status = 0;
		} else if (list.get(0).getCountryId() == userWarInfo.getCountryId()) { // 如果是我方城，则进入城池

			if (null == userWarInfo.getDrawTime()) {
				userWarInfoDao.setDrawTime(userId, DateUtils.getAfterTime(new Date(), 3 * 60));
			}

			userWarInfoDao.setUserCity(userId, cityId);
			status = 1;
		} else {

			status = 2;

			logger.debug("国战战斗开始.attackUserId[" + userId + "]");
			UserWarInfo attackUser = userWarInfoDao.getUserWarInfo(userId);
			UserWarInfo defenseUser = matcher(userId, cityId);
			_handleMatch(attackUser, defenseUser, enterCityEvent.getCallack());
			return;

		}

		if (status == 0 || status == 1) {
			Event event = new EnterCityResponseEvent();
			event.setObject("st", status);
			enterCityEvent.getCallack().handle(event);
		}

	}

	@Override
	public void clearActionCD(String userId, int grantType) {
		Date date = new Date();

		User user = this.userService.get(userId);
		UserWarInfo userWarInfo = this.userWarInfoDao.getUserWarInfo(userId);

		if (date.after(userWarInfo.getActionTime())) {
			throw new ServiceException(NO_ACTION_TIME_ERROR, "当前没有可以结束的行动CD!");
		}

		if (grantType == 0) {
			if (!this.userService.reduceGold(userId, 20, ToolUseType.WAR_ACTION_CD, user.getLevel())) {
				throw new ServiceException(NO_GOLD, "元宝不足userId[" + userId + "]");
			}
			userService.pushUser(userId);
			userWarInfoDao.clearActionCD(userId, DateUtils.getAfterTime(new Date(), -1));
		} else {
			if (userWarInfo.getClearActionCopper() >= 1) {
				throw new ServiceException(COPPER_CLEAR_ACTION_TIME_NOT_ENOUGH, "银币清空次数已用userId[" + userId + "]");
			}
			if (!this.userService.reduceCopper(userId, 100000, ToolUseType.WAR_ACTION_CD)) {
				throw new ServiceException(NO_COPPER, "银币不足userId[" + userId + "]");
			}
			userService.pushUser(userId);
			userWarInfoDao.setCopperClearTime(userId, 1);
			userWarInfoDao.clearActionCD(userId, DateUtils.getAfterTime(userWarInfo.getActionTime(), -30));
		}

	}

	@Override
	public void clearLiftCD(String userId) {
		User user = this.userService.get(userId);
		if (!this.userService.reduceGold(userId, 40, ToolUseType.WAR_ACTION_CD, user.getLevel())) {
			throw new ServiceException(NO_GOLD, "元宝不足userId[" + userId + "]");
		}
		userService.pushUser(userId);
		userWarInfoDao.clearLiftCD(userId, DateUtils.getAfterTime(new Date(), -120));
	}

	@Override
	public void inspire(String userId) {

		User user = this.userService.get(userId);
		UserWarInfo userWarInfo = userWarInfoDao.getUserWarInfo(userId);

		if (userWarInfo.getInspireNum() >= 10) {
			throw new ServiceException(MAX_INSPIRE, "鼓舞次数已达上限userId[" + userId + "]");
		}
		if (!this.userService.reduceGold(userId, 20, ToolUseType.WAR_INSPIRE, user.getLevel())) {
			throw new ServiceException(NO_GOLD, "元宝不足userId[" + userId + "]");
		}
		userService.pushUser(userId);
		userWarInfoDao.inspire(userId, DateUtils.getAfterTime(new Date(), 30));
	}

	@Override
	public List<CityBo> look(String userId) {
		User user = this.userService.get(userId);
		List<CityBo> cityBos = new ArrayList<CityBo>();
		if (!this.userService.reduceGold(userId, 10, ToolUseType.WAR_LOOK, user.getLevel())) {
			throw new ServiceException(NO_GOLD, "元宝不足userId[" + userId + "]");
		}
		userService.pushUser(userId);
		List<WarCity> list = userWarInfoDao.getCityStatus();
		Map<String, CityBo> maps = new HashMap<String, CityBo>();
		for (int i = 1; i <= 5; i++) {
			for (int j = 1; j <= 5; j++) {
				String point = i + "" + j;
				CityBo cityBo = new CityBo();
				cityBo.setPoint(point);
				maps.put(point, cityBo);
			}
		}
		for (WarCity wc : list) {
			if (maps.containsKey(wc.getPoint())) {
				maps.get(wc.getPoint()).setCountryID(wc.getCountryId());
				maps.get(wc.getPoint()).setPeopleNum(wc.getNum());
			}
		}
		Set set = maps.keySet();
		Iterator it = set.iterator();
		while (it.hasNext()) {
			String s = (String) it.next();
			cityBos.add(maps.get(s));
		}
		return cityBos;
	}

	@Override
	public WarAllCDBO getCDAndAttackNum(String userId) {
		WarAllCDBO warAllCDBO = new WarAllCDBO();
		UserWarInfo userWarInfo = userWarInfoDao.getUserWarInfo(userId);
		warAllCDBO.setActionTime(userWarInfo.getActionTime().getTime());
		warAllCDBO.setAttackNum(userWarInfo.getAttackNum());
		warAllCDBO.setDefenseNum(userWarInfo.getDefenseNum());
		if (userWarInfo.getDrawTime() != null) {
			warAllCDBO.setDrawTime(userWarInfo.getDrawTime().getTime());
		} else {
			warAllCDBO.setDrawTime(0);
		}
		warAllCDBO.setInspireTime(userWarInfo.getInspireTime().getTime());
		warAllCDBO.setLiftTime(userWarInfo.getLiftTime().getTime());
		warAllCDBO.setPoint(userWarInfo.getPoint());
		warAllCDBO.setInspireNum(userWarInfo.getInspireNum());
		warAllCDBO.setStartTime(getStartTime().getTime());
		warAllCDBO.setEndTime(getEndTime().getTime());
		return warAllCDBO;
	}

	@Override
	public List<CityBo> getAllCity() {
		List<CityBo> list = new ArrayList<CityBo>();
		List<WarCity> currWarCities = userWarInfoDao.getCityStatus();
		for (WarCity wc : currWarCities) {
			CityBo cityBo = new CityBo();
			cityBo.setCountryID(wc.getCountryId());
			cityBo.setPeopleNum(wc.getNum());
			cityBo.setPoint(wc.getPoint());
			list.add(cityBo);
		}
		return list;
	}

	/**
	 * 推送所有城池
	 */
	public void pushAllCity() {

		Command command = new Command();
		command.setCommand(CommandType.COMMAND_WAR_ALL_CITY);
		command.setType(CommandType.PUSH_ALL);
		command.setPriority(Priority.NORMAL);
		command.setParams(new HashMap<String, String>());

		commandDao.add(command);
	}

	/**
	 * 推送国战结束
	 */
	public void pushWarEnd() {
		Command command = new Command();
		command.setCommand(CommandType.COMMAND_WAR_END);
		command.setType(CommandType.PUSH_ALL);
		command.setPriority(Priority.NORMAL);
		command.setParams(new HashMap<String, String>());

		commandDao.add(command);
	}

	/**
	 * 推送防守次数
	 */
	public void pushDefenseNum(String userId, int num) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", userId);
		params.put("defenseNum", num + "");

		Command command = new Command();
		command.setCommand(CommandType.COMMAND_WAR_DEFENSE_NUM);
		command.setType(CommandType.PUSH_USER);
		command.setPriority(Priority.NORMAL);
		command.setParams(params);

		commandDao.add(command);
	}

	/**
	 * 推送防守战报
	 */
	public void pushBattle(String userId, Map<String, String> params) {
		params.put("userId", userId);

		Command command = new Command();
		command.setCommand(CommandType.COMMAND_WAR_DEFENSE_REPORT);
		command.setType(CommandType.PUSH_USER);
		command.setPriority(Priority.NORMAL);
		command.setParams(params);

		commandDao.add(command);
	}

	/**
	 * 推送国战开始
	 */
	public void pushBattleStart() {

		Map<String, String> params = new HashMap<String, String>();
		params.put("tp", 1 + "");
		Command command = new Command();
		command.setCommand(CommandType.COMMAND_BATTLE_START);
		command.setType(CommandType.PUSH_ALL);
		command.setPriority(Priority.NORMAL);
		command.setParams(params);

		commandDao.add(command);
	}

	/**
	 * 推送国战开始
	 */
	public void pushWarAttackRankEnd() {

		Map<String, String> params = new HashMap<String, String>();
		Command command = new Command();
		command.setCommand(CommandType.COMMAND_WAR_ATTACK_RANK_CREATE);
		command.setType(CommandType.PUSH_ALL);
		command.setPriority(Priority.NORMAL);
		command.setParams(params);

		commandDao.add(command);
	}

	public void init() {

		if (!Config.ins().isGameServer()) {
			return;
		}
		new Thread(new Runnable() {

			public void run() {
				while (true) {
					try {
						statusCheck();
					} catch (Throwable t) {
						logger.error(t.getMessage(), t);
					}

					try {
						Thread.sleep(1000);
					} catch (InterruptedException ie) {
						logger.error(ie.getMessage(), ie);
					}

				}
			}

		}).start();

	}

	/**
	 * 获取状态描述
	 * 
	 * @param status
	 */
	private String getStatusDesc(int status) {

		switch (status) {
		case WarStatus.NOT_STARTED:
			return "活动未开始(" + status + ")";
		case WarStatus.STARTED:
			return "活动进行中(" + status + ")";
		default:
			return "未知状态(" + status + ")";
		}

	}

	/**
	 * 开始逻辑
	 */
	private void start() {

		this.status = WarStatus.STARTED;
		userWarInfoDao.cleanData();

		// 清除排行数据
		int week = DateUtils.getDayOfWeek(new Date());
		if (WarHelper.isCleanRankData(week)) {
			this.userWarInfoDao.backUpRankData();
		}

		pushBattleStart();

	}

	/**
	 * 结束逻辑
	 */
	private void end() {

		this.status = WarStatus.NOT_STARTED;

		saveAttackRank();

		pushBattleStart();

		logger.debug("推送国战结束");
		pushWarEnd();

		this.startTime = null;

	}

	/**
	 * 保存击杀榜
	 */
	private void saveAttackRank() {

		this.userWarInfoDao.saveRankData();
		int week = DateUtils.getDayOfWeek(new Date());
		if (WarHelper.isRankRewardDay(week)) {
			this.userWarInfoDao.updateWarRankAttackStatus();

			List<WarAttackRank> rankList = this.userWarInfoDao.getWarAttackRankList();
			if (rankList.size() > 0) {
				this.messageService.sendWarAttackRankCreate(rankList.get(0).getUsername());
			}
		}
	}

	/**
	 * 状态监控方法
	 */
	private void statusCheck() {

		Date now = new Date();

//		logger.debug("状态切换线程.status[" + getStatusDesc(this.status) + "]");

		if (this.status == WarStatus.NOT_STARTED) {

//			logger.debug("国战活动未开始,now[" + DateUtils.getTime() + "], start time[" + DateUtils.getTime(this.getStartTime().getTime()) + "]");

			if (now.after(this.getStartTime())) {
				logger.debug("国战开始");
				this.start();

			}

		} else if (this.status == WarStatus.STARTED) {

//			logger.debug("国战活动进行中,now[" + DateUtils.getTime() + "], end time[" + DateUtils.getTime(this.getEndTime().getTime()) + "]");

			if (now.after(this.getEndTime())) {
				logger.debug("国战结束");
				this.end();
			}

		}

	}

	/**
	 * 匹配
	 * 
	 * @param arenaRegList
	 * @return
	 */
	private UserWarInfo matcher(String userId, int cityId) {

		List<UserWarInfo> list = getAllUserByPoint(cityId);

		this.sortArenaRegList(list);

		for (UserWarInfo userWarInfo : list) {
			if (userWarInfo.isBeAttack()) {
				continue;
			}
			return userWarInfo;
		}

		return list.get(0);
	}

	private void sortArenaRegList(List<UserWarInfo> enemyList) {
		// 排序
		Collections.sort(enemyList, new Comparator<UserWarInfo>() {
			@Override
			public int compare(UserWarInfo o1, UserWarInfo o2) {
				return o1.getAbility() - o2.getAbility();
			}

		});

	}

	private void _handleMatch(final UserWarInfo attackUserA, final UserWarInfo defenseUserB, final EventHandle handle) {

		defenseUserB.setBeAttack(true);

		final Map<String, Integer> lifeMap = new HashMap<String, Integer>();
		final Map<String, String> heroPosMap = new HashMap<String, String>();
		final int cityNum = userWarInfoDao.getAllUserByCityId(defenseUserB.getPoint()).size();
		// 进攻方信息
		BattleBO attack = new BattleBO();
		// 鼓舞加成
		attack.setAddRatio(attackUserA.getInspireNum() * 0.1);

		final User attackUser = userService.get(attackUserA.getUserId());
		List<BattleHeroBO> attackHeroBOList = this.heroService.getUserBattleHeroBOList(attackUserA.getUserId());
		// 设置初始血量
		// this.setHeroLife(attackUser.getUserId(), attackHeroBOList);

		for (BattleHeroBO battleHeroBo : attackHeroBOList) {
			heroPosMap.put(battleHeroBo.getUserHeroId(), "L_a" + battleHeroBo.getPos());
			// lifeMap.put(battleHeroBo.getUserHeroId(), 0);
		}
		attack.setUserLevel(attackUser.getLevel());
		attack.setBattleHeroBOList(attackHeroBOList);

		// 防守方信息
		User defenseUser = userService.get(defenseUserB.getUserId());
		BattleBO defense = new BattleBO();
		// 鼓舞加成
		defense.setAddRatio(defenseUserB.getInspireNum() * 0.1);

		List<BattleHeroBO> defenseHeroBOList = this.heroService.getUserBattleHeroBOList(defenseUserB.getUserId());
		// 设置初始血量
		// this.setHeroLife(defenseUser.getUserId(), defenseHeroBOList);

		defense.setBattleHeroBOList(defenseHeroBOList);
		for (BattleHeroBO battleHeroBo : defenseHeroBOList) {
			heroPosMap.put(battleHeroBo.getUserHeroId(), "L_d" + battleHeroBo.getPos());
			// lifeMap.put(battleHeroBo.getUserHeroId(), 0);
		}
		defense.setUserLevel(defenseUser.getLevel());
		defense.setBattleHeroBOList(defenseHeroBOList);

		battleService.fight(attack, defense, 6, new EventHandle() {

			@Override
			public boolean handle(Event event) {

				event.setObject("duid", defenseUserB.getUserId());
				event.setObject("aun", attackUserA.getUserName());
				event.setObject("dun", defenseUserB.getUserName());
				event.setObject("acid", attackUserA.getCountryId());
				event.setObject("dcid", defenseUserB.getCountryId());

				int flag = event.getInt("flag");

				String report = event.getString("report");

				UserWarInfoBo userWarInfoBo = getUserWarInfoBo(attackUserA.getUserId());
				event.setObject("uws", userWarInfoBo);
				event.setObject("st", 2);

				// 处理比赛结束
				_handleMatchEnd(attackUserA, defenseUserB, flag, report, lifeMap);
				if (flag == 1) {
					if (cityNum == 1) {
						event.setObject("occ", 1);
					} else {
						event.setObject("occ", 0);
					}
				} else {
					event.setObject("occ", 0);
				}
				handle.handle(event);

				return true;
			}
		});
	}

	/**
	 * 
	 * @param arenaRegA
	 * @param arenaRegB
	 */
	private void _handleMatchEnd(UserWarInfo attackUserA, UserWarInfo defenseUserB, int flag, String report, Map<String, Integer> lifeMap) {

		defenseUserB.setBeAttack(false);

		SystemWarCity systemWarCity = systemWarCityDao.get(defenseUserB.getPoint());
		String rf = "0";
		int occ = 0;
		// 进攻方赢了
		if (flag == 1) {
			rf = "1";
			if (WarHelper.getInitialPoint(attackUserA.getCountryId()) != attackUserA.getPoint()) {
				this.userWarInfoDao.setDrawTime(attackUserA.getUserId(), DateUtils.getAfterTime(attackUserA.getDrawTime(), -60));
			}
			// saveLife(attackUserA.getUserId(), lifeMap);
			this.userWarInfoDao.setAttackNum(attackUserA.getUserId());

			List<UserWarInfo> list = userWarInfoDao.getAllUserByCityId(defenseUserB.getPoint());
			// 输了回大本营
			this.userWarInfoDao.setUserCity(defenseUserB.getUserId(), WarHelper.getInitialPoint(defenseUserB.getCountryId()));
			// 设置复活时间
			this.userWarInfoDao.setLifeTime(defenseUserB.getUserId(), DateUtils.getAfterTime(new Date(), 30));

			// 占领城池
			if (list.size() == 1) {
				occ = 1;
				this.userWarInfoDao.setUserCity(attackUserA.getUserId(), systemWarCity.getCityId());
				pushAllCity();
				if (systemWarCity.getCityId() == 33) {
					WarEvent warEvent = new WarEvent(WarHelper.getCountryName(attackUserA.getCountryId()), attackUserA.getUserName(), systemWarCity.getCityName(), 1);
					eventServcie.dispatchEvent(warEvent);
				}
			}
		} else {
			if (WarHelper.getInitialPoint(attackUserA.getCountryId()) != attackUserA.getPoint()) {
				this.userWarInfoDao.setDrawTime(attackUserA.getUserId(), DateUtils.getAfterTime(attackUserA.getDrawTime(), -30));
			}
			UserWarInfo userWarInfo = userWarInfoDao.getUserWarInfo(defenseUserB.getUserId());
			// 防守胜利10次回大本营
			if (userWarInfo.getDefenseNum() >= 9) {
				WarEvent warEvent = new WarEvent(WarHelper.getCountryName(defenseUserB.getCountryId()), defenseUserB.getUserName(), systemWarCity.getCityName(), 2);
				eventServcie.dispatchEvent(warEvent);
				// 回大本营
				this.userWarInfoDao.setUserCity(defenseUserB.getUserId(), WarHelper.getInitialPoint(defenseUserB.getCountryId()));
				// 设置复活时间
				this.userWarInfoDao.setLifeTime(defenseUserB.getUserId(), DateUtils.getAfterTime(new Date(), 30));

				// 清空防御次数
				this.userWarInfoDao.clearDefenseNum(defenseUserB.getUserId());

			} else {
				this.userWarInfoDao.setDefenseNum(defenseUserB.getUserId());
			}
			pushDefenseNum(userWarInfo.getUserId(), userWarInfo.getDefenseNum());

		}

		// 发送战报给防守方
		Map<String, String> pushParam = new HashMap<String, String>();
		pushParam.put("rid", UUID.randomUUID().toString());
		pushParam.put("acid", attackUserA.getCountryId() + "");
		pushParam.put("dcid", defenseUserB.getCountryId() + "");
		pushParam.put("dpo", systemWarCity.getCityId() + "");
		pushParam.put("occ", occ + "");
		pushParam.put("uid", defenseUserB.getUserId());
		pushParam.put("attackUserId", attackUserA.getUserId());
		pushParam.put("attackUsername", attackUserA.getUserName());
		pushParam.put("defenseUserId", defenseUserB.getUserId());
		pushParam.put("defenseUsername", defenseUserB.getUserName());
		pushParam.put("rf", rf);
		pushParam.put("report", report);
		pushParam.put("tp", "6");
		pushParam.put("isc", "0");
		this.pushBattle(defenseUserB.getUserId(), pushParam);

	}

	@Override
	public UserWarInfoBo getUserWarInfoBo(String userId) {
		Date now = new Date();
		UserWarInfo userWarInfo = userWarInfoDao.getUserWarInfo(userId);
		UserWarInfoBo userWarInfoBo = new UserWarInfoBo();
		userWarInfoBo.setPoint(userWarInfo.getPoint());
		userWarInfoBo.setMyDefenseNum(userWarInfo.getDefenseNum());
		userWarInfoBo.setMyAttackNum(userWarInfo.getAttackNum());
		userWarInfoBo.setCountryID(userWarInfo.getCountryId());
		userWarInfoBo.setActionCD(userWarInfo.getActionTime().getTime());
		User user = userService.get(userId);
		userWarInfoBo.setReputationNum(user.getReputation());
		if (userWarInfo.getDrawTime() != null) {
			userWarInfoBo.setDrawCD(userWarInfo.getDrawTime().getTime());
		} else {
			userWarInfoBo.setDrawCD(0);
		}
		userWarInfoBo.setInspireNum(userWarInfo.getInspireNum());
		userWarInfoBo.setInspireCD(userWarInfo.getInspireTime().getTime());
		userWarInfoBo.setUserId(userId);
		userWarInfoBo.setLiftCD(now.after(userWarInfo.getLiftTime()) ? 0 : userWarInfo.getLiftTime().getTime());
		return userWarInfoBo;
	}

	@Override
	public List<WarAwardBo> getAwardList() {
		List<WarAwardBo> boList = new ArrayList<WarAwardBo>();
		List<WarAward> list = warAwardDao.getAll();
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				WarAward warAward = list.get(i);
				WarAwardBo bo = new WarAwardBo();
				bo.setAwardId(warAward.getAwardId());
				bo.setName(warAward.getName());
				bo.setNeedNum(warAward.getNeedNum());
				bo.setImgId(warAward.getImgId());
				bo.setDescription(warAward.getDescription());

				List<DropToolBO> dropToolBOList = DropToolHelper.parseDropTool(warAward.getTools());
				if (!dropToolBOList.isEmpty()) {
					bo.setToolId(dropToolBOList.get(0).getToolId());
					bo.setToolType(dropToolBOList.get(0).getToolType());
				} else {
					bo.setToolId(0);
					bo.setToolType(0);
				}

				boList.add(bo);
			}
		}
		return boList;
	}

	@Override
	public CommonDropBO exchange(String userId, int awardId, int num) {

		WarAward warAward = warAwardDao.get(awardId);
		User user = userService.get(userId);
		int remainder = user.getReputation() - warAward.getNeedNum() * num;
		if (remainder < 0) {
			throw new ServiceException(EXCHANGE_REPUTATION_NOT_ENOUGH, "剩余声望不足");
		}
		CommonDropBO commonDropBO = new CommonDropBO();
		userService.reduceReputation(userId, warAward.getNeedNum() * num, ToolUseType.WAR_REPUTATION);

		logDao.toolLog(userId, ToolType.WAR_REPUTATION, ToolId.TOOL_REPUTATION_ID, warAward.getNeedNum() * num, ToolUseType.WAR_REPUTATION, -1, "", true);

		logger.debug("userId:" + userId + ", award id: " + awardId + ", reputation:" + user.getReputation() + ", remainder score:" + remainder);
		List<DropToolBO> dropTools = DropToolHelper.parseDropTool(warAward.getTools());
		checkBag(userId, dropTools);
		// 发道具到用户背包
		for (DropToolBO dropToolBO : dropTools) {
			List<DropToolBO> dropToolBOList = toolService.giveTools(userId, dropToolBO.getToolType(), dropToolBO.getToolId(), dropToolBO.getToolNum() * num, ToolUseType.WAR_REPUTATION);
			for (DropToolBO db : dropToolBOList) {
				toolService.appendToDropBO(userId, commonDropBO, db);
			}
		}
		userService.pushUser(userId);
		return commonDropBO;
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
	public List<UserWarInfo> getAllUserByPoint(Integer point) {
		return userWarInfoDao.getAllUserByCityId(point);
	}

	@Override
	public UserWarInfo get(String userId) {
		return this.userWarInfoDao.getUserWarInfo(userId);
	}

	@Override
	public void exitWar(String userId) {
		UserWarInfo userWarInfo = userWarInfoDao.getUserWarInfo(userId);
		if (userWarInfo != null) {
			userWarInfoDao.setUserCity(userId, WarHelper.getInitialPoint(userWarInfo.getCountryId()));
		}

	}

	@Override
	public List<WarAttackRankBO> getWarAttackRankBOList(String userId) {

		List<WarAttackRankBO> list = new ArrayList<WarAttackRankBO>();
		List<WarAttackRank> rankList = this.userWarInfoDao.getWarAttackRankList();
		int sort = 1;
		for (WarAttackRank rank : rankList) {

			int status = rank.getStatus();
			if (sort > 10 || (status == 1 && !StringUtils.endsWithIgnoreCase(userId, rank.getUserId()))) {
				status = 0;
			}

			WarAttackRankBO bo = new WarAttackRankBO();
			bo.setStatus(status);
			bo.setLevel(rank.getLevel());
			bo.setUsername(rank.getUsername());
			bo.setSystemHeroId(rank.getSystemHeroId());
			bo.setVipLevel(rank.getVipLevel());
			bo.setRank(sort);
			bo.setAttackNum(rank.getAttackNum());

			list.add(bo);

			sort += 1;

		}

		return list;

	}

	@Override
	public CommonDropBO receive(String userId) {

		WarAttackRank rank = null;
		int sort = 1;

		List<WarAttackRank> rankList = this.userWarInfoDao.getWarAttackRankList();
		for (WarAttackRank warAttackRank : rankList) {
			if (StringUtils.equalsIgnoreCase(warAttackRank.getUserId(), userId)) {
				rank = warAttackRank;
				break;
			}
			sort += 1;
		}

		if (rank == null || rank.getStatus() != 1) {// 没有奖励可以领取
			throw new ServiceException(ATTACK_REWARD_NOT_REWARD, "没有奖励可以领取");
		}

		this.userWarInfoDao.updateWarRankAttackStatus(userId, 2);

		WarAttackRankReward warAttackRankReward = this.userWarInfoDao.getAttackRankReward(sort);

		List<DropToolBO> dropTools = DropToolHelper.parseDropTool(warAttackRankReward.getToolIds());
		// 发道具到用户背包
		CommonDropBO commonDropBO = new CommonDropBO();
		for (DropToolBO dropToolBO : dropTools) {
			List<DropToolBO> dropToolBOList = toolService.giveTools(userId, dropToolBO.getToolType(), dropToolBO.getToolId(), dropToolBO.getToolNum(), ToolUseType.WAR_ATTACK_RANK_REWARD);
			for (DropToolBO db : dropToolBOList) {
				toolService.appendToDropBO(userId, commonDropBO, db);
			}
		}

		return commonDropBO;
	}
}
