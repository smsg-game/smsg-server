package com.lodogame.game.dao.impl.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.NotImplementedException;

import com.lodogame.game.dao.SystemEquipSuitDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.model.SystemEquipSuit;
import com.lodogame.model.SystemSuitDetail;

public class SystemEquipSuitDaoCacheImpl implements SystemEquipSuitDao, ReloadAble {

	private Map<Integer, SystemEquipSuit> heroEquipSuitMap = new ConcurrentHashMap<Integer, SystemEquipSuit>();

	private Map<Integer, List<SystemSuitDetail>> suitDetailMap = new ConcurrentHashMap<Integer, List<SystemSuitDetail>>();

	private SystemEquipSuitDao systemEquipSuitDaoMysqlImpl;

	public SystemEquipSuitDao getSystemEquipSuitDaoMysqlImpl() {
		return systemEquipSuitDaoMysqlImpl;
	}

	public void setSystemEquipSuitDaoMysqlImpl(SystemEquipSuitDao systemEquipSuitDaoMysqlImpl) {
		this.systemEquipSuitDaoMysqlImpl = systemEquipSuitDaoMysqlImpl;
	}

	@Override
	public List<SystemEquipSuit> getHeroEquipSuitList() {
		throw new NotImplementedException();
	}

	@Override
	public SystemEquipSuit getHeroEquipSuit(int heroId) {
		return this.heroEquipSuitMap.get(heroId);
	}

	@Override
	public List<SystemSuitDetail> getSuitDetailList(int suitId) {

		if (suitDetailMap.containsKey(suitId)) {
			return suitDetailMap.get(suitId);
		}

		List<SystemSuitDetail> list = this.systemEquipSuitDaoMysqlImpl.getSuitDetailList(suitId);
		suitDetailMap.put(suitId, list);

		return list;
	}

	@Override
	public void reload() {
		suitDetailMap.clear();
		initData();
	}

	private void initData() {

		heroEquipSuitMap.clear();
		List<SystemEquipSuit> systemEquipSuitList = this.systemEquipSuitDaoMysqlImpl.getHeroEquipSuitList();
		for (SystemEquipSuit systemEquipSuit : systemEquipSuitList) {
			heroEquipSuitMap.put(systemEquipSuit.getHeroId(), systemEquipSuit);
		}
	}

	public void init() {
		initData();
		ReloadManager.getInstance().register(this.getClass().getSimpleName(), this);
	}

}
