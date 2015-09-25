package com.lodogame.ldsg.event;

public class WarEvent extends BaseEvent implements Event {
	
	public WarEvent(String countryName, String username, String cityName,int sendType){
		this.setObject("countryName", countryName);
		this.setObject("username", username);
		this.setObject("cityName", cityName);
		this.setObject("sendType", sendType);
	}
}
