package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.VersionServerDao;
import com.lodogame.model.VersionServer;

public class VersionServerDaoMysqlImpl implements VersionServerDao {

	private String table = "version_server";
	
	@Autowired
	private Jdbc jdbc;
	
	@Override
	public VersionServer getVersionServer(String version) {
		
		String sql = "SELECT * FROM "+table+" WHERE version = ?";
		
		SqlParameter parameter = new SqlParameter();
		parameter.setString(version);
		
		return jdbc.get(sql, VersionServer.class, parameter);
	}
	
}
