package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.WarAwardDao;
import com.lodogame.model.WarAward;

public class WarAwardDaoMysqlImpl implements WarAwardDao {

	@Autowired
	private Jdbc jdbc;
	
	@Override
	public WarAward get(int awardId) {
		String sql = "select * from war_award where award_id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(awardId);
		return jdbc.get(sql, WarAward.class, parameter);
	}

	@Override
	public List<WarAward> getAll() {
		String sql = "select * from war_award ";
		return jdbc.getList(sql, WarAward.class);
	}

}
