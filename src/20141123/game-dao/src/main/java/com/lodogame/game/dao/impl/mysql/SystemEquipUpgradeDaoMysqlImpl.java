package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.SystemEquipUpgradeDao;
import com.lodogame.model.SystemEquipUpgrade;

public class SystemEquipUpgradeDaoMysqlImpl implements SystemEquipUpgradeDao {

	private String table = "system_equip_upgrade";

	@Autowired
	private Jdbc jdbc;

	@Override
	public SystemEquipUpgrade get(int equipId) {

		String sql = "SELECT * FROM " + table + " WHERE equip_id = ? LIMIT 1";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(equipId);

		return this.jdbc.get(sql, SystemEquipUpgrade.class, parameter);

	}

}
