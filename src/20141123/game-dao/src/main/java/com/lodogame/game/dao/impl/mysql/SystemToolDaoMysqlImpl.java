package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.ExportDataDao;
import com.lodogame.game.dao.SystemToolDao;
import com.lodogame.game.utils.json.Json;
import com.lodogame.model.SystemTool;

public class SystemToolDaoMysqlImpl implements SystemToolDao, ExportDataDao {

	public final static String table = "system_tool";

	public final static String columns = "*";

	@Autowired
	private Jdbc jdbc;

	public SystemTool get(int toolId) {

		String sql = "SELECT " + columns + " FROM " + table + " WHERE tool_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(toolId);

		return this.jdbc.get(sql, SystemTool.class, parameter);
	}

	public List<SystemTool> getSystemToolList() {

		String sql = "SELECT " + columns + " FROM " + table + "  ";

		return this.jdbc.getList(sql, SystemTool.class);
	}

	public String toJson() {
		List<SystemTool> list = getSystemToolList();
		return Json.toJson(list);
	}

	@Override
	public List<SystemTool> getSystemToolListByType(int type) {
		String sql = "SELECT " + columns + " FROM " + table + " where type = ? ";
		
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(type);
		
		return this.jdbc.getList(sql, SystemTool.class,parameter);
	}

}
