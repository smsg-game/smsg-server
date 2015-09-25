package com.lodogame.game.dao.impl.mysql;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserReduceGoldRebateLogDao;

public class UserReduceGoldRebateLogDaoMysqlImpl implements UserReduceGoldRebateLogDao {

	@Autowired
	private Jdbc jdbc;
	
	@Override
	public int getTodayReduceGold(String userId, String now) {
		String sql = "select sum(amount) from user_gold_use_log where  DATE(created_time) = ? and user_id = ? and flag = -1";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(now);
		parameter.setString(userId);
		
		return jdbc.getInt(sql, parameter);
	}

	@Override
	public int getTotalReduceGold(String userId, Date startDate, Date endDate) {
		String sql = "select sum(amount) from user_gold_use_log where created_time >= ? and created_time <= ? and user_id = ? and flag = -1";
		SqlParameter parameter = new SqlParameter();
		parameter.setObject(startDate);
		parameter.setObject(endDate);
		parameter.setString(userId);
		
		return jdbc.getInt(sql, parameter);
	}

	@Override
	public boolean isAwardRecord(String userId, String date, int amount, int type) {
		String sql = "select 1 from user_gold_rebate_award_log where user_id = ? and date(created_time) = ? and amount = ? and type = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setString(date);
		parameter.setInt(amount);
		parameter.setInt(type);
		return jdbc.getInt(sql, parameter) > 0;
	}

	@Override
	public boolean isTotalAwardRecord(String userId, Date startTime, Date endTime, int amount, int type) {
		String sql = "select 1 from user_gold_rebate_award_log where user_id = ? and created_time >= ? and created_time <= ?  and amount = ? and type = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setObject(startTime);
		parameter.setObject(endTime);
		parameter.setInt(amount);
		parameter.setInt(type);
		return jdbc.getInt(sql, parameter) > 0;
	}

	@Override
	public boolean addAwardRecord(String userId, Date date, int type, int amount) {
		String sql = "insert into user_gold_rebate_award_log(user_id,created_time,amount,type) values(?,?,?,?)";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setObject(date);
		parameter.setInt(amount);
		parameter.setInt(type);
		return jdbc.update(sql, parameter) > 0;
	}

	

}
