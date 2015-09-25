package com.lodogame.ldsg.handler.event;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.SceneEvent;
import com.lodogame.ldsg.factory.EventHandleFactory;
import com.lodogame.ldsg.service.MessageService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.User;

public class SceneEventHandle implements EventHandle {

	@Autowired
	private UserService userService;

	@Autowired
	private MessageService messageService;

	@Override
	public boolean handle(Event event) {

		if (!(event instanceof SceneEvent)) {
			return true;
		}
		String userId = event.getString("userId");
		String scene = event.getString("scene");
		User user = userService.get(userId);

		messageService.sendSceneMsg(userId, user.getUsername(), scene);

		return true;
	}

	public void init() {
		EventHandleFactory.getInstance().register(this.getClass().getSimpleName(), this);
	}
}
