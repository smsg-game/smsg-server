package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserDailyGainLogDao;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.model.UserDailyGainLog;

public class UserDailyGainLogDaoMysqlImpl implements UserDailyGainLogDao {

	private String table = "user_daily_gain_log";

	@Autowired
	private Jdbc jdbc;

	@Override
	public int getUserDailyGain(String userId, int type) {

		String date = DateUtils.getDate();

		String sql = "SELECT amount FROM " + table + " WHERE user_id = ? AND date = ?  AND type = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setString(date);
		parameter.setInt(type);

		return this.jdbc.getInt(sql, parameter);
	}
    /**
     * 获取用户今日获得列表
     * @param userId
     * @return
     */
	public List<UserDailyGainLog> getUserTodayAllGainList(String userId){
		String date = DateUtils.getDate();
		String sql = "SELECT * FROM " + table + " WHERE user_id = ? AND date = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setString(date);
		return this.jdbc.getList(sql, UserDailyGainLog.class, parameter);
	}
	
	@Override
	public boolean addUserDailyGain(String userId, int type, int amount) {

		String date = DateUtils.getDate();

		String sql = "INSERT INTO " + table + "(user_id, type, date, amount, created_time, updated_time) VALUES(?, ?, ?, ?, now(), now()) ";
		sql += "ON DUPLICATE KEY update amount = amount + VALUES(amount), updated_time = now()";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(type);
		parameter.setString(date);
		parameter.setInt(amount);

		return this.jdbc.update(sql, parameter) > 0;
	}

}
