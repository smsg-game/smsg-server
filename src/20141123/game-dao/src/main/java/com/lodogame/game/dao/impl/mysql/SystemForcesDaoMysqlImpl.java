package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.SystemForcesDao;
import com.lodogame.model.SystemForces;

public class SystemForcesDaoMysqlImpl implements SystemForcesDao {

	private final static String table = "system_forces";

	private final static String columns = "*";

	@Autowired
	private Jdbc jdbc;

	public List<SystemForces> getSceneForcesList(int sceneId) {

		String sql = "SELECT " + columns + " FROM " + table + " WHERE scene_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(sceneId);

		return this.jdbc.getList(sql, SystemForces.class, parameter);
	}

	public List<SystemForces> getSysForcesList() {

		String sql = "SELECT " + columns + " FROM " + table;

		return this.jdbc.getList(sql, SystemForces.class);
	}

	@Override
	public SystemForces get(int forcesId) {

		String sql = "SELECT " + columns + " FROM " + table + " WHERE forces_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(forcesId);

		return this.jdbc.get(sql, SystemForces.class, parameter);
	}

	@Override
	public List<SystemForces> getSystemForcesByPreForcesId(int preForcesId) {

		String sql = "SELECT " + columns + " FROM " + table + " WHERE ";

		if (preForcesId == 0) {
			sql += " pre_forces_id = ? and pre_forces_idb = ? ";
		} else {
			sql += " pre_forces_id = ? or pre_forces_idb = ? ";
		}

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(preForcesId);
		parameter.setInt(preForcesId);

		return this.jdbc.getList(sql, SystemForces.class, parameter);
	}

	@Override
	public List<SystemForces> getSystemForcesByType(int type) {
		String sql = "SELECT " + columns + " FROM " + table + " WHERE forces_type = ?";
		
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(type);
		
		return this.jdbc.getList(sql, SystemForces.class, parameter);
	}

	@Override
	public int updateTimes(int forcesId, int times) {
		String sql = "UPDATE " + table + " set times_limit = ? where forces_id = ?";
		
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(times);
		parameter.setInt(forcesId);
		
		return this.jdbc.update(sql, parameter);
	}
}
