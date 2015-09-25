package com.lodogame.ldsg.event;

/**
 * 专属神器专用
 * @author Candon
 *
 */
public class DeifyEvent extends BaseEvent implements Event{
	
	public DeifyEvent(String username, String userHeroName, String equipName){
		this.setObject("username", username);
		this.setObject("userHeroName", userHeroName);
		this.setObject("equipName", equipName);
	}
}
