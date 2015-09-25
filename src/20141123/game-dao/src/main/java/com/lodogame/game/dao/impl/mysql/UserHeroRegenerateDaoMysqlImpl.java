package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserHeroRegenerateDao;

public class UserHeroRegenerateDaoMysqlImpl implements UserHeroRegenerateDao {

	@Autowired
	private Jdbc jdbc;
	private String table = "regenerate_filter";

	@Override
	public int getPostHeroId(int preHeroId) {
		String sql = "SELECT post_hero_id FROM " + table
				+ " WHERE pre_hero_id=?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(preHeroId);

		return this.jdbc.getInt(sql, parameter);
	}

	@Override
	public int getPillNum(int preHeroId) {
		String sql = "SELECT pill_num FROM " + table + " WHERE pre_hero_id=?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(preHeroId);

		return this.jdbc.getInt(sql, parameter);
	}

	@Override
	public int getContractNum(int preHeroId) {
		String sql = "SELECT contract_num FROM " + table + " WHERE pre_hero_id=?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(preHeroId);

		return this.jdbc.getInt(sql, parameter);
	}

	@Override
	public int getHeroNum(int preHeroId) {
		String sql = "SELECT hero_num FROM " + table + " WHERE pre_hero_id=?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(preHeroId);

		return this.jdbc.getInt(sql, parameter);
	}

}
