package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.SystemHeroSkillDao;
import com.lodogame.model.SystemHeroSkill;

public class SystemHeroSkillDaoMysqlImpl implements SystemHeroSkillDao {

	private String table = "system_hero_skill";

	private String columns = "*";

	@Autowired
	private Jdbc jdbc;

	@Override
	public List<SystemHeroSkill> getHeroSkillList(int heroId) {
		String sql = "SELECT " + columns + " FROM " + table + " WHERE hero_id = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(heroId);
		return this.jdbc.getList(sql, SystemHeroSkill.class, parameter);
	}
}
