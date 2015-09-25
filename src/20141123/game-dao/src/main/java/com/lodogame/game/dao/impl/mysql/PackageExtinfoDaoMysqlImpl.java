package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.PackageExtinfoDao;
import com.lodogame.model.PackageExtinfo;

public class PackageExtinfoDaoMysqlImpl implements PackageExtinfoDao {

	@Autowired
	private Jdbc jdbc;

	public final static String table = "package_extinfo";
	
	@Override
	public PackageExtinfo getByVersion(String version, String partnerId) {
		String sql = "select * from " + table + " where version = ? and partner_id = ?";
		SqlParameter params = new SqlParameter();
		params.setString(version);
		params.setString(partnerId);
		return this.jdbc.get(sql, PackageExtinfo.class, params);
	}

}
