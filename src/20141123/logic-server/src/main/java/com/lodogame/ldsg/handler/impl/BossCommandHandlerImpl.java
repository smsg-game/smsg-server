package com.lodogame.ldsg.handler.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.CommandDao;
import com.lodogame.game.dao.UserDao;
import com.lodogame.ldsg.constants.CommandType;
import com.lodogame.ldsg.handler.BossPushHandler;
import com.lodogame.ldsg.handler.CommandHandler;
import com.lodogame.ldsg.handler.CommandHandlerFactory;
import com.lodogame.model.Command;

public class BossCommandHandlerImpl implements CommandHandler {

	private static final Logger logger = Logger.getLogger(BossCommandHandlerImpl.class);

	@Autowired
	private UserDao userDao;

	@Autowired
	private BossPushHandler bossPushHandler;

	@Autowired
	private CommandDao commandDao;

	@Override
	public void handle(Command command) {

		int comm = command.getCommand();
		int type = command.getType();
		Map<String, String> params = command.getParams();

		switch (comm) {
		case CommandType.COMMAND_BOSS_PUSH_BOSS_APPEAR:
			handleBossPushAppear(type, params);
			break;
		case CommandType.COMMAND_BOSS_PUSH_BOSS_DISAPPEAR:
			handleBossPushDisappear(type, params);
			break;
		case CommandType.COMMAND_BOSS_PUSH_BOSS_TEAM_USER_LOGOUT:
			handleBossPushTeamUserLogout(type, params);
			break;
		case CommandType.COMMAND_BOSS_PUSH_CHALLENGE_BOSS_RESULT:
			handleBossPushChallengeBossResult(type, params);
			break;
		case CommandType.COMMAND_BOSS_PUSH_USER_BOSS_INFORMATION:
			handleBossPushUserBossInfo(type, params);
			break;
		case CommandType.COMMAND_BOSS_PUSH_TEAM_DISMISSED:
			handleBossPushTeamDismissed(type, params);
			break;
		case CommandType.COMMAND_BOSS_PUSH_TEAM_UPDATE:
			handleBossPushTeamUpdate(type, params);
			break;
		default:
			break;
		}

	}

	private void handleBossPushAppear(int type, Map<String, String> params) {
		if (type == CommandType.PUSH_ALL) {
			logger.debug("开始处理推送魔物出现消息");

			Set<String> onlineUserIdList = this.userDao.getOnlineUserIdList();
			for (String userId : onlineUserIdList) {
				Map<String, String> m = new HashMap<String, String>(1);
				m.put("userId", userId);

				Command command = new Command();
				command.setCommand(CommandType.COMMAND_BOSS_PUSH_BOSS_APPEAR);
				command.setType(CommandType.PUSH_USER);
				command.setParams(m);

				commandDao.add(command);
			}
		} else {
			String userId = params.get("userId");
			this.bossPushHandler.pushBossAppear(userId);
		}
	}

	private void handleBossPushDisappear(int type, Map<String, String> params) {
		if (type == CommandType.PUSH_ALL) {
			logger.debug("开始处理推送魔物消失消息");

			Set<String> onlineUserIdList = this.userDao.getOnlineUserIdList();
			for (String userId : onlineUserIdList) {
				Map<String, String> m = new HashMap<String, String>(1);
				m.put("userId", userId);

				Command command = new Command();
				command.setCommand(CommandType.COMMAND_BOSS_PUSH_BOSS_DISAPPEAR);
				command.setType(CommandType.PUSH_USER);
				command.setParams(m);

				commandDao.add(command);
			}
		} else {
			String userId = params.get("userId");
			this.bossPushHandler.pushBossDisappear(userId);
		}
	}

	private void handleBossPushTeamUserLogout(int type, Map<String, String> params) {
		if (type == CommandType.PUSH_USER) {
			logger.debug("开始处理推送玩家离开封魔小队消息");

			long exitedPlayerId = Long.valueOf(params.get("pid"));
			String[] otherMembers = params.get("otherMembers").split(";");
			long captainId = Long.valueOf(params.get("captainId"));

			for (String otherMemberUserId : otherMembers) {
				if (otherMemberUserId.length() > 0) {
					this.bossPushHandler.pushBossTeamUserLogout(exitedPlayerId, otherMemberUserId, captainId);
				}
			}

		}
	}

	private void handleBossPushChallengeBossResult(int type, Map<String, String> params) {
		if (type == CommandType.PUSH_USER) {
			logger.debug("开始处理推送封魔结果消息");

			String reportsId = params.get("reportsId");
			String[] members = params.get("memebers").split(";");
			for (String userId : members) {
				if (userId.length() > 0) {
					this.bossPushHandler.pushResultOfChallengeBoss(userId, reportsId);
				}
			}
		}
	}

	private void handleBossPushUserBossInfo(int type, Map<String, String> params) {
		if (type == CommandType.PUSH_USER) {
			logger.debug("开始处理推送用户封魔信息");

			String[] members = params.get("memebers").split(";");
			for (String memId : members) {
				if (memId.length() > 0) {
					this.bossPushHandler.pushUserBossInfo(memId);
				}
			}
		}
	}

	private void handleBossPushTeamDismissed(int type, Map<String, String> params) {
		if (type == CommandType.PUSH_USER) {
			logger.debug("开始处理推送封魔小队解散消息");

			String teamId = params.get("teamId");
			int cause = Integer.valueOf(params.get("ca"));
			// int mid = Integer.valueOf(params.get("mid"));

			this.bossPushHandler.pushDismissTeam(teamId, cause);
		}
	}

	private void handleBossPushTeamUpdate(int type, Map<String, String> params) {

		int status = Integer.parseInt(params.get("status"));
		int forcesId = Integer.parseInt(params.get("forcesId"));
		String teamId = params.get("teamId");

		this.bossPushHandler.pushUpdatedTeamInfo(teamId, forcesId, status);

	}

	@Override
	public void init() {
		CommandHandlerFactory.getInstance().register(3, this);
	}

}
