package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserMonthlyCardTaskDao;
import com.lodogame.model.UserMonthlyCardTask;

public class UserMonthlyCardTaskDaoMysqlImpl implements UserMonthlyCardTaskDao{

	@Autowired
	private Jdbc jdbc;
	private static String table = "user_monthly_card_task";

	@Override
	public UserMonthlyCardTask getByUserId(String userId) {
		String sql = "SELECT * FROM " + table + " WHERE user_id=?";
		SqlParameter param = new SqlParameter();
		param.setString(userId);
		return this.jdbc.get(sql, UserMonthlyCardTask.class, param);
	}

	@Override
	public boolean add(UserMonthlyCardTask task) {
		return this.jdbc.insert(task) > 0;
	}

	@Override
	public void update(UserMonthlyCardTask task) {
		String sql = "UPDATE " + table + " SET start_date = ?, end_date = ? WHERE user_id=?";
		SqlParameter param = new SqlParameter();
		param.setString(task.getStartDate());
		param.setString(task.getEndDate());
		param.setString(task.getUserId());
		
		this.jdbc.update(sql, param);
	}

}
