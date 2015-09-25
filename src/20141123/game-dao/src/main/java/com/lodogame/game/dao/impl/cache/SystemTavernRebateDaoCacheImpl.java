package com.lodogame.game.dao.impl.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.SystemTavernRebateDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.model.SystemTavernRebate;

public class SystemTavernRebateDaoCacheImpl implements SystemTavernRebateDao, ReloadAble {

	private Map<Integer, SystemTavernRebate> cacheMap = new ConcurrentHashMap<Integer, SystemTavernRebate>();

	private Map<String, List<SystemTavernRebate>> listMap = new ConcurrentHashMap<String, List<SystemTavernRebate>>();

	private SystemTavernRebateDao systemTavernRebateDaoImpl;

	public void setSystemTavernRebateDaoImpl(SystemTavernRebateDao systemTavernRebateDaoImpl) {
		this.systemTavernRebateDaoImpl = systemTavernRebateDaoImpl;
	}

	@Override
	public SystemTavernRebate get(int id) {
		if (cacheMap.containsKey(id)) {
			return cacheMap.get(id);
		}
		SystemTavernRebate systemTavernRebate = this.systemTavernRebateDaoImpl.get(id);
		if (systemTavernRebate != null) {
			cacheMap.put(id, systemTavernRebate);
		}
		return systemTavernRebate;
	}

	@Override
	public void reload() {
		cacheMap.clear();
		listMap.clear();
	}

	@Override
	public void init() {
		ReloadManager.getInstance().register(getClass().getSimpleName(), this);
	}

	@Override
	public List<SystemTavernRebate> getAllByType(int typeA, int typeB) {
		String key = "" + typeA + typeB;
		if (listMap.containsKey(key)) {
			return listMap.get(key);
		}
		List<SystemTavernRebate> list = this.systemTavernRebateDaoImpl.getAllByType(typeA, typeB);
		listMap.put(key, list);
		return list;
	}
}
