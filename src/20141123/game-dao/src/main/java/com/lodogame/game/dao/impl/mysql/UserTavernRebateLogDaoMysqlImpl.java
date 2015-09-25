package com.lodogame.game.dao.impl.mysql;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserTavernRebateLogDao;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.model.UserTavernRebateLog;

public class UserTavernRebateLogDaoMysqlImpl implements UserTavernRebateLogDao {

	@Autowired
	private Jdbc jdbc;

	@Override
	public int getTodayNum(String userId, int type, String now) {

		String sql = "select times from user_tavern_rebate_log where user_id = ? and type = ? and date = ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(type);
		parameter.setString(now);

		return jdbc.getInt(sql, parameter);
	}

	@Override
	public int getTotalNum(String userId, int type, Date startDate, Date endDate) {

		String sql = "select sum(times) from user_tavern_rebate_log where user_id = ? and type = ? and created_time >= ? and created_time <= ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(type);
		parameter.setObject(startDate);
		parameter.setObject(endDate);

		return jdbc.getInt(sql, parameter);
	}

	@Override
	public boolean setNum(String userId, String now, int times, int type) {

		String sql = "insert into user_tavern_rebate_log(user_id, times, type, date, created_time) values(?, ?, ?, ?, now()) on duplicate key update times = times + VALUES(times) ";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(times);
		parameter.setInt(type);
		parameter.setString(now);

		return jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean isAwardRecord(String userId, String date, int type, int times) {

		String sql = "select 1 from user_tavern_rebate_award_log where user_id = ? and date = ? and times = ? and type = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setString(date);
		parameter.setInt(times);
		parameter.setInt(type);
		return jdbc.getInt(sql, parameter) > 0;

	}

	@Override
	public boolean addAwardRecord(String userId, Date date, int type, int times) {

		String sql = "insert into user_tavern_rebate_award_log(user_id, date, receive_date,times,type) values(?,?,?,?,?)";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setString(DateUtils.getDate(date));
		parameter.setObject(date);
		parameter.setInt(times);
		parameter.setInt(type);
		return jdbc.update(sql, parameter) > 0;

	}

	@Override
	public boolean isTotalAwardRecord(String userId, Date startTime, Date endTime, int type, int times) {

		String sql = "select 1 from user_tavern_rebate_award_log where user_id = ? and receive_date >= ? and receive_date <= ?  and times = ? and type = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setObject(startTime);
		parameter.setObject(endTime);
		parameter.setInt(times);
		parameter.setInt(type);
		return jdbc.getInt(sql, parameter) > 0;

	}

}
