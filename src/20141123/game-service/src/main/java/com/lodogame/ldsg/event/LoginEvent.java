package com.lodogame.ldsg.event;

/**
 * 登录事件
 * 
 * @author jacky
 * 
 */
public class LoginEvent extends BaseEvent implements Event {

	public LoginEvent(String userId) {
		this.userId = userId;
	}

}
