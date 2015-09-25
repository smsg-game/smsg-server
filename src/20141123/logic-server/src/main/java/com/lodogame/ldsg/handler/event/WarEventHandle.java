package com.lodogame.ldsg.handler.event;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.WarEvent;
import com.lodogame.ldsg.factory.EventHandleFactory;
import com.lodogame.ldsg.service.MessageService;

public class WarEventHandle implements EventHandle {

	@Autowired
	private MessageService messageService;
	
	@Override
	public boolean handle(Event event) {
		if(!(event instanceof WarEvent)){
			return true;
		}
		int sendType = event.getInt("sendType");
		String countryName = event.getString("countryName");
		String username = event.getString("username");
		
		if(sendType == 1){
			messageService.sendWarMajorCity(countryName, username);
		}else{
			String cityName = event.getString("cityName");
			messageService.sendWarDefense(countryName, username, cityName);
		}
		
		return true;
	}
	public void init() {
		EventHandleFactory.getInstance().register(this.getClass().getSimpleName(), this);
	}
}
