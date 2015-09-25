package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.game.dao.SystemTowerMapDao;
import com.lodogame.model.SystemTowerMap;

public class SystemTowerMapDaoMysqlImpl implements SystemTowerMapDao {

	@Autowired
	private Jdbc jdbc;
	
	@Override
	public List<SystemTowerMap> getAll() {
		String sql = "select * from system_tower_map";
		return jdbc.getList(sql, SystemTowerMap.class);
	}

}
