package com.lodogame.ldsg.handler.event;

import org.springframework.beans.factory.annotation.Autowired;
import com.lodogame.ldsg.event.ArenaEvent;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.factory.EventHandleFactory;
import com.lodogame.ldsg.service.MessageService;

public class ArenaEventHandle implements EventHandle {
	
	@Autowired
	private MessageService messageService;
	
	@Override
	public boolean handle(Event event) {
		
		if(!(event instanceof ArenaEvent)){
			return true;
		}
		
		String rank1 = event.getString("rank1");
		String username1 = event.getString("username1");
		String rank2 = event.getString("rank2");
		String username2 = event.getString("username2");
		String rank3 = event.getString("rank3");
		String username3 = event.getString("username3");
		
		messageService.sendArenaMsg(rank1,username1,rank2,username2,rank3,username3);
		
		return true;
	}
	public void init() {
		EventHandleFactory.getInstance().register(this.getClass().getSimpleName(), this);
	}
}
