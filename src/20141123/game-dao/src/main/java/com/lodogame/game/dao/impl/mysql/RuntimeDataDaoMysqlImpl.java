package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.RuntimeDataDao;
import com.lodogame.model.RuntimeData;

public class RuntimeDataDaoMysqlImpl implements RuntimeDataDao {

	@Autowired
	private Jdbc jdbc;

	public final static String table = "runtime_data";

	public final static String columns = "*";

	@Override
	public boolean add(RuntimeData runtimeData) {
		return this.jdbc.insert(runtimeData) > 0;
	}

	@Override
	public RuntimeData get(String key) {

		String sql = "SELECT " + columns + " FROM " + table + " WHERE value_key = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(key);

		return this.jdbc.get(sql, RuntimeData.class, parameter);
	}

	@Override
	public boolean inc(String key) {

		String sql = "UPDATE " + table + " SET value = value + 1 WHERE value_key = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(key);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean set(String key, long value) {

		String sql = "UPDATE " + table + " SET value = ? WHERE value_key = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setLong(value);
		parameter.setString(key);

		return this.jdbc.update(sql, parameter) > 0;
	}

}
