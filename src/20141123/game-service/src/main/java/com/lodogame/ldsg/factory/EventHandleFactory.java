package com.lodogame.ldsg.factory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.lodogame.ldsg.event.EventHandle;

public class EventHandleFactory {

	private static final Logger LOG = Logger.getLogger(EventHandleFactory.class);

	private Map<String, EventHandle> handleMap = new HashMap<String, EventHandle>();

	private static EventHandleFactory instance = null;

	private EventHandleFactory() {

	}

	public static EventHandleFactory getInstance() {

		if (instance == null) {
			instance = new EventHandleFactory();
		}
		return instance;
	}

	public void register(String className, EventHandle eventHandle) {
		LOG.info("eventHandle register.class[" + className + "]");
		handleMap.put(className, eventHandle);
	}

	public Iterator<EventHandle> iterator() {
		return handleMap.values().iterator();
	}
}
