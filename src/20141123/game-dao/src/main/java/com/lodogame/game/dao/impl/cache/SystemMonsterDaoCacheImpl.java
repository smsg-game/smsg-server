package com.lodogame.game.dao.impl.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.NotImplementedException;

import com.lodogame.game.dao.SystemMonsterDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.model.SystemMonster;

public class SystemMonsterDaoCacheImpl implements SystemMonsterDao, ReloadAble {

	private SystemMonsterDao systemMonsterDaoMysqlImpl;

	private Map<Integer, SystemMonster> monsterMap = new ConcurrentHashMap<Integer, SystemMonster>();

	public void setSystemMonsterDaoMysqlImpl(SystemMonsterDao systemMonsterDaoMysqlImpl) {
		this.systemMonsterDaoMysqlImpl = systemMonsterDaoMysqlImpl;
	}

	@Override
	public SystemMonster get(int monsterId) {

		if (monsterMap.containsKey(monsterId)) {
			return monsterMap.get(monsterId);
		}
		SystemMonster systemMonster = null;


			systemMonster = this.systemMonsterDaoMysqlImpl.get(monsterId);
			if (systemMonster != null) {
				monsterMap.put(monsterId, systemMonster);
			}

		return systemMonster;

	}

	@Override
	public void add(SystemMonster systemMonster) {
		throw new NotImplementedException();
	}

	@Override
	public void reload() {
		monsterMap.clear();
	}

	@Override
	public void init() {
		ReloadManager.getInstance().register(getClass().getSimpleName(), this);
	}

}
