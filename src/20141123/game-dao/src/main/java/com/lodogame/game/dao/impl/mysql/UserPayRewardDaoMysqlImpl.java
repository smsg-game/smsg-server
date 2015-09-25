package com.lodogame.game.dao.impl.mysql;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserPayRewardDao;
import com.lodogame.model.UserPayReward;

public class UserPayRewardDaoMysqlImpl implements UserPayRewardDao {

	@Autowired
	private Jdbc jdbc;
	
	@Override
	public boolean add(UserPayReward userPayReward) {
		return jdbc.insert(userPayReward) > 0;
	}

	@Override
	public List<UserPayReward> getReceivedOrderIdList(String userId, int aid, int rid, Date startTime, Date endTime) {
		String sql = "select order_id from user_pay_reward where user_id = ? and activity_id = ? and reward_id = ? and created_time between ? and ?;";
		SqlParameter params = new SqlParameter();
		params.setString(userId);
		params.setInt(aid);
		params.setInt(rid);
		params.setObject(startTime);
		params.setObject(endTime);
		return jdbc.getList(sql, UserPayReward.class, params);
	}

	@Override
	public List<UserPayReward> getList(String userId) {
		String sql = "select * from user_pay_reward where user_id = ?";
		SqlParameter params = new SqlParameter();
		params.setString(userId);
		return jdbc.getList(sql, UserPayReward.class, params);
	}

	@Override
	public UserPayReward getUserPayReward(String userId, int aid, int rid, Date startTime, Date endTime) {
		String sql = "SELECT * FROM user_pay_reward WHERE user_id = ? AND activity_id = ? AND reward_id = ? and created_time between ? and ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(aid);
		parameter.setInt(rid);
		parameter.setObject(startTime);
		parameter.setObject(endTime);
		return this.jdbc.get(sql, UserPayReward.class, parameter);
	}
}

