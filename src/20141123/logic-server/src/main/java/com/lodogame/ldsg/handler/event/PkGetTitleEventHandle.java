package com.lodogame.ldsg.handler.event;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.PkGetTitleEvent;
import com.lodogame.ldsg.factory.EventHandleFactory;
import com.lodogame.ldsg.service.MessageService;

public class PkGetTitleEventHandle implements EventHandle {

	@Autowired
	private MessageService messageService;
	
	@Override
	public boolean handle(Event event) {
		if(!(event instanceof PkGetTitleEvent)){
			return true;
		}
		
		String userId = event.getUserId();
		String username = event.getString("username");
		String title = event.getString("title");
		
		if(!StringUtils.isBlank(title)){
			messageService.sendPkGetTitle(userId, username, title);
		}
		
		return true;
	}
	
	public void init() {
		EventHandleFactory.getInstance().register(this.getClass().getSimpleName(), this);
	}

}
