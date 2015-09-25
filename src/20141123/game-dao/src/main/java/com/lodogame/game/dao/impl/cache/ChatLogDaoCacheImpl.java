package com.lodogame.game.dao.impl.cache;

import com.lodogame.game.dao.ChatLogDao;
import com.lodogame.model.ChatLog;



public class ChatLogDaoCacheImpl implements ChatLogDao {

	private ChatLogDao chatLogDaoMysqlImpl;

	public void setChatLogDaoMysqlImpl(ChatLogDao chatLogDaoMysqlImpl) {
		this.chatLogDaoMysqlImpl = chatLogDaoMysqlImpl;
	}

	@Override
	public boolean addChatLog(ChatLog chatLog) {
		return this.chatLogDaoMysqlImpl.addChatLog(chatLog);
	}

}
