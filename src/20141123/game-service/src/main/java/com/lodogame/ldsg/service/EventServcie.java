package com.lodogame.ldsg.service;

import com.lodogame.ldsg.event.Event;

public interface EventServcie {

	/**
	 * 分发事件
	 * 
	 * @param event
	 */
	public void dispatchEvent(Event event);

	public void init();
}
