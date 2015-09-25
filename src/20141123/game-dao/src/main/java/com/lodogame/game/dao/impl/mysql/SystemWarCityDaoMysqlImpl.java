package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.SystemWarCityDao;
import com.lodogame.model.SystemWarCity;

public class SystemWarCityDaoMysqlImpl implements SystemWarCityDao {

	@Autowired
	private Jdbc jdbc;
	
	@Override
	public SystemWarCity get(Integer point) {
		String sql = "select * from system_war_city where city_id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(point);
		return jdbc.get(sql, SystemWarCity.class, parameter);
	}

}
