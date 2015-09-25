package com.lodogame.ldsg.bo;

import java.io.Serializable;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class UserTowerBO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 关卡数
	 */
	@Mapper(name = "sg")
	private int stage;
	/**
	 * 楼层数
	 */
	@Mapper(name = "fl")
	private int floor;
	/**
	 * 状态
	 */
	@Mapper(name = "st")
	private int status;
	
	/**
	 * 次数
	 */
	@Mapper(name = "ts")
	private int times;
	
	public int getStage() {
		return stage;
	}
	public void setStage(int stage) {
		this.stage = stage;
	}
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
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
}
