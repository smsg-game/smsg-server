package com.lodogame.ldsg.handler.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.ldsg.constants.CommandType;
import com.lodogame.ldsg.handler.CommandHandler;
import com.lodogame.ldsg.handler.CommandHandlerFactory;
import com.lodogame.ldsg.handler.FactionPushHandler;
import com.lodogame.model.Command;

public class FactionCommandHandlerImpl implements CommandHandler {

	@Autowired
	private FactionPushHandler factionPushHandler; 
	
	@Override
	public void handle(Command command) {
		int comm = command.getCommand();
		int type = command.getType();
		Map<String, String> params = command.getParams();

		switch (comm) {

		case CommandType.COMMAND_FACTION_APPLY_TIPS:
			handlePushFactionApplyTips(type, params);
			break;
		case CommandType.COMMAND_DISSOLVE_FACTION:
			handlePushDissolveFaction(type, params);
			break;
		default:
			break;
		}
		
	}
	
	private void handlePushFactionApplyTips(int type, Map<String, String> params){
		String userId = params.get("userId");
		this.factionPushHandler.pushFactionApplyTips(userId);
	}
	
	private void handlePushDissolveFaction(int type, Map<String, String> params){
		String userId = params.get("userId");
		this.factionPushHandler.pushDissolveFaction(userId);
	}

	@Override
	public void init() {
		CommandHandlerFactory.getInstance().register(9, this);
	}

}
