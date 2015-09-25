package com.lodogame.ldsg.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.ChatLogDao;
import com.lodogame.game.dao.CommandDao;
import com.lodogame.game.utils.Constant;
import com.lodogame.game.utils.IllegalWordUtills;
import com.lodogame.ldsg.constants.CommandType;
import com.lodogame.ldsg.constants.InitDefine;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.service.ChatService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.ChatLog;
import com.lodogame.model.Command;
import com.lodogame.model.User;



public class ChatServiceImpl implements ChatService {

	@Autowired
	private UserService userService;
	
	@Autowired
	private CommandDao commandDao;
	
	@Autowired
	private ChatLogDao chatLogDao;
	
	@Override
	public String sendMessage(String userId, int channel, String toUserName, String content) {

		if (channel ==2) {
			//私聊的对象是否正确
	        chenckToUserId(toUserName);	
		}
		
	    //发送的消息是否正确
		checkMessageIsRight(content);
	
		//检查发送消息的用户是否合法
        chenckUser(userId);
        String toUserId = null;
		//发送消息
		if (channel == 1) {
			this.pushAllUserMessage(userId, content);
		} else if(channel == 2) {
		     toUserId = this.pushUserMessage(userId, toUserName, content);
		} else if (channel == 4) {
			this.pushAllFactionUserMessage(userId, content);
		}
		
		//保存日志
		ChatLog chatLog = new ChatLog();
		chatLog.setContent(content);
		chatLog.setToUserName(toUserName);
		chatLog.setChannel(channel);
		chatLog.setCreatedTime(new Date());
		chatLog.setUserId(userId);
		this.chatLogDao.addChatLog(chatLog);
		return toUserId;
	}

	//推送帮派消息
	public void pushAllFactionUserMessage(String userId, String content){
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", userId);
		params.put("content", content);

		Command command = new Command();
		command.setCommand(CommandType.COMMAND_CHAT_FACTION_ALL);
		command.setType(CommandType.PUSH_LOGIN);
		command.setParams(params);

		commandDao.add(command);
	}
	
	
	//推送私聊消息
	public String pushUserMessage(String userId, String toUserName, String content){
		
		User toUser = this.userService.getUserByUserName(toUserName);
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", toUser.getUserId());
		params.put("content", content);
		params.put("uid", userId);

		Command command = new Command();
		command.setCommand(CommandType.COMMAND_CHAT_PLIVATE);
		command.setType(CommandType.PUSH_USER);
		command.setParams(params);

		commandDao.add(command);
		return toUser.getUserId();
	}
	
	//推送世界消息
	public void pushAllUserMessage(String userId, String content){
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", userId);
		params.put("content", content);

		Command command = new Command();
		command.setCommand(CommandType.COMMAND_CHAT_ALL);
		command.setType(CommandType.PUSH_LOGIN);
		command.setParams(params);

		commandDao.add(command);
	}
	
	private void chenckUser(String userId) {
	    //是否被禁言
		User user = this.userService.get(userId);
		if (user.getBannedChatTime() != null){
			Date now = new Date();// 当前时间
			long timestamp1 = now.getTime();
			Date timestamp2 = user.getBannedChatTime();
			long sub = timestamp1 - timestamp2.getTime();
			if (sub <  InitDefine.BANNED_TO_POST_TIME_INTERVAL) {
				throw new ServiceException(CHAT_USER_IS_BANNED, "发送消息失败,该用户正在禁言期间内");
			}
		}
	}

	private void checkMessageIsRight(String content) {

		if (content.length()  == 0 ) {
			throw new ServiceException(CHAT_MESSAGE_IS_NULL, "消息长度非法.content[" + content + "]");
		} else if (Constant.getBytes(content) > 50) {
			throw new ServiceException(CHAT_MESSAGE_TOO_LONG, "消息长度非法.content[" + content + "]");
		} else if (!IllegalWordUtills.checkWords(content)) {
			throw new ServiceException(CHART_HAS_CONTAIN_ILLEGAL_WORDS, "消息包含非法文字.username[" + content + "]");
		}
			 
	}

	public void chenckToUserId(String toUserName ){
		
		User user = this.userService.getUserByUserName(toUserName);
		
		if ( user == null) {
			throw new ServiceException(CHAT_TO_USER_NOT_EXIST, "发送消息失败，该用户不存在.toUserName[" + toUserName + "]");
		}
		
		if (!this.userService.isOnline(user.getUserId())) {
			throw new ServiceException(CHAT_TO_USER_NOT_ONLINE, "发送消息失败，该用户在线.toUserName[" + toUserName + "]");
		}
		
	}

	@Override
	public void bannedToPost(String userId) {

		this.userService.bannedToPost(userId);
		
	}

	
	
}
