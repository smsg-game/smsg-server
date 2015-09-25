package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.SystemTavernRebateDao;
import com.lodogame.model.SystemTavernRebate;

public class SystemTavernRebateDaoMysqlImpl implements SystemTavernRebateDao {

	
	@Autowired
	private Jdbc jdbc;
	
	@Override
	public SystemTavernRebate get(int id) {
		
		String sql = "select * from system_tavern_rebate where id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(id);
		
		return jdbc.get(sql, SystemTavernRebate.class, parameter);
	}

	@Override
	public List<SystemTavernRebate> getAllByType(int typeA, int typeB) {
		String sql = "select * from system_tavern_rebate where type = ? or type = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(typeA);
		parameter.setInt(typeB);

		return jdbc.getList(sql, SystemTavernRebate.class, parameter);
	}

}
