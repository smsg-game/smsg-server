package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.CallbackRewardDao;
import com.lodogame.model.CallbackReward;

public class CallbackRewardDaoMysqlImpl implements CallbackRewardDao {

	@Autowired
	private Jdbc jdbc;

	@Override
	public CallbackReward getReward(int day, int level) {

		String sql = "SELECT * FROM callback_reward WHERE lower_day <= ? AND upper_day >= ? AND lower_level <= ? AND upper_level >= ? LIMIT 1";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(day);
		parameter.setInt(day);
		parameter.setInt(level);
		parameter.setInt(level);

		return this.jdbc.get(sql, CallbackReward.class, parameter);

	}

}
