package com.lodogame.ldsg.handler.event;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.ldsg.event.DeifyEvent;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.factory.EventHandleFactory;
import com.lodogame.ldsg.service.MessageService;

public class DeifyEventHandle implements EventHandle {
	
	@Autowired
	private MessageService messageService;
	
	@Override
	public boolean handle(Event event) {
		if(!(event instanceof DeifyEvent)){
			return true;
		}
		

		String username = event.getString("username");
		String userHeroName = event.getString("userHeroName");
		String equipName = event.getString("equipName");
		
		messageService.sendDeifyForgeMsg(username, userHeroName, equipName);
		
		return true;
	}
	public void init() {
		EventHandleFactory.getInstance().register(this.getClass().getSimpleName(), this);
	}
}
