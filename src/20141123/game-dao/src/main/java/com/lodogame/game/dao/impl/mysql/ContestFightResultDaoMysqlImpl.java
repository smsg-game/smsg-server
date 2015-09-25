package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.ContestFightResultDao;
import com.lodogame.model.ContestFightResult;

public class ContestFightResultDaoMysqlImpl implements ContestFightResultDao{

	private static final String table = "contest_fight_result";
	
	@Autowired
	private Jdbc jdbc;
	
	@Override
	public boolean save(ContestFightResult fightResult) {
		return this.jdbc.insert(fightResult) > 0;
	}

	@Override
	public ContestFightResult get(String contestId) {
		String sql = "SELECT * FROM " + table + " WHERE player_pair_id = ?";
		SqlParameter param = new SqlParameter();
		param.setString(contestId);
		return this.jdbc.get(sql, ContestFightResult.class, param);
	}

}
