package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.SystemVIPDao;
import com.lodogame.model.SystemVIP;

public class SystemVIPDaoMysqlImpl implements SystemVIPDao {

	private String table = "system_vip";

	private String columns = "*";

	@Autowired
	private Jdbc jdbc;


	@Override
	public SystemVIP get(int VIPLevel) {
		String sql = "SELECT " + columns + " FROM " + table + " WHERE vip_level = ?;";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(VIPLevel);
		return this.jdbc.get(sql, SystemVIP.class, parameter);
	}
}
