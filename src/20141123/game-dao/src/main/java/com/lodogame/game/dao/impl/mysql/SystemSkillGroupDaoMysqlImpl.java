package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.SystemSkillGroupDao;
import com.lodogame.model.SystemSkillGroup;

public class SystemSkillGroupDaoMysqlImpl implements SystemSkillGroupDao {

	private String table = "system_skill_group";

	@Autowired
	private Jdbc jdbc;

	@Override
	public SystemSkillGroup getByToolId(int toolId) {

		String sql = "SELECT * FROM " + table + " WHERE tool_id =  ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(toolId);

		return this.jdbc.get(sql, SystemSkillGroup.class, parameter);
	}

	@Override
	public SystemSkillGroup get(int skillGroupId) {

		String sql = "SELECT * FROM " + table + " WHERE skill_group_id =  ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(skillGroupId);

		return this.jdbc.get(sql, SystemSkillGroup.class, parameter);
	}

}
