package com.lodogame.game.dao.impl.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.SystemEquipUpgradeToolDao;
import com.lodogame.game.dao.reload.ReloadAbleBase;
import com.lodogame.model.SystemEquipUpgradeTool;

public class SystemEquipUpgradeToolDaoCacheImpl extends ReloadAbleBase implements SystemEquipUpgradeToolDao {

	private SystemEquipUpgradeToolDao systemEquipUpgradeToolDaoMysqlImpl;

	private Map<Integer,List<SystemEquipUpgradeTool>> sysEuipCache = new ConcurrentHashMap<Integer,List<SystemEquipUpgradeTool>>();
	public void setSystemEquipUpgradeToolDaoMysqlImpl(SystemEquipUpgradeToolDao systemEquipUpgradeToolDaoMysqlImpl) {
		this.systemEquipUpgradeToolDaoMysqlImpl = systemEquipUpgradeToolDaoMysqlImpl;
	}

	@Override
	public List<SystemEquipUpgradeTool> getList(int equipId) {
		if(sysEuipCache.containsKey(equipId)){
			return sysEuipCache.get(equipId);
		}
		List<SystemEquipUpgradeTool> list = this.systemEquipUpgradeToolDaoMysqlImpl.getList(equipId);
		if(list!=null){
			sysEuipCache.put(equipId, list);
		}
		return list;
	}

	@Override
	public void reload() {
		sysEuipCache.clear();
	}
}
