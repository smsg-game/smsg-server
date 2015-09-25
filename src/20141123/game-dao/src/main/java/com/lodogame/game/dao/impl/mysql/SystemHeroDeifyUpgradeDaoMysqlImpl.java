package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.SystemHeroDeifyUpgradeDao;
import com.lodogame.model.SystemHeroDeifyUpgrade;

public class SystemHeroDeifyUpgradeDaoMysqlImpl implements SystemHeroDeifyUpgradeDao {

	private String table = "system_hero_deify_upgrade";

	private String columns = "*";

	@Autowired
	private Jdbc jdbc;

	@Override
	public SystemHeroDeifyUpgrade getSystemHeroDeifyUpgradeById(int systemHeroId) {
		String sql = "SELECT " + columns + " FROM " + table + " WHERE before_deify_system_hero_id = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(systemHeroId);
		return this.jdbc.get(sql, SystemHeroDeifyUpgrade.class, parameter);
	}
}
