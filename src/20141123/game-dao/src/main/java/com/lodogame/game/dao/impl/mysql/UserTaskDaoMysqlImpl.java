package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserTaskDao;
import com.lodogame.model.UserTask;

public class UserTaskDaoMysqlImpl implements UserTaskDao {

	/**
	 * 表名
	 */
	public final static String table = "user_task";

	/**
	 * 字段列表
	 */
	public final static String columns = "*";
	@Autowired
	private Jdbc jdbc;

	public List<UserTask> getList(String userId, int status) {

		SqlParameter parameter = new SqlParameter();

		String sql = "SELECT " + columns + " FROM " + table + " WHERE user_id = ? AND status <> 4";
		parameter.setString(userId);
		if (status != 100) {
			sql += " AND status = ? ";
			parameter.setInt(status);
		}

		return this.jdbc.getList(sql, UserTask.class, parameter);
	}

	public boolean update(String userId, int systemTaskId, int finishTimes, int status) {

		String sql = "UPDATE " + table + " SET finish_times = ? , status = ?, updated_time = now()  WHERE user_id = ? AND system_task_id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(finishTimes);
		parameter.setInt(status);
		parameter.setString(userId);
		parameter.setInt(systemTaskId);

		return this.jdbc.update(sql, parameter) > 0;
	}

	public boolean update(String userId, int systemTaskId, int status) {

		String sql = "UPDATE " + table + " SET status = ?, updated_time = now() WHERE user_id = ? AND system_task_id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(status);
		parameter.setString(userId);
		parameter.setInt(systemTaskId);

		return this.jdbc.update(sql, parameter) > 0;

	}

	@Override
	public boolean delete(String userId, int systemTaskId) {

		/*
		 * String sql = "DELETE FROM " + table +
		 * " WHERE user_id = ? AND system_task_id = ?"; SqlParameter parameter =
		 * new SqlParameter(); parameter.setString(userId);
		 * parameter.setInt(systemTaskId);
		 * 
		 * return this.jdbc.update(sql, parameter) > 0;
		 */

		String sql = "UPDATE " + table + " SET status = ?, updated_time = now() WHERE user_id = ? AND system_task_id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(4);
		parameter.setString(userId);
		parameter.setInt(systemTaskId);

		return this.jdbc.update(sql, parameter) > 0;

	}

	public UserTask get(String userId, int systemTaskId) {

		SqlParameter parameter = new SqlParameter();

		String sql = "SELECT " + columns + " FROM " + table + " WHERE user_id = ? AND system_task_id = ? ";
		parameter.setString(userId);
		parameter.setInt(systemTaskId);

		return this.jdbc.get(sql, UserTask.class, parameter);
	}

	public void add(List<UserTask> userTaskList) {
		this.jdbc.insert(userTaskList);
	}
}
