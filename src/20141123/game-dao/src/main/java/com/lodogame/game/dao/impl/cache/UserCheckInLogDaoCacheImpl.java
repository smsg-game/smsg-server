package com.lodogame.game.dao.impl.cache;

import com.lodogame.game.dao.UserCheckInLogDao;
import com.lodogame.model.UserCheckinLog;

public class UserCheckInLogDaoCacheImpl implements UserCheckInLogDao {

	private UserCheckInLogDao userCheckInLogDaoMysqlImpl;

	public void setUserCheckInLogDaoMysqlImpl(UserCheckInLogDao userCheckInLogDaoMysqlImpl) {
		this.userCheckInLogDaoMysqlImpl = userCheckInLogDaoMysqlImpl;
	}

	@Override
	public boolean addUserCheckInLog(UserCheckinLog userCheckInLog) {
		return this.userCheckInLogDaoMysqlImpl.addUserCheckInLog(userCheckInLog);
	}

	@Override
	public UserCheckinLog getLastUserCheckInLog(String userId, int groupId) {
		return this.userCheckInLogDaoMysqlImpl.getLastUserCheckInLog(userId, groupId);
	}

	@Override
	public boolean cleanUserCheckInLog() {
		return this.userCheckInLogDaoMysqlImpl.cleanUserCheckInLog();
	}

}
