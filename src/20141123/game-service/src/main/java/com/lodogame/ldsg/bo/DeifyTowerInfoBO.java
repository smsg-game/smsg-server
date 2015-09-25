package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class DeifyTowerInfoBO {

	@Mapper(name = "id")
	private int towerId;
	
	@Mapper(name = "rmn")
	private int roomNum;
	
	@Mapper(name = "ern")
	private int occupiedRoomNum;

	public int getTowerId() {
		return towerId;
	}

	public void setTowerId(int towerId) {
		this.towerId = towerId;
	}

	public int getRoomNum() {
		return roomNum;
	}

	public void setRoomNum(int roomNum) {
		this.roomNum = roomNum;
	}

	public int getOccupiedRoomNum() {
		return occupiedRoomNum;
	}

	public void setOccupiedRoomNum(int occupiedRoomNum) {
		this.occupiedRoomNum = occupiedRoomNum;
	}

	
}
