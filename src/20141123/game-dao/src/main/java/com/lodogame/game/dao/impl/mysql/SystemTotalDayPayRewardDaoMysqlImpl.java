package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.SystemTotalDayPayRewardDao;
import com.lodogame.model.SystemTotalDayPayReward;

public class SystemTotalDayPayRewardDaoMysqlImpl implements SystemTotalDayPayRewardDao {

	@Autowired
	private Jdbc jdbc;
	
	private String table = "system_total_day_pay_reward";

	@Override
	public List<SystemTotalDayPayReward> getAll() {
		String sql = "select * from "+table; 
		return this.jdbc.getList(sql, SystemTotalDayPayReward.class);
	}

	@Override
	public SystemTotalDayPayReward getPayRewardByAid(int aid) {
		
		String sql = "select * from "+table +" where id  = ? ";
		
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(aid);
		
		return this.jdbc.get(sql, SystemTotalDayPayReward.class, parameter);
		
	}


}
