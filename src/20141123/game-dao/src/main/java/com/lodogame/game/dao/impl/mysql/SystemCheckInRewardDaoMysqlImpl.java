package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.SystemCheckInRewardDao;
import com.lodogame.model.SystemCheckInReward;

public class SystemCheckInRewardDaoMysqlImpl implements SystemCheckInRewardDao {

	private String table = "system_checkin_reward";

	@Autowired
	private Jdbc jdbc;

	@Override
	public SystemCheckInReward getSystemCheckInReward(int groupId, int day) {

		String sql = "SELECT * FROM " + table + " WHERE group_id = ? AND day = ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(groupId);
		parameter.setInt(day);

		return this.jdbc.get(sql, SystemCheckInReward.class, parameter);
	}

	@Override
	public List<SystemCheckInReward> getSystemCheckInReward(int groupId) {

		String sql = "SELECT * FROM " + table + " where group_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(groupId);

		return this.jdbc.getList(sql, SystemCheckInReward.class, parameter);
	}

}
