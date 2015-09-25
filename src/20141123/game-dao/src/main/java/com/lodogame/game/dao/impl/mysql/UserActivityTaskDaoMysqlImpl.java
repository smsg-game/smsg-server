package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserActivityTaskDao;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.model.UserActivityRewardLog;
import com.lodogame.model.UserActivityTask;

public class UserActivityTaskDaoMysqlImpl implements UserActivityTaskDao {

	@Autowired
	private Jdbc jdbc;

	@Override
	public List<UserActivityTask> getUserActivityTaskList(String userId) {

		String sql = "SELECT * FROM user_activity_task WHERE user_id = ? AND created_time >= ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setObject(DateUtils.getDate());

		return this.jdbc.getList(sql, UserActivityTask.class, parameter);
	}

	@Override
	public UserActivityTask getUserActivityTask(String userId, int activityTaskId) {

		String sql = "SELECT * FROM user_activity_task WHERE user_id = ? AND activity_task_id = ? AND created_time >= ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(activityTaskId);
		parameter.setObject(DateUtils.getDate());

		return this.jdbc.get(sql, UserActivityTask.class, parameter);
	}

	@Override
	public List<UserActivityRewardLog> getUserActivityRewardLogList(String userId) {

		String sql = "SELECT * FROM user_activity_reward_log WHERE user_id = ?  AND created_time >= ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setObject(DateUtils.getDate());

		return this.jdbc.getList(sql, UserActivityRewardLog.class, parameter);
	}

	@Override
	public boolean addUserActivityRewardLog(UserActivityRewardLog userActivityRewardLog) {
		return this.jdbc.insert(userActivityRewardLog) > 0;
	}

	@Override
	public boolean addUserActivityTask(List<UserActivityTask> userActivityTaskList) {
		this.jdbc.insert(userActivityTaskList);
		return true;
	}

	@Override
	public boolean updateUserActivityTask(String userId, int activityTaskId, int times, int status) {

		String sql = "UPDATE user_activity_task SET finish_times = ? , status = ? WHERE user_id = ? AND activity_task_id = ? AND date = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(times);
		parameter.setInt(status);
		parameter.setString(userId);
		parameter.setInt(activityTaskId);
		parameter.setObject(DateUtils.getDate());

		return this.jdbc.update(sql, parameter) > 0;

	}

}
