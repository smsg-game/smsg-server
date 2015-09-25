package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.SystemActivityDao;
import com.lodogame.model.SystemActivity;

public class SystemActivityDaoMysqlImpl implements SystemActivityDao {

	@Autowired
	private Jdbc jdbc;

	public final static String table = "system_activity";

	public final static String columns = "*";

	@Override
	public List<SystemActivity> getList(int activityType) {

		String sql = "SELECT " + columns + " FROM " + table + " WHERE activity_type = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(activityType);

		return this.jdbc.getList(sql, SystemActivity.class, parameter);
	}

	@Override
	public SystemActivity get(int activityId) {
		String sql = "SELECT " + columns + " FROM " + table + " WHERE activity_id = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(activityId);

		return this.jdbc.get(sql, SystemActivity.class, parameter);
	}

	@Override
	public List<SystemActivity> getDisplayActivityLiset() {

		String sql = "SELECT " + columns + " FROM " + table + " WHERE display = 1 ORDER BY sort ASC  ";
		SqlParameter parameter = new SqlParameter();

		return this.jdbc.getList(sql, SystemActivity.class, parameter);
	}

	@Override
	public boolean addActivity(SystemActivity systemActivity) {
		return this.jdbc.insert(systemActivity) > 0;

	}

	@Override
	public boolean modifyActivity(SystemActivity systemActivity) {
		String sql = "UPDATE system_activity SET activity_type = ?,activity_name = ?,activity_desc = ?,start_time = ?,end_time = ?,open_weeks = ?,param = ?,display = ?,sort = ? WHERE activity_id=?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(systemActivity.getActivityType());
		parameter.setString(systemActivity.getActivityName());
		parameter.setString(systemActivity.getActivityDesc());
		parameter.setObject(systemActivity.getStartTime());
		parameter.setObject(systemActivity.getEndTime());
		parameter.setString(systemActivity.getOpenWeeks());
		parameter.setString(systemActivity.getParam());
		parameter.setInt(systemActivity.getDisplay());
		parameter.setInt(systemActivity.getSort());
		parameter.setInt(systemActivity.getActivityId());

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean execute(String sql) {
		SqlParameter parameter = new SqlParameter();
		return this.jdbc.update(sql, parameter) > 0;
	}

}
