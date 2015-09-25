package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.game.dao.PackageSettingsDao;
import com.lodogame.model.PackageSettings;

public class PackageSettingsDaoMysqlImpl implements PackageSettingsDao {
	@Autowired
	private Jdbc jdbc;

	public final static String table = "package_settings";

	@Override
	public PackageSettings get() {
		String sql = "select * from " + table + " where id = 1";
		return this.jdbc.get(sql, PackageSettings.class, null);
	}

}
