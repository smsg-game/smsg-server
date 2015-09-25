package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;

public class ActiveCodeDaoMysqlImpl implements com.lodogame.game.dao.ActiveCodeDao {

	@Autowired
	private Jdbc jdbc;

	public final static String table = "active_code";

	public final static String columns = "*";

	@Override
	public boolean isActive(String uuid, String partnerId) {

		String sql = "SELECT count(1) FROM " + table + " WHERE uuid = ? and partner_id = ? LIMIT 1";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(uuid);
		parameter.setString(partnerId);
		return jdbc.getInt(sql, parameter) > 0;
	}

	@Override
	public boolean isBlackImei(String imei, String partnerId) {

		String sql = "SELECT count(1) FROM black_imei WHERE imei = ? and partner_id = ? LIMIT 1";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(imei);
		parameter.setString(partnerId);
		return jdbc.getInt(sql, parameter) > 0;
	}

	@Override
	public boolean active(String uuid, String code, String partnerId) {

		String sql = "UPDATE " + table + " SET uuid = ?, updated_time = now() WHERE code = ? AND partner_id = ? AND uuid IS NULL  LIMIT 1";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(uuid);
		parameter.setString(code);
		parameter.setString(partnerId);
		return jdbc.update(sql, parameter) > 0;
	}

}
