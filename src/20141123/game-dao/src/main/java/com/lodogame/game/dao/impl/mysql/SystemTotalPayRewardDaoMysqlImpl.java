package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.SystemTotalPayRewardDao;
import com.lodogame.model.SystemTotalPayReward;

public class SystemTotalPayRewardDaoMysqlImpl implements SystemTotalPayRewardDao {

	@Autowired
	private Jdbc jdbc;

	@Override
	public SystemTotalPayReward getById(int rid) {
		String sql = "SELECT * FROM system_total_pay_reward WHERE id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(rid);

		return this.jdbc.get(sql, SystemTotalPayReward.class, parameter);
	}

	@Override
	public List<SystemTotalPayReward> getByPayment(int userTotalPay) {
		String sql = "SELECT * FROM system_total_pay_reward WHERE pay_money < ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(userTotalPay);
		return this.jdbc.getList(sql, SystemTotalPayReward.class, parameter);
	}

	@Override
	public List<SystemTotalPayReward> getAll() {
		String sql = "SELECT * FROM system_total_pay_reward";
		return this.jdbc.getList(sql, SystemTotalPayReward.class);
	}

}
