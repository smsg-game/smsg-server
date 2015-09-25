package com.lodogame.game.dao.impl.mysql;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserTavernDao;
import com.lodogame.model.UserTavern;

public class UserTavernDaoMysqlImpl implements UserTavernDao {

	public final static String table = "user_tavern";

	public final static String columns = "*";

	@Autowired
	private Jdbc jdbc;

	public boolean add(UserTavern userTavern) {
		return this.jdbc.insert(userTavern) > 0;
	}

	public List<UserTavern> getList(String userId) {

		String sql = "SELECT " + columns + " FROM " + table + " WHERE user_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);

		return this.jdbc.getList(sql, UserTavern.class, parameter);
	}

	public UserTavern get(String userId, int type) {

		String sql = "SELECT " + columns + " FROM " + table + " WHERE user_id = ? AND type = ?  ";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(type);

		return this.jdbc.get(sql, UserTavern.class, parameter);
	}

	@Override
	public boolean updateTavernInfo(String userId, int type, Date updatedTime, int totalTimes, int amendTimes, int hadUsedMoney) {

		String sql = "UPDATE " + table + " SET total_times = ? , amend_times = ?, had_Used_money = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(totalTimes);
		parameter.setInt(amendTimes);
		parameter.setInt(hadUsedMoney);

		if (updatedTime != null) {
			sql += " , updated_time = ? ";
			parameter.setObject(updatedTime);
		}

		sql += "WHERE user_id = ? AND type = ?";

		parameter.setString(userId);
		parameter.setInt(type);

		return this.jdbc.update(sql, parameter) > 0;
	}
}
