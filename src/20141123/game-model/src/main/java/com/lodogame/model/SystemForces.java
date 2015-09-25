package com.lodogame.model;

import java.io.Serializable;

public class SystemForces implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 部队ID
	 */
	private int forcesId;

	/**
	 * 关卡ID
	 */
	private int sceneId;

	/**
	 * 掉格道具
	 */
	private String dropToolList;

	/**
	 * 部队名字
	 */
	private String forcesName;

	/**
	 * 部队类型
	 */
	private int forcesType;

	/**
	 * 部队等级
	 */
	private int forcesLevel;

	/**
	 * 前置部队
	 */
	private int preForcesId;

	/*
	 * 前置部队2
	 */
	private int preForcesIdb;

	/**
	 * 次数限制
	 */
	private int timesLimit;

	/**
	 * 需要体力
	 */
	private int needPower;

	public int getForcesId() {
		return forcesId;
	}

	public void setForcesId(int forcesId) {
		this.forcesId = forcesId;
	}

	public int getSceneId() {
		return sceneId;
	}

	public void setSceneId(int sceneId) {
		this.sceneId = sceneId;
	}

	public String getDropToolList() {
		return dropToolList;
	}

	public void setDropToolList(String dropToolList) {
		this.dropToolList = dropToolList;
	}

	public String getForcesName() {
		return forcesName;
	}

	public void setForcesName(String forcesName) {
		this.forcesName = forcesName;
	}

	public int getForcesLevel() {
		return forcesLevel;
	}

	public void setForcesLevel(int forcesLevel) {
		this.forcesLevel = forcesLevel;
	}

	public int getPreForcesId() {
		return preForcesId;
	}

	public void setPreForcesId(int preForcesId) {
		this.preForcesId = preForcesId;
	}

	public int getForcesType() {
		return forcesType;
	}

	public void setForcesType(int forcesType) {
		this.forcesType = forcesType;
	}

	public int getNeedPower() {
		return needPower;
	}

	public void setNeedPower(int needPower) {
		this.needPower = needPower;
	}

	public int getPreForcesIdb() {
		return preForcesIdb;
	}

	public void setPreForcesIdb(int preForcesIdb) {
		this.preForcesIdb = preForcesIdb;
	}

	public int getTimesLimit() {
		return timesLimit;
	}

	public void setTimesLimit(int timesLimit) {
		this.timesLimit = timesLimit;
	}

}
