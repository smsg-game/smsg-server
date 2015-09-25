package com.lodogame.game.dao.impl.mysql;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserOnlineLogDao;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.game.utils.TableUtils;
import com.lodogame.model.UserOnlineLog;

public class UserOnlineLogDaoMysqlImpl implements UserOnlineLogDao {

	@Autowired
	private Jdbc jdbc;

	public final static String columns = "*";

	@Override
	public boolean add(UserOnlineLog userOnlineLog) {

		String table = TableUtils.getUserOnlineLogTable(userOnlineLog.getUserId());

		String sql = "INSERT INTO " + table + "(user_id, login_time, logout_time, user_ip) VALUES(?, ?, ?, ?)";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userOnlineLog.getUserId());
		parameter.setObject(userOnlineLog.getLoginTime());
		parameter.setObject(userOnlineLog.getLogoutTime());
		parameter.setString(userOnlineLog.getUserIp());

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public UserOnlineLog getLastOnlineLog(String userId) {

		String table = TableUtils.getUserOnlineLogTable(userId);

		String sql = "SELECT " + columns + " FROM " + table + " WHERE user_id = ? ORDER BY log_id DESC LIMIT 1";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);

		return this.jdbc.get(sql, UserOnlineLog.class, parameter);
	}

	@Override
	public boolean updateLogoutTime(String userId, int logId, Date logoutTime) {

		String table = TableUtils.getUserOnlineLogTable(userId);

		String sql = "UPDATE " + table + " SET logout_time = ? WHERE user_id = ? AND log_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setObject(logoutTime);
		parameter.setString(userId);
		parameter.setInt(logId);

		return this.jdbc.update(sql, parameter) > 0;

	}

	@Override
	public long getUserOnline(String userId) {

		String table = TableUtils.getUserOnlineLogTable(userId);

		String sql = "SELECT SUM(unix_timestamp(logout_time) - unix_timestamp(login_time)) FROM " + table + " WHERE user_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);

		return this.jdbc.getInt(sql, parameter);
	}

	@Override
	public long getUserOnline(String userId, Date startTime) {

		String table = TableUtils.getUserOnlineLogTable(userId);

		String sql = "SELECT SUM(unix_timestamp(logout_time) - unix_timestamp(CASE WHEN login_time > ? THEN login_time ELSE ? END)) FROM " + table + " WHERE user_id = ? AND logout_time >= ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setObject(startTime);
		parameter.setObject(startTime);
		parameter.setString(userId);
		parameter.setObject(startTime);

		return this.jdbc.getInt(sql, parameter);
	}

	@Override
	public long getLastOnline(String userId) {

		String table = TableUtils.getUserOnlineLogTable(userId);

		String sql = "SELECT unix_timestamp(now()) - unix_timestamp(login_time) FROM " + table + " WHERE user_id = ? ORDER BY log_id DESC LIMIT 1";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);

		return this.jdbc.getInt(sql, parameter);
	}

	@Override
	public boolean isLogin(String userId, Date date) {

		String table = TableUtils.getUserOnlineLogTable(userId);
		String sql = "select * from " + table + " where user_id = ? and login_time >= ? and login_time <= ? LIMIT 1";
		SqlParameter params = new SqlParameter();
		params.setString(userId);
		params.setString(DateUtils.getDate(date) + " 00:00:00");
		params.setString(DateUtils.getDate(date) + " 23:59:59");

		UserOnlineLog log = this.jdbc.get(sql, UserOnlineLog.class, params);
		if (log != null) {
			return true;
		}

		return false;

	}
}
