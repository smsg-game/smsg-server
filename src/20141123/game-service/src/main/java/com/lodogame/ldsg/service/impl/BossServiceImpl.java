/**
 * BossServiceImpl.java
 *
 * Copyright 2013 Easou Inc. All Rights Reserved.
 *
 */

package com.lodogame.ldsg.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.BossDao;
import com.lodogame.game.dao.BossTeamDao;
import com.lodogame.game.dao.CommandDao;
import com.lodogame.game.dao.ConfigDataDao;
import com.lodogame.game.dao.ForcesDropToolDao;
import com.lodogame.game.dao.SystemForcesDao;
import com.lodogame.game.dao.SystemSceneDao;
import com.lodogame.game.dao.SystemUserLevelDao;
import com.lodogame.game.dao.UserBossDao;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.ldsg.bo.BattleBO;
import com.lodogame.ldsg.bo.BattleHeroBO;
import com.lodogame.ldsg.bo.BossBattleReportBO;
import com.lodogame.ldsg.bo.BossTeamBO;
import com.lodogame.ldsg.bo.BossTeamDetailBO;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.DropToolBO;
import com.lodogame.ldsg.bo.UserBO;
import com.lodogame.ldsg.bo.UserBossBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.config.Config;
import com.lodogame.ldsg.constants.ActivityTargetType;
import com.lodogame.ldsg.constants.BossStatus;
import com.lodogame.ldsg.constants.CommandType;
import com.lodogame.ldsg.constants.ConfigKey;
import com.lodogame.ldsg.constants.ToolType;
import com.lodogame.ldsg.constants.ToolUseType;
import com.lodogame.ldsg.event.BattleResponseEvent;
import com.lodogame.ldsg.event.BossEvent;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.helper.DropToolHelper;
import com.lodogame.ldsg.service.ActivityTaskService;
import com.lodogame.ldsg.service.BattleService;
import com.lodogame.ldsg.service.BossService;
import com.lodogame.ldsg.service.ForcesService;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.MessageService;
import com.lodogame.ldsg.service.ToolService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.ldsg.service.VipService;
import com.lodogame.model.Boss;
import com.lodogame.model.BossTeam;
import com.lodogame.model.Command;
import com.lodogame.model.ForcesDropTool;
import com.lodogame.model.SystemUserLevel;
import com.lodogame.model.User;
import com.lodogame.model.UserBoss;

/**
 * <h3>### 关于Boss刷新与消失</h3> <br/>
 * Boss在消失时只会清除Boss本身信息，解散所有未开始战斗的小组，以及清除UserBoss中的Boss信息，只有
 * 在Boss刷新之后之后，所有与Boss（封魔）相关的其他信息才会被删除掉。
 * <p>
 * </p>
 * 这样做的原因是Boss消失后，还 会有一些队伍正在战斗中，而战斗中只需要Boss的ForcesId信息（即部队信息），
 * 因此Boss本身可以删除，但是战斗之后还有一些逻辑处理还需要使用到BossTeam和UserBoss对象，所以Boss刷新时 才没有立即清除所
 * 有相关状态。
 * <p>
 * </p>
 * 
 * @author <a href="mailto:clact_jia@staff.easou.com">Clact</a>
 * @since v1.0.0.2013-9-23
 */
public class BossServiceImpl implements BossService {

	private static final Logger LOG = Logger.getLogger(BossServiceImpl.class);
	private static final Logger LOG_STAT = Logger.getLogger("stat");

	private static final int DISMISS_TEAM_CAUSE_ACK_CHALLENGE_BOSS_INTERRUPT = 1;
	private static final int DISMISS_TEAM_CAUSE_THIRTY_SECONDS_EXPIRED = 2;
	private static final int DISMISS_TEAM_CAUSE_BOSS_DISAPPEAR = 3;

	// ----------------------------------------------------------------------------------------------

	// Business exception code [Start]

	/**
	 * 玩家已在一个队伍中
	 */
	private static final int EXCODE_USER_ALREADY_IN_BOSS_TEAM = 9002;

	/**
	 * 魔怪不存在
	 */
	private static final int EXCODE_BOSS_NOT_EXIST = 9003;

	/**
	 * 已达到封魔最大次数限制
	 */
	private static final int EXCODE_NO_MORE_CHANCE_TO_FIGHT_BOSS = 9004;

	/**
	 * 封魔时间正在冷却中
	 */
	private static final int EXCODE_COOLDOWN_UNCOMPLETED = 9005;

	/**
	 * 队伍不存在
	 */
	private static final int EXCODE_TEAM_NOT_EXIST = 9006;

	/**
	 * 小队已达最大人数
	 */
	private static final int EXCODE_TEAM_HAVE_MAX_MEMBER = 9007;

	/**
	 * 玩家不在队伍中
	 */
	private static final int EXCODE_TEAM_MEMBER_NOT_EXIST = 9008;

	/**
	 * 队伍成员小于开启封魔必须数量
	 */
	private static final int EXCODE_TEAM_MEMBER_NOT_ENOUGH_TO_CHALLENGE_BOSS = 9009;

	/**
	 * 不需要重置封魔冷却时间
	 */
	private static final int EXCODE_NEED_NOT_RESET_COOLDOWN = 9010;

	/**
	 * 重置冷却时间所需金币不足
	 */
	private static final int EXCODE_RESET_BOSS_COOLDOWN_MONEY_NOT_ENOUGH = 9011;

	/**
	 * 没有合适的小队能够加入（因为没有队伍或者没有合适人数的队伍）
	 */
	private static final int EXCODE_HAVE_NOT_SUITABLE_TEAM_TO_JOININ = 9012;

	/**
	 * 没有足够的权限踢出小队成员（即只有队长有权限踢出小队成员）
	 */
	private static final int EXCODE_HAVE_NOT_PRIVILEGE_TO_KICKOUT_TEAM_MEMBER = 9013;

	/**
	 * 没有权利开启封魔（即只有队长能够进行开始封魔操作）
	 */
	private static final int EXCODE_HAVE_NOT_PRIVILEGE_TO_START_CHALLENGE = 9014;

	/**
	 * 没有合适的小队能够切换（因为没有队伍或者没有合适人数的队伍）
	 */
	private static final int EXCODE_HAVE_NOT_ANOTHER_TEAM_ENABLE_TO_SWAP = 9015;

	/**
	 * 玩家没有加入任何小队
	 */
	private static final int EXCODE_USER_HAVE_NOT_JOINED_ANY_TEAM = 9016;

	/**
	 * 小队正在战斗（即封魔已开始）
	 */
	private static final int EXCODE_TEAM_IS_FIGHTING = 9017;

	/**
	 * 不在 Boss 活动时间内
	 */
	private static final int EXCODE_BOSS_NOT_STARTED = 9018;

	private int status = BossStatus.BOSS_STATUS_NOT_STARTED;

	/**
	 * 开始时间
	 */
	private Date startTime;

	/**
	 * 结束时间
	 */
	private Date endTime;

	// Business exception code [End]

	// ----------------------------------------------------------------------------------------------

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * 封魔战役类型
	 */
	private static final int BATTLE_TYPE_OF_BOSS = 4;

	private Map<String, List<BossBattleReportBO>> reportsCache = new HashMap<String, List<BossBattleReportBO>>();
	private Executor poolForWaitAckPrepareChallengeBattle = Executors.newCachedThreadPool();

	/**
	 * 魔怪存在时间，用于清理魔怪定时任务，即在等待该时间长度后，清除已存在的魔怪信息
	 */
	private static final int B0SS_EXIST_TIME = Boss.BOSS_EXISTS_MINUTES * 60 * 1000;

	@Autowired
	private BossDao bossDao;

	@Autowired
	private UserBossDao userBossDao;

	@Autowired
	private BossTeamDao bossTeamDao;

	@Autowired
	private ConfigDataDao configDataDao; // cache

	@Autowired
	private CommandDao commandDao;

	@Autowired
	private SystemSceneDao systemSceneDao;

	@Autowired
	private ForcesDropToolDao forcesDropToolDao;

	@Autowired
	private SystemUserLevelDao systemUserLevelDao;

	@Autowired
	private SystemForcesDao systemForcesDao;

	@Autowired
	private UserService userService;

	@Autowired
	private HeroService heroService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private BattleService battleService;

	@Autowired
	private ForcesService forcesService;

	@Autowired
	private ToolService toolService;

	@Autowired
	private VipService vipService;

	@Autowired
	private ActivityTaskService activityTaskService;
	
	@Override
	public BossTeam getBossTeam(String teamId) {
		return bossTeamDao.getTeam(teamId);
	}

	@Override
	public List<BossBattleReportBO> getReports(String reportsId) {
		return reportsCache.get(reportsId);
	}

	/**
	 * 推送魔怪出现信息
	 */
	private void pushBossAppear() {
		Command command = new Command();
		command.setCommand(CommandType.COMMAND_BOSS_PUSH_BOSS_APPEAR);
		command.setType(CommandType.PUSH_ALL);
		command.setParams(new HashMap<String, String>());

		commandDao.add(command);
	}

	/**
	 * 推送魔怪消失信息
	 */
	private void pushBossDisappear() {
		Command command = new Command();
		command.setCommand(CommandType.COMMAND_BOSS_PUSH_BOSS_DISAPPEAR);
		command.setType(CommandType.PUSH_ALL);
		command.setParams(new HashMap<String, String>());

		commandDao.add(command);
	}

	/**
	 * 推送用户登出自动离队消息
	 * 
	 * @param params
	 */
	private void pushUserLogout(Map<String, String> params) {
		Command command = new Command();
		command.setCommand(CommandType.COMMAND_BOSS_PUSH_BOSS_TEAM_USER_LOGOUT);
		command.setType(CommandType.PUSH_USER);
		command.setParams(params);

		commandDao.add(command);
	}

	/**
	 * 推送用户封魔战报
	 * 
	 * @param members
	 * @param reportsId
	 */
	private void pushChallengeBossResult(String members, String reportsId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("memebers", members);
		params.put("reportsId", reportsId);

		Command command = new Command();
		command.setCommand(CommandType.COMMAND_BOSS_PUSH_CHALLENGE_BOSS_RESULT);
		command.setType(CommandType.PUSH_USER);
		command.setParams(params);

		commandDao.add(command);
	}

	/**
	 * 推送用户封魔信息
	 * 
	 * @param members
	 */
	private void pushUserBossInfo(String members) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("memebers", members);

		Command command = new Command();
		command.setCommand(CommandType.COMMAND_BOSS_PUSH_USER_BOSS_INFORMATION);
		command.setType(CommandType.PUSH_USER);
		command.setParams(params);

		commandDao.add(command);
	}

	/**
	 * 推送小组信息
	 * 
	 * @param members
	 */
	private void pushBoosTeamUpdate(String teamId, int forcesId, int status) {

		Map<String, String> params = new HashMap<String, String>();
		params.put("teamId", teamId);
		params.put("forcesId", String.valueOf(forcesId));
		params.put("status", String.valueOf(status));

		Command command = new Command();
		command.setCommand(CommandType.COMMAND_BOSS_PUSH_TEAM_UPDATE);
		command.setType(CommandType.PUSH_ALL);
		command.setParams(params);

		commandDao.add(command);
	}

	private void pushTeamDismissed(String teamId, int cause) {

		Map<String, String> params = new HashMap<String, String>();
		params.put("teamId", teamId);
		params.put("ca", String.valueOf(cause));

		Command command = new Command();
		command.setCommand(CommandType.COMMAND_BOSS_PUSH_TEAM_DISMISSED);
		command.setType(CommandType.PUSH_USER);
		command.setParams(params);

		commandDao.add(command);
	}

	/**
	 * 转换用户封魔信息
	 * 
	 * @param ub
	 * @return
	 */
	private UserBossBO createUserBossBO(UserBoss ub) {

		UserBossBO ubo = new UserBossBO();
		ubo.setUserId(ub.getUserId());
		ubo.setForcesId(ub.getForcesId());
		// ubo.setMapId(getMapId(ub.getBoss().getSceneId()));
		// ubo.setSceneId(ub.getBoss().getSceneId());

		ubo.setTimes(ub.getTimes());
		ubo.setCooldown(ub.getCooldownCompleteTimepoint());

		ubo.setBossDisappearTime(this.getEndTime().getTime());

		return ubo;
	}

	private ServiceException exception(int code, String message) {
		LOG.error(message);
		return new ServiceException(code, message);
	}

	/**
	 * 检查魔物是否存在
	 * 
	 * @param userId
	 *            用户编号
	 */
	private void checkGeneralConditions(String userId) {
		// if (bossDao.getBoss() == null) {
		// throw exception(EXCODE_BOSS_NOT_EXIST, "封魔失败，魔兽不存在.userId[" + userId
		// + "]");
		// }
		// connectUserBoss(userId);
	}

	/**
	 * 检查用户是否已经在封魔队伍中
	 * 
	 * @param userId
	 *            用户编号
	 */
	private void checkUserAlreadyInBossTeam(String userId) {
		if (bossTeamDao.isUserAlreadyInBossTeam(userId)) {
			throw exception(EXCODE_USER_ALREADY_IN_BOSS_TEAM, "封魔失败，玩家已在封魔队伍中.userId[" + userId + "]");
		}
	}

	/**
	 * 检查用户是否有剩余次数，以及是否处于攻击CD
	 * 
	 * @param userId
	 *            用户编号
	 */
	private void checkCooldown(String userId, int forcesId) {
		UserBoss ub = userBossDao.getBoss(userId, forcesId);
		if (ub != null) {
			if (ub.getTimes() >= Boss.MAX_CHALLENGE_TIMES) {
				throw exception(EXCODE_NO_MORE_CHANCE_TO_FIGHT_BOSS, "封魔失败，玩家挑战次数已用完.userId[" + userId + "]");
			}
			if (!ub.isCooldownCompleted()) {
				throw exception(EXCODE_COOLDOWN_UNCOMPLETED, "封魔失败，玩家挑战冷却中.userId[" + userId + "]");
			}
		}
	}

	/**
	 * 检查指定队伍是否存在
	 * 
	 * @param userId
	 *            用户编号
	 * @param teamId
	 *            指定的队伍编号
	 * @return
	 */
	private BossTeam checkTeam(String userId, String teamId) {
		BossTeam t;
		if (teamId == null) {
			t = bossTeamDao.getTeamByUserId(userId);
		} else {
			t = bossTeamDao.getTeam(teamId);
		}

		if (t == null) {
			throw exception(EXCODE_TEAM_NOT_EXIST, "封魔失败，队伍不存在.userId[" + userId + "].teamId[" + teamId + "]");
		}

		return t;
	}

	private void checkTeamMember(String userId) {
		BossTeam t = checkTeam(userId, null);
		if (!t.getMembers().contains(userId)) {
			throw exception(EXCODE_TEAM_MEMBER_NOT_EXIST, "封魔失败，玩家不在队伍中.userId[" + userId + "].");
		}
	}

	@Override
	@Deprecated
	public List<BossTeamBO> accessBossRoom(String userId) {
		int mid = userBossDao.getUserMap(userId);

		checkGeneralConditions(userId);

		List<BossTeamBO> ts = new ArrayList<BossTeamBO>(bossTeamDao.getTeamsCount(mid));

		for (BossTeam t : bossTeamDao.getTeamsByForcesId(mid)) {
			User user = userService.get(t.getCaptainId());
			List<UserHeroBO> userHeroBOs = heroService.getUserHeroList(user.getUserId(), 1);

			ts.add(new BossTeamBO(t.getId(), user.getLodoId(), user.getLevel(), user.getUsername(), userService.getUserPower(userHeroBOs), t.getMembers().size(), BossTeam.MAX_MEBMER_NUMBER, userHeroBOs));
		}

		return ts;
	}

	@Override
	public List<BossTeamDetailBO> createTeam(String userId, int forcesId) {

		checkGeneralConditions(userId);
		// checkUserAlreadyInBossTeam(userId);
		checkCooldown(userId, forcesId);

		// 如果玩家已经在某一个队伍中了，那么就直接返回他的队伍信息
		BossTeam team = bossTeamDao.getTeamByUserId(userId);
		if (team != null) {
			return getBossTeamDetailBOList(team);
		}

		List<BossTeamDetailBO> ts = new ArrayList<BossTeamDetailBO>(1);

		BossTeam t = bossTeamDao.addTeam(forcesId, userId);
		UserBO user = userService.getUserBO(userId);

		List<UserHeroBO> userHeros = heroService.getUserHeroList(userId, 1);

		ts.add(new BossTeamDetailBO(userHeros, userId == t.getCaptainId(), user.getPlayerId(), user.getUsername(), user.getLevel(), userService.getUserPower(userHeros)));

		// 在玩家创建队伍或者加入队伍时更新活跃度奖励
		activityTaskService.updateActvityTask(userId, ActivityTargetType.BOSS_BATTLE, 1);
		
		this.pushBoosTeamUpdate(t.getId(), forcesId, 1);

		return ts;
	}

	@Override
	public List<BossTeamDetailBO> joinTeam(String userId, String teamId, EventHandle handle) {

		BossTeam bossTeam = this.bossTeamDao.getTeam(teamId);

		checkGeneralConditions(userId);
		checkUserAlreadyInBossTeam(userId);
		checkTeam(userId, teamId);
		checkCooldown(userId, bossTeam.getForcesId());

		return doJoinTeam(userId, teamId, handle);
	}

	private List<BossTeamDetailBO> getBossTeamDetailBOList(BossTeam t) {
		List<BossTeamDetailBO> ds = new ArrayList<BossTeamDetailBO>(t.getTeamMemberCount());
		synchronized (t) {
			for (String memId : t.getMembers()) {
				ds.add(getBossTeamDetailBO(memId, t.getId(), t.getCaptainId()));
			}
		}

		return ds;
	}

	private List<BossTeamDetailBO> doJoinTeam(String userId, String teamId, EventHandle handle) {

		BossTeam t = bossTeamDao.getTeam(teamId);

		if (t == null) {
			throw exception(EXCODE_TEAM_NOT_EXIST, "加入小组失败，队伍不存在.userId[" + userId + "]");
		}

		synchronized (t) {
			if (t.getTeamMemberCount() >= BossTeam.MAX_MEBMER_NUMBER) {
				throw exception(EXCODE_TEAM_HAVE_MAX_MEMBER, "加入小组失败，队伍已达最大人数.userId[" + userId + "]");
			}

			if (t.getTeamMemberCount() < 1) {
				throw exception(EXCODE_TEAM_NOT_EXIST, "加入小组失败，队伍不存在.userId[" + userId + "]");
			}

			if (!bossTeamDao.addMember(teamId, userId)) {
				throw exception(EXCODE_TEAM_NOT_EXIST, "加入小组失败，队伍不存在或玩家已在队伍中.userId[" + userId + "]");
			}
		}

		List<BossTeamDetailBO> ds = getBossTeamDetailBOList(t);

		if (handle != null) {
			handle.handle(new BossEvent(t, userId));
		}

		// 在玩家创建队伍或者加入队伍时更新活跃度奖励
		activityTaskService.updateActvityTask(userId, ActivityTargetType.BOSS_BATTLE, 1);
		
		this.pushBoosTeamUpdate(teamId, t.getForcesId(), 2);

		return ds;
	}

	private BossTeamDetailBO getBossTeamDetailBO(String userId, String teamId, String captainId) {

		BossTeamDetailBO d = new BossTeamDetailBO();
		d.setUserHeroBOList(heroService.getUserHeroList(userId, 1));
		d.setCaptain(captainId.equals(userId));
		d.setPower(userService.getUserPower(d.getUserHeroBOList()));

		UserBO u = userService.getUserBO(userId);

		d.setPlayerId(u.getPlayerId());
		d.setUserLevel(u.getLevel());
		d.setUserName(u.getUsername());

		return d;
	}

	@Override
	public List<BossTeamDetailBO> quickStart(String userId, int forcesId, EventHandle handle) {

		checkGeneralConditions(userId);
		// checkUserAlreadyInBossTeam(userId);
		checkCooldown(userId, forcesId);

		// 如果玩家已经在某一个队伍中了，那么就直接返回他的队伍信息
		BossTeam team = bossTeamDao.getTeamByUserId(userId);
		if (team != null) {
			return getBossTeamDetailBOList(team);
		}

		team = bossTeamDao.getTeamForQuickStart(forcesId, null);

		// 如果玩家不在任何一个队伍中，那么则为他选取一直队伍用作快速开始
		if (team == null) {
			throw exception(EXCODE_HAVE_NOT_SUITABLE_TEAM_TO_JOININ, "封魔切换队伍失败，没有更多的队伍可以加入.userId[" + userId + "]");
		}

		return doJoinTeam(userId, team.getId(), handle);
	}

	@Override
	public List<BossTeamDetailBO> quickSwapping(String userId, int forcesId, EventHandle handle) {

		checkGeneralConditions(userId);
		checkCooldown(userId, forcesId);
		checkTeamMember(userId);

		if (bossTeamDao.getTeamsCount(forcesId) == 1)
			throw exception(EXCODE_HAVE_NOT_ANOTHER_TEAM_ENABLE_TO_SWAP, "封魔切换队伍失败，没有更多的队伍可以切换.userId[" + userId + "].");

		BossTeam ot = bossTeamDao.getTeamByUserId(userId);
		String oldTeamId = ot.getId();

		synchronized (ot) {
			if (ot.isFighting()) {
				throw exception(EXCODE_TEAM_IS_FIGHTING, "封魔已开始，不能够切换队伍.userId[" + userId + "].");
			}
		}

		exitTeam(userId);

		BossTeam t = bossTeamDao.getTeamForQuickStart(forcesId, oldTeamId);
		if (t == null) {
			throw exception(EXCODE_HAVE_NOT_ANOTHER_TEAM_ENABLE_TO_SWAP, "封魔切换队伍失败，没有更多的队伍可以切换.userId[" + userId + "].");
		}

		this.pushBoosTeamUpdate(t.getId(), forcesId, 2);

		return doJoinTeam(userId, t.getId(), handle);
	}

	@Override
	public void resetCooldown(String userId, int forcesId) {

		checkGeneralConditions(userId);

		UserBoss ub = userBossDao.getBoss(userId, forcesId);

		if (ub == null)
			throw exception(EXCODE_NEED_NOT_RESET_COOLDOWN, "重置封魔冷却失败，玩家没有未完成冷却.userId[" + userId + "]");
		if (ub.getTimes() >= Boss.MAX_CHALLENGE_TIMES)
			throw exception(EXCODE_NO_MORE_CHANCE_TO_FIGHT_BOSS, "封魔失败，玩家封魔次数已用完.userId[" + userId + "]");
		if (ub.isCooldownCompleted())
			throw exception(EXCODE_NEED_NOT_RESET_COOLDOWN, "重置封魔冷却失败，玩家没有未完成冷却.userId[" + userId + "]");

		// 获取离上次战斗有多长时间，以分钟为单位
		int pt = ub.getCooldown(TimeUnit.MINUTES);
		// 用冷却时间减去战斗时间差，获得剩余CD时间
		int cd = configDataDao.getInt(ConfigKey.RESET_BOSS_COOLDOWN, 10) - pt;
		// 剩余CD时间乘以消除每分钟CD时间所需的金币，获得最终重置封魔所需的金币
		int cost = configDataDao.getInt(ConfigKey.RESET_BOSS_COOLDOWN_PER_MIMUTE_NEED_GOLD, 50);// *
																								// cd;
																								// 固定50元宝

		if (!userService.reduceGold(userId, cost, ToolUseType.REDUCE_RESET_BOSS_COOLDOWN, userService.getUserBO(userId).getLevel())) {
			throw exception(EXCODE_RESET_BOSS_COOLDOWN_MONEY_NOT_ENOUGH, "重置封魔冷却时间失败，金币不足.userId[" + userId + "], needMoney[" + cost + "]");
		}

		userBossDao.resetCooldown(userId, forcesId);

		pushUserBossInfo(userId);
	}

	/**
	 * 仅用于用户登出游戏时，使用此方法将该用户在封魔队伍中的状态清楚掉
	 * 
	 * @param userId
	 *            用户编号
	 */
	@Override
	public void exitTeam(String userId) {

		BossTeam t = bossTeamDao.getTeamByUserId(userId);

		// ---- Clean user's team state ----------------------------------

		if (t == null) {
			LOG.error("用户退出小组时，获取小组信息失败.teamId[" + userId + "]");
			return; // Have not team state need to clean.
		}
		int st = 0;
		synchronized (t) {
			if (t.isFighting()) {
				return; // 如果已开始封魔，则不要让玩家退出队伍
			}

			bossTeamDao.clean(userId);

			// 如果让用户离队之后其所在小队还有其他成员，则向他们推送该用户离队消息，否则，解散小队
			if (t.getTeamMemberCount() > 0) {

				t.shiftCaptain();
				Map<String, String> params = new HashMap<String, String>();
				params.put("otherMembers", join(t.getMembers(), ";"));
				params.put("captainId", String.valueOf(userService.getUserBO(t.getCaptainId()).getPlayerId()));
				params.put("pid", String.valueOf(userService.getUserBO(userId).getPlayerId()));

				pushUserLogout(params);

				st = 2;

			} else {
				bossTeamDao.removeTeam(t.getId());

				st = 3;
			}
		}

		this.pushBoosTeamUpdate(t.getId(), t.getForcesId(), st);

	}

	private String join(Collection<String> collection, String delimiter) {
		StringBuilder sb = new StringBuilder();
		for (String otherMemId : collection) {
			sb.append(otherMemId).append(delimiter);
		}
		if (sb.length() > 0) {
			sb.setLength(sb.length() - 1);
		}

		return sb.toString();
	}

	@Override
	public void exitTeam(String userId, EventHandle handle) {

		BossTeam t = bossTeamDao.getTeamByUserId(userId);

		if (t == null || !t.getMembers().contains(userId)) {
			// 客户端和服务端状态不一致时，包容处理
			return;
		}

		int status = 2;

		synchronized (t) {
			// 封魔已开始，不处理离队申请
			if (t.isFighting()) {
				return;
			}

			bossTeamDao.removeMember(t.getId(), userId);

			if (t.getTeamMemberCount() == 0) {
				status = 3;
				bossTeamDao.removeTeam(t.getId());
			} else if (t.getCaptainId().equals(userId)) {
				t.shiftCaptain();
			}
		}

		this.pushBoosTeamUpdate(t.getId(), t.getForcesId(), status);

		if (handle != null)
			handle.handle(new BossEvent(t, userId));
	}

	@Override
	public void kickoutTeamMember(String captainId, String pupilId, EventHandle handle) {

		BossTeam t = bossTeamDao.getTeamByUserId(captainId);

		if (t == null || !t.containsMember(captainId)) {
			// 客户端和服务端状态不一致时，包容处理
			return;
		}

		if (t.isFighting()) {
			return;
		}

		if (!t.getCaptainId().equals(captainId))
			throw exception(EXCODE_HAVE_NOT_PRIVILEGE_TO_KICKOUT_TEAM_MEMBER, "封魔踢出玩家失败，玩家不是队长.userId[" + captainId + "].pupilId[" + pupilId + "].teamId[" + t.getId() + "]");

		if (handle != null) {
			handle.handle(new BossEvent(t, captainId)); // Add `captainId` means
														// don't send messages
														// to captain.
		}

		this.pushBoosTeamUpdate(t.getId(), t.getForcesId(), 2);

		bossTeamDao.removeMember(t.getId(), pupilId);
	}

	@Override
	public void challengeBoss(final String userId, EventHandle handle) {

		final BossTeam t = bossTeamDao.getTeamByUserId(userId);
		synchronized (t) {
			if (t == null) {
				throw exception(EXCODE_TEAM_NOT_EXIST, "封魔失败，队伍不存在.userId[" + userId + "]");
			}
			if (t.getMembers().size() < BossTeam.MIN_MEBMER_NUMBER) {
				throw exception(EXCODE_TEAM_MEMBER_NOT_ENOUGH_TO_CHALLENGE_BOSS, "封魔失败，队伍成员不足.userId[" + userId + "]");

			}
			if (!t.getCaptainId().equals(userId)) {
				throw exception(EXCODE_HAVE_NOT_PRIVILEGE_TO_START_CHALLENGE, "封魔失败，只有队长能够开启封魔.userId[" + userId + "]");
			}

			for (String memId : t.getMembers()) {
				checkGeneralConditions(memId);
				checkCooldown(memId, t.getForcesId());
				checkTeamMember(memId);
			}

			t.setStatus(BossTeam.STATUS_FIGHTING);

			// 队长率先准备好封魔
			t.addPreparedMember(userId);
		}

		if (handle != null) {
			handle.handle(new BossEvent(t, userId));
		}

		// 开始封魔确认等待
		poolForWaitAckPrepareChallengeBattle.execute(new Runnable() {
			@Override
			public void run() {
				for (int count = 1; count <= 10 && !t.isAllMembersReady(); count++) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
				}
				if (!t.isAllMembersReady()) {
					LOG.debug("超过10秒没有准备好，解散队伍");
					pushTeamDismissed(t.getId(), DISMISS_TEAM_CAUSE_ACK_CHALLENGE_BOSS_INTERRUPT);
				}

				// 再过过一分钟没好则删掉小队
				for (int count = 1; count <= 10 && t.getStatus() != BossTeam.STATUS_FINISH; count++) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
				}
				LOG.debug("队伍状态：" + t.getStatus());
				if (t.getStatus() != BossTeam.STATUS_FINISH) {// 一分钟后强制解散
					LOG.debug("超过15秒没有准备好，解散队伍");
					// dismissTeam(t.getId(), true);
					pushTeamDismissed(t.getId(), DISMISS_TEAM_CAUSE_ACK_CHALLENGE_BOSS_INTERRUPT);
				}
			}
		});
	}

	/**
	 * BossPushHandler专用
	 */
	@Override
	public void dismissTeam(String teamId, boolean force) {

		BossTeam team = bossTeamDao.getTeam(teamId);

		if (team == null) {
			LOG.info("dismissTeam失败,队伍不存在.teamI[" + teamId + "]");
			return;
		}
		synchronized (team) {
			if (team.isFighting() && !force) {
				LOG.info("队伍已经开始封魔并且不是强制解散,不做处理.teamId[" + teamId + "]");
				return;
			}

			bossTeamDao.removeTeam(teamId);
		}
	}

	@Override
	public void dismissTeamByCaptain(String captainId) {

		BossTeam t = bossTeamDao.getTeamByUserId(captainId);

		if (t == null || !t.getMembers().contains(captainId)) {
			// 客户端和服务端状态不一致时，包容处理
			return;
		}

		if (t.isFighting()) {
			return;
		}

		if (!t.getCaptainId().equals(captainId)) {
			throw exception(EXCODE_HAVE_NOT_PRIVILEGE_TO_KICKOUT_TEAM_MEMBER, "封魔踢出玩家失败，玩家不是队长.userId[" + captainId + "].teamId[" + t.getId() + "]");
		}

		pushTeamDismissed(t.getId(), DISMISS_TEAM_CAUSE_THIRTY_SECONDS_EXPIRED);
	}

	private void doChallengeBoss(final int bossForcesId, final int userIndex, final String userId, final BossTeam team, final List<BattleHeroBO> userHeros, final List<BattleHeroBO> bossHeros,
			final List<BossBattleReportBO> reports) {

		BattleBO attack = new BattleBO();
		attack.setBattleHeroBOList(userHeros);
		BattleBO defense = new BattleBO();
		defense.setBattleHeroBOList(bossHeros);
		
		battleService.fight(attack, defense, BATTLE_TYPE_OF_BOSS, new EventHandle() {
			@Override
			public boolean handle(Event event) {
				if (event instanceof BattleResponseEvent) {

					User user = userService.get(userId);
					LOG_STAT.info("action[attackForces], userId[" + user.getUserId() + "], userLevel[" + user.getLevel() + "], forcesId[" + bossForcesId + "], flag[" + event.getInt("flag") + "]");

					reports.get(userIndex).setReport(event.getString("report"));

					// LOG.error(event.getString("report"));

					// 战斗胜利
					if (event.getInt("flag") == 1) {

						// 遍历的顺序与序号顺序是一样的
						int i = 0;
						for (String uid : team.getMembers()) {
							// 设置战斗掉落至对应小队成员

							boolean isFourthTimes = false;
							UserBoss userBoss = userBossDao.getBoss(uid, team.getForcesId());
							if (userBoss != null) {
								if (userBoss.getTimes() >= 4) {
									isFourthTimes = true;
								}
							} else {
								LOG.error("user boss 信息为空.userId[" + userId + "], focesId[" + team.getForcesId() + "]");
							}

							reports.get(i).setDrop(pickUpForcesDropToolList(uid, bossForcesId, isFourthTimes));

							if (userBoss != null) {
								userBoss.addTimes();
								userBoss.refreshLastime();
								userService.pushUser(uid);
							}
							i++;
						}

					} else {
						@SuppressWarnings("unchecked")
						Map<String, Integer> lifeData = (Map<String, Integer>) event.getObject("life");

						// 设置魔怪战后血量
						String hpos = "";
						for (BattleHeroBO hero : bossHeros) {
							hpos = "L_d" + hero.getPos();
							if (lifeData.get(hpos) != null)
								hero.setLife(lifeData.get(hpos));
							else
								bossHeros.remove(hero);
						}

						// 下一个挑战者准备战斗
						int nextUserIndex = userIndex + 1;
						if (nextUserIndex < reports.size()) {
							String nextUserId = reports.get(nextUserIndex).getUserId();
							doChallengeBoss(bossForcesId, nextUserIndex, reports.get(nextUserIndex).getUserId(), team, heroService.getUserBattleHeroBOList(nextUserId), bossHeros, reports);
						}
					}
					
					// 如果整场战斗已结束（战斗胜利，或者最后一名小队成员战斗失败时）
					if (event.getInt("flag") == 1 || (event.getInt("flag") != 1 && userIndex == reports.size() - 1)) {
						// 设置战斗结果至所有小队成员
						for (BossBattleReportBO report : reports) {
							report.setResult(event.getInt("flag"));
						}

						// 封魔战斗结束后，推送相关消息
						pushMessagesWhenBossBattleEnd(team, reports);
						// 封魔战斗结束，解散小队（不需要推送信息）
						team.setStatus(BossTeam.STATUS_FINISH);// 完成
						synchronized (team) {
							bossTeamDao.removeTeam(team.getId());
						}
					}
				}

				return true;
			}
		});
	}

	private CommonDropBO pickUpForcesDropToolList(String userId, int forcesId, boolean isFourthTimes) {

		CommonDropBO forcesDropBO = new CommonDropBO();

		User user = this.userService.get(userId);

		int oldLevel = user.getLevel();

		SystemUserLevel systemUserLevel = this.systemUserLevelDao.get(user.getLevel());
		int oldAttack = systemUserLevel.getAttack();
		int oldDefense = systemUserLevel.getDefense();

		int rand = RandomUtils.nextInt(10000);

		LOG.debug("获取怪物部队掉落道具.userId[" + userId + "], forcesId[" + forcesId + "]");

		List<ForcesDropTool> forcesDropToolList = this.forcesDropToolDao.getForcesDropToolList(forcesId);
		for (ForcesDropTool forcesDropTool : forcesDropToolList) {

			List<DropToolBO> dropToolBOList = this.pickUpForcesDropTool(userId, forcesDropTool, rand, isFourthTimes);
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

	private List<DropToolBO> pickUpForcesDropTool(String userId, ForcesDropTool forcesDropTool, int rand, boolean isFourthTimes) {

		User user = this.userService.get(userId);

		List<DropToolBO> dropToolBOList = null;

		int toolType = forcesDropTool.getToolType();
		int toolId = forcesDropTool.getToolId();
		int toolNum = forcesDropTool.getToolNum();
		int lowerNum = forcesDropTool.getLowerNum();
		int upperNum = forcesDropTool.getUpperNum();

		// 第四次封魔胜利，则有额外掉落奖励
		if (isFourthTimes = false || toolType != ToolType.GOLD) {
			if (!DropToolHelper.isDrop(lowerNum, upperNum, rand)) {
				LOG.debug("该道具未掉落.toolType[" + toolType + "]");
				return null;
			}
		}

		// VIP加成
		if (toolType == ToolType.COPPER) {
			toolNum += vipService.getCopperAddRatio(user.getVipLevel()) * toolNum;
		} else if (toolType == ToolType.EXP) {
			toolNum += vipService.getExpAddRatio(user.getVipLevel()) * toolNum;
		}

		if (toolType == ToolType.GOLD && !userService.checkUserGoldGainLimit(user.getUserId(), toolNum)) {
			return null;
		}

		dropToolBOList = toolService.giveTools(userId, toolType, toolId, toolNum, ToolUseType.ADD_FORCES);

		return dropToolBOList;
	}

	@Override
	public void ackPrepareChallengeBoss(String userId, int forcesId) {

		checkGeneralConditions(userId);
		checkCooldown(userId, forcesId);

		BossTeam t = bossTeamDao.getTeamByUserId(userId);

		if (t == null) {
			throw exception(EXCODE_USER_HAVE_NOT_JOINED_ANY_TEAM, "封魔失败，获取玩家小组失败.userId[" + userId + "]");
		} else if (!t.containsMember(userId)) {
			throw exception(EXCODE_USER_HAVE_NOT_JOINED_ANY_TEAM, "封魔失败，玩家不在所在小组的成员列表中.userId[" + userId + "], teamId[" + t.getId() + "]");
		}

		t.addPreparedMember(userId);

		// 如果小队成员全体准备封魔完毕，则开始封魔
		if (t.isAllMembersReady()) {
			t.setStatus(BossTeam.STATUS_FIGHTING);

			List<BossBattleReportBO> reports = new ArrayList<BossBattleReportBO>(t.getTeamMemberCount());

			int bossForcesId = t.getForcesId();
			int bgId = this.systemSceneDao.get(this.systemForcesDao.get(bossForcesId).getSceneId()).getImgId();

			List<BattleHeroBO> bossHeros = this.forcesService.getForcesHeroBOList(bossForcesId);

			// 逆序成员顺序
			List<String> members = new ArrayList<String>();
			for (int i = 0; i < t.getMembers().size(); i++) {
				members.add(t.getMembers().get(i));
			}

			// 初始化战报列表
			for (String memId : t.getMembers()) {
				UserBO user = userService.getUserBO(memId);
				reports.add(new BossBattleReportBO(bossForcesId, BATTLE_TYPE_OF_BOSS, bgId, user.getUserId(), user.getUsername(), user.getPlayerId(), heroService.getUserHeroList(memId, 1).get(0)));
			}

			// 队长一定是第一个小队成员，因此对应的成员序号为零
			doChallengeBoss(bossForcesId, 0, userId, t, this.heroService.getUserBattleHeroBOList(t.getMembers().get(0)), bossHeros, reports);
		}
	}

	private void pushMessagesWhenBossBattleEnd(BossTeam team, List<BossBattleReportBO> reports) {
		String members = join(team.getMembers(), ";");

		String reportsId = UUID.randomUUID().toString().replaceAll("-", "");
		reportsCache.put(reportsId, reports);

		pushChallengeBossResult(members, reportsId);
		pushUserBossInfo(members);
	}

	@Override
	public List<BossTeamBO> getBossTeamInfoList(int forcesId, String userId) {
		checkGeneralConditions(userId);

		// 保存用户进入的是哪张地图
		userBossDao.addUserMap(userId, forcesId);

		List<BossTeamBO> ts = new ArrayList<BossTeamBO>(bossTeamDao.getTeamsCount(forcesId));

		List<BossTeam> teamList = bossTeamDao.getTeamsByForcesId(forcesId);

		for (BossTeam t : teamList) {

			if (t.getStatus() != BossTeam.STATUS_WAITING) {
				continue;
			}

			User user = userService.get(t.getCaptainId());
			List<UserHeroBO> userHeroBOs = heroService.getUserHeroList(user.getUserId(), 1);

			ts.add(new BossTeamBO(t.getId(), user.getLodoId(), user.getLevel(), user.getUsername(), userService.getUserPower(userHeroBOs), t.getMembers().size(), BossTeam.MAX_MEBMER_NUMBER, userHeroBOs));
		}
		return ts;
	}

	/**
	 * 初始化用户 Boss 信息，将用户和每个 Boss 关联起来，并将次数设置为0
	 * 
	 * @param userId
	 * @return
	 */
	private List<UserBoss> initUserBossList(String userId) {

		List<UserBoss> userBossList = userBossDao.getBossList(userId);

		if (userBossList != null) {

			return userBossList;

		} else {

			userBossList = new ArrayList<UserBoss>();

			List<Boss> bossList = bossDao.getBossList();

			LOG.debug("boss list size[" + bossList.size() + "]");

			if (bossList != null && userBossDao.getBossList(userId) == null) {
				for (Boss boss : bossList) {
					userBossList.add((new UserBoss(userId, boss.getForcesId())));
				}
			}
			userBossDao.addToList(userId, userBossList);
		}

		return userBossList;
	}

	@Override
	public List<UserBossBO> getUserBossList(String userId) {
		//
		// if (this.status != BossStatus.BOSS_STATUS_STARTED) {
		// return new ArrayList<UserBossBO>();
		// }

		List<UserBoss> bossList = initUserBossList(userId);
		List<UserBossBO> bossBOList = new ArrayList<UserBossBO>();

		for (UserBoss ub : bossList) {
			UserBossBO userBossBO = createUserBossBO(ub);
			bossBOList.add(userBossBO);
		}

		return bossBOList;
	}

	@Override
	public void checkBossStarted() {
		int startTime = configDataDao.getInt(ConfigKey.BOSS_START_TIME, 10);

		Date now = new Date();
		// Boss 的结束时间等于开始时间加上 Boss 持续时间： boss_exist_time
		Date bossStartTime = DateUtils.str2Date(DateUtils.getDate() + " " + startTime + ":00:00");

		Date bossDisappearTime = DateUtils.add(bossStartTime, Calendar.MILLISECOND, configDataDao.getInt(ConfigKey.B0SS_EXIST_TIME, B0SS_EXIST_TIME));
		if (now.before(bossStartTime) || now.after(bossDisappearTime)) {
			throw new ServiceException(EXCODE_BOSS_NOT_STARTED, "Boss 活动还没有开始");
		}
	}

	public void init() {

		if (!Config.ins().isGameServer()) {
			return;
		}

		// 状态切换线程
		new Thread(new Runnable() {

			public void run() {
				while (true) {
					try {
						statusCheck();
					} catch (Throwable t) {
						LOG.error(t.getMessage(), t);
					}

					try {
						Thread.sleep(1000);
					} catch (InterruptedException ie) {
						LOG.error(ie.getMessage(), ie);
					}

				}
			}

		}).start();

	}

	/**
	 * 开始,写开始逻辑
	 */
	private void start() {

		// 更改状态
		this.status = BossStatus.BOSS_STATUS_STARTED;

		// 始初化BOSS
		initBoss();
	}

	/**
	 * 初始化方法
	 */
	private void initBoss() {

		userBossDao.clean();
		bossTeamDao.cleanAll();
		reportsCache.clear();

		messageService.sendSystemMsg("野外魔将出现，合伙人们赶紧前去封印");

		pushBossAppear();

	}

	/**
	 * 结束，写结束逻辑
	 */
	private void end() {

		// 更改状态
		this.status = BossStatus.BOSS_STATUS_NOT_STARTED;
		this.setNextTime();

		// 推送boss消失信息
		this.pushBossDisappear();

		for (BossTeam t : bossTeamDao.getTeamList()) {
			if (t.getStatus() != BossTeam.STATUS_FIGHTING) {
				pushTeamDismissed(t.getId(), DISMISS_TEAM_CAUSE_BOSS_DISAPPEAR);
			}
		}

	}

	private String getStatusDesc(int status) {
		if (this.status == BossStatus.BOSS_STATUS_NOT_STARTED) {
			return "未开始";
		} else {
			return "进行中";
		}
	}

	public Date getStartTime() {

		Date now = new Date();

		if (this.startTime == null) {

			if (Config.ins().isDebug()) {

				startTime = new Date();
				endTime = DateUtils.add(startTime, Calendar.MINUTE, 30);

			} else {

				startTime = DateUtils.str2Date(DateUtils.getDate() + " 21:00:00");
				endTime = DateUtils.add(startTime, Calendar.MINUTE, 30);

				if (now.after(endTime)) {// 晚上的也过了
					startTime = DateUtils.addDays(startTime, 1);
					endTime = DateUtils.addDays(endTime, 1);
				}

			}
		}

		return startTime;
	}

	/**
	 * 设置下次的开始时间
	 */
	public void setNextTime() {
		startTime = DateUtils.addDays(this.startTime, 1);
		endTime = DateUtils.addDays(this.endTime, 1);
	}

	public Date getEndTime() {
		return endTime;
	}

	private void statusCheck() {

		Date now = new Date();

		Date st = this.getStartTime();
		Date et = this.getEndTime();

//		LOG.debug("状态切换线程.status[" + getStatusDesc(this.status) + "(" + this.status + ")]");

		if (this.status == BossStatus.BOSS_STATUS_NOT_STARTED) {// 未开始

//			LOG.debug("Boss战未开始，开始时间[" + DateUtils.getTime(st.getTime()) + "], 当前时间[" + DateUtils.getTime(now.getTime()) + "]");

			if (now.after(st)) {// 过了开始
				this.start();
			}

		} else if (this.status == BossStatus.BOSS_STATUS_STARTED) {// 已经开始

//			LOG.debug("Boss战进行中，结束时间[" + DateUtils.getTime(et.getTime()) + "], 当前时间[" + DateUtils.getTime(now.getTime()) + "]");

			if (now.after(et)) {// 过了结束时间
				this.end();
			}
		}

	}

	@Override
	public int getStatus() {
		return this.status;
	}
}
