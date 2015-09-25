package com.lodogame.ldsg.handler.event;

import org.springframework.beans.factory.annotation.Autowired;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.VipLevelEvent;
import com.lodogame.ldsg.factory.EventHandleFactory;
import com.lodogame.ldsg.service.MessageService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.User;

/**
 * 
 * @author Candon
 *
 */
public class VipLevelEventHandle implements EventHandle {

	@Autowired 
	private UserService userService;
	
	@Autowired
	private MessageService messageService;
	
	@Override
	public boolean handle(Event event) {
		
		if(!(event instanceof VipLevelEvent)){
			return true;
		}
		
//		String userId = event.getUserId();
//		User user = userService.get(userId);
//		
//		if(user != null && user.getVipLevel() >= 7){
//			messageService.sendVipLevelMsg(userId, user.getUsername(), user.getVipLevel());
//		}
//		
		return true;
	}
	
	public void init() {
		EventHandleFactory.getInstance().register(this.getClass().getSimpleName(), this);
	}

}
