package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserCheckInLogDao;
import com.lodogame.model.UserCheckinLog;

public class UserCheckInLogDaoMysqlImpl implements UserCheckInLogDao {

	private String table = "user_checkin_log";

	@Autowired
	private Jdbc jdbc;

	@Override
	public boolean addUserCheckInLog(UserCheckinLog userCheckInLog) {
		return this.jdbc.insert(userCheckInLog) > 0;
	}

	@Override
	public UserCheckinLog getLastUserCheckInLog(String userId, int groupId) {

		String sql = "SELECT * FROM " + table + " WHERE user_id = ? AND group_id = ? ORDER BY day DESC LIMIT 1";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(groupId);

		return this.jdbc.get(sql, UserCheckinLog.class, parameter);
	}

	@Override
	public boolean cleanUserCheckInLog() {

		String sql = "truncate table " + table;

		SqlParameter parameter = new SqlParameter();

		return this.jdbc.update(sql, parameter) > 0;
	}

}
