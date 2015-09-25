package com.lodogame.ldsg.event;

/**
 * 用户等级升级后触发的事件
 * @author chengevo
 *
 */
public class UserLevelUpEvent extends BaseEvent implements Event {

	protected int userLevel; 
	
	public UserLevelUpEvent(String uid, int userLevel) {
		this.userId = uid;
		this.data.put("userLevel", userLevel);
	}
}
