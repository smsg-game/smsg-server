package com.lodogame.ldsg.event;

/**
 * 道具掉落事件
 * 
 * @author Candon
 * 
 */
public class ToolDropEvent extends BaseEvent implements Event {

	public ToolDropEvent(String userId, int toolId, String toolName, int useType, int isFlash) {

		this.setObject("userId", userId);
		this.setObject("toolId", toolId);
		this.setObject("useType", useType);
		this.setObject("isFlash", isFlash);
		this.setObject("toolName", toolName);
	}

	public ToolDropEvent(String userId, int toolId, int useType, int isFlash) {

		this.setObject("userId", userId);
		this.setObject("toolId", toolId);
		this.setObject("useType", useType);
		this.setObject("isFlash", isFlash);

	}
}
