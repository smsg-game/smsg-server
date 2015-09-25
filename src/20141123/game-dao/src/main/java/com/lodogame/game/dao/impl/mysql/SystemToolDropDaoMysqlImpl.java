package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.SystemToolDropDao;
import com.lodogame.model.SystemToolDrop;

public class SystemToolDropDaoMysqlImpl implements SystemToolDropDao {

	private final static String table = "system_tool_drop";

	@Autowired
	private Jdbc jdbc;

	@Override
	public List<SystemToolDrop> getSystemToolDropList(int toolId) {

		String sql = "SELECT * FROM " + table + " WHERE tool_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(toolId);

		return this.jdbc.getList(sql, SystemToolDrop.class, parameter);

	}

}
