package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.SystemOncePayRewardDao;
import com.lodogame.model.SystemOncePayReward;

public class SystemOncePayRewardDaoMysqlImpl implements SystemOncePayRewardDao{

	private final static String table = "system_once_pay_reward";
	
	@Autowired
	private Jdbc jdbc;
	
	@Override
	public List<SystemOncePayReward> getAll() {
		String sql = "SELECT * FROM " + table;
		return this.jdbc.getList(sql, SystemOncePayReward.class);
	}

	@Override
	public SystemOncePayReward getById(int id) {
		String sql = "SELECT * FROM " + table + " WHERE id=?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(id);
		return this.jdbc.get(sql, SystemOncePayReward.class, parameter);
	}

	@Override
	public SystemOncePayReward getNextById(int rid) {
		String sql = "SELECT * FROM " + table + " WHERE id=?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(rid + 1);
		
		return this.jdbc.get(sql, SystemOncePayReward.class, parameter);
	}


}
