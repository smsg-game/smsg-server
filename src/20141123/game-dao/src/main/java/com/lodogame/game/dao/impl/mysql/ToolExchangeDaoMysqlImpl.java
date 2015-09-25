package com.lodogame.game.dao.impl.mysql;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.ToolExchangeDao;
import com.lodogame.model.ToolExchange;
import com.lodogame.model.UserToolExchangeLog;

public class ToolExchangeDaoMysqlImpl implements ToolExchangeDao {

	@Autowired
	private Jdbc jdbc;

	@Override
	public ToolExchange getExchangeItems(int toolExchangeId) {
		String sql = "SELECT * FROM system_tool_exchange WHERE exchange_id = ?;";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(toolExchangeId);
		return this.jdbc.get(sql, ToolExchange.class, parameter);
	}

	@Override
	public int getUserTimes(String userId, int toolExchangeId, Date startTime, Date endTime) {
		String sql = "SELECT times FROM user_tool_exchange_log WHERE user_id = ? AND exchange_id = ? AND created_time BETWEEN ? AND ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(toolExchangeId);
		parameter.setString(startTime.toString());
		parameter.setString(endTime.toString());

		return this.jdbc.getInt(sql, parameter);
	}

	@Override
	public int getTimes(int toolExchangeId) {
		String sql = "SELECT times FROM system_tool_exchange WHERE exchange_id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(toolExchangeId);
		return this.jdbc.getInt(sql, parameter);

	}

	@Override
	/**
	 * 查询一共有多少种物品兑换方式
	 */
	public int getNum() {
		String sql = "SELECT count(*) FROM system_tool_exchange";
		return this.jdbc.getInt(sql, null);
	}

	@Override
	public boolean updateExchangeCount(String userId, int toolExchangeId, int times) {
		String sql = "UPDATE user_tool_exchange_log SET times = ? WHERE user_id = ? AND exchange_id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(times);
		parameter.setString(userId);
		parameter.setInt(toolExchangeId);
		return this.jdbc.update(sql, parameter) > 0;

	}

	@Override
	public boolean addExchangeCount(UserToolExchangeLog userToolExchangeLog) {
		return this.jdbc.insert(userToolExchangeLog) > 0;
	}

	@Override
	public List<ToolExchange> getAll() {

		String sql = "SELECT * FROM system_tool_exchange;";
		return this.jdbc.getList(sql, ToolExchange.class);
	}

}
