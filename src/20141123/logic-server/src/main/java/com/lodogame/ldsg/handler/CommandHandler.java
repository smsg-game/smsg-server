package com.lodogame.ldsg.handler;

import com.lodogame.model.Command;

public interface CommandHandler {

	/**
	 * 处理命令
	 * 
	 * @param command
	 */
	public void handle(Command command);

	public void init();

}
