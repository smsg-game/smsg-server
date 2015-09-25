package com.lodogame.game.dao.impl.mysql;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.ConfigDataDao;
import com.lodogame.model.ConfigData;

public class ConfigDataDaoMysqlImpl implements ConfigDataDao {

	@Autowired
	private Jdbc jdbc;

	public final static String table = "config_data";

	public final static String columns = "*";

	@Override
	public ConfigData get(String configKey) {

		String sql = "SELECT " + columns + " FROM " + table + " WHERE config_key = ? ";
		SqlParameter paramter = new SqlParameter();
		paramter.setString(configKey);
		return this.jdbc.get(sql, ConfigData.class, paramter);
	}

	@Override
	public int getInt(String configKey, int defaultValue) {
		throw new NotImplementedException();
	}

	@Override
	public String getString(String configKey, String defaultValue) {
		throw new NotImplementedException();
	}

}
