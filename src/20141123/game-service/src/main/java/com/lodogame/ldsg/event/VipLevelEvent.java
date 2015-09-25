package com.lodogame.ldsg.event;

/**
 * VIP等级事件
 * 
 * @author Candon
 * 
 */
public class VipLevelEvent extends BaseEvent implements Event {
	
	public VipLevelEvent(String userId) {
		this.userId = userId;
	}
}
