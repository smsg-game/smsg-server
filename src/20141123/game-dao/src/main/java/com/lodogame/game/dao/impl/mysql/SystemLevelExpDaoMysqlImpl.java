package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.ExportDataDao;
import com.lodogame.game.dao.SystemLevelExpDao;
import com.lodogame.game.utils.json.Json;
import com.lodogame.model.SystemLevelExp;

public class SystemLevelExpDaoMysqlImpl implements SystemLevelExpDao, ExportDataDao {

	/**
	 * 表名
	 */
	public final static String table = "system_level_exp";

	/**
	 * 字段列表
	 */
	public final static String columns = "level, exp";

	@Autowired
	private Jdbc jdbc;

	public SystemLevelExp getHeroLevel(int exp) {

		String sql = "SELECT " + columns + " FROM " + table + " WHERE exp <= ? ORDER BY level desc LIMIT 1";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(exp);

		return this.jdbc.get(sql, SystemLevelExp.class, parameter);
	}

	@Override
	public SystemLevelExp getHeroExp(int level) {

		String sql = "SELECT " + columns + " FROM " + table + " WHERE level = ? LIMIT 1";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(level);

		return this.jdbc.get(sql, SystemLevelExp.class, parameter);
	}

	public List<SystemLevelExp> getSystemLevelExpList() {

		String sql = "SELECT " + columns + " FROM " + table;

		return this.jdbc.getList(sql, SystemLevelExp.class);
	}

	public String toJson() {
		List<SystemLevelExp> list = getSystemLevelExpList();
		return Json.toJson(list);
	}
}
