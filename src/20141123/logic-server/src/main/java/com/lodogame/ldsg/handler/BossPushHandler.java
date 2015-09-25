/**
 * BossPushHandler.java
 *
 * Copyright 2013 Easou Inc. All Rights Reserved.
 *
 */

package com.lodogame.ldsg.handler;

/**
 * @author <a href="mailto:clact_jia@staff.easou.com">Clact</a>
 * @since v1.0.0.2013-9-29
 */
public interface BossPushHandler {
	/**
	 * 推送魔物出现
	 */
	public void pushBossAppear(String userId);

	/**
	 * 推送魔物消失
	 */
	public void pushBossDisappear(String userId);

	/**
	 * 推送封魔小队成员登出游戏而自动离队
	 * 
	 * @param exitedUserId
	 *            登出的小队成员
	 * @param otherMemberUserId
	 *            另外一个小队成员
	 */
	public void pushBossTeamUserLogout(long exitedPlayerId, String otherMemberUserId, long captainId);

	/**
	 * 推送封魔战斗结果
	 * 
	 * @param userId
	 *            消息目标用户编号
	 * @param reportsId
	 *            战报编号
	 */
	public void pushResultOfChallengeBoss(String userId, String reportsId);

	/**
	 * 推送用户封魔信息
	 * 
	 * @param userId
	 *            用户编号
	 */
	public void pushUserBossInfo(String userId);

	/**
	 * 推送小队解散消息
	 * 
	 * @param teamId
	 *            小队编号
	 * @param cause
	 *            解散原因
	 */
	public void pushDismissTeam(String teamId, int cause);

	/**
	 * 在队伍发生变化时，推送更新后的队伍信息
	 * 
	 * @param teamId
	 * @param status
	 *            状态码，1表示新增队伍，2表示队伍中人数发生改变，3表示队伍解散
	 */
	void pushUpdatedTeamInfo(String teamId, int forcesId, int status);
}
