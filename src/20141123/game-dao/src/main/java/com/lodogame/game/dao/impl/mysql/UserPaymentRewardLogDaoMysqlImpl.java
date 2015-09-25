package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserPaymentRewardLogDao;
import com.lodogame.model.UserPaymentRewardLog;

public class UserPaymentRewardLogDaoMysqlImpl implements UserPaymentRewardLogDao {

	private String table = "user_payment_reward_log";

	@Autowired
	private Jdbc jdbc;

	@Override
	public void add(UserPaymentRewardLog userPaymentRewardLog) {
		this.jdbc.insert(userPaymentRewardLog);
	}

	@Override
	public int getUserPaymentRewardLogCount(String userId, int amount) {

		String sql = "SELECT count(1) as total FROM " + table + " WHERE user_id = ? AND amount = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(amount);

		return this.jdbc.getInt(sql, parameter);
	}

}
