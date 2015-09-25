package com.lodogame.ldsg.handler.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.UserDao;
import com.lodogame.game.dao.UserFactionDao;
import com.lodogame.ldsg.bo.ChatBO;
import com.lodogame.ldsg.constants.CommandType;
import com.lodogame.ldsg.handler.CommandHandler;
import com.lodogame.ldsg.handler.CommandHandlerFactory;
import com.lodogame.ldsg.handler.PushHandler;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.Command;
import com.lodogame.model.User;
import com.lodogame.model.UserFaction;

public class ChatCommandHandlerImpl implements CommandHandler {


	@Autowired
	private PushHandler pushHandler;

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserFactionDao userFactionDao;
//	
//	@Autowired
//	private FriendServiceImpl friendServiceImpl;
	
	@Override
	public void handle(final Command command) {

		int comm = command.getCommand();
		int type = command.getType();
		Map<String, String> params = command.getParams();

		switch (comm) {
		case CommandType.COMMAND_CHAT_PLIVATE:
			handlePushChatInfo(type, params);
			break;
		case CommandType.COMMAND_CHAT_ALL:
			handlePushAllChatInfo(type, params);
			break;
		case CommandType.COMMAND_CHAT_FACTION_ALL:
			handlePushAllFactionChatInfo(type, params);
			break;
		default:
			break;
		}

	}

	private void handlePushAllFactionChatInfo(int type, Map<String, String> params) {
		String userId = params.get("userId");
		String content = params.get("content");
		User user = this.userService.get(userId);
		ChatBO chatBO = new ChatBO();
		chatBO.setContent(content);
		chatBO.setUsername(user.getUsername());
		chatBO.setLevel(user.getLevel());
		chatBO.setVipLevel(user.getVipLevel());
		chatBO.setChannel(4);
		chatBO.setUserId(userId);
		Set<String> onlineUserIdList = this.userDao.getOnlineUserIdList();
		List<UserFaction>  userFactionList = this.userFactionDao.getFactionMemberByFid(user.getFactionId());
		
		for (UserFaction userFaction : userFactionList) {
			String uid = userFaction.getUserId();
            if (onlineUserIdList.contains(uid) && !uid.equalsIgnoreCase(userId)) {
            	this.pushHandler.pushChat(uid, chatBO);
            }
		}
	}
	
	private void handlePushAllChatInfo(int type, Map<String, String> params) {
		String userId = params.get("userId");
		String content = params.get("content");
		User user = this.userService.get(userId);
		ChatBO chatBO = new ChatBO();
		chatBO.setContent(content);
		chatBO.setUsername(user.getUsername());
		chatBO.setLevel(user.getLevel());
		chatBO.setVipLevel(user.getVipLevel());
		chatBO.setChannel(1);
		chatBO.setUserId(userId);
		Set<String> onlineUserIdList = this.userDao.getOnlineUserIdList();
		for (String uid : onlineUserIdList) {
//			if (uid.equals(userId)) {
//				continue;
//			}
//			if (friendServiceImpl.isInBlackList(uid, userId)) {
//				continue;
//			}
			this.pushHandler.pushChat(uid, chatBO);
		}
	}

	private void handlePushChatInfo(int type, Map<String, String> params) {
		String userId = params.get("userId");
		String content = params.get("content");
		String uid = params.get("uid");
		//过滤黑名单
//		if (friendServiceImpl.isInBlackList(userId, uid)) {
//			return;
//		}
		ChatBO chatBO = new ChatBO();
		User user = this.userService.get(uid);
	    chatBO.setContent(content);
	    chatBO.setUsername(user.getUsername());
	    chatBO.setLevel(user.getLevel());
	    chatBO.setVipLevel(user.getVipLevel());
	    chatBO.setChannel(2);
	    chatBO.setUserId(uid);
	    this.pushHandler.pushChat(uid, chatBO);
		this.pushHandler.pushChat(userId, chatBO);
	}

	@Override
	public void init() {
		CommandHandlerFactory.getInstance().register(8, this);
	}
	
}
