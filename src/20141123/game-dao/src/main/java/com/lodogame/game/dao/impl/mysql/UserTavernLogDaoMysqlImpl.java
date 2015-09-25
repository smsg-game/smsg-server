package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserTavernLogDao;

public class UserTavernLogDaoMysqlImpl implements UserTavernLogDao {

	private String table = "user_tavern_log";

	@Autowired
	private Jdbc jdbc;

	@Override
	public boolean isExistLog(String userId) {

		String sql = "SELECT user_tavern_log_id FROM " + table + " WHERE user_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);

		return this.jdbc.getInt(sql, parameter) > 0;

	}

	@Override
	public boolean addTavernLog(String userId) {

		String sql = "INSERT INTO " + table + "(user_id, created_time) VALUES(?, now())";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);

		return this.jdbc.update(sql, parameter) > 0;

	}

}
