package com.lodogame.game.dao.impl.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.SystemForcesMonsterDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.model.SystemForcesMonster;

public class SystemForcesMonsterDaoCacheImpl implements SystemForcesMonsterDao, ReloadAble {

	private SystemForcesMonsterDao systemForcesMonsterDaoMysqlImpl;

	private Map<Integer, List<SystemForcesMonster>> forcesMonsterMap = new ConcurrentHashMap<Integer, List<SystemForcesMonster>>();

	public void setSystemForcesMonsterDaoMysqlImpl(SystemForcesMonsterDao systemForcesMonsterDaoMysqlImpl) {
		this.systemForcesMonsterDaoMysqlImpl = systemForcesMonsterDaoMysqlImpl;
	}

	public List<SystemForcesMonster> getForcesMonsterList(int forcesId) {

		if (forcesMonsterMap.containsKey(forcesId)) {
			return forcesMonsterMap.get(forcesId);
		}

		List<SystemForcesMonster> systemForcesMonsterList = null;

		synchronized (SystemForcesMonsterDaoCacheImpl.class) {

			systemForcesMonsterList = this.systemForcesMonsterDaoMysqlImpl.getForcesMonsterList(forcesId);
			if (systemForcesMonsterList != null) {
				forcesMonsterMap.put(forcesId, systemForcesMonsterList);
			}

		}

		return systemForcesMonsterList;
	}

	@Override
	public void reload() {
		forcesMonsterMap.clear();
	}

	@Override
	public void init() {
		ReloadManager.getInstance().register(getClass().getSimpleName(), this);
	}

}
