package com.lodogame.game.dao.impl.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.SystemHeroAttrDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.model.SystemHeroAttr;

public class SystemHeroAttrDaoCacheImpl implements SystemHeroAttrDao, ReloadAble {

	private SystemHeroAttrDao systemHeroAttrDaoMysqlImpl;

	private Map<String, SystemHeroAttr> heroAttrMap = new ConcurrentHashMap<String, SystemHeroAttr>();

	public void setSystemHeroAttrDaoMysqlImpl(SystemHeroAttrDao systemHeroAttrDaoMysqlImpl) {
		this.systemHeroAttrDaoMysqlImpl = systemHeroAttrDaoMysqlImpl;
	}

	@Override
	public SystemHeroAttr getHeroAttr(int systemHeroId, int heroLevel) {

		String key = systemHeroId + "_" + heroLevel;

		if (heroAttrMap.containsKey(key)) {
			return heroAttrMap.get(key);
		}
		SystemHeroAttr systemHeroAttr;

		synchronized (SystemHeroAttrDaoCacheImpl.class) {

			systemHeroAttr = this.systemHeroAttrDaoMysqlImpl.getHeroAttr(systemHeroId, heroLevel);
			if (systemHeroAttr != null) {
				heroAttrMap.put(key, systemHeroAttr);
			}
		}

		return systemHeroAttr;
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
