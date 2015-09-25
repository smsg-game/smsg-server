package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.game.dao.UnSynDao;

/**
 * 
 * @author foxwang
 * 
 */
public class UnSynDaoMysqlImpl implements UnSynDao {

	@Autowired
	private Jdbc jdbc;

	@Override
	public void executSql(String sql) {
		jdbc.update(sql, null);
	}

	@Override
	public void executeLogSql(String sql) {
		// jdbcLog.update(sql, null);
	}

}
