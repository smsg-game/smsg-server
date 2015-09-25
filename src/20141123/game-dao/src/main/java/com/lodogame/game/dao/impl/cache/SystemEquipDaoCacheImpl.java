package com.lodogame.game.dao.impl.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.NotImplementedException;

import com.lodogame.game.dao.SystemEquipDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.model.SystemEquip;

public class SystemEquipDaoCacheImpl implements SystemEquipDao, ReloadAble {

	private SystemEquipDao systemEquipDaoMysqlImpl;

	private Map<Integer, SystemEquip> equipMap = new ConcurrentHashMap<Integer, SystemEquip>();

	public void setSystemEquipDaoMysqlImpl(SystemEquipDao systemEquipDaoMysqlImpl) {
		this.systemEquipDaoMysqlImpl = systemEquipDaoMysqlImpl;
	}

	@Override
	public List<SystemEquip> getSystemEquipList() {
		return this.systemEquipDaoMysqlImpl.getSystemEquipList();
	}

	@Override
	public SystemEquip get(int equipId) {

		if (this.equipMap.containsKey(equipId)) {
			return equipMap.get(equipId);
		}

		SystemEquip systemEquip = this.systemEquipDaoMysqlImpl.get(equipId);

		synchronized (SystemEquipDaoCacheImpl.class) {

			if (systemEquip != null) {
				equipMap.put(equipId, systemEquip);
			}
		}

		return systemEquip;
	}

	@Override
	public boolean add(SystemEquip systemEquip) {
		throw new NotImplementedException();
	}

	@Override
	public void reload() {
		equipMap.clear();
	}

	@Override
	public void init() {
		ReloadManager.getInstance().register(getClass().getSimpleName(), this);
	}

}
