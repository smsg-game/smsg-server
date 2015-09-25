package com.lodogame.game.dao.impl.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.SystemEquipUpgradeDao;
import com.lodogame.game.dao.reload.ReloadAbleBase;
import com.lodogame.model.SystemEquipUpgrade;

public class SystemEquipUpgradeDaoCacheImpl extends ReloadAbleBase implements SystemEquipUpgradeDao {

	private SystemEquipUpgradeDao systemEquipUpgradeDaoMysqlImpl;

	private Map<Integer,SystemEquipUpgrade> equipMentCache = new ConcurrentHashMap<Integer,SystemEquipUpgrade>();
	
	public void setSystemEquipUpgradeDaoMysqlImpl(SystemEquipUpgradeDao systemEquipUpgradeDaoMysqlImpl) {
		this.systemEquipUpgradeDaoMysqlImpl = systemEquipUpgradeDaoMysqlImpl;
	}

	@Override
	public SystemEquipUpgrade get(int equipId) {
		if(equipMentCache.containsKey(equipId)){
			return equipMentCache.get(equipId);
		}
		SystemEquipUpgrade result = this.systemEquipUpgradeDaoMysqlImpl.get(equipId);
		if(result!=null){
			equipMentCache.put(equipId, result);
		}
		return result;
	}

	@Override
	public void reload() {
		equipMentCache.clear();
	}


}
