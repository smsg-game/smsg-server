package com.lodogame.game.dao.impl.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.SystemHeroUpgradeToolDao;
import com.lodogame.game.dao.impl.mysql.SystemHeroUpgradeToolDaoMysqlImpl;
import com.lodogame.game.dao.reload.ReloadAbleBase;
import com.lodogame.model.SystemHeroUpgradeTool;

public class SystemHeroUpgradeToolDaoCacheImpl extends ReloadAbleBase implements
		SystemHeroUpgradeToolDao {

	private SystemHeroUpgradeToolDaoMysqlImpl systemHeroUpgradeToolDaoMysqlImpl;
	
	private Map<String,List<SystemHeroUpgradeTool>> cache = new ConcurrentHashMap<String,List<SystemHeroUpgradeTool>>();
	
	@Override
	public List<SystemHeroUpgradeTool> getNeedToolList(int systemHeroId,
			int upgradeHeroId) {
		// TODO Auto-generated method stub
		String key = systemHeroId+""+upgradeHeroId;
		if(cache.containsKey(key)){
			return cache.get(key);
		}
		List<SystemHeroUpgradeTool> list = systemHeroUpgradeToolDaoMysqlImpl.getNeedToolList(systemHeroId, upgradeHeroId);
		if(list!=null){
			cache.put(key, list);
		}
		return list;
	}
	@Override
	public void reload() {
		// TODO Auto-generated method stub
		 cache.clear();
	}
 
	public void setSystemHeroUpgradeToolDaoMysqlImpl(
			SystemHeroUpgradeToolDaoMysqlImpl systemHeroUpgradeToolDaoMysqlImpl) {
		this.systemHeroUpgradeToolDaoMysqlImpl = systemHeroUpgradeToolDaoMysqlImpl;
	}

	
}
