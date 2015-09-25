package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.game.dao.ChatLogDao;
import com.lodogame.model.ChatLog;

public class ChatLogDaoMysqlImpl implements ChatLogDao {

	@Autowired
	private Jdbc jdbc;

	public final static String table = "chart_log";

	@Override
	public boolean addChatLog(ChatLog chatLog) {
		return this.jdbc.insert(chatLog) > 0;
	}




}
