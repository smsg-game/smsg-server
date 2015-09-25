package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.SystemEquipSuitDao;
import com.lodogame.model.SystemEquipSuit;
import com.lodogame.model.SystemEquipUpgrade;
import com.lodogame.model.SystemSuitDetail;

public class SystemEquipSuitDaoMysqlImpl implements SystemEquipSuitDao {

	private String table = "system_equip_suit";

	@Autowired
	private Jdbc jdbc;

	@Override
	public List<SystemEquipSuit> getHeroEquipSuitList() {

		String sql = "SELECT * FROM " + table;

		return this.jdbc.getList(sql, SystemEquipSuit.class);
	}

	@Override
	public SystemEquipSuit getHeroEquipSuit(int heroId) {

		String sql = "SELECT * FROM " + table + " WHERE hero_id = ? LIMIT 1";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(heroId);

		return this.jdbc.get(sql, SystemEquipSuit.class, parameter);
	}

	@Override
	public List<SystemSuitDetail> getSuitDetailList(int suitId) {

		String sql = "SELECT * FROM system_suit_detail WHERE suit_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(suitId);

		return this.jdbc.getList(sql, SystemSuitDetail.class, parameter);
	}

}
