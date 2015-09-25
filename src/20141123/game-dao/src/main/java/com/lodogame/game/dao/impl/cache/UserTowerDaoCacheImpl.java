package com.lodogame.game.dao.impl.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.UserTowerDao;
import com.lodogame.model.UserTower;

public class UserTowerDaoCacheImpl implements UserTowerDao {

	@Autowired
	private UserTowerDao userTowerDaoMysqlImpl;

	private Map<String, List<UserTower>> cache = new ConcurrentHashMap<String, List<UserTower>>();

	@Override
	public List<UserTower> getUserTower(String uid) {
		List<UserTower> list = cache.get(uid);
		if (list == null) {
			list = userTowerDaoMysqlImpl.getUserTower(uid);
			if (list != null) {
				cache.put(uid, list);
			}
		}
		return list;
	}

	@Override
	public boolean resetTowerStage(String uid) {

		userTowerDaoMysqlImpl.resetTowerStage(uid);
		List<UserTower> list = userTowerDaoMysqlImpl.getUserTower(uid);
		if (list != null) {
			cache.put(uid, list);
		}
		// System.out.println(cache.get(uid));
		// System.out.println(cache.size());
		return true;
	}

	@Override
	public UserTower newStage(String uid, int stage, int floor) {
		userTowerDaoMysqlImpl.newStage(uid, stage, floor);
		List<UserTower> list = userTowerDaoMysqlImpl.getUserTower(uid);
		if (list != null) {
			cache.put(uid, list);
		}
		for (UserTower ut : list) {
			if (ut.getStage() == stage && ut.getFloor() == floor) {
				return ut;
			}
		}
		return null;
	}

	public UserTowerDao getUserTowerDaoMysqlImpl() {
		return userTowerDaoMysqlImpl;
	}

	public void setUserTowerDaoMysqlImpl(UserTowerDao userTowerDaoMysqlImpl) {
		this.userTowerDaoMysqlImpl = userTowerDaoMysqlImpl;
	}

	@Override
	public boolean updateTowerStatus(String uid, int stage, int status, int times) {
		userTowerDaoMysqlImpl.updateTowerStatus(uid, stage, status, times);
		List<UserTower> list = cache.get(uid);
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				UserTower ut = list.get(i);
				if (ut.getStage() == stage) {
					ut.setTimes(ut.getTimes() + times);
					ut.setStatus(status);
				}
			}
		}
		return true;
	}

	@Override
	public boolean updateStageFloor(String uid, int curStage, int floor) {
		userTowerDaoMysqlImpl.updateStageFloor(uid, curStage, floor);
		List<UserTower> list = cache.get(uid);
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				UserTower ut = list.get(i);
				if (ut.getStage() == curStage) {
					ut.setFloor(floor);
				}
			}
		}
		return true;
	}

}
