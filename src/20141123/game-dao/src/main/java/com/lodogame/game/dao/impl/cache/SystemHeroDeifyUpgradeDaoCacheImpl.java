package com.lodogame.game.dao.impl.cache;

import java.util.HashMap;
import java.util.Map;

import com.lodogame.game.dao.SystemHeroDeifyUpgradeDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.model.SystemHeroDeifyUpgrade;

public class SystemHeroDeifyUpgradeDaoCacheImpl implements SystemHeroDeifyUpgradeDao, ReloadAble {

	private Map<Integer, SystemHeroDeifyUpgrade> systemHeroDeifyUpgradeMap = new HashMap<Integer, SystemHeroDeifyUpgrade>();
	
	private  SystemHeroDeifyUpgradeDao systemHeroDeifyUpgradeDaoMysqlImpl;
	
	public void setSystemHeroDeifyUpgradeDaoMysqlImpl(
			SystemHeroDeifyUpgradeDao systemHeroDeifyUpgradeDaoMysqlImpl) {
		this.systemHeroDeifyUpgradeDaoMysqlImpl = systemHeroDeifyUpgradeDaoMysqlImpl;
	}



	@Override
	public SystemHeroDeifyUpgrade getSystemHeroDeifyUpgradeById(int systemHeroId) {
		if (!systemHeroDeifyUpgradeMap.containsKey(systemHeroId)) {
			SystemHeroDeifyUpgrade systemHeroDeifyUpgrade = this.systemHeroDeifyUpgradeDaoMysqlImpl.getSystemHeroDeifyUpgradeById(systemHeroId);
			systemHeroDeifyUpgradeMap.put(systemHeroId, systemHeroDeifyUpgrade);
		}
		
		return systemHeroDeifyUpgradeMap.get(systemHeroId);
	}

	@Override
	public void reload() {
		systemHeroDeifyUpgradeMap.clear();
	}

	@Override
	public void init() {
		ReloadManager.getInstance().register(getClass().getSimpleName(), this);
	}


}
