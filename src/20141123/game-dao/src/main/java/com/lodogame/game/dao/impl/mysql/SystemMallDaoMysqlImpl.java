package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.SystemMallDao;
import com.lodogame.model.SystemMall;

public class SystemMallDaoMysqlImpl implements SystemMallDao {

	private final static String table = "system_mall";

	@Autowired
	private Jdbc jdbc;

	@Override
	public List<SystemMall> getList() {

		String sql = "SELECT * FROM " + table + " ORDER BY mall_id";

		return this.jdbc.getList(sql, SystemMall.class);
	}

	@Override
	public SystemMall get(int mallId) {

		String sql = "SELECT * FROM " + table + " WHERE mall_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(mallId);

		return this.jdbc.get(sql, SystemMall.class, parameter);
	}
}
