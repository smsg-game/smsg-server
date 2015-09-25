package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserTotalDayPayRewardDao;
import com.lodogame.model.UserTotalDayPayRewardLog;

public class UserTotalDayPayRewardDaoMysqlImpl implements UserTotalDayPayRewardDao {

	private String table = "user_total_day_pay_reward_log";

	@Autowired
	private Jdbc jdbc;

	@Override
	public boolean add(UserTotalDayPayRewardLog userTotalPayReward) {
		return this.jdbc.insert(userTotalPayReward) > 0;
	}

	@Override
	public UserTotalDayPayRewardLog getUserTotalDayPayReward(String userId, int day) {
		String sql = "select * from " + table + " where day = ? and user_id = ? limit 1 ";
		SqlParameter parameter =  new SqlParameter();
		parameter.setInt(day);
		parameter.setString(userId);
		return this.jdbc.get(sql, UserTotalDayPayRewardLog.class, parameter);
	}

	@Override
	public UserTotalDayPayRewardLog getLastUserTotalDayPayRewardLog(String userId) {
		String sql = "select * from "+ table + " where user_id = ? order by day desc limit 1 ";
		SqlParameter parameter =  new SqlParameter();
		parameter.setString(userId);
		return this.jdbc.get(sql, UserTotalDayPayRewardLog.class, parameter);
	}

	@Override
	public boolean addReceiveTotalDayPayRewardLog(String userId, int day) {
		String sql  = "update " + table + " set reward_status = 2 , date = now() where day = ? and user_id = ? ";
		SqlParameter parameter =  new SqlParameter();
		parameter.setInt(day);
		parameter.setString(userId);
		return this.jdbc.update(sql, parameter) > 0 ;
	}

	@Override
	public List<UserTotalDayPayRewardLog> getUserAllTotalDayPayRewardLog(String userId) {
		
		String sql = "select * from " + table + " where user_id = ? ";
		SqlParameter parameter =  new SqlParameter();
		parameter.setString(userId);
		return this.jdbc.getList(sql, UserTotalDayPayRewardLog.class, parameter);
	}


}
