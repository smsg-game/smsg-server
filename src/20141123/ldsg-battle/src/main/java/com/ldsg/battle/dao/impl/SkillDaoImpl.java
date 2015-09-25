package com.ldsg.battle.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ldsg.battle.dao.SkillDao;
import com.ldsg.battle.model.EffectModel;
import com.ldsg.battle.model.SkillEffectModel;
import com.ldsg.battle.model.SkillModel;
import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;

public class SkillDaoImpl implements SkillDao {

	@Autowired
	private Jdbc jdbc;

	public List<SkillModel> getSkillList() {

		String sql = "SELECT * FROM system_skill ";

		return this.jdbc.getList(sql, SkillModel.class);
	}

	public List<EffectModel> getEffectList() {

		String sql = "SELECT * FROM system_effect ";

		return this.jdbc.getList(sql, EffectModel.class);
	}

	public List<SkillEffectModel> getSkillEffectList() {

		String sql = "SELECT * FROM skill_effect_list ";

		SqlParameter parameter = new SqlParameter();

		return this.jdbc.getList(sql, SkillEffectModel.class, parameter);
	}
}
