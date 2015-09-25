package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.game.dao.BattleDao;
import com.lodogame.model.UserBattleReport;

public class BattleDaoMysqlImpl implements BattleDao {

	@Autowired
	private Jdbc jdbc;

	@Override
	public void addReport(UserBattleReport report) {
		jdbc.insert(report);
	}

}
