package com.lodogame.ldsg.handler;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class CommandHandlerFactory {

	private static final Logger LOG = Logger.getLogger(CommandHandlerFactory.class);

	private Map<Integer, CommandHandler> commandHandlerMap = new HashMap<Integer, CommandHandler>();

	private static CommandHandlerFactory instance = null;

	private CommandHandlerFactory() {

	}

	public static CommandHandlerFactory getInstance() {

		if (instance == null) {
			instance = new CommandHandlerFactory();
		}
		return instance;
	}

	public void register(int command, CommandHandler handler) {
		LOG.info("command handler register.command[" + command + "]");
		commandHandlerMap.put(command, handler);
	}

	public CommandHandler getHandler(int command) {

		int key = command / 10000;

		if (commandHandlerMap.containsKey(key)) {
			return commandHandlerMap.get(key);
		} else {
			LOG.warn("命令处理器不存在.command[" + command + "], key[" + key + "]");
		}

		return null;
	}

}
