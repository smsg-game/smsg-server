package com.lodogame.ldsg.service;

import java.util.List;
import java.util.Map;

import com.lodogame.ldsg.bo.BattleStartBO;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.ContestFightResultBO;
import com.lodogame.ldsg.bo.ContestRankBO;
import com.lodogame.ldsg.bo.ContestTeamBO;
import com.lodogame.ldsg.bo.ContestTeamInfoBO;
import com.lodogame.model.ContestPlayerPair;
import com.lodogame.model.ContestRound;
import com.lodogame.model.ContestTeam;

public interface ContestService {
	

	/**
	 * 进入报名界面
	 */
	public static final int REG_ENDED = 2001; 
	
	/**
	 * 用户等级未到40级，不能报名
	 */
	public static final int USER_LEVEL_NOT_ENOUGH = 2001;
	
	/**
	 * 报名 - 参赛人数已满，不能报名
	 */
	public static final int USER_FULL = 2001; 
	
	/**
	 * 报名 - 用户已经报名
	 */
	public static final int USER_ALREADY_REGISTERED = 2002;
	
	/**
	 * 进入对战列表 - 选择对战列表界面失败
	 */
	public static final int  GET_CURRENT_ROUND_FAILED = 2001;

	/**
	 * 进入对战列表 - 不能获取当天的比赛
	 */
	public static final int GET_ROUNDS_BY_DAY_ERROR = 2002;
	
	/**
	 * 领奖 - 玩家没有赢得这场比赛，不可以领奖
	 */
	public static final int USER_NOT_WIN = 2001;
	
	/**
	 * 领奖 - 玩家已经领取过奖励
	 */
	public static final int USER_RECEIVED_REWARD = 2002;

	/**
	 * 下注 - 用户元宝不足
	 */
	public static final int USER_COPPER_NOT_ENOUGH = 2001;
	
	/**
	 * 下注 - 用户已经下注
	 */
	public static final int USER_ALREADY_BET = 2002;
	
	/**
	 * 下注 - 还未到下注时间
	 */
	public static final int NOT_BET_TIME = 2003;

	/**
	 * 下注 - 只有16强以外的玩家可以下注
	 */
	public static final int USER_CANNOT_BET = 2004;

	public static final int PLAYER_PAIR_FULL = 4001;

	public static final int SAVE_PLAYER_PAIR_ERROR = 4002;

	public static final int FIRST_SESSION_NOT_OPEN = 2001;

	/**
	 * 战报不存在
	 */
	public static final int FIGHT_REPORT_NOT_EXIST = 2001;


	/**
	 * 获取擂台赛进行到哪个阶段，依次执行下列操作：
	 * <li>检查用户等级是否到达40级，如果没达到返回 USER_LEVEL_NOT_ENOUGH 错误码
	 * <li>
	 * 
	 * @return
	 */
	public int getStatus(String userId);

	/**
	 * 获取登录界面所需的信息，依次执行下列操作：
	 * 
	 * @return
	 * <li>距离报名结束（星期一 24:00）结束还有多长时间
	 * <li>一个列表，包含多个 ContestTeamInfoBO 对象，每个对象中包含这个小组允许容纳的玩家数量和已经进入该组的玩家数量
	 */
	public Map<String, Object> enterReg();

	/**
	 * <h1>玩家报名</h1>
	 * 
	 * <h1>这个函数的执行流程如下：</h1>
	 * 从所有的小组中随机选择一个
	 * <li>如果这个小组人数未满，则将玩家加入该小组</li>
	 * <li>如果这个小组人数已满，则检查是不是所有小组人数已满，1）如果是，返回『人数已满，不能报名』的错误码；2）否走，重复整个选择过程</li
	 * 
	 * @return
	 */
	public Map<String, Object> register(String userId);

	/**
	 * 选择进入对战、战斗、下注中的一个界面
	 * @param uid 
	 * @return
	 */
	public Map<String, Object> enterVersus(String uid);

	public void contestFight(int round);
	
	/**
	 * 下注
	 * @param userId 去下注的用户 id
	 * @param betOnUserId 被下注的用户 id
	 */
	public void bet(String userId, String betOnUserId);

	public List<ContestRankBO> getLastSessionRank();

	/**
	 * 进入领奖界面
	 * @return
	 */
	public Map<String, Object> enterRecReward(String userId);

	/**
	 * 领奖
	 * @param rewardId 
	 * @param string 
	 * @return
	 */
	public CommonDropBO recReward(String string, int rewardId);

	/**
	 * 一场比赛结束时，判断这轮比赛是否结束
	 * <p>0表示一场比赛结束但是这轮比赛还没有结束，1表示一轮比赛结束
	 */
	public int getRoundFinishedStatus(int round);
	
	/**
	 * 
	 * @return
	 */
	public List<ContestTeamInfoBO> createTeamInfoBOList();

	/**
	 * 获取上一场比赛的结果，并且返回下一场比赛的战斗队列。如果上一场比赛是128强，则返回用户所在小组（青龙、白虎等等）的战斗结果，
	 * 如果用户不再其中任何一个小队中，怎随机返回四个小队中的一个。
	 * 
	 * @param uid
	 * @param round 
	 * @return
	 */
	public List<ContestTeamBO> getRoundResult(String uid, int round);

	public ContestRound getCurrentRound();

	public ContestTeamBO createPushBetTeamBO(String userId);

	public Map<String, Object> getUserInfo(String userId);
	
	public BattleStartBO createContestBattleStartBo();

	public ContestFightResultBO getReport(String contestId);

	
}
