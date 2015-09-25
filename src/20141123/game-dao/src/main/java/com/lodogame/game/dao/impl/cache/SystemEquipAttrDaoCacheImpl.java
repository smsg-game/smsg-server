package com.lodogame.game.dao.impl.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.SystemEquipAttrDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.model.SystemEquipAttr;

public class SystemEquipAttrDaoCacheImpl implements SystemEquipAttrDao, ReloadAble {

	private SystemEquipAttrDao systemEquipAttrDaoMysqlImpl;

	public SystemEquipAttrDao getSystemEquipAttrDaoMysqlImpl() {
		return systemEquipAttrDaoMysqlImpl;
	}

	public void setSystemEquipAttrDaoMysqlImpl(SystemEquipAttrDao systemEquipAttrDaoMysqlImpl) {
		this.systemEquipAttrDaoMysqlImpl = systemEquipAttrDaoMysqlImpl;
	}

	private Map<String, SystemEquipAttr> equipAttrMap = new ConcurrentHashMap<String, SystemEquipAttr>();

	@Override
	public SystemEquipAttr getEquipAttr(int equipId, int equipLevel) {

		String key = equipId + "_" + equipLevel;

		if (equipAttrMap.containsKey(key)) {
			return equipAttrMap.get(key);
		}

		SystemEquipAttr systemEquipAttr;

		synchronized (SystemEquipAttrDaoCacheImpl.class) {

			systemEquipAttr = this.systemEquipAttrDaoMysqlImpl.getEquipAttr(equipId, equipLevel);
			if (systemEquipAttr != null) {
				equipAttrMap.put(key, systemEquipAttr);
			}
		}

		return systemEquipAttr;
	}

	@Override
	public void reload() {
		equipAttrMap.clear();
	}

	@Override
	public void init() {
		ReloadManager.getInstance().register(getClass().getSimpleName(), this);
	}

}
