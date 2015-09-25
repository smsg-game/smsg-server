package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.SystemReduceRebateDao;
import com.lodogame.model.SystemReduceRebate;

public class SystemReduceRebateDaoMysqlImpl implements SystemReduceRebateDao {

	@Autowired
	private Jdbc jdbc;
	
	@Override
	public SystemReduceRebate get(int id) {
		
		String sql = "select * from system_reduce_rebate where id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(id);
		
		return jdbc.get(sql, SystemReduceRebate.class, parameter);
	}

	@Override
	public List<SystemReduceRebate> getAllByType(int type) {
		
		String sql = "select * from system_reduce_rebate where type = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(type);
		
		return jdbc.getList(sql, SystemReduceRebate.class,parameter);
	}
	
}
