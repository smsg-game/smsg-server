package com.lodogame.game.dao.impl.mysql;


import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.SystemPolishConsumeDao;
import com.lodogame.model.SystemPolishConsume;

public class SystemPolishConsumeDaoMysqlImpl implements SystemPolishConsumeDao {

	private static final String table = "system_polish_consume";
	
	@Autowired
	private Jdbc jdbc;

	@Override
	public SystemPolishConsume getSystemPolishConsume(int polishType) {
		String sql = "SELECT * FROM " + table +" WHERE polish_type = ? LIMIT 1";
		SqlParameter param = new SqlParameter();
		param.setInt(polishType);
		return this.jdbc.get(sql, SystemPolishConsume.class, param);
	}

}
