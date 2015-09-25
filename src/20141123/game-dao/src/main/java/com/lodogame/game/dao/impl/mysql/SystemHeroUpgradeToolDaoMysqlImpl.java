package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.ExportDataDao;
import com.lodogame.game.dao.SystemHeroUpgradeToolDao;
import com.lodogame.game.utils.json.Json;
import com.lodogame.model.SystemHeroUpgradeTool;

public class SystemHeroUpgradeToolDaoMysqlImpl implements SystemHeroUpgradeToolDao, ExportDataDao {

	public final static String table = "system_hero_upgrade_tool";

	public final static String columns = "tool_type_id, tool_id, tool_num";

	@Autowired
	private Jdbc jdbc;

	public List<SystemHeroUpgradeTool> getNeedToolList(int systemHeroId, int upgradeHeroId) {

		String sql = "SELECT " + columns + " FROM " + table + " WHERE system_hero_id = ? AND upgrade_hero_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(systemHeroId);
		parameter.setInt(upgradeHeroId);

		return this.jdbc.getList(sql, SystemHeroUpgradeTool.class, parameter);
	}

	public List<SystemHeroUpgradeTool> getSystemHeroUpgradeToolList() {

		String sql = "SELECT " + columns + " FROM " + table;

		return this.jdbc.getList(sql, SystemHeroUpgradeTool.class);

	}

	public String toJson() {
		List<SystemHeroUpgradeTool> list = getSystemHeroUpgradeToolList();
		return Json.toJson(list);
	}

}
