package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.SystemLoginRewardDao;
import com.lodogame.model.SystemLoginReward;

public class SystemLoginRewardDaoMysqlImpl implements SystemLoginRewardDao {

	private String table = "system_30_login_reward";

	@Autowired
	private Jdbc jdbc;

	@Override
	public SystemLoginReward getSystemLoginRewardByDay(int day) {
		String sql =  "SELECT * FROM " + table + " WHERE day = ? ORDER BY day DESC LIMIT 1";
		
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(day);
		
		return this.jdbc.get(sql, SystemLoginReward.class, parameter);
	}

	@Override
	public List<SystemLoginReward> getSystemLoginReward() {
		String sql =  "SELECT * FROM " + table ;
		return this.jdbc.getList(sql, SystemLoginReward.class);
	}

}
