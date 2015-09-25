package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserDeifyDao;
import com.lodogame.model.UserDeifyInfo;

public class UserDeifyDaoMysqlImpl implements UserDeifyDao{

	private static final String table = "user_deify_info";
	
	@Autowired
	private Jdbc jdbc;
	
	@Override
	public UserDeifyInfo getUserDeifyInfo(String uid) {
		String sql = "SELECT * FROM " + table + " WHERE user_id = ?";
		SqlParameter param = new SqlParameter();
		param.setString(uid);
		return this.jdbc.get(sql, UserDeifyInfo.class, param);
	}

	@Override
	public boolean add(UserDeifyInfo userDeifyInfo) {
		return this.jdbc.insert(userDeifyInfo) > 0;
	}

}
