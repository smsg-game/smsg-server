package com.lodogame.game.dao;

import java.util.Collection;
import java.util.List;

import com.lodogame.model.DeifyRoomInfo;

public interface DeifyRoomDao {

	public List<DeifyRoomInfo> getRoomListByTowerId(int towerId);

	public Collection<DeifyRoomInfo> getAllRoomInfoList();

	public DeifyRoomInfo getRoomByUid(String uid);

	public DeifyRoomInfo getRoom(int towerId, int roomId);

	public boolean update(DeifyRoomInfo room);

	public boolean add(DeifyRoomInfo roomInfo);

	public boolean cleanRoom();

}
