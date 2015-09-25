package com.lodogame.ldsg.handler.impl;

import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.lodogame.ldsg.constants.CommandType;
import com.lodogame.ldsg.handler.CommandHandler;
import com.lodogame.ldsg.handler.CommandHandlerFactory;
import com.lodogame.ldsg.handler.WarPushHandler;
import com.lodogame.model.Command;

public class WarCommandHandlerImpl implements CommandHandler {

	private static final Logger logger = Logger.getLogger(WarCommandHandlerImpl.class);

	@Autowired
	private WarPushHandler warPushHandler;

	@Override
	public void handle(final Command command) {
		int comm = command.getCommand();
		int type = command.getType();
		Map<String, String> params = command.getParams();

		switch (comm) {
		case CommandType.COMMAND_WAR_ALL_CITY:
			handlePushAllCity(type);
			break;
		case CommandType.COMMAND_WAR_END:
			handlePushWarEnd(type);
			break;
		case CommandType.COMMAND_WAR_DEFENSE_NUM:
			handlePushDefenseNum(type, params);
			break;
		case CommandType.COMMAND_WAR_DEFENSE_REPORT:
			handlePushBattle(type, params);
			break;
		default:
			break;
		}

	}

	public void handlePushAllCity(int type) {
		this.warPushHandler.pushAllCity();
	}

	public void handlePushWarEnd(int type) {
		this.warPushHandler.pushWarEnd();
	}

	public void handlePushDefenseNum(int type, Map<String, String> params) {
		String userId = params.get("userId");
		this.warPushHandler.pushDefenseNum(userId, params);
	}

	public void handlePushBattle(int type, Map<String, String> params) {
		String userId = params.get("userId");
		this.warPushHandler.pushBattle(userId, params);
	}

	@Override
	public void init() {
		CommandHandlerFactory.getInstance().register(6, this);
	}

}
