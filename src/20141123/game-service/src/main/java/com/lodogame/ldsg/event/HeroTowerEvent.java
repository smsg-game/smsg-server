package com.lodogame.ldsg.event;

public class HeroTowerEvent extends BaseEvent implements Event {
	
	public HeroTowerEvent(String userId,String heroName) {
		this.setObject("userId", userId);
		this.setObject("heroName", heroName);
	}
}
