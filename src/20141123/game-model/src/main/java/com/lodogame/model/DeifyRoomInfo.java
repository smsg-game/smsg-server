package com.lodogame.model;

import java.util.Date;

public class DeifyRoomInfo {

	public DeifyRoomInfo() {
		Date now = new Date();
		this.protectEndTime = now;
		this.protectStartTime = now;
		this.doubleProfitEndTime = now;
		this.doubleProfitStartTime = now;
	}

	private String id;

	private int towerId;

	private int roomId;

	private String userId;

	/**
	 * 修炼开始时间
	 */
	private Date deifyStartTime;

	/**
	 * 修炼结束时间
	 */
	private Date deifyEndTime;

	/**
	 * 修炼保护开始时间
	 */
	private Date protectStartTime;

	/**
	 * 修炼保护结束时间
	 */
	private Date protectEndTime;

	/**
	 * 双倍收益开始
	 */
	private Date doubleProfitStartTime;

	/**
	 * 双倍收益结束时间
	 */
	private Date doubleProfitEndTime;

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getDeifyEndTime() {
		return deifyEndTime;
	}

	public void setDeifyEndTime(Date deifyEndTime) {
		this.deifyEndTime = deifyEndTime;
	}

	public Date getProtectEndTime() {
		return protectEndTime;
	}

	public void setProtectEndTime(Date protectEndTime) {
		this.protectEndTime = protectEndTime;
	}

	public Date getDoubleProfitEndTime() {
		return doubleProfitEndTime;
	}

	public void setDoubleProfitEndTime(Date doubleProfitEndTime) {
		this.doubleProfitEndTime = doubleProfitEndTime;
	}

	public int getTowerId() {
		return towerId;
	}

	public void setTowerId(int towerId) {
		this.towerId = towerId;
	}

	public Date getDeifyStartTime() {
		return deifyStartTime;
	}

	public void setDeifyStartTime(Date deifyStartTime) {
		this.deifyStartTime = deifyStartTime;
	}

	public Date getDoubleProfitStartTime() {
		return doubleProfitStartTime;
	}

	public void setDoubleProfitStartTime(Date doubleProfitStartTime) {
		this.doubleProfitStartTime = doubleProfitStartTime;
	}

	public Date getProtectStartTime() {
		return protectStartTime;
	}

	public void setProtectStartTime(Date protectStartTime) {
		this.protectStartTime = protectStartTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
