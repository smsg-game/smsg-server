package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.SystemLoginReward7Dao;
import com.lodogame.model.SystemLoginReward7;

public class SystemLoginReward7DaoMysqlImpl implements SystemLoginReward7Dao {

	private static final String table = "system_login_reward7";
	
	@Autowired
	private Jdbc jdbc;
	
	@Override
	public List<SystemLoginReward7> getAll() {
		String sql = "SELECT * FROM " + table;
		return this.jdbc.getList(sql, SystemLoginReward7.class);
	}

	@Override
	public SystemLoginReward7 getByDay(int day) {
		String sql = "SELECT * FROM " + table + " WHERE day = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(day);
		return this.jdbc.get(sql, SystemLoginReward7.class, parameter);
	}

}
