package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserHeroExchangeDao;
import com.lodogame.model.UserHeroExchange;

public class UserHeroExchangeDaoMysqlImpl implements UserHeroExchangeDao {

	@Autowired
	private Jdbc jdbc;

	@Override
	public UserHeroExchange get(String userId) {

		String sql = "SELECT * FROM user_hero_exchange WHERE user_id = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);

		return this.jdbc.get(sql, UserHeroExchange.class, parameter);

	}

	@Override
	public boolean add(UserHeroExchange userHeroExchange) {
		return this.jdbc.insert(userHeroExchange) > 0;
	}

	@Override
	public boolean updateUserHeroExchange(String userId, int userWeek, int systemWeek, int times) {

		String sql = "UPDATE user_hero_exchange SET user_week = ? , system_week = ?, times = ? , updated_time = now() WHERE user_id = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(userWeek);
		parameter.setInt(systemWeek);
		parameter.setInt(times);
		parameter.setString(userId);

		return this.jdbc.update(sql, parameter) > 0;
	}
}
