package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.SystemDeifyAttrDao;
import com.lodogame.model.SystemDeifyAttr;

public class SystemDeifyAttrDaoMysqlImpl implements SystemDeifyAttrDao {

	@Autowired
	private Jdbc jdbc;
	
	@Override
	public SystemDeifyAttr get(int deifyId, int deifyLevel) {
		String sql = "select * from system_deify_attr where deify_id = ? and deify_level = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(deifyId);
		parameter.setInt(deifyLevel);
		
		return jdbc.get(sql, SystemDeifyAttr.class, parameter);
	}

	@Override
	public int maxLevel(int deifyId) {
		String sql = "select max(deify_level) from system_deify_attr where deify_id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(deifyId);
		
		return this.jdbc.getInt(sql, parameter);
	}

}
