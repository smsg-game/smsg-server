package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.SystemUserLevelDao;
import com.lodogame.model.SystemUserLevel;

public class SystemUserLevelDaoMysqlImpl implements SystemUserLevelDao {

	/**
	 * 表名
	 */
	public final static String table = "system_user_level";

	/**
	 * 字段列表
	 */
	public final static String columns = "user_level, exp, attack, defense, battle_num";

	@Autowired
	private Jdbc jdbc;

	public SystemUserLevel getUserLevel(long exp) {

		String sql = "SELECT " + columns + " FROM " + table + " WHERE exp <= ? ORDER BY user_level DESC LIMIT 1 ";

		SqlParameter parameter = new SqlParameter();
		parameter.setLong(exp);

		return this.jdbc.get(sql, SystemUserLevel.class, parameter);
	}

	public SystemUserLevel get(int level) {

		String sql = "SELECT " + columns + " FROM " + table + " WHERE user_level = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(level);

		return this.jdbc.get(sql, SystemUserLevel.class, parameter);
	}
    
	public List<SystemUserLevel> getAll(){
		String sql = "SELECT " + columns + " FROM " + table;
		return this.jdbc.getList(sql, SystemUserLevel.class);
	}
	public void add(SystemUserLevel systemUserLevel) {
		throw new NotImplementedException();
	}

}
