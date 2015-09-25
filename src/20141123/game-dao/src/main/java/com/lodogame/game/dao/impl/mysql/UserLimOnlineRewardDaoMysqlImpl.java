package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserLimOnlineRewardDao;
import com.lodogame.model.UserLimOnlineReward;

public class UserLimOnlineRewardDaoMysqlImpl implements UserLimOnlineRewardDao{

	private static final String table = "user_lim_online_reward";
	
	@Autowired
	private Jdbc jdbc;
	
	@Override
	public UserLimOnlineReward getLatestLog(String uid) {
		String sql = "SELECT * FROM " + table + " WHERE user_id = ? ORDER BY created_time DESC LIMIT 1";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(uid);
		return this.jdbc.get(sql, UserLimOnlineReward.class, parameter);
	}

	@Override
	public boolean add(UserLimOnlineReward reward) {
		return this.jdbc.insert(reward) > 0;
	}

}
