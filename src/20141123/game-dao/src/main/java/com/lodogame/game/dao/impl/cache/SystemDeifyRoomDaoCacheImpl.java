package com.lodogame.game.dao.impl.cache;

import java.util.ArrayList;
import java.util.List;

import com.lodogame.game.dao.SystemDeifyRoomDao;
import com.lodogame.game.dao.impl.mysql.SystemDeifyRoomDaoMysqlImpl;
import com.lodogame.model.SystemDeifyRoom;

public class SystemDeifyRoomDaoCacheImpl implements SystemDeifyRoomDao{

	private SystemDeifyRoomDaoMysqlImpl systemDeifyRoomDaoMysqlImpl;
	public void setSystemDeifyRoomDaoMysqlImpl(
			SystemDeifyRoomDaoMysqlImpl systemDeifyRoomDaoMysqlImpl) {
		this.systemDeifyRoomDaoMysqlImpl = systemDeifyRoomDaoMysqlImpl;
	}
	
	private List<SystemDeifyRoom> cacheList = new ArrayList<SystemDeifyRoom>();

	@Override
	public List<SystemDeifyRoom> getAllRoom() {
		if (cacheList.size() == 0) {
			cacheList = systemDeifyRoomDaoMysqlImpl.getAllRoom();
		}
		return cacheList;
	}
	
	public void init() {
		cacheList = systemDeifyRoomDaoMysqlImpl.getAllRoom();
	}

}
