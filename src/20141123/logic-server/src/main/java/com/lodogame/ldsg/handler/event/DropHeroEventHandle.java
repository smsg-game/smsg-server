package com.lodogame.ldsg.handler.event;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.DropHeroEvent;
import com.lodogame.ldsg.factory.EventHandleFactory;
import com.lodogame.ldsg.service.MessageService;

public class DropHeroEventHandle implements EventHandle {
	
	@Autowired
	private MessageService messageService;
	
	@Override
	public boolean handle(Event event) {
		
		if(!(event instanceof DropHeroEvent)){
			return true;
		}
		
		String userId = event.getString("userId");
		String username = event.getString("username");
		String heroName = event.getString("heroName");
		int heroStar = event.getInt("heroStar");
		String toolName = event.getString("toolName");
		
		messageService.sendGainHeroMsg(userId, username, heroName, heroStar, toolName);
		
		return true;
	}
	
	public void init() {
		EventHandleFactory.getInstance().register(this.getClass().getSimpleName(), this);
	}

}
