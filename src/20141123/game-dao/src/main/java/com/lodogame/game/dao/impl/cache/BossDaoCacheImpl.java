/**
 * BossDaoCacheImpl.java
 *
 * Copyright 2013 Easou Inc. All Rights Reserved.
 *
 */

package com.lodogame.game.dao.impl.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.BossDao;
import com.lodogame.game.dao.SystemForcesDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.model.Boss;
import com.lodogame.model.SystemForces;

/**
 * @author <a href="mailto:clact_jia@staff.easou.com">Clact</a>
 * @since v1.0.0.2013-9-25
 */
public class BossDaoCacheImpl implements BossDao, ReloadAble {

	private static final Logger LOG = Logger.getLogger(BossDaoCacheImpl.class);

	@Autowired
	private SystemForcesDao systemForcesDao;

	private Map<Integer, Boss> bossMap = new HashMap<Integer, Boss>();

	@Override
	public List<Boss> getBossList() {

		if (bossMap.isEmpty()) {
			List<SystemForces> forcesList = systemForcesDao.getSystemForcesByType(BOSS_FORCE_TYPE);

			if (forcesList != null && forcesList.size() != 0) {
				for (SystemForces systemForces : forcesList) {
					bossMap.put(systemForces.getForcesId(), new Boss(systemForces.getForcesId()));
				}
			}
		}

		Set<Entry<Integer, Boss>> entrySet = bossMap.entrySet();
		Iterator<Entry<Integer, Boss>> iterator = entrySet.iterator();
		List<Boss> bossList = new ArrayList<Boss>();

		while (iterator.hasNext()) {
			bossList.add(iterator.next().getValue());
		}

		return bossList;
	}

	@Override
	public Boss getBossByForcesId(int forcesId) {
		if (this.bossMap.containsKey(forcesId)) {

		}
		return this.bossMap.get(forcesId);
	}

	@Override
	public void reload() {
		bossMap.clear();
	}

	@Override
	public void init() {

	}

}
