package com.lodogame.ldsg.event;

public class UserInfoUpdateEvent extends BaseEvent {

	public UserInfoUpdateEvent(String userId) {
		this.userId = userId;
	}
}
