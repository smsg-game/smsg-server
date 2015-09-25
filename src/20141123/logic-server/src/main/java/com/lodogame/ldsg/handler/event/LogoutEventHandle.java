package com.lodogame.ldsg.handler.event;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.lodogame.game.dao.PkGroupAwardLogDao;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.LogoutEvent;
import com.lodogame.ldsg.factory.EventHandleFactory;
import com.lodogame.ldsg.service.MessageService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.PkGroupAwardLog;
import com.lodogame.model.User;

public class LogoutEventHandle implements EventHandle {

	@Autowired 
	private UserService userService;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private PkGroupAwardLogDao pkGroupAwardLogDao;
	
	@Override
	public boolean handle(Event event) {
		if(!(event instanceof LogoutEvent)){
			return true;
		}
		String userId = event.getUserId();
		User user = userService.get(userId);
		PkGroupAwardLog pkGroupAwardLog = pkGroupAwardLogDao.get(userId);
		
		if(pkGroupAwardLog != null){
			if(!StringUtils.isBlank(pkGroupAwardLog.getTitle()) && !pkGroupAwardLog.getTitle().equals("0")){
				messageService.sendPkUserLogout(pkGroupAwardLog.getTitle(), user.getUsername());
			}
		}
		
		return true;
	}

	public void init() {
		EventHandleFactory.getInstance().register(this.getClass().getSimpleName(), this);
	}
}
