package com.lodogame.ldsg.handler.impl;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.ldsg.constants.CommandType;
import com.lodogame.ldsg.handler.ArenaPusHandler;
import com.lodogame.ldsg.handler.CommandHandler;
import com.lodogame.ldsg.handler.CommandHandlerFactory;
import com.lodogame.ldsg.service.ArenaService;
import com.lodogame.model.Command;

public class ArenaCommandHandlerImpl implements CommandHandler {

	private static final Logger logger = Logger.getLogger(ArenaCommandHandlerImpl.class);

	@Autowired
	private ArenaPusHandler arenaPushHandler;

	@Autowired
	private ArenaService arenaService;

	@Override
	public void handle(final Command command) {

		int comm = command.getCommand();
		int type = command.getType();
		Map<String, String> params = command.getParams();

		switch (comm) {

		case CommandType.COMMAND_ARENA_PUSH_USER_INFO:
			handlePushAreanUserInfo(type, params);
			break;
		case CommandType.COMMAND_ARENA_PUSH_RANK_LIST:
			handlePushAreanRankList(type, params);
			break;
		case CommandType.COMMAND_ARENA_PUSH_REWARD:
			handlePushAreanReward(type, params);
			break;
		case CommandType.COMMAND_ARENA_PUSH_BATTLE_LOG:
			handlePushAreanBattleRecord(type, params);
			break;
		case CommandType.COMMAND_ARENA_PUSH_BATTLE:
			handlePushAreanBattle(type, params);
			break;
		case CommandType.COMMAND_ARENA_PAUSE:
			handlePauseArena(type, params);
			break;

		default:
			break;

		}

	}

	private void handlePushAreanBattle(int type, Map<String, String> params) {
		String userId = params.get("userId");
		this.arenaPushHandler.pushBattle(userId, params);
	}

	private void handlePauseArena(int type, Map<String, String> params) {
		String cmd = params.get("CMD");
		this.arenaService.execute(cmd);
	}

	private void handlePushAreanReward(int type, Map<String, String> params) {
		String userId = params.get("userId");
		this.arenaPushHandler.pushReward(userId);
	}

	private void handlePushAreanBattleRecord(int type, Map<String, String> params) {
		String userId = params.get("userId");
		this.arenaPushHandler.pushBattleRecord(userId, params);
	}

	private void handlePushAreanUserInfo(int type, Map<String, String> params) {
		String userId = params.get("userId");
		this.arenaPushHandler.pushUserInfo(userId);
	}

	private void handlePushAreanRankList(int type, Map<String, String> params) {
		String userId = params.get("userId");
		this.arenaPushHandler.pushRank(userId);
	}

	@Override
	public void init() {
		CommandHandlerFactory.getInstance().register(4, this);
	}

}
