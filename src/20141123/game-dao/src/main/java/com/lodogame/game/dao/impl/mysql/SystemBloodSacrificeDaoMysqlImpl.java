package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.SystemBloodSacrificeDao;
import com.lodogame.model.SystemBloodSacrifice;

public class SystemBloodSacrificeDaoMysqlImpl implements  SystemBloodSacrificeDao {

	private String table = "system_blood_sacrifice";
	
	@Autowired
	private Jdbc jdbc;

	@Override
	public SystemBloodSacrifice get(int heroId, int stage) {
		
		String sql = "SELECT * FROM " + table + " WHERE hero_id = ? AND stage = ? LIMIT 1";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(heroId);
		parameter.setInt(stage);
		
		return this.jdbc.get(sql, SystemBloodSacrifice.class, parameter);
	}
}
