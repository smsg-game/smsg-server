package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.SystemSkillUpgradeDao;
import com.lodogame.model.SystemSkillUpgrade;

public class SystemSkillUpgradeDaoMysqlImpl implements SystemSkillUpgradeDao {

	private String table = "system_skill_upgrade";

	@Autowired
	private Jdbc jdbc;

	@Override
	public SystemSkillUpgrade get(int skillGroupId) {

		String sql = "SELECT * FROM " + table + " WHERE skill_group_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(skillGroupId);

		return this.jdbc.get(sql, SystemSkillUpgrade.class, parameter);

	}
}
