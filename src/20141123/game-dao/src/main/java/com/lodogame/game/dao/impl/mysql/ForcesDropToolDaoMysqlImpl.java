package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.ForcesDropToolDao;
import com.lodogame.model.ForcesDropTool;

public class ForcesDropToolDaoMysqlImpl implements ForcesDropToolDao {

	@Autowired
	private Jdbc jdbc;

	public final static String table = "forces_drop_tool";

	public final static String columns = "*";

	@Override
	public List<ForcesDropTool> getForcesDropToolList(int forcesId) {

		String sql = "SELECT " + columns + " FROM " + table + " WHERE forces_id = ? ;";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(forcesId);

		return this.jdbc.getList(sql, ForcesDropTool.class, parameter);
	}

}
