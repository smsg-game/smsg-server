package com.lodogame.model;

public class SystemTavernRebate {
	
	private int id;
	
	//招募类型（1：每日豪杰，2：每日宴席，3：累计豪杰，4累计宴席）
	private int type;
	
	//次数
	private int times;
	
	//道具
	private String tools;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	public String getTools() {
		return tools;
	}
	public void setTools(String tools) {
		this.tools = tools;
	}
}
