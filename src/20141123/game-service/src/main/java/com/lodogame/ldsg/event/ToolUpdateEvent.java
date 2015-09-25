package com.lodogame.ldsg.event;

import com.lodogame.ldsg.bo.UserToolBO;

/**
 * 道具更新事件
 * 
 * @author jacky
 * 
 */
public class ToolUpdateEvent extends BaseEvent {

	public ToolUpdateEvent(String userId, UserToolBO tool) {
		this.userId = userId;
		this.data.put("tool", tool);
	}

	public ToolUpdateEvent(String userId) {
		this.userId = userId;
	}
}
