package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.ExportDataDao;
import com.lodogame.game.dao.SystemHeroDao;
import com.lodogame.game.utils.json.Json;
import com.lodogame.model.SystemHero;

public class SystemHeroDaoMysqlImpl implements SystemHeroDao, ExportDataDao {

	@Autowired
	private Jdbc jdbc;

	public final static String table = "system_hero";

	public final static String columns = "*";

	public List<SystemHero> getSysHeroList() {

		String sql = "SELECT " + columns + " FROM " + table;

		return this.jdbc.getList(sql, SystemHero.class);
	}

	public SystemHero get(Integer systemHeroId) {

		String sql = "SELECT " + columns + " FROM " + table + " WHERE system_hero_id = ? ;";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(systemHeroId);
		
		return this.jdbc.get(sql, SystemHero.class, parameter);
	}

	public String toJson() {
		List<SystemHero> list = getSysHeroList();
		return Json.toJson(list);
	}

	public void add(SystemHero systemHero) {
		throw new NotImplementedException();
	}

	@Override
	public int getSystemHeroId(Integer heroId, Integer heroColor) {
		String sql = "SELECT system_hero_id FROM " + table + " WHERE hero_id = ? AND hero_color = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(heroId);
		parameter.setInt(heroColor);
		
		return this.jdbc.getInt(sql, parameter);
	}

}
