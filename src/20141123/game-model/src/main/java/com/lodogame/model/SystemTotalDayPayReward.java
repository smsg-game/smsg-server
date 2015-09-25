package com.lodogame.model;

import java.io.Serializable;

public class SystemTotalDayPayReward implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	private int day;
	private String dropTool;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public String getDropTool() {
		return dropTool;
	}
	public void setDropTool(String dropTool) {
		this.dropTool = dropTool;
	}



}
