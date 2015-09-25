package com.lodogame.game.dao.impl.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.UserTowerFloorDao;
import com.lodogame.model.UserTowerFloor;

public class UserTowerFloorDaoCacheImpl implements UserTowerFloorDao {

	@Autowired
	private UserTowerFloorDao userTowerFloorDaoMysqlImpl;

	private Map<String, List<UserTowerFloor>> cache = new ConcurrentHashMap<String, List<UserTowerFloor>>();

	@Override
	public List<UserTowerFloor> getUserTowerFloor(String uid, int floor) {
		List<UserTowerFloor> list = cache.get(uid);
		if(list == null){
			list = new ArrayList<UserTowerFloor>();
		}
		List<UserTowerFloor> ret = getByFloor(list, floor);
		if (ret == null || ret.isEmpty()) {
			ret = userTowerFloorDaoMysqlImpl.getUserTowerFloor(uid, floor);
			if (ret != null && !ret.isEmpty()) {
				list.addAll(ret);
				cache.put(uid, list);
			}
		}

		return ret;
	}

	private List<UserTowerFloor> getByFloor(List<UserTowerFloor> list, int floor) {
		List<UserTowerFloor> ret = new ArrayList<UserTowerFloor>();
		if (list != null && !list.isEmpty()) {
			for (UserTowerFloor f : list) {
				if (f.getFloor() == floor) {
					ret.add(f);
				}
			}
		}
		return ret;
	}

	@Override
	public boolean clearTowerFloorData(String uid) {
		userTowerFloorDaoMysqlImpl.clearTowerFloorData(uid);
		cache.remove(uid);
		return true;
	}

	@Override
	public UserTowerFloor getUserTowerFloor(String uid, int floor, int pos, int tid) {
		List<UserTowerFloor> list = cache.get(uid);
		UserTowerFloor ret = getByFloorPosTid(list, floor, pos, tid);
		if (ret == null) {
			ret = userTowerFloorDaoMysqlImpl.getUserTowerFloor(uid, floor, pos, tid);
			if (ret != null) {
				list.add(ret);
				cache.put(uid, list);
			}
		}

		return ret;
	}

	private UserTowerFloor getByFloorPosTid(List<UserTowerFloor> list, int floor, int pos, int tid) {
		if (list != null) {
			for (UserTowerFloor f : list) {
				if (f.getFloor() == floor && f.getPos() == pos && f.getObjId() == tid) {
					return f;
				}
			}
		}
		return null;
	}

	@Override
	public boolean saveUserTowerFloor(UserTowerFloor data) {
		userTowerFloorDaoMysqlImpl.saveUserTowerFloor(data);
		List<UserTowerFloor> list = cache.get(data.getUserId());
		if (list == null) {
			list = new ArrayList<UserTowerFloor>();
		}
		list.add(data);
		cache.put(data.getUserId(), list);
		return true;
	}

	public UserTowerFloorDao getUserTowerFloorDaoMysqlImpl() {
		return userTowerFloorDaoMysqlImpl;
	}

	public void setUserTowerFloorDaoMysqlImpl(UserTowerFloorDao userTowerFloorDaoMysqlImpl) {
		this.userTowerFloorDaoMysqlImpl = userTowerFloorDaoMysqlImpl;
	}

	@Override
	public boolean updateObjStatus(String uid, int floor, int pos, int isPassed) {
		userTowerFloorDaoMysqlImpl.updateObjStatus(uid, floor, pos, isPassed);
		List<UserTowerFloor> list = cache.get(uid);
		if (list != null) {
			for(UserTowerFloor f : list)
			if (f.getFloor() == floor && f.getPos() == pos) {
				f.setPassed(isPassed);
				return true;
			}
		}
		return false;
	}

}
