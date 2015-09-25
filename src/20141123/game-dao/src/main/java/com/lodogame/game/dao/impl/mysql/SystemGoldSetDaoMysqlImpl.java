package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.SystemGoldSetDao;
import com.lodogame.model.SystemGoldSet;

public class SystemGoldSetDaoMysqlImpl implements SystemGoldSetDao {

	@Autowired
	private Jdbc jdbc;

	public final static String table = "system_gold_set";

	public final static String columns = "*";

	@Override
	public List<SystemGoldSet> getList(int type) {

		String sql = "SELECT " + columns + " FROM " + table + " WHERE type = ? ORDER BY gold DESC";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(type);

		return this.jdbc.getList(sql, SystemGoldSet.class, parameter);
	}

	@Override
	public SystemGoldSet getByPayAmount(int type, double amount) {

		String sql = "SELECT " + columns + " FROM " + table + " WHERE type = ? AND money <= ? ORDER BY gold DESC LIMIT 1";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(type);
		parameter.setDouble(amount);

		return this.jdbc.get(sql, SystemGoldSet.class, parameter);
	}

}
