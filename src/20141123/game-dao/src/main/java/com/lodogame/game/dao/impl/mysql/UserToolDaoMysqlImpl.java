package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserToolDao;
import com.lodogame.model.UserTool;

public class UserToolDaoMysqlImpl implements UserToolDao {

	public final static String table = "user_tool";

	public final static String columns = "tool_id, tool_num, tool_type";

	@Autowired
	private Jdbc jdbc;

	public int getUserToolNum(String userId, int toolId) {
		
		UserTool userTool = this.get(userId, toolId);
		if (userTool != null) {
			return userTool.getToolNum();
		}

		return 0;
	}

	public boolean reduceUserTool(String userId, int toolId, int num) {

		if (num < 0) {
			return false;
		}

		String sql = "UPDATE " + table + " SET tool_num = tool_num - ? WHERE user_id = ? AND tool_id = ? AND tool_num >= ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(num);
		parameter.setString(userId);
		parameter.setInt(toolId);
		parameter.setInt(num);

		return this.jdbc.update(sql, parameter) > 0;
	}

	public UserTool get(String userId, int toolId) {

		String sql = "SELECT " + columns + " FROM " + table + " WHERE user_id = ? AND tool_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(toolId);

		return this.jdbc.get(sql, UserTool.class, parameter);
	}

	public List<UserTool> getList(String userId) {

		String sql = "SELECT " + columns + " FROM " + table + " WHERE user_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);

		return this.jdbc.getList(sql, UserTool.class, parameter);
	}

	public boolean addUserTool(String userId, int toolId, int num) {

		String sql = "UPDATE " + table + " SET tool_num = tool_num + ? WHERE user_id = ? AND tool_id = ? LIMIT 1";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(num);
		parameter.setString(userId);
		parameter.setInt(toolId);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean deleteZeroNumTools(String userId) {

		String sql = "DELETE FROM " + table + " WHERE user_id = ? AND tool_num = 0";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);

		return this.jdbc.update(sql, parameter) > 0;
	}

	public boolean add(UserTool userTool) {
		return this.jdbc.insert(userTool) > 0;
	}

	@Override
	public List<UserTool> getUserToolListByToolType(String userId, int toolType) {
		String sql = "SELECT " + columns + " FROM " + table + " WHERE user_id = ? and tool_type = ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(toolType);

		return this.jdbc.getList(sql, UserTool.class, parameter);
	}

}
