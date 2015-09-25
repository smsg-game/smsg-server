package com.lodogame.ldsg.event;

/**
 * 英雄掉落事件
 * @author Candon
 *
 */
public class DropHeroEvent extends BaseEvent implements Event {
	
	public DropHeroEvent(String userId, String username, String heroName, int heroStar, String toolName){
		this.setObject("userId", userId);
		this.setObject("username", username);
		this.setObject("heroName", heroName);
		this.setObject("heroStar", heroStar);
		this.setObject("toolName", toolName);
	}
}
