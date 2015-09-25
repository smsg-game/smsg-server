package com.lodogame.ldsg.event;

/**
 * 争霸获得称号
 * @author Candon
 *
 */
public class PkGetTitleEvent extends BaseEvent implements Event {

	public PkGetTitleEvent(String userId, String username, String title){
		this.userId = userId;
		setObject("username", username);
		setObject("title", title);
	}
}
