package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.SystemHeroExchangeDao;
import com.lodogame.model.SystemHeroExchange;

public class SystemHeroExchangeDaoMysqlImpl implements SystemHeroExchangeDao {

	private String table = "system_hero_exchange";

	@Autowired
	private Jdbc jdbc;

	@Override
	public List<SystemHeroExchange> getList(int week) {

		String sql = "SELECT * FROM " + table + " WHERE week = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(week);

		return this.jdbc.getList(sql, SystemHeroExchange.class, parameter);
	}

	@Override
	public SystemHeroExchange get(int exchangeHeroId) {

		String sql = "SELECT * FROM " + table + " WHERE system_hero_exchange_id = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(exchangeHeroId);

		return this.jdbc.get(sql, SystemHeroExchange.class, parameter);
	}

}
