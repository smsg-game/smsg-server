package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.ExportDataDao;
import com.lodogame.game.dao.SystemHeroUpgradeDao;
import com.lodogame.game.utils.json.Json;
import com.lodogame.model.SystemHeroUpgrade;

public class SystemHeroUpgradeDaoMysqlImpl implements SystemHeroUpgradeDao, ExportDataDao {

	@Autowired
	private Jdbc jdbc;

	public final static String table = "system_hero_upgrade";

	public final static String columns = "*";

	public SystemHeroUpgrade get(int systemHeroId) {

		String sql = "SELECT " + columns + " FROM " + table + " WHERE system_hero_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(systemHeroId);

		return this.jdbc.get(sql, SystemHeroUpgrade.class, parameter);

	}

	public List<SystemHeroUpgrade> getSystemHeroUpgradeList() {

		String sql = "SELECT " + columns + " FROM " + table;

		return this.jdbc.getList(sql, SystemHeroUpgrade.class);

	}
	public String toJson() {
		List<SystemHeroUpgrade> list = getSystemHeroUpgradeList();
		return Json.toJson(list);
	}

}
