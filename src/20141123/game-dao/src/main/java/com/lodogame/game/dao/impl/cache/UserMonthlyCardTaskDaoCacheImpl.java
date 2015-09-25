package com.lodogame.game.dao.impl.cache;

import com.lodogame.game.dao.UserMonthlyCardTaskDao;
import com.lodogame.game.dao.impl.mysql.UserMonthlyCardTaskDaoMysqlImpl;
import com.lodogame.model.UserMonthlyCardTask;

public class UserMonthlyCardTaskDaoCacheImpl implements UserMonthlyCardTaskDao {
	
	private UserMonthlyCardTaskDaoMysqlImpl userMonthlyCardTaskDaoMysqlImpl;

	public void setUserMonthlyCardTaskDaoMysqlImpl(
			UserMonthlyCardTaskDaoMysqlImpl userMonthlyCardTaskDaoMysqlImpl) {
		this.userMonthlyCardTaskDaoMysqlImpl = userMonthlyCardTaskDaoMysqlImpl;
	}

	@Override
	public UserMonthlyCardTask getByUserId(String userId) {
		UserMonthlyCardTask task = userMonthlyCardTaskDaoMysqlImpl.getByUserId(userId);
		return task;
	}
	
	@Override
	public boolean add(UserMonthlyCardTask task) {
		return userMonthlyCardTaskDaoMysqlImpl.add(task);
	}
	@Override
	public void update(UserMonthlyCardTask task) {
		userMonthlyCardTaskDaoMysqlImpl.update(task);
	}
}
