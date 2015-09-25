package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.SystemRecivePowerDao;
import com.lodogame.model.SystemRecivePower;

public class SystemRecivePowerDaoMysqlImpl implements SystemRecivePowerDao {

	private String table = "system_recive_power";

	@Autowired
	private Jdbc jdbc;

	@Override
	public List<SystemRecivePower> getList() {

		String sql = "SELECT * FROM " + table;
		return this.jdbc.getList(sql, SystemRecivePower.class);
	}

	@Override
	public SystemRecivePower get(int type) {
		String sql = "SELECT * FROM " + table + " WHERE type = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(type);

		return this.jdbc.get(sql, SystemRecivePower.class, parameter);
	}
}
