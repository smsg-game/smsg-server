package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.SystemTaskDao;
import com.lodogame.model.SystemTask;

public class SystemTaskDaoMysqlImpl implements SystemTaskDao {

	/**
	 * 表名
	 */
	public final static String table = "system_task";

	/**
	 * 字段列表
	 */
	public final static String columns = "*";

	@Autowired
	private Jdbc jdbc;

	public SystemTask get(int systemTaskId) {

		String sql = "SELECT " + columns + " FROM " + table + " WHERE system_task_id = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(systemTaskId);

		return this.jdbc.get(sql, SystemTask.class, parameter);
	}

	public List<SystemTask> getPosTaskList(int systemTaskId) {

		String sql = "SELECT " + columns + " FROM " + table + " WHERE pre_task_id = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(systemTaskId);

		return this.jdbc.getList(sql, SystemTask.class, parameter);
	}

	@Override
	public List<SystemTask> getByTaskTargetType(int targetType) {
		String sql = "SELECT " + columns + " FROM " + table + " WHERE task_target = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(targetType);
		return this.jdbc.getList(sql, SystemTask.class, parameter);
	}

}
