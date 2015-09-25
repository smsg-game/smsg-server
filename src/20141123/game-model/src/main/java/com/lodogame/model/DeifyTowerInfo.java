package com.lodogame.model;

public class DeifyTowerInfo {
	
	private int towerId;
	
	/**
	 * 修炼室数量
	 */
	private int roomNum;
	
	/**
	 * 已经占领数量
	 */
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
