package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserOnlineRewardDao;
import com.lodogame.model.UserOnlineReward;

public class UserOnlineRewardDaoMysqlImpl implements UserOnlineRewardDao {

	@Autowired
	private Jdbc jdbc;

	@Override
	public UserOnlineReward get(String userId) {

		String sql = "SELECT * FROM user_online_reward WHERE user_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);

		return this.jdbc.get(sql, UserOnlineReward.class, parameter);

	}

	@Override
	public boolean add(UserOnlineReward userOnlineReward) {
		return this.jdbc.insert(userOnlineReward) > 0;
	}

	@Override
	public boolean update(String userId, int subType) {

		String sql = "UPDATE user_online_reward SET sub_type = ? , updated_time = now() WHERE user_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(subType);
		parameter.setString(userId);

		return this.jdbc.update(sql, parameter) > 0;
	}
}
