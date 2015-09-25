package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserLoginReward7InfoDao;
import com.lodogame.model.UserLoginReward7Info;

public class UserLoginReward7InfoDaoMysqlImpl implements UserLoginReward7InfoDao{

	@Autowired
	private Jdbc jdbc;
	
	private final static String table = "user_login_reward7_info";
	
	@Override
	public boolean addUserLoginReward7Info(UserLoginReward7Info loginInfo) {
		return this.jdbc.insert(loginInfo) > 0;
	}

	@Override
	public UserLoginReward7Info getLastLogin(String userId) {
		String sql = "SELECT * FROM " + table + " WHERE user_id = ? ORDER BY id DESC LIMIT 1";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		return this.jdbc.get(sql, UserLoginReward7Info.class, parameter);
	}

	@Override
	public List<UserLoginReward7Info> getLoginInfoList(String userId, int day) {
		String sql = "SELECT * FROM " + table + " WHERE user_id = ? ORDER BY id LIMIT ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(day);
		return this.jdbc.getList(sql, UserLoginReward7Info.class, parameter);
	}
}
