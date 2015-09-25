package com.lodogame.ldsg.handler;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class LogHandlerFactory {

	private static final Logger LOG = Logger.getLogger(LogHandlerFactory.class);

	private Map<Class<?>, LogHandle> logHandlerMap = new HashMap<Class<?>, LogHandle>();

	private static LogHandlerFactory instance = null;

	private LogHandlerFactory() {

	}

	public static LogHandlerFactory getInstance() {

		if (instance == null) {
			instance = new LogHandlerFactory();
		}
		return instance;
	}

	public void register(Class<?> cls, LogHandle handler) {
		LOG.info("log handler register.cls[" + cls.getName() + "]");
		logHandlerMap.put(cls, handler);
	}

	public LogHandle getHandler(Class<?> cls) {

		if (logHandlerMap.containsKey(cls)) {
			return logHandlerMap.get(cls);
		} else {
			LOG.warn("日志处理器不存在.command[" + cls + "]");
		}

		return null;
	}
}
