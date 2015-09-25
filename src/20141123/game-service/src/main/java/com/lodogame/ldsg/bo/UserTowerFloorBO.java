package com.lodogame.ldsg.bo;

import java.io.Serializable;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

/**
 * 玩家楼层数据对象
 * 
 * @author chenjian
 * 
 */
@Compress
public class UserTowerFloorBO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 楼层数
	 */
	@Mapper(name = "fl")
	private int floor;
	
	/**
	 * 状态，-1:失败, 0:正在打, 1:为通过
	 */
	@Mapper(name = "st")
	private int status;

	/**
	 * 部队ID
	 */
	@Mapper(name = "fid")
	private int forcesId;

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getForcesId() {
		return forcesId;
	}

	public void setForcesId(int forcesId) {
		this.forcesId = forcesId;
	}
}
