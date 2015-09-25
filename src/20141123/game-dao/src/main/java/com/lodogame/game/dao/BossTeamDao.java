/**
 * BossTeamDao.java
 *
 * Copyright 2013 Easou Inc. All Rights Reserved.
 *
 */

package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.BossTeam;

/**
 * @author <a href="mailto:clact_jia@staff.easou.com">Clact</a>
 * @since v1.0.0.2013-9-25
 */
public interface BossTeamDao {
	boolean isUserAlreadyInBossTeam(String userId);

	/**
	 * 
	 * @param mid
	 *            地图编号
	 * @param userId
	 * @return
	 */
	BossTeam addTeam(int forcesId, String userId);

	/**
	 * 获取队伍
	 * 
	 * @param teamId
	 * @return
	 */
	BossTeam getTeam(String teamId);

	/**
	 * 
	 * @return
	 */
	List<BossTeam> getTeamList();

	/**
	 * 获取一支快速加入(切换的队伍)
	 * 
	 * @param forcesId
	 * @param oldTeamId
	 * @return
	 */
	BossTeam getTeamForQuickStart(int forcesId, String oldTeamId);

	/**
	 * 获取取伍数量
	 * 
	 * @param forcesId
	 * @return
	 */
	int getTeamsCount(int forcesId);

	/**
	 * 删除队伍
	 * 
	 * @param teamId
	 * @return
	 */
	boolean removeTeam(String teamId);

	/**
	 * 添加成员
	 * 
	 * @param teamId
	 * @param userId
	 * @return
	 */
	boolean addMember(String teamId, String userId);

	/**
	 * 删除成员
	 * 
	 * @param teamId
	 * @param userId
	 * @return
	 */
	boolean removeMember(String teamId, String userId);

	/**
	 * 清除用户
	 * 
	 * @param userId
	 * @return
	 */
	boolean clean(String userId);

	/**
	 * 获取某一幅地图中的所有队伍
	 * 
	 * @param mid
	 * @return
	 */
	List<BossTeam> getTeamsByForcesId(int mid);

	/**
	 * 根据地图编号和用户 id 获取封魔小队
	 * 
	 * @param mid
	 * @param userId
	 * @return
	 */
	BossTeam getTeamByUserId(String userId);

	/**
	 * 清除所有地图中所有的队伍
	 */
	void cleanAll();
}
