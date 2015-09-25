package com.lodogame.ldsg.handler.log;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.LogDao;
import com.lodogame.ldsg.handler.LogHandle;
import com.lodogame.ldsg.handler.LogHandlerFactory;
import com.lodogame.model.HeroLog;
import com.lodogame.model.ILog;

public class HeroLogHandlerImpl implements LogHandle {

	@Autowired
	private LogDao logDao;

	@Override
	public void handle(ILog log) {
		HeroLog heroLog = (HeroLog) log;
		logDao.heroLog(heroLog.getUserId(), heroLog.getUserHeroId(), heroLog.getSystemHeroId(), heroLog.getUseType(), heroLog.getFlag(), heroLog.getHeroExp(), heroLog.getHeroLevel());
	}

	@Override
	public void init() {
		LogHandlerFactory.getInstance().register(HeroLog.class, this);
	}

}
