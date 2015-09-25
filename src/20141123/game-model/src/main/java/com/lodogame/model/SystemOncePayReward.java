package com.lodogame.model;

import java.io.Serializable;

public class SystemOncePayReward implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	private int payMoney;
	private int timesLimit;
	private String description;
	private String dropToolIds;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(int payMoney) {
		this.payMoney = payMoney;
	}

	public int getTimesLimit() {
		return timesLimit;
	}

	public void setTimesLimit(int timesLimit) {
		this.timesLimit = timesLimit;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDropToolIds() {
		return dropToolIds;
	}

	public void setDropToolIds(String dropToolIds) {
		this.dropToolIds = dropToolIds;
	}

}
