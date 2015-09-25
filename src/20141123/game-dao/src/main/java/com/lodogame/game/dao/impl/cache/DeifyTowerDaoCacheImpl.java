package com.lodogame.game.dao.impl.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.DeifyTowerDao;
import com.lodogame.game.dao.SystemDeifyTowerDao;
import com.lodogame.game.dao.impl.mysql.DeifyTowerDaoMysqlImpl;
import com.lodogame.model.DeifyTowerInfo;
import com.lodogame.model.SystemDeifyTower;

public class DeifyTowerDaoCacheImpl implements DeifyTowerDao {

//	@Autowired
//	private SystemDeifyTowerDao systemDeifyTowerDao;
//
//	private DeifyTowerDaoMysqlImpl deifyTowerDaoMysqlImpl;

//	public void setDeifyTowerDaoMysqlImpl(DeifyTowerDaoMysqlImpl deifyTowerDaoMysqlImpl) {
//		this.deifyTowerDaoMysqlImpl = deifyTowerDaoMysqlImpl;
//	}

//	/*
//	 * key 是 towerId
//	 */
//	private Map<Integer, DeifyTowerInfo> cacheMap = new HashMap<Integer, DeifyTowerInfo>();
//	private List<DeifyTowerInfo> cacheList = new ArrayList<DeifyTowerInfo>();

//	@Override
//	public List<DeifyTowerInfo> getAllTowerInfoList(Map<>) {
//		if (cacheList.size() == 0) {
//			init();
//		}
//		return cacheList;
//	}

//	public void init() {
//		cacheList = deifyTowerDaoMysqlImpl.getAllTowerInfoList();
//		if (cacheList != null && cacheList.size() != 0) {
//			for (DeifyTowerInfo towerInfo : cacheList) {
//				cacheMap.put(towerInfo.getTowerId(), towerInfo);
//			}
//		} else {
//			List<SystemDeifyTower> systemDeifyTowerList = systemDeifyTowerDao.getAllSystemDeifyTower();
//			for (SystemDeifyTower tower : systemDeifyTowerList) {
//				DeifyTowerInfo towerInfo = new DeifyTowerInfo();
//				towerInfo.setOccupiedRoomNum(0);
//				towerInfo.setRoomNum(tower.getRoomNum());
//				towerInfo.setTowerId(tower.getSystemTowerId());
//				add(towerInfo);
//			}
//		}
//	}

//	@Override
//	public boolean add(DeifyTowerInfo towerInfo) {
//		cacheMap.put(towerInfo.getTowerId(), towerInfo);
//		cacheList.add(towerInfo);
//		return deifyTowerDaoMysqlImpl.add(towerInfo);
//	}
//
//	@Override
//	public boolean addOccupiedRoomNum(int towerId) {
//		DeifyTowerInfo towerInfo = cacheMap.get(towerId);
//		int occupiedRoomNum = towerInfo.getOccupiedRoomNum();
//		towerInfo.setOccupiedRoomNum(occupiedRoomNum + 1);
//		return deifyTowerDaoMysqlImpl.addOccupiedRoomNum(towerId);
//	}

//	@Override
//	public boolean reduceOccupiedRoomNum(int towerId) {
//		DeifyTowerInfo towerInfo = cacheMap.get(towerId);
//		int occupiedRoomNum = towerInfo.getOccupiedRoomNum();
//		towerInfo.setOccupiedRoomNum(occupiedRoomNum - 1);
//		return deifyTowerDaoMysqlImpl.reduceOccupiedRoomNum(towerId);
//	}
//
//	@Override
//	public DeifyTowerInfo getTower(int towerId) {
//		DeifyTowerInfo tower = cacheMap.get(towerId);
//		return tower;
//	}

}
