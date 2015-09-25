package com.lodogame.game.dao.impl.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.SystemBloodSacrificeDao;
import com.lodogame.game.dao.impl.mysql.SystemBloodSacrificeDaoMysqlImpl;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.model.SystemBloodSacrifice;

public class SystemBloodSacrificeCacheImpl implements SystemBloodSacrificeDao, ReloadAble {

	private SystemBloodSacrificeDao systemBloodSacrificeDaoMysqlImpl;

	private Map<String, SystemBloodSacrifice> systemBloodSacrificeMap = new ConcurrentHashMap<String, SystemBloodSacrifice>();

	@Override
	public void reload() {
		this.systemBloodSacrificeMap.clear();
	}

	@Override
	public void init() {
		ReloadManager.getInstance().register(getClass().getSimpleName(), this);
	}

	@Override
	public SystemBloodSacrifice get(int heroId, int stage) {
		String key = heroId + "_" + stage;

		if (this.systemBloodSacrificeMap.containsKey(key)) {
			return this.systemBloodSacrificeMap.get(key);
		}

		SystemBloodSacrifice systemBloodSacrifice = this.systemBloodSacrificeDaoMysqlImpl.get(heroId, stage);

		if (systemBloodSacrifice != null) {

			this.systemBloodSacrificeMap.put(key, systemBloodSacrifice);

		}

		return systemBloodSacrifice;
	}

	public void setSystemBloodSacrificeDaoMysqlImpl(SystemBloodSacrificeDaoMysqlImpl systemBloodSacrificeDaoMysqlImpl) {
		this.systemBloodSacrificeDaoMysqlImpl = systemBloodSacrificeDaoMysqlImpl;
	}

}
