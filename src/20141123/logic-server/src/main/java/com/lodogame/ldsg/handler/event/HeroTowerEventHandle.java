package com.lodogame.ldsg.handler.event;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.HeroTowerEvent;
import com.lodogame.ldsg.factory.EventHandleFactory;
import com.lodogame.ldsg.service.MessageService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.User;

public class HeroTowerEventHandle implements EventHandle {
	@Autowired
	private UserService userService;
	
	@Autowired 
	private MessageService messageService;
	
	@Override
	public boolean handle(Event event) {
		if(!(event instanceof HeroTowerEvent)){
			return true;
		}
		
		String userId = event.getString("userId");
		User user = userService.get(userId);
		String heroName = event.getString("heroName");
		messageService.sendHeroTowerMsg(userId, user.getUsername(), heroName);
		
		return true;
	}
	
	public void init() {
		EventHandleFactory.getInstance().register(this.getClass().getSimpleName(), this);
	}

}
