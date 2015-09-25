package com.lodogame.ldsg.event;

import java.util.List;

/**
 * 百人斩消息
 * @author Candon
 *
 */
public class ArenaEvent extends BaseEvent implements Event {
	
	public ArenaEvent(String rank1, String username1, String rank2, String username2, String rank3, String username3){
		this.setObject("rank1", rank1);
		this.setObject("username1", username1);
		this.setObject("rank2", rank2);
		this.setObject("username2", username2);
		this.setObject("rank3", rank3);
		this.setObject("username3", username3);
	}
}
