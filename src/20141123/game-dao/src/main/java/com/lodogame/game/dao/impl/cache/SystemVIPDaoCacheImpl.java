package com.lodogame.game.dao.impl.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.SystemVIPDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.model.SystemVIP;

public class SystemVIPDaoCacheImpl implements SystemVIPDao, ReloadAble {

	private SystemVIPDao systemVIPDaoMysqlImpl;

	private Map<Integer, SystemVIP> heroAttrMap = new ConcurrentHashMap<Integer, SystemVIP>();

	public void setSystemVIPDaoMysqlImpl(SystemVIPDao systemVIPDaoMysqlImpl) {
		this.systemVIPDaoMysqlImpl = systemVIPDaoMysqlImpl;
	}

	@Override
	public SystemVIP get(int VIPLevel) {

		if (heroAttrMap.containsKey(VIPLevel)) {
			return heroAttrMap.get(VIPLevel);
		}
		SystemVIP systemVIP;

		synchronized (SystemVIPDaoCacheImpl.class) {

			systemVIP = this.systemVIPDaoMysqlImpl.get(VIPLevel);
			if (systemVIP != null) {
				heroAttrMap.put(VIPLevel, systemVIP);
			}
		}

		return systemVIP;
	}

	@Override
	public void reload() {
		heroAttrMap.clear();
	}

	@Override
	public void init() {
		ReloadManager.getInstance().register(getClass().getSimpleName(), this);
	}

}
