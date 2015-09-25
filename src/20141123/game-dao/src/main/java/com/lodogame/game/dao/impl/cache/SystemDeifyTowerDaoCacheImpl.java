package com.lodogame.game.dao.impl.cache;

import java.util.ArrayList;
import java.util.List;

import com.lodogame.game.dao.SystemDeifyTowerDao;
import com.lodogame.game.dao.impl.mysql.SystemDeifyTowerDaoMysqlImpl;
import com.lodogame.model.SystemDeifyTower;

public class SystemDeifyTowerDaoCacheImpl implements SystemDeifyTowerDao{

	private SystemDeifyTowerDaoMysqlImpl systemDeifyTowerDaoMysqlImpl;
	public void setSystemDeifyTowerDaoMysqlImpl(
			SystemDeifyTowerDaoMysqlImpl systemDeifyTowerDaoMysqlImpl) {
		this.systemDeifyTowerDaoMysqlImpl = systemDeifyTowerDaoMysqlImpl;
	}


	List<SystemDeifyTower> cacheList = new ArrayList<SystemDeifyTower>();
	
	@Override
	public List<SystemDeifyTower> getAllSystemDeifyTower() {
		return cacheList;
	}
	
	public void init() {
		cacheList = systemDeifyTowerDaoMysqlImpl.getAllSystemDeifyTower();
	}

	@Override
	public SystemDeifyTower get(int towerId) {
		for (SystemDeifyTower tower : cacheList) {
			if (towerId == tower.getSystemTowerId()) {
				return tower;
			}
		}
		return null;
	}

}
