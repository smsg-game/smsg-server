package com.lodogame.ldsg.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.CommandDao;
import com.lodogame.game.dao.ContestBetDao;
import com.lodogame.game.dao.ContestFightResultDao;
import com.lodogame.game.dao.ContestPlayerPairDao;
import com.lodogame.game.dao.ContestRankDao;
import com.lodogame.game.dao.ContestRewardDao;
import com.lodogame.game.dao.ContestTeamDao;
import com.lodogame.game.dao.UserContestBetRewardDao;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.game.utils.IDGenerator;
import com.lodogame.ldsg.bo.BattleBO;
import com.lodogame.ldsg.bo.BattleHeroBO;
import com.lodogame.ldsg.bo.BattleStartBO;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.ContestFightResultBO;
import com.lodogame.ldsg.bo.ContestPlayerPairBO;
import com.lodogame.ldsg.bo.ContestRankBO;
import com.lodogame.ldsg.bo.ContestRewardBO;
import com.lodogame.ldsg.bo.ContestTeamBO;
import com.lodogame.ldsg.bo.ContestTeamInfoBO;
import com.lodogame.ldsg.bo.DropToolBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.constants.CommandType;
import com.lodogame.ldsg.constants.ContestConstant;
import com.lodogame.ldsg.constants.ToolUseType;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.helper.BOHelper;
import com.lodogame.ldsg.helper.ContestHelper;
import com.lodogame.ldsg.helper.DropToolHelper;
import com.lodogame.ldsg.service.BattleService;
import com.lodogame.ldsg.service.ContestService;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.ToolService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.Command;
import com.lodogame.model.ContestFightResult;
import com.lodogame.model.ContestPlayerPair;
import com.lodogame.model.ContestReward;
import com.lodogame.model.ContestRound;
import com.lodogame.model.ContestTeam;
import com.lodogame.model.User;
import com.lodogame.model.UserContestBetLog;
import com.lodogame.model.UserContestBetReward;
import com.lodogame.model.UserContestRank;
import com.lodogame.model.UserRecContestRewardLog;



public class ContestServiceImpl implements ContestService{
	
	private static final Logger LOG = Logger.getLogger(ContestServiceImpl.class);
	private static int LAST_ROUND = -1;
	private static int CONTEST_STATUS = 0;
	private static int CURRENT_SESSION; 
	private static final int ENTER_REG = 1;
	private static final int ENTER_VERSUS = 2;
	private static final int ENTER_REC_REWARD = 3;
	
	/**
	 * 玩家达到这个等级才可以进入擂台赛
	 */
	private static final int USER_ACCESS_LEVEL = 40;
	
	private static int WEEK_OF_THE_YEAR;
	
	@Autowired
	private UserContestBetRewardDao userContestBetRewardDao;
	
	@Autowired
	private ContestFightResultDao contestFightResultDao;
	
	@Autowired
	private ContestBetDao contestBetDao;
	
	@Autowired
	private ContestRankDao contestRankDao;
	
	@Autowired
	private ContestRewardDao contestRewardDao;
	
	@Autowired
	private ContestPlayerPairDao contestPlayerPairDao;
	
	@Autowired
	private CommandDao commandDao;
	
	@Autowired
	private ToolService toolService;

	@Autowired
	private BattleService battleService;
	
	@Autowired
	private ContestTeamDao contestTeamDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private HeroService heroService;
	
	@Override
	public int getStatus(String userId) {
		if (CURRENT_SESSION == 0) {
			String message = "擂台赛还未开放";
			throw new ServiceException(ContestService.FIRST_SESSION_NOT_OPEN, message);
		}
		checkUserLevel(userId);
		if (ContestHelper.isTimeToEnterReg()) {
			return ENTER_REG;
		} else if (ContestHelper.isTimeToEnterVersus()) {
			return ENTER_VERSUS;
		} else {
			return ENTER_REC_REWARD;
		}
	}
	
	private void checkUserLevel(String userId) {
		User user = userService.get(userId);
		if (user.getLevel() < USER_ACCESS_LEVEL) {
			String message = "玩家未达到开放等级";
			throw new ServiceException(USER_LEVEL_NOT_ENOUGH, message);
		}
		
	}

	@Override
	public void contestFight(int round) {
		LAST_ROUND = round;
		
		List<ContestPlayerPair> playerPairList = getPlayerPairsForFight(round);
		
		for (ContestPlayerPair playerPair : playerPairList) {
			
			playerPairFight(playerPair);
			
//			int result = RandomUtils.nextInt(2) + 1;
//			playerPair.setResult(result);
		}
		
		checkAllPlayerPairsFighted(playerPairList, round);
		
		saveContestResult(playerPairList, round);
		setTeamFighted(round);
		
		if (round >= ContestConstant.ROUND_8) {
			saveContestRank(playerPairList, round);
		}
		
		
		if (round != ContestConstant.ROUND_1) {
			createTeamForNextFight(playerPairList, round);
		}

		pushContestFight();
	}
	
	private void checkAllPlayerPairsFighted(List<ContestPlayerPair> playerPairList, int round) {
		boolean isAllPlayerPairFighted = false;
		int count = 0;
		
		while(!isAllPlayerPairFighted) {
			for (ContestPlayerPair playerPair : playerPairList) {
				if (playerPair.getResult() == 0) {
					break;
				} else {
					count++;
					continue;
				}
			}
			
			if (count == playerPairList.size()) {
				isAllPlayerPairFighted = true;
			} else {
				try {
					LOG.debug("战斗服还未处理完擂台赛第[" + ContestHelper.getTeamNameByRound(round) + "]所有战斗请求，主服等待中");
					count = 0;
					Thread.sleep(2 * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void setTeamFighted(int round) {
		
		if (round == ContestConstant.ROUND_128) {
			for (int teamId : ContestConstant.REG_TEAM_IDS) {
				ContestTeam team = contestTeamDao.getTeamByTeamId(CURRENT_SESSION, teamId);
				team.setTeamFighted();
				contestTeamDao.setTeamFighted(team);
			}
		} else {
			ContestTeam team = contestTeamDao.getTeamByTeamId(CURRENT_SESSION, ContestHelper.getTeamIdByRound(round));
			team.setTeamFighted();
			contestTeamDao.setTeamFighted(team);
		}
		
	}

	private void pushContestFight() {
		Command command = new Command();
		command.setCommand(CommandType.COMMAND_CONTEST_ROUND_FINISHED);
		command.setType(CommandType.PUSH_ALL);
		command.setParams(new HashMap<String, String>());
		commandDao.add(command);
	}
	
	private List<ContestPlayerPair> getPlayerPairsForFight(int round) {
		List<ContestPlayerPair> playerPairList = new ArrayList<ContestPlayerPair>();
		int teamId = ContestHelper.getTeamIdByRound(round);
		
		if (teamId == 128) {
			List<ContestTeam> teamsForRegister = getTeamsForRegister();
			
			for (ContestTeam team : teamsForRegister) {
				List<ContestPlayerPair> list = contestPlayerPairDao.getPlayerPairsByContestId(team.getContestId());
				playerPairList.addAll(list);
			}
		} else {
			ContestTeam team = contestTeamDao.getTeamByTeamId(CURRENT_SESSION, teamId);
			playerPairList = contestPlayerPairDao.getPlayerPairsByContestId(team.getContestId());
		}
		
		return playerPairList;
	}

	private void createTeamForNextFight(List<ContestPlayerPair> playerPairList, int round) {
		int nextRound = ContestHelper.getNextRound(round);
		
		ContestTeam teamForNextFight = createTeamByRound(nextRound);

		for (ContestPlayerPair playerPair : playerPairList) {
			int result = playerPair.getResult();

			if (result == ContestConstant.ATTACK_USER_WIN) {
				String userId = playerPair.getAttUserId();

				addUserToTeam(userId, teamForNextFight);
				
			} else if (result == ContestConstant.DEFENSE_USER_WIN) {
				String userId = playerPair.getDefUserId();

				addUserToTeam(userId, teamForNextFight);
			} 
		}
		
		contestTeamDao.saveTeam(teamForNextFight);

		// 在16强比赛结束后，要创建两支队伍分别用户下注和8强比赛。
		if (nextRound == ContestConstant.ROUND_BET) {
			createTeamForNextFight(playerPairList, nextRound);
		}
		
	}

	private ContestTeam createTeamByRound(int round) {
		int teamId = ContestHelper.getTeamIdByRound(round);
		String teamName = ContestHelper.getTeamNameByRound(round);
		Date createdTime = new Date();
	
		ContestTeam team = new ContestTeam(IDGenerator.getID(), CURRENT_SESSION, teamId, teamName, createdTime);
		int capacity = ContestHelper.getTeamCapacityByRound(round);
		team.setCapacity(capacity);
		return team;
	}

	/**
	 * 决赛后，给在下注中压中冠军的所有用户发放奖励
	 */
	private void giveBetRewards(String champUserId) {
		
		// 读取下注冠军的用户 id 列表
		List<String> userIdList = contestBetDao.getGoodBetUserIdList(CURRENT_SESSION, champUserId);
		ContestReward contestReward = contestRewardDao.getContestReward(0);
		

		// 给压中的用户发放奖励
		List<DropToolBO> dropToolBOList = DropToolHelper.parseDropTool(contestReward.getDropToolIds());
		
		for (String userId : userIdList) {
			if (isUserReceiveBetReward(userId) == false) {
				for (DropToolBO dropToolBO : dropToolBOList) {
					int toolType = dropToolBO.getToolType();
					int toolId = dropToolBO.getToolId();
					int toolNum = dropToolBO.getToolNum();
		
					toolService.giveTools(userId, toolType, toolId, toolNum, ToolUseType.ADD_CONTEST_BET_REWARD);
				}
			}
		}
	}

	private void saveContestRank(List<ContestPlayerPair> playerPairList, int round) {
		
		for (ContestPlayerPair playerPair : playerPairList) {
			UserContestRank userContestRank = new UserContestRank();
			
			// 如果是决赛，保存冠军信息
			if (round == ContestConstant.ROUND_1) {
				UserContestRank champ = new UserContestRank();
				champ.setCreatedTime(new Date());
				champ.setRank(1);
				champ.setSession(CURRENT_SESSION);
				champ.setUserId(playerPair.getWinnerUserId());
				
				contestRankDao.saveContestRank(champ);
				
				// 给压中冠军的用户发放奖励
				giveBetRewards(playerPair.getWinnerUserId());

			}
			
			// 选择输的一方
			String loseUserId = playerPair.getLoseUserId();
			userContestRank.setUserId(loseUserId);
			
			userContestRank.setRank(ContestHelper.getRankByRound(round));
			userContestRank.setSession(CURRENT_SESSION);
			userContestRank.setCreatedTime(new Date());
			
			contestRankDao.saveContestRank(userContestRank);
		}
	}

	private void saveContestResult(List<ContestPlayerPair> playerPairList, int round) {
		for (ContestPlayerPair playerPair : playerPairList) {
			contestPlayerPairDao.updatePlayerPairFightResult(playerPair);
		}
	}

	

	/**
	 * 两个玩家对战
	 */
	private boolean playerPairFight(final ContestPlayerPair playerPair) {
		
		final String attackUserId = playerPair.getAttUserId();
		final String defenseUserId = playerPair.getDefUserId();
		LOG.debug("playerPairFight attUserId[" + attackUserId + "] defUserId[" + defenseUserId + "]");
		// 设置进攻方信息
		BattleBO attack = new BattleBO();
		User attackUser = userService.get(attackUserId);
		List<BattleHeroBO> attackHeroBOList = this.heroService.getUserBattleHeroBOList(attackUser.getUserId());
		attack.setAddRatio(0);
		attack.setUserLevel(attackUser.getLevel());
		attack.setBattleHeroBOList(attackHeroBOList);
		
		// 设置防守方信息
		BattleBO defense = new BattleBO();
		
		if (playerPair.getDefUserId() == null || playerPair.getDefUserId().equals("")) {
			playerPair.setResult(ContestConstant.ATTACK_USER_WIN);
		} else {
			User defenseUser = userService.get(defenseUserId);
			List<BattleHeroBO> defenseHeroBOList = this.heroService.getUserBattleHeroBOList(defenseUser.getUserId());
			defense.setAddRatio(0);
			defense.setBattleHeroBOList(defenseHeroBOList);
			defense.setUserLevel(defenseUser.getLevel());
		
		
			battleService.fight(attack, defense, 7, new EventHandle() {
				
				@Override
				public boolean handle(Event event) {
					int flag = event.getInt("flag");
					String fightReport = event.getString("report");
					if (flag == 1 || flag == 0) { // 当双方打成平局时，默认为进攻方获胜
						 playerPair.setResult(ContestConstant.ATTACK_USER_WIN);
					} else {
						playerPair.setResult(ContestConstant.DEFENSE_USER_WIN);
					}
					
					if (playerPair.getResult() == 0) {
						String message = "playerPair fight error attUserId[" + attackUserId + "] defUserId[" + defenseUserId + "]";
						throw new ServiceException(4003, message);
					}
					
					saveFightReport(playerPair, flag, fightReport);
					return true;
				}
			});
		}

		return true;
	}
	
	private void saveFightReport(ContestPlayerPair playerPair, int flag, String fightReport) {
		ContestFightResult fightResult = new ContestFightResult();
		BeanUtils.copyProperties(playerPair, fightResult);
		fightResult.setReport(fightReport);
		fightResult.setResult(flag);
		contestFightResultDao.save(fightResult);
	}
	
	@Override
	public Map<String, Object> enterReg() {
		checkIsRegisterEnded();
		return packRegisterInfo();
	}

	private Map<String, Object> packRegisterInfo() {
		Map<String, Object> rt = new HashMap<String, Object>();

		List<ContestTeamInfoBO> teamInfoBOList = createTeamInfoBOList();
		
		rt.put("ct", ContestHelper.getRegEndDate());
		rt.put("tl", teamInfoBOList);
		rt.put("se", CURRENT_SESSION);
		
		return rt;
	}

	private void checkIsRegisterEnded() {
		
		Date regEndDate = ContestHelper.getRegEndDate();
		Date now = new Date();
		
		if (now.after(regEndDate)) {
			throw new ServiceException(ContestService.REG_ENDED, "已经过了报名时间");
		}
		
	}

	
	
	/**
	 * 读取出四个报名小组，如果不存在，则创建小组供玩家报名
	 * @return
	 */
	private List<ContestTeam> getTeamsForRegister() {
		int session = CURRENT_SESSION;
		List<ContestTeam> teamList = new ArrayList<ContestTeam>();
		
		for (int teamId : ContestConstant.REG_TEAM_IDS) {
			ContestTeam team = contestTeamDao.getTeamByTeamId(session, teamId);
			if (team == null) {
				team = createTeamForRegister(session, teamId);
				contestTeamDao.saveTeam(team);
			}
			teamList.add(team);
		}
		
		return teamList;
	}

	private ContestTeam createTeamForRegister(int session, int teamId) {
		String contestId = IDGenerator.getID();
		String teamName = ContestHelper.getTeamNameByTeamId(teamId);
		ContestTeam team = new ContestTeam(contestId, session, teamId, teamName, new Date());
		team.setCapacity(ContestConstant.REG_TEAM_CAPACITY);
		return team;
	}

	@Override
	public Map<String, Object> register(String userId) {
		checkRegisterRequirement(userId);
		
		Map<String, Object> rt = addUserToRandomTeam(userId);

		pushRegister(userId);
		return rt;
	}

	private Map<String, Object> addUserToRandomTeam(String userId) {
		
		while(!isAllTeamsFull()) {
			ContestTeam registerTeam =  selectTeamForRegister();

			if(!isTeamFull(registerTeam)) {
				addUserToTeam(userId, registerTeam);
				
				
				LOG.debug("添加玩家 userId[" + userId + "], 队伍名[" + registerTeam.getTeamName() + "], 队伍中现有人数[" + registerTeam.getPlayerNum() + "]") ;
				break;
			}
		}
		
		return createRegisterReturnData();
	}

	/**
	 * 查看是否所有小组的人数已满
	 * @return
	 */
	private boolean isAllTeamsFull() {
		List<ContestTeam> teams = getTeamsForRegister();
	
		for (ContestTeam team : teams) {
			 if (!isTeamFull(team)) {
				 return false;
			 }
		}

		return true;
	}

	private void addUserToTeam(String userId, ContestTeam team) {

		int capacity = team.getCapacity();
		int playerNum = team.getPlayerNum();
		
		if (playerNum < capacity/2) {
			addNewPlayerPairToTeam(userId, team);
		} else {
			List<ContestPlayerPair> playerPairs = contestPlayerPairDao.getPlayerPairsByContestId(team.getContestId());
			for (ContestPlayerPair playerPair : playerPairs) {
				if (playerPair.isFull() == false) {
					addToPlayerPair(userId, playerPair);
					break;
				}
			}
		}
		
		team.incrementMembCount();
		contestTeamDao.updateMembCount(team);
	}

	private void addNewPlayerPairToTeam(String userId, ContestTeam team) {
		ContestPlayerPair newPlayerPair = new ContestPlayerPair(team.getContestId(), IDGenerator.getID(), CURRENT_SESSION, team.getTeamId(), new Date());
		addToPlayerPair(userId, newPlayerPair);
	}
	
	
	private void addToPlayerPair(String userId, ContestPlayerPair playerPair) {

		if (playerPair.getAttUserId() == null) {

			playerPair.setAttUserId(userId);
			playerPair.setAttUserName(getUserName(userId));
			contestPlayerPairDao.savePlayerPair(playerPair);
			
		} else if (playerPair.getDefUserId() == null) {

			playerPair.setDefUserId(userId);
			playerPair.setDefUserName(getUserName(userId));
			contestPlayerPairDao.saveDefUser(playerPair);

		} else {
			throwPlayerPairFullError(userId, playerPair);
		}
	}

	private String getUserName(String userId) {
		User user = userService.get(userId);
		return user.getUsername();
	}


	private void throwPlayerPairFullError(String userId,
			ContestPlayerPair playerPair) {
		String message = "擂台赛添加用户失败, contestPlayerPair 已满。userId[" + userId + "] attUid[" + playerPair.getAttUserId() + "] defUid[" + playerPair.getDefUserId() +"]";
		int errorCode = ContestService.PLAYER_PAIR_FULL;
		throw new ServiceException(errorCode, message);
	}

	private void checkRegisterRequirement(String userId) {
		
		checkIsRegisterEnded();
		
		isUserRegistered(userId);
		
		if (isAllTeamsFull()) {
			throwRegisterAllTeamFullError(userId);
		}
	}

	private void throwRegisterAllTeamFullError(String userId) {
		String message = "参赛人数已满，不能报名.userId[" + userId  + "]";
		int errorCode = ContestService.USER_FULL;
		throw new ServiceException(errorCode, message);
	}
	
	private Map<String, Object> createRegisterReturnData() {
		Map<String, Object> rt = new HashMap<String, Object>();

		long countdown = ContestHelper.getRoundStartCountdown(ContestConstant.ROUND_128);
		
		rt.put("cd", countdown);
		rt.put("tl", createTeamInfoBOList());
		return rt;
	}

	private void isUserRegistered(String userId) {
		List<ContestTeam> regTeamList = getTeamsForRegister();
		
		for (ContestTeam team : regTeamList) {
			
			List<ContestPlayerPair> playerPairList = contestPlayerPairDao.getPlayerPairsByContestId(team.getContestId());
			
			if (isUserInThisPlayerPairList(userId, playerPairList)) {
				throw new ServiceException(ContestService.USER_ALREADY_REGISTERED, "用户已经报名 userId[" + userId + "]");
			}
		}
	}

	private boolean isUserInThisPlayerPairList(String userId, List<ContestPlayerPair> playerPairList) {
		for (ContestPlayerPair playerPair : playerPairList) {
			if (userId.equals(playerPair.getAttUserId()) || userId.equals(playerPair.getDefUserId())) {
				return true;
			}
		}
		return false;
	}

	private void pushRegister(String userId) {
		Command command = new Command();
		command.setCommand(CommandType.COMMAND_CONTEST_REG);
		command.setType(CommandType.PUSH_ALL);
		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", userId);
		command.setParams(params);
		commandDao.add(command);
	}
	
	@Override
	public List<ContestTeamInfoBO> createTeamInfoBOList() {
		List<ContestTeamInfoBO> teamInfoBOList = new ArrayList<ContestTeamInfoBO>();
		List<ContestTeam> teamsList = contestTeamDao.getTeamList();
		
		for (ContestTeam team : teamsList) {
			ContestTeamInfoBO teamInfoBO = BOHelper.createContestTeamInfoBO(team);
			teamInfoBOList.add(teamInfoBO);
		}
		
		return teamInfoBOList;
	}
	
	/**
	 * 随机选择一个小组，返回该小组的 id
	 * @return
	 */
	private ContestTeam selectTeamForRegister() {
		
		List<ContestTeam> teamsForRegister = getTeamsForRegister();
		
		Comparator<ContestTeam> comparator = new Comparator<ContestTeam>() {
			
			@Override
			public int compare(ContestTeam o1, ContestTeam o2) {
				int i = o1.getPlayerNum() - o2.getPlayerNum();
				if (i == 0) {
					return o1.getTeamId() - o2.getTeamId();
				} else {
					return i;
				}
			}
		};
		
		Collections.sort(teamsForRegister, comparator);
		
		return teamsForRegister.get(0);
	}

	

	/**
	 * 检查某个小组人数是否已满
	 */
	private boolean isTeamFull(ContestTeam team) {

		int teamCapacity = ContestConstant.REG_TEAM_CAPACITY;
		
		if (team.getPlayerNum() < teamCapacity) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public Map<String, Object> enterVersus(String uid) {
		
		List<ContestTeamBO> teamBOList = createTeamBOList(uid);
		return packEnterVersusReturnData(teamBOList);

	}

	private Map<String, Object> packEnterVersusReturnData(List<ContestTeamBO> teamBOList) {
		Map<String, Object> rt = new HashMap<String, Object>();

		ContestRound contestRound = getCurrentRound();

		
		if (contestRound.getRoundId() == 5) {
			rt.put("rid", 4);
		} else {
			rt.put("rid",contestRound.getRoundId());
		}
		rt.put("tl", teamBOList);
		rt.put("st", ContestHelper.getDayOfContestByRound(contestRound.getRoundId()));
		rt.put("se", CURRENT_SESSION);

		return rt;
	}

	private List<ContestTeamBO> createTeamBOList(String uid) {
		List<ContestTeamBO> teamBOList = new ArrayList<ContestTeamBO>();

		
		//对于128强的比赛，获取现在登录用户所在的小组，如果登录用户都不在这些组中，随机选择一个小组返回
		ContestTeamBO teamBO = createTeamBOForRound128(uid);
		teamBOList.add(teamBO);
		
		
		// 其他小组
		List<ContestTeam> teamList = contestTeamDao.getTeamListBySession(CURRENT_SESSION);

		for (ContestTeam team : teamList) {
			int teamId = team.getTeamId();
			
			if (teamId == 12801 || teamId == 12802 || teamId == 12803 || teamId == 12804) {
				continue;
			}
			
			ContestTeamBO bo = createTeamBO(team, uid);
			teamBOList.add(bo);
		}
		return teamBOList;
	}

	private ContestTeamBO createTeamBOForRound128(String uid) {
		ContestTeam teamContainsLoginUser = getTeamContainsLoginUser(uid);
		ContestTeamBO teamBO = createTeamBO(teamContainsLoginUser, uid);
		return teamBO;
	}
	
	/**
	 * 对于128强的比赛，获取现在登录用户所在的小组，如果登录用户都不在这些组中，随机选择一个小组返回
	 * @param uid
	 * @return
	 */
	private ContestTeam getTeamContainsLoginUser(String uid) {
		List<ContestTeam> regTeamList = getTeamsForRegister();

		for (ContestTeam team : regTeamList) { 

			boolean contains = isContainsLoginUser(uid, team);
			
			if (contains) {
				return team;
			}
		}
		
		/*
		 * 从128强比赛的四个小组（青龙、白虎等等）中，随机选择一个 
		 */
		int i = RandomUtils.nextInt(3);
		return regTeamList.get(i);
	}
	
	private boolean isContainsLoginUser(String uid, ContestTeam team) {
		List<ContestPlayerPair> playerPairList = contestPlayerPairDao.getPlayerPairsByContestId(team.getContestId());
		
		for (ContestPlayerPair playerPair : playerPairList) {
			
			if (uid.equals(playerPair.getAttUserId())) {
				return true;
			} else if (uid.equals(playerPair.getDefUserId())) {
				return true;
			} else {
				
			}
		}
		return false;
	}
	
	/**
	 * 计算现在处于哪一场比赛。如果这一届比赛的决赛已经打完了，则返回决赛
	 * @return
	 */
	@Override
	public ContestRound getCurrentRound() {
		
		int dayOfWeek = DateUtils.getDayOfWeek();
		
//		int[] roundsOfTheDay = ContestHelper.getRoundsByDay(dayOfWeek);
		
		for (int round : ContestConstant.ROUNDS) {
			long roundStartCountdown = ContestHelper.getRoundStartCountdown(round);
			if (roundStartCountdown > 0) {
				ContestRound contestRound = new ContestRound(round, ContestHelper.getRoundStartDate(round).getTime());
				return contestRound;
			} 
		}
		
		return new ContestRound(ContestConstant.ROUND_1, 0);
	}

	private ContestTeamBO createTeamBO(ContestTeam team, String uid) {
		ContestTeamBO teamBO = new ContestTeamBO();

		List<ContestPlayerPairBO> playerPairBOList = createPlayerPairBOList(team, uid);
		teamBO.setContestPairBOList(playerPairBOList);
		
		if (team.isFighted()) {
			teamBO.setRoundStartTime(0);
		} else if (team.getTeamId() == 0 && ContestHelper.isBetTimeEnd() == true) {
			Date betEndDate = ContestHelper.getBetEndDate();
			Date now = new Date();
			if (now.after(betEndDate)) {
				teamBO.setRoundStartTime(0);
			} 
		}else {
			
			ContestRound currentRound = getCurrentRound();
			teamBO.setRoundStartTime(currentRound.getRoundStartTime());
		}
		teamBO.setId(team.getTeamId());
		teamBO.setName(team.getTeamName());
			
		return teamBO;
	}

	private List<ContestPlayerPairBO> createPlayerPairBOList(ContestTeam team, String uid) {
		List<ContestPlayerPair> playerPairList = contestPlayerPairDao.getPlayerPairsByContestId(team.getContestId());
		List<ContestPlayerPairBO> playerPairBOList = new ArrayList<ContestPlayerPairBO>();
		
		for (ContestPlayerPair playerPair : playerPairList) {
			ContestPlayerPairBO playerPairBO = new ContestPlayerPairBO();
			BeanUtils.copyProperties(playerPair, playerPairBO);
			
			if (uid.equals(playerPair.getAttUserId())) {
				playerPairBO.setIsLoginUserIncluded(1);
			} else if (uid.equals(playerPair.getDefUserId())) {
				playerPairBO.setIsLoginUserIncluded(2);
			} else {
				playerPairBO.setIsLoginUserIncluded(0);
			}
			
			playerPairBOList.add(playerPairBO);

		}
		return playerPairBOList;
	}

	@Override
	public void bet(String userId, String betOnUserId) {
		User user = userService.get(userId);
		int teamId = ContestHelper.getTeamIdByRound(ContestConstant.ROUND_BET);
		ContestTeam team = contestTeamDao.getTeamByTeamId(CURRENT_SESSION, teamId);
		List<ContestPlayerPair> playerPairList= contestPlayerPairDao.getPlayerPairsByContestId(team.getContestId());

		checkBetCondition(user, playerPairList);
		
		for (ContestPlayerPair playerPair : playerPairList) {
			String attUserId = playerPair.getAttUserId();
			String defUserid = playerPair.getDefUserId();
			
			if (attUserId.equals(betOnUserId)) {

				playerPair.incrAttUserBetNum();
				contestPlayerPairDao.incrAttUserBetNum(playerPair);
			} else if (defUserid != null && defUserid.equals(betOnUserId)) {
				playerPair.incrDefUserBetNum();
				contestPlayerPairDao.incrDefUserBetNum(playerPair);
				
			} else {
				continue;
			}
			UserContestBetLog newBetLog = new UserContestBetLog(CURRENT_SESSION, userId, betOnUserId, new Date());
			contestBetDao.saveContestBetLog(newBetLog);
			
			userService.reduceCopper(userId, ContestConstant.COPPER_NEED_TO_BET, ToolUseType.REDUCE_CONTEST_BET);

			pushBet(userId);
		
		}
	}
	
	

	private void checkBetCondition(User user, List<ContestPlayerPair> playerPairList) {
		String userId = user.getUserId();
		checkIsTimeToBet();
		checkUserCopperEnoughToBet(user);
		checkUserAlreadyBeted(user.getUserId());
		
		for (ContestPlayerPair playerPair : playerPairList) {
			String attUserId = playerPair.getAttUserId();
			String defUserid = playerPair.getDefUserId();
			
			// 检查下注的用户是不是十六强玩家，如果是抛出异常
			if (userId.equals(attUserId) || userId.equals(defUserid)) {
				throw new ServiceException(ContestService.USER_CANNOT_BET, "用户是16强之一，只有16强以外的玩家可以下注");
			}
		}
			
	}

	private void checkIsTimeToBet() {
		if (!ContestHelper.isTimeToBet()) {
			throw new ServiceException(ContestService.NOT_BET_TIME, "还未到下注时间");
		}
	}

	private void pushBet(String userId) {
		Command command = new Command();
		command.setCommand(CommandType.COMMAND_CONTEST_BET_SUCCESS);
		command.setType(CommandType.PUSH_ALL);
		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", userId);
		command.setParams(params);
		commandDao.add(command);
	}

	private void checkUserAlreadyBeted(String userId) {
		UserContestBetLog betLog = contestBetDao.getUserBetLog(CURRENT_SESSION, userId);
		
		if (betLog != null) {
			String message = "用户已经下注 userId[" + userId + "] 被下注用户 betOnUserId[" + betLog.getBetOnUserId() + "]";
			throw new ServiceException(ContestService.USER_ALREADY_BET, message);
		}
	}

	private void checkUserCopperEnoughToBet(User user) {
		if (user.getCopper() < ContestConstant.COPPER_NEED_TO_BET) {
			throw new  ServiceException(ContestService.USER_COPPER_NOT_ENOUGH, "用户银币不足，不能下注 userId[" + user.getUserId() + "]");
		}
	}

	@Override
	public ContestTeamBO createPushBetTeamBO(String userId) {
		ContestTeam teamForBet = contestTeamDao.getTeamByTeamId(CURRENT_SESSION, ContestHelper.getTeamIdByRound(ContestConstant.ROUND_BET));
		ContestTeamBO teamBO = createTeamBO(teamForBet, userId);
		return teamBO;
	}
	
	
	@Override
	public List<ContestRankBO> getLastSessionRank() {
		
		List<ContestRankBO> rankBOList = new ArrayList<ContestRankBO>();
		int currentSession = CURRENT_SESSION;

		List<UserContestRank> rankList = contestRankDao.getLastSessionRank(currentSession - 1);
		
		if (rankList != null) {
			
			for (UserContestRank rank : rankList) {
				ContestRankBO rankBO = createRankBO(rank);
				if (rankBO != null) {
					rankBOList.add(rankBO);
				}
			}
		}
		
		return rankBOList;
	}

	private ContestRankBO createRankBO(UserContestRank rank) {

		String userId = rank.getUserId();
		if (userId != null && userId != "") {
			ContestRankBO rankBO = new ContestRankBO();

			User user = userService.get(userId);
	
			rankBO.setRank(rank.getRank());
			rankBO.setUserLevel(user.getLevel());
			rankBO.setUserName(user.getUsername());
			rankBO.setUserVipLevel(user.getVipLevel());
			List<UserHeroBO> userHeroList = heroService.getUserHeroList(user.getUserId(), 1);
			rankBO.setUserHeroBO(userHeroList.get(0));
			return rankBO;

		}
		return null;

	}

	@Override
	public Map<String, Object> enterRecReward(String userId) {
		Map<String, Object> rt = new HashMap<String, Object>();
		
		ContestRound currentRound = getCurrentRound();
		// 已经结束的是第几轮的比赛（一共是周二、周四和周六三天）
		int dayOfContest = ContestHelper.getDayOfContestByRound(currentRound.getRoundId()-1);
		List<ContestReward> rewardList = contestRewardDao.getContestRewardListByDay(dayOfContest);
		
		List<ContestRewardBO> rewardBOList = createRewardBOList(userId, rewardList, currentRound.getRoundId(), dayOfContest);
		
		
		if (dayOfContest == 3) {
			rt.put("st", dayOfContest);

		} else {
			rt.put("st", dayOfContest + 1);
		}
		rt.put("cd", currentRound.getRoundStartTime());
		rt.put("se", CURRENT_SESSION);
		rt.put("cls", rewardBOList);
		
		return rt;
	}

	private List<ContestRewardBO> createRewardBOList(String userId, List<ContestReward> rewardList, int currentRound, int dayOfContest) {

		List<ContestRewardBO> rewardBOList = new ArrayList<ContestRewardBO>();
		
		for (ContestReward reward : rewardList) {
			if (reward.getRewardId() != 0) {
				ContestRewardBO rewardBO = new ContestRewardBO();
				
				if (!isWinner(userId, reward.getRewardId())) {
					rewardBO.setFlag(1);
				} else if (!isRewardReceived(userId, CURRENT_SESSION, reward.getRewardId())) {
					rewardBO.setFlag(0);
				} else {
					rewardBO.setFlag(1);
				}
				
				List<DropToolBO> dropTool = DropToolHelper.parseDropTool(reward.getDropToolIds());
				
				rewardBO.setId(reward.getRewardId());
				rewardBO.setDayOfContest(dayOfContest);
				rewardBO.setDropToolBOList(dropTool);
				
				rewardBOList.add(rewardBO);
			}
		}
		
		return rewardBOList;
	}
	
	private boolean isRewardReceived(String userId, int session, int rewardId) {
		UserRecContestRewardLog recLog = contestRewardDao.getUserRecRewardLog(userId, CURRENT_SESSION, rewardId);
		return (recLog == null) ? false : true;
	}

	@Override
	public CommonDropBO recReward(String uid, int rewardId) {
		checkRecRewardCondition(uid, rewardId);
		
		ContestReward reward = contestRewardDao.getContestReward(rewardId);
		int currentSession = CURRENT_SESSION;
		
		UserRecContestRewardLog recLog = new UserRecContestRewardLog(uid, currentSession, rewardId);
		recLog.setDescription(reward.getDescription());
		
		List<DropToolBO> dropTools = DropToolHelper.parseDropTool(reward.getDropToolIds());
		CommonDropBO commonDropBO = new CommonDropBO();
		
		for (DropToolBO dropToolBO : dropTools) {
			List<DropToolBO> dropToolBOList = toolService.giveTools(uid, dropToolBO.getToolType(), dropToolBO.getToolId(), dropToolBO.getToolNum(), ToolUseType.CONTEST_REWARD);
			for (DropToolBO db : dropToolBOList) {
				toolService.appendToDropBO(uid, commonDropBO, db);
			}
		}

		contestRewardDao.saveUserRecRewardLog(recLog);

		return commonDropBO;
	}

	private void checkRecRewardCondition(String uid, int rewardId) {
		
		boolean isUSerWinner = isWinner(uid, rewardId);
		
		if (!isUSerWinner) {
			throw new ServiceException(ContestService.USER_NOT_WIN, "玩家没有赢得这场比赛，不可以领取奖励 userId[" + uid + "] rewardId[" + rewardId + "]");
		}
		
		if (isRewardReceived(uid, CURRENT_SESSION, rewardId)) {
			throw new ServiceException(ContestService.USER_RECEIVED_REWARD, "玩家已经领取过奖励 userId[" + uid + "] rewardId[" + rewardId + "]");
		}
	}

	private boolean isWinner(String uid, int rewardId) {
		boolean isUSerWinner = false;
		
		if (rewardId == 8) {
			for (int teamId : ContestConstant.REG_TEAM_IDS) {
				isUSerWinner = isUserWinner(uid, teamId);
				if (isUSerWinner) {
					break;
				} else {
					continue;
				}
			}
		} else {
			int teamId = ContestHelper.getTeamIdByRewardId(rewardId);
			isUSerWinner = isUserWinner(uid, teamId);
		}
		return isUSerWinner;
	}

	private boolean isUserWinner(String uid, int teamId) {
		ContestTeam team = contestTeamDao.getTeamByTeamId(CURRENT_SESSION, teamId);
		List<ContestPlayerPair> playerPairList = contestPlayerPairDao.getPlayerPairsByContestId(team.getContestId());
		for (ContestPlayerPair playerPair : playerPairList) {
			if (uid.equals(playerPair.getAttUserId()) && playerPair.getResult() == ContestConstant.ATTACK_USER_WIN) {
				return true;
			} else if (uid.equals(playerPair.getDefUserId()) && playerPair.getResult() == ContestConstant.DEFENSE_USER_WIN) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public int getRoundFinishedStatus(int round) {
		return ContestHelper.isRoundsOfTheDayFinished(round);
	}

	public void setContestTeamDaoCacheImpl(ContestTeamDao contestTeamDaoCacheImpl) {
		this.contestTeamDao = contestTeamDaoCacheImpl;
	}
	
	private void pushStatusChange() {
		Command command = new Command();
		command.setCommand(CommandType.COMMAND_CONTEST_STATUS_CHANGED);
		command.setType(CommandType.PUSH_ALL);
		Map<String, String> params = new HashMap<String, String>();
		command.setParams(params);
		commandDao.add(command);
	}
	
	

	private void setCurrentSession() {
		if (WEEK_OF_THE_YEAR < DateUtils.getWeekOfYear()) {
				CURRENT_SESSION += 1; 
				WEEK_OF_THE_YEAR = DateUtils.getWeekOfYear();
				LAST_ROUND = -1;
		}
	}

	private void initContestTeamCache() {
		contestTeamDao.getTeamListBySession(CURRENT_SESSION);
	}


	private void checkRoundStartAndFight() {
		for (int round : ContestConstant.ROUNDS) {
			if (round != ContestConstant.ROUND_BET) {
				long roundStartCountdown = ContestHelper.getRoundStartCountdown(round);
				if (roundStartCountdown <= 0 && round > LAST_ROUND) {
					contestFight(round);
				}
			}
		}
	}
	
	private void setLastRound() {
		ContestTeam team = contestTeamDao.getLatestCreatedTeam(CURRENT_SESSION);
		
		if (team != null) {
			int currentRound = ContestHelper.getRoundByTeamId(team.getTeamId());
			if (!team.isFighted()) {
				LAST_ROUND = currentRound - 1;
			} else {
				LAST_ROUND = currentRound;
			}
		}
	}

	private void checkStatusChangeAndPush() {
		
		// 推送进入报名界面
		if (ContestHelper.isTimeToEnterReg()) {
			if (CONTEST_STATUS != ENTER_REG) {
				pushStatusChange();
				contestTeamDao.clearTeamMapCache();
				contestBetDao.clearBetCache();
				CONTEST_STATUS = ENTER_REG;
			}
		}
		
		// 推送进入战斗队列界面
		if (ContestHelper.isTimeToEnterVersus()) {
			if (CONTEST_STATUS != ENTER_VERSUS) {
				pushStatusChange();
				CONTEST_STATUS = ENTER_VERSUS;
			}
		}

		// 推送进入领奖界面
		if (ContestHelper.isTimeToEnterRecReward()) {
			if (CONTEST_STATUS != ENTER_REC_REWARD) {
				pushStatusChange();
				CONTEST_STATUS = ENTER_REC_REWARD;
			}
		}
	}
	
	@Override
	public List<ContestTeamBO> getRoundResult(String uid, int round) {
		List<ContestTeamBO> teamBOList = new ArrayList<ContestTeamBO>();
		
		if (round == ContestConstant.ROUND_128) {
			ContestTeam teamContainsLoginUser = getTeamContainsLoginUser(uid);
			ContestTeamBO teamBO = createTeamBO(teamContainsLoginUser, uid);
			teamBOList.add(teamBO); 
		} else {
			int session = CURRENT_SESSION;
			int teamId = ContestHelper.getTeamIdByRound(round);
			ContestTeam team = contestTeamDao.getTeamByTeamId(session, teamId);
			ContestTeamBO teamBO = createTeamBO(team, uid);
			teamBOList.add(teamBO); 
		}
		
		ContestTeam teamsForFight = getTeamForNextFight(round);
		ContestTeamBO teamBO = createTeamBO(teamsForFight, uid);
		teamBOList.add(teamBO);

		return teamBOList;
	}

	private ContestTeam getTeamForNextFight(int round) {
		
		int nextRound = ContestHelper.getNextRound(round);
		// 如果下一场比赛是下注，则实际上读取的是第8场比赛的队伍
		if (nextRound == ContestConstant.ROUND_BET) {
			nextRound = ContestHelper.getNextRound(ContestConstant.ROUND_BET);
		}
		
		int teamIdForNextFight = ContestHelper.getTeamIdByRound(nextRound);
		return contestTeamDao.getTeamByTeamId(CURRENT_SESSION, teamIdForNextFight);
	}

	@Override
	public Map<String, Object> getUserInfo(String userId) {
		Map<String, Object> rt = new HashMap<String, Object>();
		
		User user = userService.get(userId);
		
		rt.put("nk", user.getUsername());
		rt.put("pid", user.getLodoId());
		rt.put("level", user.getLevel());
		List<UserHeroBO> userHeroList = heroService.getUserHeroList(userId, 1);
		rt.put("hls", userHeroList);
		return rt;
	}
	
	public void init() {
		CURRENT_SESSION = getLatestSession();
		WEEK_OF_THE_YEAR = DateUtils.getWeekOfYear();
		
		setLastRound();

		Thread contestThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					setCurrentSession();

					if (CURRENT_SESSION != 0) {
						checkStatusChangeAndPush();
						checkRoundStartAndFight();
					}
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
				
			}
		});
		contestThread.setName("contest-thread");
		contestThread.start();
		initContestTeamCache();
	}

	@Override
	public BattleStartBO createContestBattleStartBo() {
		
		return null;
	}

	@Override
	public ContestFightResultBO getReport(String contestId) {
		ContestFightResult fightResult = contestFightResultDao.get(contestId);
		
		if (fightResult == null) {
			String message = "战报不存在 contestId[" + contestId + "]";
			throw new ServiceException(ContestService.FIGHT_REPORT_NOT_EXIST, message);
		}
		ContestFightResultBO fightResultBO = new ContestFightResultBO();
		BeanUtils.copyProperties(fightResult, fightResultBO);
		
		return fightResultBO;
	}
	
	private int getLatestSession() {
		ContestTeam latestTeam = contestTeamDao.getLatestTeam();
		if (latestTeam != null) {
			return latestTeam.getSession();
		}
		return 0;
	}
	
	private boolean isUserReceiveBetReward(String userId) {
		UserContestBetReward reward = userContestBetRewardDao.get(userId, CURRENT_SESSION);
		if (reward != null) {
			return false;
		}
		return true;
	}
}
