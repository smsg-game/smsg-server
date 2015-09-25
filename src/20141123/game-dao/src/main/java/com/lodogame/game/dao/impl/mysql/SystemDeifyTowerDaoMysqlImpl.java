package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.SystemDeifyTowerDao;
import com.lodogame.model.SystemDeifyTower;

public class SystemDeifyTowerDaoMysqlImpl implements SystemDeifyTowerDao {

	private static final String table = "system_deify_tower";
	
	@Autowired
	private Jdbc jdbc;
	
	@Override
	public List<SystemDeifyTower> getAllSystemDeifyTower() {
		String sql = "SELECT * FROM " + table;
		return this.jdbc.getList(sql, SystemDeifyTower.class);
	}

	@Override
	public SystemDeifyTower get(int towerId) {
		String sql = "SELECT * FROM " + table + " WHERE tower_id = ?";
		SqlParameter param = new SqlParameter();
		param.setInt(towerId);
		return this.jdbc.get(sql, SystemDeifyTower.class, param);
	}

}
