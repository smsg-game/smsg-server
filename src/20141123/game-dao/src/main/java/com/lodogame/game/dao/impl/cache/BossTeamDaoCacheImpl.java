/**
 * BossTeamDaoCacheImpl.java
 *
 * Copyright 2013 Easou Inc. All Rights Reserved.
 *
 */

package com.lodogame.game.dao.impl.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.lodogame.game.dao.BossTeamDao;
import com.lodogame.model.BossTeam;

/**
 * @author <a href="mailto:clact_jia@staff.easou.com">Clact</a>
 * @since v1.0.0.2013-9-25
 */
public class BossTeamDaoCacheImpl implements BossTeamDao {

	private static final Logger LOG = Logger
			.getLogger(BossTeamDaoCacheImpl.class);

	/**
	 * 所有封魔小队列表，key 是地图编号，value 是每幅地图中的所有小队列表
	 */
	private Map<String, BossTeam> teamsMap = new ConcurrentHashMap<String, BossTeam>();

	/**
	 * 每张地图中，以小队成员（玩家）编号进行索引（The key of map is `UserId`）的封魔小队列表
	 */

	/**
	 * 用户-小队 map
	 */
	private Map<String, String> userTeamsMap = new ConcurrentHashMap<String, String>();

	@Override
	public boolean isUserAlreadyInBossTeam(String userId) {
		return this.userTeamsMap.containsKey(userId);
	}

	@Override
	public BossTeam addTeam(int forcesId, String userId) {

		BossTeam t = new BossTeam(forcesId, userId, new Date());

		teamsMap.put(t.getId(), t);
		userTeamsMap.put(userId, t.getId());

		return t;

	}

	@Override
	public BossTeam getTeam(String teamId) {
		return teamsMap.get(teamId);
	}

	@Override
	public int getTeamsCount(int forcesId) {

		int count = 0;

		for (Entry<String, BossTeam> entry : this.teamsMap.entrySet()) {
			if (entry.getValue().getForcesId() == forcesId) {
				count += 1;
			}
		}

		return count;
	}

	@Override
	public boolean removeTeam(String teamId) {

		BossTeam t = null;
		Collection<String> members = null;

		t = teamsMap.remove(teamId);
		if (t == null) {
			return false;
		}

		members = t.getMembers();

		for (String memId : members) {
			userTeamsMap.remove(memId);
		}

		t.clean();

		return true;
	}

	@Override
	public boolean addMember(String teamId, String userId) {

		BossTeam t = teamsMap.get(teamId);
		if (t == null) {
			return false;
		}

		boolean isAdded = false;

		isAdded = t.addMember(userId);

		if (isAdded) {
			userTeamsMap.put(userId, t.getId());
		}

		return true;
	}

	@Override
	public boolean removeMember(String teamId, String userId) {

		BossTeam t = teamsMap.get(teamId);
		if (t == null) {
			return false;
		}

		boolean isRemoved = false;

		isRemoved = t.removeMember(userId);

		if (isRemoved) {
			userTeamsMap.remove(userId);
		}

		return true;
	}

	@Override
	public boolean clean(String userId) {

		String teamId = userTeamsMap.get(userId);
		BossTeam t = teamsMap.get(teamId);

		if (t != null) {
			removeMember(t.getId(), userId);
		}

		return true;
	}

	@Override
	public List<BossTeam> getTeamsByForcesId(int forcesId) {

		List<BossTeam> list = new ArrayList<BossTeam>();

		for (Entry<String, BossTeam> entry : teamsMap.entrySet()) {

			if (entry.getValue().getForcesId() != forcesId) {
				continue;
			}

			list.add(entry.getValue());
		}

		Collections.sort(list, new Comparator<BossTeam>() {

			@Override
			public int compare(BossTeam t1, BossTeam t2) {
				Date createdTime1 = t1.getCreatedTime();
				Date createdTime2 = t2.getCreatedTime();

				// 将队伍按照创建时间倒序排序，最后创建的队伍在最前面
				return -1 * createdTime1.compareTo(createdTime2);
			}

		});

		return list;
	}

	@Override
	public BossTeam getTeamForQuickStart(int forcesId, String oldTeamId) {

		for (int i = 2; i >= 1; i--) {

			for (Entry<String, BossTeam> etnry : teamsMap.entrySet()) {

				BossTeam bossTeam = etnry.getValue();

				if (bossTeam.getForcesId() != forcesId) {
					continue;
				}

				if (oldTeamId != null && bossTeam.getId().equals(oldTeamId)) {
					continue;
				}

				if (bossTeam.getTeamMemberCount() != i) {
					continue;
				}

				return bossTeam;
			}

		}

		return null;

	}

	@Override
	public List<BossTeam> getTeamList() {

		List<BossTeam> list = new ArrayList<BossTeam>();

		for (Entry<String, BossTeam> entry : teamsMap.entrySet()) {

			list.add(entry.getValue());
		}

		return list;
	}

	@Override
	public BossTeam getTeamByUserId(String userId) {

		if (!this.userTeamsMap.containsKey(userId)) {
			LOG.info("用户没有加入任何一支小队.userId[" + userId + "]");
			return null;
		}

		String teamId = this.userTeamsMap.get(userId);

		if (!this.teamsMap.containsKey(teamId)) {
			LOG.info("用户所在的小组已经被解散.userId[" + userId + "],teamId[" + teamId
					+ "]");
			this.userTeamsMap.remove(userId);
			return null;
		}

		return this.teamsMap.get(teamId);
	}

	@Override
	public void cleanAll() {
		this.userTeamsMap.clear();
		this.teamsMap.clear();
	}
}
