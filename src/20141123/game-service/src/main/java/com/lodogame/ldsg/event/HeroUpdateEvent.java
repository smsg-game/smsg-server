package com.lodogame.ldsg.event;

public class HeroUpdateEvent extends BaseEvent {

	public HeroUpdateEvent(String userId, String userHeroId) {
		this.userId = userId;
		this.data.put("userHeroId", userHeroId);
	}
}
