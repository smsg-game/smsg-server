package com.lodogame.ldsg.event;

public class PkFightEvent extends BaseEvent implements Event {
	
	public PkFightEvent(String userId,String attack,String defense,int rank,int attackLevel, int defenseLevel, String defenseUserId){
		this.userId = userId;
		this.setObject("attack", attack);
		this.setObject("defense", defense);
		this.setObject("rank", rank);
		this.setObject("attackLevel", attackLevel);
		this.setObject("defenseLevel", defenseLevel);
		this.setObject("defenseUserId", defenseUserId);
	}
}
