package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.ExportDataDao;
import com.lodogame.game.dao.SystemMonsterDao;
import com.lodogame.game.utils.json.Json;
import com.lodogame.model.SystemMonster;

public class SystemMonsterDaoMysqlImpl implements SystemMonsterDao, ExportDataDao {

	private final static String table = "system_monster";

	private final static String columns = "*";

	@Autowired
	private Jdbc jdbc;

	public SystemMonster get(int monsterId) {

		String sql = "SELECT " + columns + " FROM " + table + " WHERE monster_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(monsterId);

		return this.jdbc.get(sql, SystemMonster.class, parameter);
	}

	public List<SystemMonster> getSystemMonsterList() {

		String sql = "SELECT " + columns + " FROM " + table;

		return this.jdbc.getList(sql, SystemMonster.class);
	}

	public String toJson() {
		List<SystemMonster> list = getSystemMonsterList();
		return Json.toJson(list);
	}

	@Override
	public void add(SystemMonster systemMonster) {
		throw new NotImplementedException();
	}

}
