package com.lodogame.ldsg.event;

/**
 * 关卡事件
 * @author Candon
 *
 */
public class SceneEvent extends BaseEvent implements Event {
	
	public SceneEvent(String userId, String scene){
		this.setObject("userId", userId);
		this.setObject("scene", scene);
	}
}
