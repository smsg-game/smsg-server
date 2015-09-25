package com.lodogame.ldsg.handler.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.CommandDao;
import com.lodogame.game.dao.UserDao;
import com.lodogame.ldsg.constants.CommandType;
import com.lodogame.ldsg.handler.CommandHandler;
import com.lodogame.ldsg.handler.CommandHandlerFactory;
import com.lodogame.ldsg.handler.DeifyPushHandler;
import com.lodogame.model.Command;

public class DeifyCommandHandlerImpl implements CommandHandler{

	@Autowired
	private DeifyPushHandler deifyPushHandler;
	
	@Autowired
	private CommandDao commandDao;
	
	@Autowired
	private UserDao userDao;
	
	@Override
	public void handle(Command command) {
		int comm = command.getCommand();
		int type = command.getType();
		Map<String, String> params = command.getParams();
		switch (comm) {
		case CommandType.COMMAND_DEIFY_FINISHED:
			handleDeifyFinished(type, params);
			break;
		case CommandType.COMMAND_DEIFY_PUSH_PROTECTED:
			handleDeifyProtected(type, params);
			break;
		case CommandType.COMMAND_DEIFY_PUSH_REPORT:
			handleDeifyReport(type, params);
			break;
		case CommandType.COMMAND_DEIFY_PUSH_ROOM_LIST:
			handleDeifyRoomList(type, params);
			break;
		case CommandType.COMMAND_DEIFY_PUSH_TOWER_LIST:
			handleDeifyTowerList(type, params);
			break;
		default:
			break;
		}
		
	}

	private void handleDeifyTowerList(int type, Map<String, String> params) {
		if (type == CommandType.PUSH_ALL) {

			Set<String> onlineUserIdList = this.userDao.getOnlineUserIdList();
			for (String userId : onlineUserIdList) {
				Map<String, String> m = new HashMap<String, String>(1);
				m.put("userId", userId);

				Command command = new Command();
				command.setCommand(CommandType.COMMAND_DEIFY_PUSH_TOWER_LIST);
				command.setType(CommandType.PUSH_USER);
				command.setParams(m);

				commandDao.add(command);
			}
		} else {
			String userId = params.get("userId");
			this.deifyPushHandler.pushTowerList(userId);
		}
		
	}

	private void handleDeifyRoomList(int type, Map<String, String> params) {
		if (type == CommandType.PUSH_ALL) {

			Set<String> onlineUserIdList = this.userDao.getOnlineUserIdList();
			for (String userId : onlineUserIdList) {
				Map<String, String> m = new HashMap<String, String>(1);
				m.put("userId", userId);
				m.put("towerId", params.get("towerId"));

				Command command = new Command();
				command.setCommand(CommandType.COMMAND_DEIFY_PUSH_ROOM_LIST);
				command.setType(CommandType.PUSH_USER);
				command.setParams(m);

				commandDao.add(command);
			}
		} else {
			String userId = params.get("userId");
			int towerId = Integer.valueOf(params.get("towerId"));
			this.deifyPushHandler.pushRoomList(userId, towerId);
		}
		
	}

	private void handleDeifyReport(int type, Map<String, String> params) {
		String userId = params.get("uid");
		this.deifyPushHandler.pushReport(userId, params);
		
	}

	private void handleDeifyProtected(int type, Map<String, String> params) {
		
		if (type == CommandType.PUSH_ALL) {

			Set<String> onlineUserIdList = this.userDao.getOnlineUserIdList();
			for (String userId : onlineUserIdList) {
				Map<String, String> m = new HashMap<String, String>(1);
				m.put("userId", userId);
				m.put("towerId", params.get("towerId"));

				Command command = new Command();
				command.setCommand(CommandType.COMMAND_DEIFY_PUSH_PROTECTED);
				command.setType(CommandType.PUSH_USER);
				command.setParams(m);

				commandDao.add(command);
			}
		} else {
			String userId = params.get("userId");
			int towerId = Integer.valueOf(params.get("towerId"));
			this.deifyPushHandler.pushProtected(userId, towerId);
		}
	}

	private void handleDeifyFinished(int type, Map<String, String> params) {
		String userId = params.get("uid");
		this.deifyPushHandler.pushDeifyStatus(userId);
	}

	@Override
	public void init() {
		CommandHandlerFactory.getInstance().register(7, this);
	}

}
