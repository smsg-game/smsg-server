package com.lodogame.ldsg.handler.log;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.LogDao;
import com.lodogame.ldsg.handler.LogHandle;
import com.lodogame.ldsg.handler.LogHandlerFactory;
import com.lodogame.model.ILog;
import com.lodogame.model.UserLevelUpLog;

public class UserLevelUpLogHandlerImpl implements LogHandle {

	@Autowired
	private LogDao logDao;

	@Override
	public void handle(ILog log) {
		UserLevelUpLog userLevelUpLog = (UserLevelUpLog) log;
		logDao.levelUpLog(userLevelUpLog.getUserId(), userLevelUpLog.getLevel(), userLevelUpLog.getExp());
	}

	@Override
	public void init() {
		LogHandlerFactory.getInstance().register(UserLevelUpLog.class, this);
	}

}
