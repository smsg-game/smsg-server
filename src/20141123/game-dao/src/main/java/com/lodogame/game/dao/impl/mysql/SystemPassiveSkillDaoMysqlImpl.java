package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.SystemPassiveSkillDao;
import com.lodogame.model.SystemPassiveSkill;

public class SystemPassiveSkillDaoMysqlImpl implements SystemPassiveSkillDao {

	private String table = "system_passive_skill";

	@Autowired
	private Jdbc jdbc;

	@Override
	public SystemPassiveSkill get(int skillGroupId, int skillId) {

		String sql = "SELECT * FROM " + table + " WHERE skill_group_id = ? AND skill_id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(skillGroupId);
		parameter.setInt(skillId);

		return this.jdbc.get(sql, SystemPassiveSkill.class, parameter);
	}

	@Override
	public SystemPassiveSkill getFirst(int skillGroupId) {

		String sql = "SELECT * FROM " + table + " WHERE skill_group_id = ? ORDER BY passive_skill_id asc LIMIT 1";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(skillGroupId);

		return this.jdbc.get(sql, SystemPassiveSkill.class, parameter);
	}

	@Override
	public List<SystemPassiveSkill> getList(int skillGroupId) {

		String sql = "SELECT * FROM " + table + " WHERE skill_group_id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(skillGroupId);

		return this.jdbc.getList(sql, SystemPassiveSkill.class, parameter);
	}

}
