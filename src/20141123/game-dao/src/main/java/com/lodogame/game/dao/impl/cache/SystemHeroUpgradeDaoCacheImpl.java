package com.lodogame.game.dao.impl.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.SystemHeroUpgradeDao;
import com.lodogame.game.dao.impl.mysql.SystemHeroUpgradeDaoMysqlImpl;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.model.SystemHeroUpgrade;

public class SystemHeroUpgradeDaoCacheImpl implements SystemHeroUpgradeDao,ReloadAble {

	private Map<Integer,SystemHeroUpgrade> heroUpgradeCache = new ConcurrentHashMap<Integer,SystemHeroUpgrade>();
	
	private SystemHeroUpgradeDaoMysqlImpl systemHeroUpgradeDaoMysqlImpl;
	public SystemHeroUpgrade get(int systemHeroId) {
		return heroUpgradeCache.get(systemHeroId);
	}

	@Override
	public void reload() {
		initCache();
	}
    private void initCache(){
    	heroUpgradeCache.clear();
    	List<SystemHeroUpgrade> list = systemHeroUpgradeDaoMysqlImpl.getSystemHeroUpgradeList();
    	for(SystemHeroUpgrade systemHeroUpgrade:list){
    		heroUpgradeCache.put(systemHeroUpgrade.getSystemHeroId(), systemHeroUpgrade);
    	}
    }
	@Override
	public void init() {
		initCache();
		ReloadManager.getInstance().register(getClass().getSimpleName(), this);
	}

	public void setSystemHeroUpgradeDaoMysqlImpl(SystemHeroUpgradeDaoMysqlImpl systemHeroUpgradeDaoMysqlImpl) {
		this.systemHeroUpgradeDaoMysqlImpl = systemHeroUpgradeDaoMysqlImpl;
	}

}
