package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.SystemEquipUpgradeToolDao;
import com.lodogame.model.SystemEquipUpgradeTool;

public class SystemEquipUpgradeToolDaoMysqlImpl implements SystemEquipUpgradeToolDao {

	private String table = "system_equip_upgrade_tool";

	@Autowired
	private Jdbc jdbc;

	@Override
	public List<SystemEquipUpgradeTool> getList(int equipId) {

		String sql = "SELECT * FROM " + table + " WHERE equip_id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(equipId);

		return this.jdbc.getList(sql, SystemEquipUpgradeTool.class, parameter);
	}
}
