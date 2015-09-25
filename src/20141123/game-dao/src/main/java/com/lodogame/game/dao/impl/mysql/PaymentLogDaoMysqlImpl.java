package com.lodogame.game.dao.impl.mysql;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.PaymentLogDao;
import com.lodogame.model.PaymentLog;

public class PaymentLogDaoMysqlImpl implements PaymentLogDao {

	public final static String table = "payment_log";

	@Autowired
	private Jdbc jdbc;

	@Override
	public void add(PaymentLog paymentLog) {
		this.jdbc.insert(paymentLog);
	}

	@Override
	public int getPaymentTotal(String userId) {

		String sql = "SELECT sum(amount) FROM " + table + " WHERE user_id = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);

		return this.jdbc.getInt(sql, parameter);
	}

	@Override
	public int getPaymentTotalGold(String userId) {

		String sql = "SELECT sum(gold) FROM " + table + " WHERE user_id = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);

		return this.jdbc.getInt(sql, parameter);
	}

	@Override
	public int getPaymentTotalByTime(String userId, Date startTime, Date endTime) {

		String sql = "SELECT sum(amount) FROM " + table + " WHERE user_id = ? AND created_time BETWEEN ? AND ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setObject(startTime);
		parameter.setObject(endTime);
		return this.jdbc.getInt(sql, parameter);
	}
	
	@Override
	public int getPaymentTotalGoldByTime(String userId, Date startTime, Date endTime) {
		String sql = "SELECT sum(gold) FROM " + table + " WHERE user_id = ? AND created_time BETWEEN ? AND ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setObject(startTime);
		parameter.setObject(endTime);
		return this.jdbc.getInt(sql, parameter);
	}

	@Override
	public List<PaymentLog> getPaymenList(String userId, Date startTime, Date endTime, int payMoney, int nextPayMoney) {
		String sql = "SELECT * FROM " + table + " WHERE user_id = ? and created_time >= ? and created_time < ? and amount >= ? and amount < ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setObject(startTime);
		parameter.setObject(endTime);
		parameter.setInt(payMoney);
		parameter.setInt(nextPayMoney);
		return this.jdbc.getList(sql, PaymentLog.class, parameter);
	}
	
	@Override
	public List<PaymentLog> getPaymenListByGold(String userId, Date startTime,Date endTime, int payGold, int nextPayGold) {
		String sql = "SELECT * FROM " + table + " WHERE user_id = ? and created_time >= ? and created_time < ? and gold >= ? and gold < ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setObject(startTime);
		parameter.setObject(endTime);
		parameter.setInt(payGold);
		parameter.setInt(nextPayGold);
		return this.jdbc.getList(sql, PaymentLog.class, parameter);
	}

	@Override
	public List<PaymentLog> getPaymenList(String userId, Date startTime, Date endTime, int payMoney) {
		String sql = "SELECT * FROM " + table + " WHERE user_id = ? and created_time >= ? and created_time < ? and amount >= ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setObject(startTime);
		parameter.setObject(endTime);
		parameter.setInt(payMoney);
		return this.jdbc.getList(sql, PaymentLog.class, parameter);
	}

	@Override
	public List<PaymentLog> getPaymenListByGold(String userId, Date startTime,Date endTime, int gold) {
		String sql = "SELECT * FROM " + table + " WHERE user_id = ? and created_time >= ? and created_time < ? and gold >= ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setObject(startTime);
		parameter.setObject(endTime);
		parameter.setInt(gold);
		return this.jdbc.getList(sql, PaymentLog.class, parameter);
	}
}
