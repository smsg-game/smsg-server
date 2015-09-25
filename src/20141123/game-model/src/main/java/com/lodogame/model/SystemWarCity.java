package com.lodogame.model;

public class SystemWarCity {
	private int id;
	private String cityType;
	private Integer cityId;
	private int cityFull;
	private int multiple;
	private double ratio;
	private String attackCity;
	private String cityName;
	
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	public String getAttackCity() {
		return attackCity;
	}
	public void setAttackCity(String attackCity) {
		this.attackCity = attackCity;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCityType() {
		return cityType;
	}
	public void setCityType(String cityType) {
		this.cityType = cityType;
	}
	public Integer getCityId() {
		return cityId;
	}
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}
	public int getCityFull() {
		return cityFull;
	}
	public void setCityFull(int cityFull) {
		this.cityFull = cityFull;
	}
	public int getMultiple() {
		return multiple;
	}
	public void setMultiple(int multiple) {
		this.multiple = multiple;
	}
	public double getRatio() {
		return ratio;
	}
	public void setRatio(double ratio) {
		this.ratio = ratio;
	}
	
	
}
