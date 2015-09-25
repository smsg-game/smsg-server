package com.lodogame.ldsg.action;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.server.response.Response;
import com.lodogame.ldsg.handler.PushHandler;
import com.lodogame.ldsg.service.ChatService;

/**
 * 聊天相关的action
 * 
 * @author jacky
 * 
 */

public class ChatAction extends LogicRequestAction {

	private static final Logger LOG = Logger.getLogger(ChatAction.class);

	@Autowired
	private ChatService chatService;

	@Autowired
	private PushHandler pushHandler;
	
	/**
	 * 发送消息
	 * @return
	 */
	public Response sendMessage() {

		LOG.debug("发送消息.uid[" + getUid() + "]");

		int channel = this.getInt("channel", 1);
		String toUserName = this.getString("tuName", null);
		String content = this.getString("content", null);
		String uid = this.chatService.sendMessage(getUid(), channel, toUserName, content);
		set("userId", uid);
 		return this.render(); 

	}

	/**
	 * 禁言
	 * 
	 */
	public Response bannedToPost() {
		LOG.debug("禁言.uid[" + getUid() + "]");
		this.chatService.bannedToPost(getUid());
		pushHandler.pushUser(getUid());
		return this.render();
	}
	

}
