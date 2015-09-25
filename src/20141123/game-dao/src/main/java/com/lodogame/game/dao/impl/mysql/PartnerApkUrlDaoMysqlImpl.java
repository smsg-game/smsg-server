package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.PartnerApkUrlDao;
import com.lodogame.model.PartnerApkUrl;

public class PartnerApkUrlDaoMysqlImpl implements PartnerApkUrlDao {

	@Autowired
	private Jdbc jdbc;

	public final static String table = "partner_apk_url";

	public final static String columns = "*";

	@Override
	public PartnerApkUrl getByPartnerId(String partnerId) {

		String sql = "SELECT " + columns + " FROM " + table + " WHERE partner_id = ? ;";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(partnerId);

		return this.jdbc.get(sql, PartnerApkUrl.class, parameter);
	}

}
