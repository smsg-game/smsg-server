package com.lodogame.game.dao.impl.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.DeifyRoomDao;
import com.lodogame.game.dao.SystemDeifyRoomDao;
import com.lodogame.game.dao.impl.mysql.DeifyRoomDaoMysqlImpl;
import com.lodogame.game.utils.IDGenerator;
import com.lodogame.model.DeifyRoomInfo;
import com.lodogame.model.SystemDeifyRoom;

public class DeifyRoomDaoCacheImpl implements DeifyRoomDao {

	@Autowired
	private SystemDeifyRoomDao systemDeifyRoomDao;

	private DeifyRoomDaoMysqlImpl deifyRoomDaoMysqlImpl;

	public void setDeifyRoomDaoMysqlImpl(DeifyRoomDaoMysqlImpl deifyRoomDaoMysqlImpl) {
		this.deifyRoomDaoMysqlImpl = deifyRoomDaoMysqlImpl;
	}

	private Map<String, DeifyRoomInfo> cache = new ConcurrentHashMap<String, DeifyRoomInfo>();

	@Override
	public boolean add(DeifyRoomInfo roomInfo) {
		boolean success = this.deifyRoomDaoMysqlImpl.add(roomInfo);
		if (success) {
			this.cache.put(roomInfo.getId(), roomInfo);
		}
		return success;
	}

	@Override
	public List<DeifyRoomInfo> getRoomListByTowerId(int towerId) {
		List<DeifyRoomInfo> roomInfoList = new ArrayList<DeifyRoomInfo>();

		for (Entry<String, DeifyRoomInfo> entry : cache.entrySet()) {
			DeifyRoomInfo deifyRoomInfo = entry.getValue();
			if (deifyRoomInfo.getTowerId() == towerId) {
				roomInfoList.add(deifyRoomInfo);
			}
		}

		return roomInfoList;
	}

	@Override
	public Collection<DeifyRoomInfo> getAllRoomInfoList() {
		return cache.values();
	}

	public void init() {

		if (!cache.isEmpty()) {
			return;
		}

		// 清除第四层的空房间
		this.deifyRoomDaoMysqlImpl.cleanRoom();

		List<DeifyRoomInfo> roomInfoList = deifyRoomDaoMysqlImpl.getAllRoomInfoList();

		if (roomInfoList.isEmpty()) {
			List<SystemDeifyRoom> systemRoomList = systemDeifyRoomDao.getAllRoom();
			for (SystemDeifyRoom room : systemRoomList) {
				DeifyRoomInfo roomInfo = new DeifyRoomInfo();
				roomInfo.setTowerId(room.getTowerId());
				roomInfo.setRoomId(room.getRoomId());
				roomInfo.setId(IDGenerator.getID());
				this.deifyRoomDaoMysqlImpl.add(roomInfo);
			}
		}

		roomInfoList = deifyRoomDaoMysqlImpl.getAllRoomInfoList();
		for (DeifyRoomInfo deifyRoomInfo : roomInfoList) {
			cache.put(deifyRoomInfo.getId(), deifyRoomInfo);
		}
	}

	@Override
	public DeifyRoomInfo getRoomByUid(String uid) {
		for (DeifyRoomInfo roomInfo : cache.values()) {
			if (StringUtils.equalsIgnoreCase(uid, roomInfo.getUserId())) {
				return roomInfo;
			}
		}
		return null;
	}

	@Override
	public boolean update(DeifyRoomInfo room) {
		cache.put(room.getId(), room);
		return this.deifyRoomDaoMysqlImpl.update(room);
	}

	@Override
	public DeifyRoomInfo getRoom(int towerId, int roomId) {
		for (DeifyRoomInfo room : cache.values()) {
			if (room.getTowerId() == towerId && room.getRoomId() == roomId) {
				return room;
			}
		}
		return null;
	}

	@Override
	public boolean cleanRoom() {
		return true;
	}

}
