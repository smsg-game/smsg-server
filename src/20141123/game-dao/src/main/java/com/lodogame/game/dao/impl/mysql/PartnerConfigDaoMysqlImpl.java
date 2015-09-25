package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.PartnerConfigDao;
import com.lodogame.model.PartnerConfig;

public class PartnerConfigDaoMysqlImpl implements PartnerConfigDao {
	@Autowired
	private Jdbc jdbc;

	public final static String table = "partner_config";

	public final static String columns = "*";

	@Override
	public PartnerConfig getById(String partnerId) {
		String sql = "SELECT " + columns + " FROM " + table + " WHERE partner_id = ? ;";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(partnerId);

		return this.jdbc.get(sql, PartnerConfig.class, parameter);
	}

	@Override
	public boolean save(PartnerConfig partnerConfig) {
		jdbc.insert(partnerConfig);
		return true;
	}

	public Jdbc getJdbc() {
		return jdbc;
	}

	public void setJdbc(Jdbc jdbc) {
		this.jdbc = jdbc;
	}
}
