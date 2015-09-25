package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.game.dao.SystemFloorObjDao;
import com.lodogame.model.SystemFloorObj;

public class SystemFloorObjDaoMysqlImpl implements SystemFloorObjDao {

	@Autowired
	private Jdbc jdbc;
	
	@Override
	public List<SystemFloorObj> getAll() {
		String sql = "select * from system_floor_obj";
		return jdbc.getList(sql, SystemFloorObj.class);
	}

}
