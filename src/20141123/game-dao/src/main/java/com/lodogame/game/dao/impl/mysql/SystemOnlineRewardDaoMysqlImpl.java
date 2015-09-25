package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.SystemOnlineRewardDao;
import com.lodogame.model.SystemOnlineReward;

public class SystemOnlineRewardDaoMysqlImpl implements SystemOnlineRewardDao {

	@Autowired
	private Jdbc jdbc;
	
	@Override
	public SystemOnlineReward get(int time) {
		String sql = "select * from system_online_reward where time = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(time);
		return jdbc.get(sql, SystemOnlineReward.class, parameter);
	}

}
