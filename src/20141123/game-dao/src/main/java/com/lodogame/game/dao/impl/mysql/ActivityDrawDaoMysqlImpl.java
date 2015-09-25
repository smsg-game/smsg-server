package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.ActivityDrawDao;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.model.ActivityDrawTool;
import com.lodogame.model.SystemActivityDrawShow;
import com.lodogame.model.UserDrawTime;

public class ActivityDrawDaoMysqlImpl implements ActivityDrawDao {

	@Autowired
	private Jdbc jdbc;

	@Override
	public int getUserDrawScore(String userId) {
		String sql = "SELECT score FROM user_draw_time WHERE user_id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		return jdbc.getInt(sql, parameter);
	}

	@Override
	public void updateUserDrawTime(String userId, String username, int time, int score) {

		String sql = "INSERT INTO user_draw_time(user_id, username, times, score) VALUES(?, ?, ?, ?) ON DUPLICATE KEY UPDATE username=VALUES(username), times=times+VALUES(times), score=score+VALUES(score)";
		SqlParameter parameter = new SqlParameter();

		parameter.setString(userId);
		parameter.setString(username);
		parameter.setInt(time);
		parameter.setInt(score);

		jdbc.update(sql, parameter);
	}

	@Override
	public int getUserGainTotal(String userId, int outId) {

		String sql = "SELECT IFNULL(SUM(num), 0) FROM user_draw_total WHERE user_id = ? AND out_id = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(outId);

		return jdbc.getInt(sql, parameter);
	}

	@Override
	public int getAllUserGainTotal(int outId) {

		String sql = "SELECT IFNULL(SUM(num), 0) FROM user_draw_total WHERE out_id = ? ";
		SqlParameter parameter = new SqlParameter();

		parameter.setInt(outId);

		return jdbc.getInt(sql, parameter);
	}

	@Override
	public void updateUserGainTotal(String userId, int outId, int num) {

		String sql = "INSERT INTO user_draw_total(user_id, out_id, num, created_time) VALUES(?, ?, ?, now()) ON DUPLICATE KEY UPDATE num=num+VALUES(num)";
		SqlParameter parameter = new SqlParameter();

		parameter.setString(userId);
		parameter.setInt(outId);
		parameter.setInt(num);

		jdbc.update(sql, parameter);
	}

	@Override
	public UserDrawTime getActivityDrawUserData(String userId) {

		String sql = "SELECT * from user_draw_time WHERE user_id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		return jdbc.get(sql, UserDrawTime.class, parameter);

	}

	@Override
	public int getDrawToolUpper(int outID) {

		String sql = "SELECT max_num FROM system_activity_draw_max_num WHERE out_id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(outID);
		return jdbc.getInt(sql, parameter);

	}

	@Override
	public List<UserDrawTime> getActivityDrawScoreRank() {

		String sql = "SELECT username, user_id, score FROM user_draw_time ORDER BY score DESC LIMIT 20 ";
		return jdbc.getList(sql, UserDrawTime.class);

	}

	@Override
	public boolean addActivityDrowToolNumRecorde(int num, int outId) {

		String sql = "INSERT INTO activity_draw_tool_reduce(out_id, num, date) VALUES(?, ?, ?) ";
		sql += "ON DUPLICATE KEY UPDATE num = num + VALUES(num)";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(outId);
		parameter.setInt(num);
		parameter.setString(DateUtils.getDate());

		return jdbc.update(sql, parameter) > 0;
	}

	@Override
	public int getTodayDrowToolNum(int outId) {

		String sql = "SELECT num FROM activity_draw_tool_reduce WHERE out_id = ? AND date = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(outId);
		parameter.setString(DateUtils.getDate());

		return jdbc.getInt(sql, parameter);
	}

	@Override
	public List<ActivityDrawTool> getNormalDropList(int type) {

		String sql = "SELECT * FROM system_activity_draw_probability WHERE draw_type = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(type);

		return jdbc.getList(sql, ActivityDrawTool.class, parameter);
	}

	@Override
	public List<ActivityDrawTool> getLimitOverDropList() {

		String sql = "SELECT * FROM system_activity_draw_num_reach";

		return jdbc.getList(sql, ActivityDrawTool.class);
	}

	@Override
	public List<ActivityDrawTool> getThirtyDropList() {

		String sql = "SELECT * FROM system_activity_draw_thirty_probability";

		return jdbc.getList(sql, ActivityDrawTool.class);
	}

	@Override
	public int getOutId(int time) {

		String sql = "SELECT out_id FROM system_activity_draw_revise WHERE revise_num = ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(time);

		return jdbc.getInt(sql, parameter);
	}

	@Override
	public ActivityDrawTool getActivityDrawTool(int outId, int type) {
		String sql = "SELECT * FROM system_activity_draw_probability where out_id = ? and draw_type = ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(outId);
		parameter.setInt(type);

		return jdbc.get(sql, ActivityDrawTool.class, parameter);
	}

	@Override
	public List<SystemActivityDrawShow> getActivityDrawShowList() {

		String sql = "SELECT * FROM system_activity_draw_show ";

		return jdbc.getList(sql, SystemActivityDrawShow.class);
	}
}
