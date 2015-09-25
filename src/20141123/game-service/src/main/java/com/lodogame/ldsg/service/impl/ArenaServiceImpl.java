package com.lodogame.ldsg.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.lodogame.game.dao.ArenaDao;
import com.lodogame.game.dao.CommandDao;
import com.lodogame.game.dao.SystemArenaRewardDao;
import com.lodogame.game.dao.UserDao;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.ldsg.bo.ArenaBattleRecordPushBO;
import com.lodogame.ldsg.bo.ArenaConfigBO;
import com.lodogame.ldsg.bo.ArenaRankBO;
import com.lodogame.ldsg.bo.ArenaRegBO;
import com.lodogame.ldsg.bo.ArenaRewardBO;
import com.lodogame.ldsg.bo.BattleBO;
import com.lodogame.ldsg.bo.BattleHeroBO;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.DropToolBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.config.Config;
import com.lodogame.ldsg.constants.ActivityTargetType;
import com.lodogame.ldsg.constants.ArenaRecordType;
import com.lodogame.ldsg.constants.ArenaRegStatus;
import com.lodogame.ldsg.constants.ArenaStatus;
import com.lodogame.ldsg.constants.CommandType;
import com.lodogame.ldsg.constants.Priority;
import com.lodogame.ldsg.constants.ToolType;
import com.lodogame.ldsg.constants.ToolUseType;
import com.lodogame.ldsg.event.ArenaEvent;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.helper.DropToolHelper;
import com.lodogame.ldsg.service.ActivityTaskService;
import com.lodogame.ldsg.service.ArenaService;
import com.lodogame.ldsg.service.BattleService;
import com.lodogame.ldsg.service.EventServcie;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.ToolService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.ArenaHero;
import com.lodogame.model.ArenaReg;
import com.lodogame.model.Command;
import com.lodogame.model.SystemArenaReward;
import com.lodogame.model.User;

public class ArenaServiceImpl implements ArenaService {

	private static final Logger logger = Logger.getLogger(ArenaServiceImpl.class);

	private static final Integer[] rewardToolIds = new Integer[] { 90003, 90002, 90001 };

	/**
	 * 正在处理的分组
	 */
	private Map<Integer, Object> workWinGroup = new ConcurrentHashMap<Integer, Object>();

	private Stack<ArenaBattleRecordPushBO> battlePushPool = new Stack<ArenaBattleRecordPushBO>();

	@Autowired
	private ArenaDao arenaDao;

	/**
	 * 是否并发处理
	 */
	private boolean concurrency = true;

	@Autowired
	private SystemArenaRewardDao systemArenaRewardDao;
	
	@Autowired
	private BattleService battleService;

	@Autowired
	private HeroService heroService;

	@Autowired
	private UserService userService;

	@Autowired
	private ToolService toolService;

	@Autowired
	private UserDao userDao;

	@Autowired
	private CommandDao commandDao;

	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;

	@Autowired
	private EventServcie eventServcie;
	
	@Autowired
	private ActivityTaskService activityTaskService;

	private boolean started = false;

	/**
	 * 测试用的用户ID
	 */
	private List<String> userIds = null;

	/**
	 * CD时间
	 */
	private final static int cdInterval = 1000 * 5;

	/**
	 * 推送排名cd时间
	 */
	private final static int pushRankInterval = 1000 * 10;

	/**
	 * 推送战斗记录cd时间
	 */
	private final static int pushBattleRecordInterval = 1000 * 10;

	/**
	 * 是否暂停
	 */
	private boolean pause = false;

	/**
	 * 开始时间
	 */
	private Date startTime;

	/**
	 * 结束时间
	 */
	private Date endTime;

	/**
	 * 状态
	 */
	private int status = ArenaStatus.NOT_STARTED;

	/**
	 * 获取状态描述
	 * 
	 * @param status
	 */
	private String getStatusDesc(int status) {

		switch (status) {
		case ArenaStatus.NOT_STARTED:
			return "活动未开始(" + status + ")";
		case ArenaStatus.STARTED:
			return "活动进行中(" + status + ")";
		case ArenaStatus.END:
			return "活动已经结束(" + status + ")";
		default:
			return "未知状态(" + status + ")";
		}

	}

	@Override
	public boolean enter(String userId) {

		Date now = new Date();
		if (now.before(this.getStartTime())) {
			throw new ServiceException(ENTER_ARENA_NOT_START, "活动未开始");
		} else if (now.after(this.getEndTime())) {
			throw new ServiceException(ENTER_ARENA_HAS_FINISH, "活动已经结束");
		}

		logger.debug("用户进入百人斩活动.userId[" + userId + "]");

		ArenaReg arenaReg = this.arenaDao.getByUserId(userId);

		List<UserHeroBO> userHeroBOList = this.heroService.getUserHeroList(userId, 1);

		int systemHeroId = 0;
		if (!userHeroBOList.isEmpty()) {
			systemHeroId = userHeroBOList.get(0).getSystemHeroId();
		}

		if (arenaReg == null) {

			User user = this.userService.get(userId);

			arenaReg = new ArenaReg();
			arenaReg.setCreatedTime(now);
			arenaReg.setUpdatedTime(DateUtils.add(now, Calendar.MILLISECOND, cdInterval * -1));
			arenaReg.setEncourageTimes(0);
			arenaReg.setStatus(ArenaRegStatus.STATUS_END);
			arenaReg.setMaxWinCount(0);
			arenaReg.setLoseTimes(0);
			arenaReg.setUsername(user.getUsername());
			arenaReg.setWinCount(0);
			arenaReg.setUserLevel(user.getLevel());
			arenaReg.setUserId(userId);
			arenaReg.setSystemHeroId(systemHeroId);
		}

		int ability = this.userService.getUserPower(userHeroBOList);

		arenaReg.setAbility(ability);

		// 推送信息
		this.arenaDao.add(arenaReg);

		return false;
	}

	/**
	 * 推送用户信息
	 * 
	 * @param userId
	 */
	private void pushUser(String userId) {

		this.push(userId, CommandType.COMMAND_ARENA_PUSH_USER_INFO);
	}

	/**
	 * 推送排行榜
	 * 
	 * @param userId
	 */
	private void pushRank(String userId) {
		this.push(userId, CommandType.COMMAND_ARENA_PUSH_RANK_LIST);
	}

	/**
	 * 推送战报
	 * 
	 * @param userId
	 */
	private void pushBattle(String userId, Map<String, String> params) {

		params.put("userId", userId);

		Command command = new Command();
		command.setCommand(CommandType.COMMAND_ARENA_PUSH_BATTLE);
		command.setType(CommandType.PUSH_USER);
		command.setPriority(Priority.HIGH);
		command.setParams(params);

		commandDao.add(command);
	}

	/**
	 * 推送战斗
	 * 
	 * @param attackUserId
	 * @param attackUsername
	 * @param defenseUserId
	 * @param defenseUsername
	 * @param copper
	 * @param falg
	 * @param report
	 */
	public void pushBattle(String attackUserId, String attackUsername, String defenseUserId, String defenseUsername, int winCopper, int flag, String report, int loseCopper) {

		Map<String, String> params = new HashMap<String, String>();
		params.put("attackUserId", attackUserId);
		params.put("attackUsername", attackUsername);
		params.put("defenseUserId", defenseUserId);
		params.put("defenseUsername", defenseUsername);
		params.put("flag", String.valueOf(flag));
		params.put("report", report);
		params.put("copper", String.valueOf(winCopper));

		if (flag == 1) {
			// 推战斗给进攻方
			this.pushBattle(attackUserId, params);
			this.pushUser(attackUserId);
			this.pushRank(attackUserId);

			// 推战斗给防守方,失败了为赢了的30%
			params.put("copper", String.valueOf((int) (loseCopper)));
			this.pushBattle(defenseUserId, params);
			this.pushUser(defenseUserId);
			this.pushRank(defenseUserId);

		} else {
			// 推战斗给防守方
			this.pushBattle(defenseUserId, params);
			this.pushUser(defenseUserId);
			this.pushRank(defenseUserId);

			// 推战斗给进攻方,失败了为赢了的30%
			params.put("copper", String.valueOf((int) (loseCopper)));
			this.pushBattle(attackUserId, params);
			this.pushUser(attackUserId);
			this.pushRank(attackUserId);
		}

	}

	/**
	 * 推送奖励信息
	 * 
	 * @param userId
	 */
	private void pushReward(String userId) {
		this.push(userId, CommandType.COMMAND_ARENA_PUSH_REWARD);
	}

	private void push(String userId, int commandType) {

		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", userId);

		Command command = new Command();
		command.setCommand(commandType);
		command.setType(CommandType.PUSH_USER);
		command.setPriority(Priority.HIGH);
		command.setParams(params);

		commandDao.add(command);

	}

	/**
	 * 推送战斗记录
	 * 
	 * @param userId
	 * @param params
	 */
	private void pushBattleRecord(String userId, Map<String, String> params) {

		params.put("userId", userId);

		Command command = new Command();
		command.setCommand(CommandType.COMMAND_ARENA_PUSH_BATTLE_LOG);
		command.setType(CommandType.PUSH_USER);
		command.setPriority(Priority.NORMAL);
		command.setParams(params);

		commandDao.add(command);
	}

	@Override
	public boolean quit(String userId) {

		ArenaReg arenaReg = this.arenaDao.getByUserId(userId);

		if (arenaReg == null) {
			return false;
		}

		arenaReg.setStatus(ArenaRegStatus.STATUS_QUIT);

		this.arenaDao.add(arenaReg);

		return true;
	}

	/**
	 * 活动开始
	 */
	private void start() {

		this.status = ArenaStatus.STARTED;

		this.arenaDao.cleanData();

	}

	/**
	 * 活动结束(推送奖励)
	 */
	private void end() {

		this.status = ArenaStatus.END;

		if (concurrency) {

			// 奖励结算
			Runnable task = new Runnable() {

				@Override
				public void run() {

					Collection<ArenaReg> arenaRegList = arenaDao.getArenaRegList();
					for (ArenaReg arenaReg : arenaRegList) {
						pushReward(arenaReg.getUserId());
					}
				}
			};

			this.taskExecutor.execute(task);

		} else {

			Collection<ArenaReg> arenaRegList = arenaDao.getArenaRegList();
			for (ArenaReg arenaReg : arenaRegList) {
				pushReward(arenaReg.getUserId());
			}

		}
		
//		dispatchMessage();

	}

	private void dispatchMessage() {
		String rank1 = "";
		String username1 = "";
		String rank2 = "";
		String username2 = "";
		String rank3 = "";
		String username3 = "";

		List<ArenaReg> list = arenaDao.getRankList();
		if (list != null && list.size() > 0) {
			ArenaReg reg = new ArenaReg();
			for (int i = 0; i < list.size(); i++) {
				reg = list.get(i);
				switch (i) {
				case 0:
					rank1 = "第一名";
					username1 = reg.getUsername();
					break;
				case 1:
					rank2 = "第二名";
					username2 = reg.getUsername();
					break;
				case 2:
					rank3 = "第三名";
					username3 = reg.getUsername();
					break;
				}
				if (i == 2) {
					break;
				}
			}
			ArenaEvent event = new ArenaEvent(rank1, username1, rank2, username2, rank3, username3);
			eventServcie.dispatchEvent(event);
		}
	}

	@Override
	public boolean encourage(String userId) {

		ArenaReg arenaReg = this.arenaDao.getByUserId(userId);

		int encourageTimes = arenaReg.getEncourageTimes();
		if (encourageTimes >= 10) {
			String message = "鼓舞失败，已经超过最大鼓舞次数.userId[" + userId + "]";
			throw new ServiceException(ENCOURAGE_OVER_MAX_TIMES, message);
		}
		User user = userService.get(userId);
		if (!this.userService.reduceGold(userId, 20, ToolUseType.REDUCE_ARENA_ENCOURAGE, user.getLevel())) {
			String message = "鼓舞失败，金币不足.userId[" + userId + "]";
			throw new ServiceException(ENCOURAGE_NOT_ENOUHT_MONEY, message);
		}

		encourageTimes += 1;
		arenaReg.setEncourageTimes(encourageTimes);

		this.arenaDao.add(arenaReg);

		return true;
	}

	public boolean startMatcher(String userId) {

		ArenaReg arenaReg = this.arenaDao.getByUserId(userId);

		if (arenaReg == null) {
			return false;
		}

		if (arenaReg.getStatus() != ArenaRegStatus.STATUS_WAIT) {// 已经是等待了则不管
			arenaReg.setUpdatedTime(DateUtils.add(new Date(), Calendar.MILLISECOND, cdInterval * 1));
			arenaReg.setStatus(ArenaRegStatus.STATUS_WAIT);
		}

		this.arenaDao.add(arenaReg);

		return true;
	}

	public Date getStartTime() {
		if (startTime == null) {
			if (Config.ins().isDebug()) {
				startTime = new Date();
			} else {
				startTime = DateUtils.str2Date(DateUtils.getDate() + " 20:00:00");
			}
		}
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		if (endTime == null) {
			if (Config.ins().isDebug()) {
				endTime = DateUtils.add(new Date(), Calendar.MINUTE, 30);
			} else {
				endTime = DateUtils.str2Date(DateUtils.getDate() + " 20:30:00");
			}

		}
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * 状态监控方法
	 */
	private void statusCheck() {

		Date now = new Date();

//		logger.debug("状态切换线程.status[" + getStatusDesc(this.status) + "]");

		if (this.status == ArenaStatus.NOT_STARTED) {

//			logger.debug("百人斩活动未开始,now[" + DateUtils.getTime() + "], start time[" + DateUtils.getTime(this.getStartTime().getTime()) + "]");

			if (now.after(this.getStartTime())) {
				logger.debug("活动开始");
				this.start();

			}

		} else if (this.status == ArenaStatus.STARTED) {

//			logger.debug("百人斩活动进行中,now[" + DateUtils.getTime() + "], end time[" + DateUtils.getTime(this.getEndTime().getTime()) + "]");

			if (now.after(this.getEndTime())) {
				logger.debug("活动结束");
				this.end();
			}

		} else if (this.status == ArenaStatus.END) {

//			logger.debug("百人斩活动已经结束,now[" + DateUtils.getTime() + "]");

			// 已经不是同一天了，到了第二天
			if (!DateUtils.isSameDay(now, this.getEndTime())) {

				logger.debug("进入第二天");
				this.status = ArenaStatus.NOT_STARTED;
				this.startTime = DateUtils.addDays(this.getStartTime(), 1);
				this.endTime = DateUtils.addDays(this.getEndTime(), 1);
			}

		}

	}

	/**
	 * 主工作方法
	 */
	private int execute() {

		if (this.status != ArenaStatus.STARTED) {
//			logger.debug("活动未开始");
			return 5000;
		}

		if (this.pause == true) {// 暂停
//			logger.debug("活动暂停");
			return 5000;
		}

		List<Integer> winCountList = this.arenaDao.getWinCountList();

//		logger.debug("连胜组.[" + winCountList + "]");

		if (winCountList.isEmpty()) {
			return 2000;
		}

		for (int winCount : winCountList) {
			this.handle(winCount);
		}

		return 1000;

	}

	/**
	 * 处理某个分组的比赛
	 * 
	 * @param winCount
	 */
	private void handle(final int winCount) {

		if (workWinGroup.containsKey(winCount)) {
//			logger.debug("当前分组正在处理.winCount[" + winCount + "]");
			return;
		}

		workWinGroup.put(winCount, 1);

		if (this.concurrency) {

			Runnable task = new Runnable() {

				@Override
				public void run() {
					_handle(winCount);

					if (workWinGroup.containsKey(winCount)) {
//						logger.debug("当前分组处理完成.winCount[" + winCount + "]");
						workWinGroup.remove(winCount);
					}

				}
			};

			taskExecutor.execute(task);

		} else {

			_handle(winCount);

			if (workWinGroup.containsKey(winCount)) {
//				logger.debug("当前分组处理完成.winCount[" + winCount + "]");
				workWinGroup.remove(winCount);
			}

		}
	}

	private void _handle(int winCount) {

		List<ArenaReg> arenaRegList = this.arenaDao.getByWinCount(winCount, ArenaRegStatus.STATUS_WAIT);

		if (arenaRegList.isEmpty()) {
			return;
		}

//		logger.debug("处理连胜组战斗匹配,threadName[" + Thread.currentThread().getName() + "]winCount[" + winCount + "], size[" + arenaRegList.size() + "]");

		// 按战力排序
		this.sortArenaRegList(arenaRegList);

		int max = arenaRegList.size() / 2 + 1;

		for (int i = 0; i < max; i++) {

			ArenaReg arenaRegA = arenaRegList.get(i);

			if (!this.isCanMatcher(arenaRegA)) {
				continue;
			}

			arenaRegA.setStatus(ArenaRegStatus.STATUS_MATCH);

			ArenaReg arenaRegB = this.matcher(arenaRegList, i, winCount);
			if (arenaRegB != null) {
				// 比赛处理
				arenaRegA.setStatus(ArenaRegStatus.STATUS_DOING);
				arenaRegA.setUpdatedTime(new Date());

				arenaRegB.setStatus(ArenaRegStatus.STATUS_DOING);
				arenaRegB.setUpdatedTime(new Date());

				this.handleMatch(arenaRegA, arenaRegB);
			} else {
				// 轮空处理
//				logger.debug("轮空.userId[" + arenaRegA.getUserId() + "]");
				arenaRegA.setStatus(ArenaRegStatus.STATUS_WAIT);
			}

		}
	}

	private void sortArenaRegList(List<ArenaReg> arenaRegList) {

		// 排序
		Collections.sort(arenaRegList, new Comparator<ArenaReg>() {

			@Override
			public int compare(ArenaReg o1, ArenaReg o2) {
				return o1.getAbility() - o2.getAbility();
			}

		});

	}

	/**
	 * 匹配
	 * 
	 * @param arenaRegList
	 * @param startIndex
	 * @param winCount
	 * @return
	 */
	private ArenaReg matcher(List<ArenaReg> arenaRegList, int startIndex, int winCount) {

		// this.sortArenaRegList(arenaRegList);

		ArenaReg arenaRegB = null;

		// 从后往前匹配
		for (int j = arenaRegList.size() - 1; j > startIndex; j--) {

			arenaRegB = arenaRegList.get(j);

			if (this.isCanMatcher(arenaRegB)) {
				arenaRegB.setStatus(ArenaRegStatus.STATUS_DOING);
				return arenaRegB;
			}
		}

		boolean go = true;
		int nextWinCount = winCount;

		// 如果当前组别匹配不到，往下一个组匹配
		while (go) {

			nextWinCount -= 1;

			if (nextWinCount < 0) {
				break;
			}

			List<ArenaReg> nextWinCountArenaRegList = this.arenaDao.getByWinCount(nextWinCount, ArenaRegStatus.STATUS_WAIT);

			// 从前往后匹配
			for (int j = 0; j < nextWinCountArenaRegList.size(); j++) {

				arenaRegB = nextWinCountArenaRegList.get(j);

				if (this.isCanMatcher(arenaRegB)) {
					arenaRegB.setStatus(ArenaRegStatus.STATUS_DOING);
					return arenaRegB;
				}
			}

		}

		return null;
	}

	/**
	 * 是否可以匹配
	 * 
	 * @param arenaReg
	 * @return
	 */
	private boolean isCanMatcher(ArenaReg arenaReg) {

		if (arenaReg.getStatus() != ArenaRegStatus.STATUS_WAIT) {
//			logger.debug("用户不是等待状态.userId[" + arenaReg.getUserId() + "], username[" + arenaReg.getUsername() + "], status[" + arenaReg.getStatus() + "]");
			return false;
		}

		if (arenaReg.getUpdatedTime().getTime() + cdInterval > System.currentTimeMillis()) {
//			logger.debug("用户CD时间未到,userId[" + arenaReg.getUserId() + "], updateTime[" + DateUtils.getTimeStr(arenaReg.getUpdatedTime()) + "], now[" + DateUtils.getTime() + "]");
			return false;
		}

		return true;
	}

	/**
	 * 处理百人斩分组比赛
	 * 
	 * @param arenaRegList
	 */
	private void handleMatch(final ArenaReg arenaRegA, final ArenaReg arenaRegB) {

//		logger.debug("开始比赛.usernameA[" + arenaRegA.getUsername() + "], usernameB[" + arenaRegB.getUsername() + "]");

		if (this.concurrency) {

			Runnable task = new Runnable() {

				@Override
				public void run() {
					_handleMatch(arenaRegA, arenaRegB);
				}
			};

			taskExecutor.execute(task);

		} else {
			_handleMatch(arenaRegA, arenaRegB);
		}
	}

	/**
	 * 
	 * @param arenaRegA
	 * @param arenaRegB
	 */
	private void _handleMatchEnd(ArenaReg arenaRegA, ArenaReg arenaRegB, int flag, String report, Map<String, Integer> lifeMap) {

		String winUserId = null;
		String winUsername;
		String loseUserId;
		String loseUsername;
		int winUserWinCount;
		int loseUserWinCount;
		int winUserLevel = 0;
		int loseUserLevel = 0;

		// 进攻方赢
		if (flag == 1) {

			winUserId = arenaRegA.getUserId();
			winUsername = arenaRegA.getUsername();
			winUserLevel = arenaRegA.getUserLevel();
			winUserWinCount = arenaRegA.getWinCount() + 1;

			loseUserId = arenaRegB.getUserId();
			loseUsername = arenaRegB.getUsername();
			loseUserWinCount = arenaRegB.getWinCount();
			loseUserLevel = arenaRegB.getUserLevel();

			this.updateWin(arenaRegA, lifeMap);
			this.updateLose(arenaRegB);

		} else {

			flag = 0;
			winUserId = arenaRegB.getUserId();
			winUsername = arenaRegB.getUsername();
			winUserLevel = arenaRegB.getUserLevel();
			winUserWinCount = arenaRegB.getWinCount() + 1;

			loseUserId = arenaRegA.getUserId();
			loseUsername = arenaRegA.getUsername();
			loseUserWinCount = arenaRegA.getWinCount();
			loseUserLevel = arenaRegA.getUserLevel();

			this.updateWin(arenaRegB, lifeMap);
			this.updateLose(arenaRegA);

		}

		int winCopper = (int) ((5 * winUserLevel + 1500) * (1 + Math.pow(winUserWinCount, 0.5) * 0.6) * 6);
		int loseCopper = (int) (((5 * loseUserLevel + 1500) * (1 + Math.pow(loseUserWinCount, 0.5) * 0.6) * 6) * 0.5);

		// 推送战斗
		this.pushBattle(arenaRegA.getUserId(), arenaRegA.getUsername(), arenaRegB.getUserId(), arenaRegB.getUsername(), winCopper, flag, report, loseCopper);

		// 发送战报
		this.pushBattleRecord(winUserId, winUsername, loseUserId, loseUsername, winUserWinCount, loseUserWinCount);

	}

	/**
	 * 更新为胜利
	 * 
	 * @param arenReg
	 * @param lifeMap
	 */
	private void updateWin(ArenaReg arenaReg, Map<String, Integer> lifeMap) {

		String userId = arenaReg.getUserId();

		int maxWinCount = arenaReg.getMaxWinCount();
		int winCount = arenaReg.getWinCount();
		int winTimes = arenaReg.getWinTimes();
		int totalCopper = arenaReg.getTotalCopper();
		int userLevel = arenaReg.getUserLevel();

		// （5*主公当前等级+1500）*（1+(当前连斩数^0.5)*0.6) * 6
		int gainMoney = (int) ((5 * userLevel + 1500) * (1 + Math.pow(winCount + 1, 0.5) * 0.6) * 6);
		totalCopper += gainMoney;

		winCount += 1;
		winTimes += 1;
		if (maxWinCount < winCount) {
			maxWinCount = winCount;
		}
		arenaReg.setMaxWinCount(maxWinCount);
		arenaReg.setWinCount(winCount);
		arenaReg.setWinTimes(winTimes);
		arenaReg.setTotalCopper(totalCopper);

		// 给银币
		this.userService.addCopper(userId, gainMoney, ToolUseType.ADD_ARENA_WIN_DROP);
		// 推送信息
		this.userService.pushUser(userId);

		// 保留残血
		Map<String, ArenaHero> heroMap = this.arenaDao.getUserArenaHero(userId);
		if (heroMap != null) {

			for (Entry<String, ArenaHero> entry : heroMap.entrySet()) {

				String userHeroId = entry.getKey();
				ArenaHero arenaHero = entry.getValue();

				long life = arenaHero.getLife();

				if (lifeMap.containsKey(userHeroId)) {

					int newLife = lifeMap.get(userHeroId);
					if (newLife < life) {

						logger.debug("战斗后用户武将血量减少,userId[" + userId + "], userHeroId[" + userHeroId + "], life[" + life + "], newLife[" + newLife + "]");

						arenaHero.setLife(newLife);
						this.arenaDao.addArenaHero(userId, userHeroId, arenaHero);
					}

				}

			}
		}

		boolean update = this.arenaDao.add(arenaReg);
		if (update) {
			Collection<ArenaReg> arenaRegList = arenaDao.getArenaRegList();
			for (ArenaReg reg : arenaRegList) {
				if (reg.getStatus() == ArenaRegStatus.STATUS_QUIT) {
					continue;
				}

				// 限制同一个用户的推送次数
				if (reg.getLastPushRankTime() + pushRankInterval > System.currentTimeMillis()) {
					continue;
				} else {
					reg.setLastPushRankTime(System.currentTimeMillis());
				}

				this.pushRank(reg.getUserId());
			}
		}
	}

	/**
	 * 更新为失败
	 * 
	 * @param arenaReg
	 */
	private void updateLose(ArenaReg arenaReg) {

		String userId = arenaReg.getUserId();

		// 给失败者银币
		int winCount = arenaReg.getWinCount();
		int userLevel = arenaReg.getUserLevel();
		int totalCopper = arenaReg.getTotalCopper();

		// 输了
		int loseTimes = arenaReg.getLoseTimes();
		loseTimes += 1;
		arenaReg.setLoseTimes(loseTimes);
		arenaReg.setWinCount(0);

		this.arenaDao.add(arenaReg);

		// 失败方银币收益公式：((5*主公当前等级+1500)*(1+(当前连斩数^0.5)*0.6) * 6)*50%
		int gainMoney = (int) ((5 * userLevel + 1500) * (1 + Math.pow(winCount, 0.5) * 0.6) * 6 * 0.5);
		totalCopper += gainMoney;

		arenaReg.setTotalCopper(totalCopper);

		// 给银币
		this.userService.addCopper(userId, gainMoney, ToolUseType.ADD_ARENA_LOSE_DROP);

		// 推送信息
		this.userService.pushUser(userId);

		// 清掉保存的武将血量信息(满血复活)
		this.arenaDao.resetUserHero(userId);
	}

	/*
	 * 发消息
	 */
	private void pushBattleRecord(final String winUserId, String winUsername, final String loseUserId, String loseUsername, final int winUserWinCount, final int loseUserWinCount) {

		final Map<String, String> params = new HashMap<String, String>();
		params.put("attackUsername", winUsername);
		params.put("defenseUsername", loseUsername);
		params.put("attackUserId", winUserId);
		params.put("defenseUserId", loseUserId);

		int winCount;
		int type;

		if (loseUserWinCount == 0 || winUserWinCount > loseUserWinCount) {
			winCount = winUserWinCount;
			if (winCount == 1) {
				type = ArenaRecordType.TYPE_NORMAL_WIN;
			} else {
				type = ArenaRecordType.TYPE_NORMAL_WIN_AGAIN;
			}
		} else {
			winCount = loseUserWinCount;
			type = ArenaRecordType.TYPE_NORMAL_STOP_WIN_AGAIN;
		}

		params.put("winCount", String.valueOf(winCount));
		params.put("type", String.valueOf(type));

		if (this.concurrency) {

			Runnable task = new Runnable() {

				@Override
				public void run() {

					int regCount = arenaDao.getRegCount();

					Collection<ArenaReg> arenaRegList = arenaDao.getArenaRegList();
					for (ArenaReg arenaReg : arenaRegList) {

						if (arenaReg.getStatus() == ArenaRegStatus.STATUS_QUIT) {// 用户已经退出界面
							logger.debug("用户已经退出界面,不需要");
							continue;
						}

						if (checkSend(arenaReg, winUserId, loseUserId, winUserWinCount, loseUserWinCount, regCount)) {
							ArenaBattleRecordPushBO bo = new ArenaBattleRecordPushBO();
							bo.setUserId(arenaReg.getUserId());
							bo.setParams(params);
							battlePushPool.add(bo);
							arenaReg.setLastPushBattleRecordTime(System.currentTimeMillis());
						}
					}
				}
			};

			this.taskExecutor.execute(task);
		} else {

			Collection<ArenaReg> arenaRegList = arenaDao.getArenaRegList();
			for (ArenaReg arenaReg : arenaRegList) {
				ArenaBattleRecordPushBO bo = new ArenaBattleRecordPushBO();
				bo.setUserId(arenaReg.getUserId());
				bo.setParams(params);
				battlePushPool.add(bo);
			}

		}
	}

	private boolean checkSend(ArenaReg arenaReg, String winUserId, String loseUserId, int winUserWinCount, int loseUserWinCount, int regCount) {

		String userId = arenaReg.getUserId();

		if (StringUtils.equalsIgnoreCase(userId, winUserId) || StringUtils.equalsIgnoreCase(userId, loseUserId)) {
			return true;
		}

		if (winUserWinCount > 10 || loseUserWinCount > 10) {
			return true;
		}

		// 限制同一个用户的推送战报次数
		if (arenaReg.getLastPushBattleRecordTime() + pushBattleRecordInterval > System.currentTimeMillis()) {
			return false;
		} else if (arenaReg.getLastPushBattleRecordTime() + 1000 * 60 < System.currentTimeMillis()) {// 60秒必推一次
			return true;
		}

		int sendRate = 1;
		if (regCount < 100) {
			sendRate = 2;
		} else if (regCount < 50) {
			sendRate = 5;
		}

		int randInd = 100;
		if (this.battlePushPool.size() > 1000) {
			randInd = 2;
		} else if (this.battlePushPool.size() > 500) {
			randInd = 4;
		} else if (this.battlePushPool.size() > 300) {
			randInd = 6;
		} else if (this.battlePushPool.size() > 100) {
			randInd = 8;
		} else {
			randInd = 10;
		}

		randInd = randInd * sendRate;

		return RandomUtils.nextInt(100) <= randInd;
	}

	private int pushBattleRecord() {

		if (this.status != ArenaStatus.STARTED) {
			return 2000;
		}

		while (this.battlePushPool.size() > 0) {

			ArenaBattleRecordPushBO bo = this.battlePushPool.pop();
			this.pushBattleRecord(bo.getUserId(), bo.getParams());

		}

		return 1000;

	}

	/**
	 * 设置武将初始血量
	 * 
	 * @param userId
	 * @param attackHeroBOList
	 */
	private List<BattleHeroBO> setHeroLife(String userId, List<BattleHeroBO> attackHeroBOList) {

		List<BattleHeroBO> list = new ArrayList<BattleHeroBO>();

		for (BattleHeroBO battleHeroBO : attackHeroBOList) {
			String userHeroId = battleHeroBO.getUserHeroId();
			ArenaHero arenaHero = this.arenaDao.getArenaHero(userId, userHeroId);
			if (arenaHero != null) {// 以前有记录,使用以前的剩余血量
				long life = arenaHero.getLife();
				if (life == 0) {
					life = 1;
				}
				battleHeroBO.setLife(life);
			} else {// 否则把血量存起来
				arenaHero = new ArenaHero();
				arenaHero.setMaxLife(battleHeroBO.getLife());
				arenaHero.setLife(battleHeroBO.getLife());
				this.arenaDao.addArenaHero(userId, userHeroId, arenaHero);
			}

			if (battleHeroBO.getLife() > 0) {
				list.add(battleHeroBO);
			}
		}

		return list;

	}

	private void _handleMatch(final ArenaReg arenaRegA, final ArenaReg arenaRegB) {

		final Map<String, Integer> lifeMap = new HashMap<String, Integer>();
		final Map<String, String> heroPosMap = new HashMap<String, String>();

		// 进攻方信息
		BattleBO attack = new BattleBO();
		// 鼓舞加成
		attack.setAddRatio(arenaRegA.getEncourageTimes() * 0.1);
		final User attackUser = userService.get(arenaRegA.getUserId());
		List<BattleHeroBO> attackHeroBOList = this.heroService.getUserBattleHeroBOList(arenaRegA.getUserId());
		// 设置初始血量
		this.setHeroLife(attackUser.getUserId(), attackHeroBOList);

		for (BattleHeroBO battleHeroBo : attackHeroBOList) {
			heroPosMap.put(battleHeroBo.getUserHeroId(), "L_a" + battleHeroBo.getPos());
			lifeMap.put(battleHeroBo.getUserHeroId(), 0);
		}
		attack.setUserLevel(attackUser.getLevel());
		attack.setBattleHeroBOList(attackHeroBOList);

		// 防守方信息
		User defenseUser = userService.get(arenaRegB.getUserId());
		BattleBO defense = new BattleBO();
		// 鼓舞加成
		defense.setAddRatio(arenaRegB.getEncourageTimes() * 0.1);
		List<BattleHeroBO> defenseHeroBOList = this.heroService.getUserBattleHeroBOList(defenseUser.getUserId());
		// 设置初始血量
		this.setHeroLife(defenseUser.getUserId(), defenseHeroBOList);

		defense.setBattleHeroBOList(defenseHeroBOList);
		for (BattleHeroBO battleHeroBo : defenseHeroBOList) {
			heroPosMap.put(battleHeroBo.getUserHeroId(), "L_d" + battleHeroBo.getPos());
			lifeMap.put(battleHeroBo.getUserHeroId(), 0);
		}
		attack.setUserLevel(defenseUser.getLevel());
		
		// 活跃度奖励
		activityTaskService.updateActvityTask(arenaRegA.getUserId(), ActivityTargetType.ARENA_BATTLE, 1);
		
		// 活跃度奖励
		activityTaskService.updateActvityTask(arenaRegB.getUserId(), ActivityTargetType.ARENA_BATTLE, 1);

		battleService.fight(attack, defense, 5, new EventHandle() {

			@SuppressWarnings("unchecked")
			@Override
			public boolean handle(Event event) {

				int flag = event.getInt("flag");
				String report = event.getString("report");

				// 残血计算
				Map<String, Integer> lifeData = (Map<String, Integer>) event.getObject("life");
				for (Entry<String, Integer> entry : lifeMap.entrySet()) {
					String userHeroId = entry.getKey();
					String posKey = heroPosMap.get(userHeroId);
					if (lifeData.containsKey(posKey)) {
						lifeMap.put(userHeroId, lifeData.get(posKey));
					}
				}

				// 处理比赛结束
				_handleMatchEnd(arenaRegA, arenaRegB, flag, report, lifeMap);

				return true;
			}
		});
		
	}

	public void init() {

		if (started || !Config.ins().isOpenArena()) {
			return;
		}

		if (!Config.ins().isGameServer()) {
			return;
		}

		started = true;

		// 主工作线程
		new Thread(new Runnable() {

			public void run() {
				while (true) {

					int sleepTimes = 1000;

					try {
						sleepTimes = execute();
					} catch (Throwable t) {

						logger.error(t.getMessage(), t);

					}

					if (sleepTimes > 0) {

						try {
//							logger.debug("主工作线程进入休眠.times[" + sleepTimes + "]");
							Thread.sleep(sleepTimes);
//							logger.debug("主工作线程休眼醒来.times[" + sleepTimes + "]");
						} catch (InterruptedException ie) {
//							logger.error(ie.getMessage(), ie);
						}
					}

				}
			}

		}).start();

		// 状态切换线程
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

		// 推送战斗记录
		new Thread(new Runnable() {

			public void run() {
				while (true) {
					try {
						pushBattleRecord();
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

		if (Config.ins().isDebug()) {
			// .//test();
		}

	}

	private void test() {

		// 状态切换线程
		new Thread(new Runnable() {

			public void run() {

				try {
					_test();
				} catch (Throwable t) {
					logger.error(t.getMessage(), t);
				}

				try {
					Thread.sleep(1000);
				} catch (InterruptedException ie) {
					logger.error(ie.getMessage(), ie);
				}

			}

		}).start();
	}

	private List<String> getTestUserIds() {
		if (userIds == null) {
			userIds = new ArrayList<String>();
			List<User> userList = this.userDao.listOrderByLevelDesc(0, 10000);
			for (User user : userList) {
				if (user.getLevel() <= 50) {
					userIds.add(user.getUserId());
				}
			}
		}

		return userIds;
	}

	private void _test() throws InterruptedException {

		int init = 0;

		List<String> userIds = this.getTestUserIds();

		while (true) {

			if (this.status == ArenaStatus.STARTED) {

				if (init == 0) {

					for (String userId : userIds) {
						this.enter(userId);
					}

					init = 1;
				}

				for (String userId : userIds) {
					this.startMatcher(userId);
				}
			}

			Thread.sleep(5000);
		}

	}

	@Override
	public List<ArenaRankBO> getRankList() {

		List<ArenaReg> arenaRankList = this.arenaDao.getRankList();

		List<ArenaRankBO> arenaRankBOList = new ArrayList<ArenaRankBO>();

		for (ArenaReg arenaReg : arenaRankList) {

			ArenaRankBO arenaRankBO = new ArenaRankBO();
			arenaRankBO.setUsername(arenaReg.getUsername());
			arenaRankBO.setUserHeroLevel(0);
			arenaRankBO.setSystemHeroId(arenaReg.getSystemHeroId());
			arenaRankBO.setMaxWinCount(arenaReg.getMaxWinCount());

			arenaRankBOList.add(arenaRankBO);
		}

		return arenaRankBOList;
	}

	@Override
	public ArenaRegBO getRegBO(String userId) {

		ArenaReg arenaReg = this.arenaDao.getByUserId(userId);
		if (arenaReg != null) {

			ArenaRegBO arenaRebBO = new ArenaRegBO();
			arenaRebBO.setEncourageTimes(arenaReg.getEncourageTimes());
			arenaRebBO.setMaxWinCount(arenaReg.getMaxWinCount());
			arenaRebBO.setWinCount(arenaReg.getWinCount());
			arenaRebBO.setWinTimes(arenaReg.getWinTimes());

			return arenaRebBO;
		}

		return null;
	}

	@Override
	public ArenaRewardBO giveReward(String userId) {

		ArenaReg arenaReg = this.arenaDao.getByUserId(userId);

		if (arenaReg != null) {

			ArenaRewardBO arenaRewardBO = new ArenaRewardBO();
			arenaRewardBO.setWinTimes(arenaReg.getWinTimes());
			arenaRewardBO.setLoseTiems(arenaReg.getLoseTimes());

			int rank = this.arenaDao.getRank(userId);
			CommonDropBO dropBO;
			if (rank >= 1 && rank <= 5) {
				dropBO = getDropBO(userId, rank);

			} else {
				// 排名在第一名到第五名之外的玩家，奖励数据表中的 rank 值是9999
				dropBO = getDropBO(userId, 9999);
			}
			
			arenaRewardBO.setDropTools(dropBO);
			arenaRewardBO.setCopperNum(arenaReg.getTotalCopper());
			arenaRewardBO.setMaxWinCount(arenaReg.getMaxWinCount());
			return arenaRewardBO;
		}

		return null;
	}

	@Override
	public ArenaConfigBO getConfigBO() {

		ArenaConfigBO bo = new ArenaConfigBO();
		bo.setStartTime(this.getStartTime().getTime());
		bo.setEndTime(this.getEndTime().getTime());

		return bo;
	}

	@Override
	public void execute(String cmd) {
		if (StringUtils.equalsIgnoreCase(cmd, "PAUSE")) {
			this.pause = true;
		} else if (this.pause || StringUtils.equalsIgnoreCase(cmd, "CONTINUE")) {
			this.pause = false;
		}
	}
	
	private CommonDropBO getDropBO(String userId, int rank) {
		
		SystemArenaReward reward = systemArenaRewardDao.getRewardByRank(rank);
		List<DropToolBO> dropTools = DropToolHelper.parseDropTool(reward.getReward());

		CommonDropBO commonDropBO = new CommonDropBO();
		for (DropToolBO dropToolBO : dropTools) {
			List<DropToolBO> dropToolBOList = toolService.giveTools(userId, dropToolBO.getToolType(), dropToolBO.getToolId(), dropToolBO.getToolNum(), ToolUseType.ARENA_RANK_REWARD);
			for (DropToolBO db : dropToolBOList) {
				toolService.appendToDropBO(userId, commonDropBO, db);
			}
		}
		
		return commonDropBO;
	}

}
