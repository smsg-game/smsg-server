package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.ExportDataDao;
import com.lodogame.game.dao.SystemForcesMonsterDao;
import com.lodogame.game.utils.json.Json;
import com.lodogame.model.SystemForcesMonster;

public class SystemForcesMonsterDaoMysqlImpl implements SystemForcesMonsterDao, ExportDataDao {

	private final static String table = "system_forces_monster";

	private final static String columns = "forces_army_id, forces_id, monster_id, monster_id, monster_name, pos, forces_type";

	@Autowired
	private Jdbc jdbc;

	public List<SystemForcesMonster> getForcesMonsterList(int forcesId) {

		String sql = "SELECT " + columns + " FROM " + table + " WHERE forces_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(forcesId);

		return this.jdbc.getList(sql, SystemForcesMonster.class, parameter);
	}

	public List<SystemForcesMonster> getSysForcesMonsterList() {

		String sql = "SELECT " + columns + " FROM " + table;

		return this.jdbc.getList(sql, SystemForcesMonster.class);
	}

	public String toJson() {
		List<SystemForcesMonster> list = getSysForcesMonsterList();
		return Json.toJson(list);
	}
}
