package com.lodogame.game.dao.impl.mysql;


import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.SystemPolishDao;
import com.lodogame.model.SystemPolish;

public class SystemPolishDaoMysqlImpl implements SystemPolishDao {

	private static final String table = "system_polish";
	
	@Autowired
	private Jdbc jdbc;


	@Override
	public SystemPolish getSystemPolish(int systemEuqipId) {
		String sql = "SELECT * FROM " + table +" WHERE system_equip_id = ? LIMIT 1";
		SqlParameter param = new SqlParameter();
		param.setInt(systemEuqipId);
		return this.jdbc.get(sql, SystemPolish.class, param);
	}

}
