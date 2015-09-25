package com.lodogame.ldsg.handler.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.CommandDao;
import com.lodogame.game.dao.UserDao;
import com.lodogame.ldsg.constants.CommandType;
import com.lodogame.ldsg.handler.CommandHandler;
import com.lodogame.ldsg.handler.CommandHandlerFactory;
import com.lodogame.ldsg.handler.ContestPushHandler;
import com.lodogame.model.Command;

public class ContestCommandHandlerImpl implements CommandHandler{
	private static final Logger logger = Logger.getLogger(ContestCommandHandlerImpl.class);

	@Autowired
	private ContestPushHandler contestPushHandler;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private CommandDao commandDao;
	
	@Override
	public void handle(Command command) {

		int comm = command.getCommand();
		int type = command.getType();
		Map<String, String> params = command.getParams();
		
		switch(comm) {
		case CommandType.COMMAND_CONTEST_BET_SUCCESS:
			handleContestBetSuccess(type, params);
			break;
		case CommandType.COMMAND_CONTEST_REG:
			handleContestUserReg(type, params);
			break;
		case CommandType.COMMAND_CONTEST_ROUND_FINISHED:
			handleContestRoundFinished(type, params);
			break;
		case CommandType.COMMAND_CONTEST_STATUS_CHANGED:
			handleContestStatusChanged(type, params);
			break;
		default:
			break;
		}
	}

	private void handleContestStatusChanged(int type, Map<String, String> params) {
		if (type == CommandType.PUSH_ALL) {
			logger.debug("开始处理推送擂台赛状态改变消息");

			Set<String> onlineUserIdList = this.userDao.getOnlineUserIdList();
			for (String userId : onlineUserIdList) {
				Map<String, String> m = new HashMap<String, String>(1);
				m.put("userId", userId);

				Command command = new Command();
				command.setCommand(CommandType.COMMAND_CONTEST_STATUS_CHANGED);
				command.setType(CommandType.PUSH_USER);
				command.setParams(m);

				commandDao.add(command);
			}
		} else {
			String userId = params.get("userId");
			this.contestPushHandler.pushStatus(userId);
		}
	}

	private void handleContestRoundFinished(int type, Map<String, String> params) {
		if (type == CommandType.PUSH_ALL) {
			logger.debug("开始处理推送擂台赛结束一场比赛消息");

			Set<String> onlineUserIdList = this.userDao.getOnlineUserIdList();
			for (String userId : onlineUserIdList) {
				Map<String, String> m = new HashMap<String, String>(1);
				m.put("userId", userId);

				Command command = new Command();
				command.setCommand(CommandType.COMMAND_CONTEST_ROUND_FINISHED);
				command.setType(CommandType.PUSH_USER);
				command.setParams(m);

				commandDao.add(command);
			}
		} else {
			String userId = params.get("userId");
			this.contestPushHandler.pushRoundFinished(userId);
		}
		
	}

	private void handleContestUserReg(int type, Map<String, String> params) {
		if (type == CommandType.PUSH_ALL) {
			logger.debug("开始处理推送用户注册比赛消息");

			Set<String> onlineUserIdList = this.userDao.getOnlineUserIdList();
			for (String userId : onlineUserIdList) {
				Map<String, String> m = new HashMap<String, String>(1);
				m.put("userId", userId);

				Command command = new Command();
				command.setCommand(CommandType.COMMAND_CONTEST_REG);
				command.setType(CommandType.PUSH_USER);
				command.setParams(m);

				commandDao.add(command);
			}
		} else {
			String userId = params.get("userId");
			this.contestPushHandler.pushUserReg(userId);
		}
		
	}

	private void handleContestBetSuccess(int type, Map<String, String> params) {
		if (type == CommandType.PUSH_ALL) {
			logger.debug("开始处理推送擂台赛下注消息");

			Set<String> onlineUserIdList = this.userDao.getOnlineUserIdList();
			for (String userId : onlineUserIdList) {
				Map<String, String> m = new HashMap<String, String>(1);
				m.put("userId", userId);

				Command command = new Command();
				command.setCommand(CommandType.COMMAND_CONTEST_BET_SUCCESS);
				command.setType(CommandType.PUSH_USER);
				command.setParams(m);

				commandDao.add(command);
			}
		} else {
			String userId = params.get("userId");
			this.contestPushHandler.pushBet(userId);
		}
	}

	@Override
	public void init() {
		CommandHandlerFactory.getInstance().register(5, this);
	}

}
