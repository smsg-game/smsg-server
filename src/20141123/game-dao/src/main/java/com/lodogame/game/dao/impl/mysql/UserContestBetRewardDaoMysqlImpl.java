package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserContestBetRewardDao;
import com.lodogame.model.UserContestBetReward;

public class UserContestBetRewardDaoMysqlImpl implements UserContestBetRewardDao{

	String table = "user_contest_bet_reward";
	@Autowired
	private Jdbc jdbc;
	
	@Override
	public UserContestBetReward get(String userId, int session) {
		String sql = "SELECT * FROM " + table + " WHERE user_id = ? AND session = ?";
		SqlParameter param = new SqlParameter();
		param.setString(userId);
		param.setInt(session);
		return this.jdbc.get(sql, UserContestBetReward.class, param);
	}

}
