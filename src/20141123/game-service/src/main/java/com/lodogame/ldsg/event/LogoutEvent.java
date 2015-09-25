package com.lodogame.ldsg.event;

public class LogoutEvent extends BaseEvent implements Event  {
	
	public LogoutEvent(String userId){
		this.userId = userId;
	}
}
