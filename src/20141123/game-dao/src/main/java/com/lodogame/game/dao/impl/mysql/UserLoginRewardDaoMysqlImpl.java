package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserLoginRewardInfoDao;
import com.lodogame.model.UserLoginRewardInfo;

public class UserLoginRewardDaoMysqlImpl implements UserLoginRewardInfoDao {

	private String table = "user_login_reward_info";

	@Autowired
	private Jdbc jdbc;

	@Override
	public boolean addUserLoginRewardInfo(UserLoginRewardInfo userLoginRewardInfo) {
		
		return this.jdbc.insert(userLoginRewardInfo) > 0;
		
	}

	@Override
	public UserLoginRewardInfo getUserLoginRewardInfoByDay(String userId, int day) {
		
		String sql = "SELECT * FROM " + table + " WHERE user_id = ? AND day = ? ORDER BY created_time DESC LIMIT 1";
		
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(day);
		
		return this.jdbc.get(sql, UserLoginRewardInfo.class, parameter);
	}

	@Override
	public int updateUserLoginRewardInfoByDay(String userId, int day, String date, int rewardStatus) {
		
		String sql = "UPDATE " + table + " SET reward_status = ?, date = ? WHERE user_id = ? AND day = ?";
		
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(rewardStatus);
		parameter.setString(date);
		parameter.setString(userId);
		parameter.setInt(day);
		
		return this.jdbc.update(sql, parameter);
	}

	@Override
	public UserLoginRewardInfo getUserLastLoginRewardInfo(String userId) {
		String sql = "SELECT * FROM " + table + " WHERE user_id = ? ORDER BY created_time DESC LIMIT 1 ";
		
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		
		return this.jdbc.get(sql, UserLoginRewardInfo.class, parameter);
	}

	@Override
	public List<UserLoginRewardInfo> getUserLoginRewardInfo(String userId) {
		String sql = "SELECT * FROM " + table + " WHERE user_id = ? ";
		
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		
		return this.jdbc.getList(sql, UserLoginRewardInfo.class, parameter);
	}
}
