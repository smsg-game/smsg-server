package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.SystemHeroAttrDao;
import com.lodogame.model.SystemHeroAttr;

public class SystemHeroAttrDaoMysqlImpl implements SystemHeroAttrDao {

	private String table = "system_hero_attr";

	private String columns = "*";

	@Autowired
	private Jdbc jdbc;

	@Override
	public SystemHeroAttr getHeroAttr(int systemHeroId, int heroLevel) {
		String sql = "SELECT " + columns + " FROM " + table + " WHERE system_hero_id = ? and hero_level = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(systemHeroId);
		parameter.setInt(heroLevel);
		return this.jdbc.get(sql, SystemHeroAttr.class, parameter);
	}
}
