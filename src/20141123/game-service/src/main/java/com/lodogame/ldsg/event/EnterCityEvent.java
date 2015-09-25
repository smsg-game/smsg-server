package com.lodogame.ldsg.event;

public class EnterCityEvent {

	private String userId;

	private int cityId;

	private EventHandle callack;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public EventHandle getCallack() {
		return callack;
	}

	public void setCallack(EventHandle callack) {
		this.callack = callack;
	}

}
