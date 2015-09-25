package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.SystemActivityTaskDao;
import com.lodogame.model.ActivityTask;
import com.lodogame.model.ActivityTaskReward;

public class SystemActivityTaskDaoMysqlImpl implements SystemActivityTaskDao {

	@Autowired
	private Jdbc jdbc;

	@Override
	public List<ActivityTaskReward> getActivityRewardList() {

		String sql = "SELECT * FROM activity_task_reward";

		return this.jdbc.getList(sql, ActivityTaskReward.class);
	}

	@Override
	public ActivityTaskReward getActivityReward(int activityTaskRewardId) {

		String sql = "SELECT * FROM activity_task_reward WHERE activity_task_reward_id = ? ";

		SqlParameter sqlParameter = new SqlParameter();
		sqlParameter.setInt(activityTaskRewardId);

		return this.jdbc.get(sql, ActivityTaskReward.class, sqlParameter);
	}

	@Override
	public List<ActivityTask> getActivityTaskList() {

		String sql = "SELECT * FROM activity_task";

		return this.jdbc.getList(sql, ActivityTask.class);
	}

	@Override
	public ActivityTask getActivityTask(int activityTaskId) {

		String sql = "SELECT * FROM activity_task WHERE activity_task_id = ? ";

		SqlParameter sqlParameter = new SqlParameter();
		sqlParameter.setInt(activityTaskId);

		return this.jdbc.get(sql, ActivityTask.class, sqlParameter);
	}

	@Override
	public ActivityTask getActivityTaskByTarget(int targetType) {

		String sql = "SELECT * FROM activity_task WHERE target_type = ? ";

		SqlParameter sqlParameter = new SqlParameter();
		sqlParameter.setInt(targetType);

		return this.jdbc.get(sql, ActivityTask.class, sqlParameter);
	}

}
