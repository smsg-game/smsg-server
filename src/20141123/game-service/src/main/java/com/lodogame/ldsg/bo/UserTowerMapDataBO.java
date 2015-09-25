package com.lodogame.ldsg.bo;

import java.io.Serializable;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class UserTowerMapDataBO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Mapper(name = "mid")
	private int mapId;
	
	@Mapper(name = "uuid")
	private String uuid;
	/**
	 * 楼层数
	 */
	@Mapper(name = "fl")
	private int floor;
	/**
	 * 位置
	 */
	@Mapper(name = "pos")
	private int position;
	/**
	 * 对象类型，0:怪物, 1:宝箱
	 */
	@Mapper(name = "obt")
	private int objType;
	
	/**
	 * 是否已通过，0:否, 1:是
	 */
	@Mapper(name = "psd")
	private int passed;
	
	/**
	 * 对象ID
	 */
	@Mapper(name = "obid")
	private int objId;

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getObjType() {
		return objType;
	}

	public void setObjType(int objType) {
		this.objType = objType;
	}

	public int getPassed() {
		return passed;
	}

	public void setPassed(int passed) {
		this.passed = passed;
	}

	public int getObjId() {
		return objId;
	}

	public void setObjId(int objId) {
		this.objId = objId;
	}

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}
