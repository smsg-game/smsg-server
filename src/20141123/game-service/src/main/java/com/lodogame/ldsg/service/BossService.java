package com.lodogame.ldsg.service;

import java.util.List;

import com.lodogame.ldsg.bo.BossBattleReportBO;
import com.lodogame.ldsg.bo.BossTeamBO;
import com.lodogame.ldsg.bo.BossTeamDetailBO;
import com.lodogame.ldsg.bo.UserBossBO;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.model.BossTeam;

public interface BossService {

	// -------------------------------------------------------------------------------------------

	// 非客户端定制接口

	/**
	 * 获得封魔小队对象
	 * 
	 * @param teamId
	 * @return
	 */
	public BossTeam getBossTeam(String teamId);

	/**
	 * 获得战报
	 * 
	 * @param reportsId
	 * @return
	 */
	public List<BossBattleReportBO> getReports(String reportsId);

	/**
	 * 用户退出小队，该方法主要用于清除指定用户的封魔小队状态信息，客户端不应该调用该方法，其更好的 选择是
	 * {@link BossService#exitTeam(String, String, EventHandle)}。
	 * 
	 * @param userId
	 *            用户编号
	 */
	void exitTeam(String userId);

	// -------------------------------------------------------------------------------------------

	// /**
	// * 获得用户封魔信息
	// *
	// * @param userId
	// * 用户编号
	// * @return 用户封魔信息
	// */
	// UserBossBO getUserBoss(String userId);

	/**
	 * 进入魔怪房间
	 * 
	 * @param userId
	 *            用户编号
	 * @return 已在魔怪房间中的各小队信息
	 */
	@Deprecated
	List<BossTeamBO> accessBossRoom(String userId);

	/**
	 * 创建小队
	 * 
	 * @param userId
	 *            用户编号
	 * @return 小队详细信息
	 */
	List<BossTeamDetailBO> createTeam(String userId, int forcesId);

	/**
	 * 加入小队
	 * 
	 * @param userId
	 *            申请加入小队的用户编号
	 * @param teamId
	 *            小队编号
	 * @param handle
	 * @return 小队详细信息
	 */
	List<BossTeamDetailBO> joinTeam(String userId, String teamId, EventHandle handle);

	/**
	 * 快速加入小队
	 * 
	 * @param userId
	 *            申请快速加入小队操作的用户编号
	 * @param handle
	 * @return 小队详细信息
	 */
	List<BossTeamDetailBO> quickStart(String userId, int forcesId, EventHandle handle);

	/**
	 * 快速更换队伍
	 * 
	 * @param userId
	 *            申请快速更换队伍操作的用户编号
	 * @param teamId
	 *            用户现在所属的小队
	 * @param handle
	 * @return 小队详细信息
	 */
	List<BossTeamDetailBO> quickSwapping(String userId, int forcesId, EventHandle handle);

	/**
	 * 重置用户封魔冷却
	 * 
	 * @param userId
	 *            用户编号
	 */
	void resetCooldown(String userId, int forcesId);

	/**
	 * 退出小队
	 * 
	 * @param userId
	 *            申请退出小队的用户编号
	 * @param teamId
	 *            用户想退出的小队编号
	 * @param handle
	 */
	void exitTeam(String userId, EventHandle handle);

	/**
	 * 解散小队
	 * 
	 * @param teamId
	 *            小队编号
	 */
	void dismissTeam(String teamId, boolean force);

	/**
	 * 解散小队
	 * 
	 * @param captainId
	 *            队长编号
	 */
	void dismissTeamByCaptain(String captainId);

	/**
	 * 踢出玩家
	 * 
	 * @param teamId
	 *            小队编号
	 * @param captainId
	 *            队长用户编号
	 * @param pupilId
	 *            被踢出用户的编号
	 * @param handle
	 */
	void kickoutTeamMember(String captainId, String pupilId, EventHandle handle);

	/**
	 * 开始封魔
	 * 
	 * @param mid
	 *            地图编号
	 * 
	 * @param userId
	 *            用户编号
	 * @param teamId
	 *            小队编号
	 * @param handle
	 */
	void challengeBoss(String userId, EventHandle handle);

	/**
	 * 确认准备封魔
	 * 
	 * @param mid
	 *            地图编号
	 * 
	 * @param userId
	 *            发送确认信息的用户编号
	 */
	void ackPrepareChallengeBoss(String userId, int forcesId);

	/**
	 * 获取已经创建的队伍基本信息
	 * 
	 * @param mid
	 *            地图编号
	 * @return
	 */
	public List<BossTeamBO> getBossTeamInfoList(int mid, String userId);

	public List<UserBossBO> getUserBossList(String uid);

	/**
	 * 判断用户登录时是否在 Boss 战开始时间内
	 */
	public void checkBossStarted();

	/**
	 * 获取 Boss 是否开始信息
	 * 
	 * @return
	 */
	public int getStatus();
}