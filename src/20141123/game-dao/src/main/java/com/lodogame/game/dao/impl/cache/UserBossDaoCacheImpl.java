/**
 * UserBossDaoCacheImpl.java
 *
 * Copyright 2013 Easou Inc. All Rights Reserved.
 *
 */

package com.lodogame.game.dao.impl.cache;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.UserBossDao;
import com.lodogame.model.UserBoss;

/**
 * @author <a href="mailto:clact_jia@staff.easou.com">Clact</a>
 * @since v1.0.0.2013-9-25
 */
public class UserBossDaoCacheImpl implements UserBossDao {

	/**
	 * key 是用户 id
	 */
	private Map<String, List<UserBoss>> userBossMap = new ConcurrentHashMap<String, List<UserBoss>>();

	/**
	 * 保存用户和地图的对应关系，key 是用户 id
	 */
	private Map<String, Integer> userForcesMap = new ConcurrentHashMap<String, Integer>();

	@Override
	public boolean resetCooldown(String userId, int forcesId) {

		if (userBossMap.containsKey(userId)) {
			List<UserBoss> userBossList = userBossMap.get(userId);
			for (UserBoss userBoss : userBossList) {
				if (userBoss.getForcesId() == forcesId) {
					userBoss.resetCooldown();
				}
			}
		}

		return true;
	}

	@Override
	public UserBoss getBoss(String userId, int forcesId) {

		if (userBossMap.containsKey(userId)) {
			List<UserBoss> userBossList = userBossMap.get(userId);
			for (UserBoss userBoss : userBossList) {
				if (userBoss.getForcesId() == forcesId) {
					return userBoss;
				}
			}
		}

		return null;

	}

	@Override
	public boolean clean() {
		userBossMap.clear();
		userForcesMap.clear();
		return true;
	}

	@Override
	public List<UserBoss> getBossList(String userId) {
		return userBossMap.get(userId);
	}

	@Override
	public void addToList(String userId, List<UserBoss> userBossList) {
		this.userBossMap.put(userId, userBossList);
	}

	@Override
	public void addUserMap(String userId, int forcesId) {
		userForcesMap.put(userId, forcesId);
	}

	@Override
	public int getUserMap(String userId) {
		return userForcesMap.get(userId);
	}

	@Override
	public List<String> getUsers(int forceId) {

		List<String> userIds = new ArrayList<String>();

		Iterator<Entry<String, Integer>> iterator = userForcesMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, Integer> entry = iterator.next();
			if (entry.getValue() == forceId) {
				userIds.add(entry.getKey());
			}
		}
		return userIds;
	}
}
