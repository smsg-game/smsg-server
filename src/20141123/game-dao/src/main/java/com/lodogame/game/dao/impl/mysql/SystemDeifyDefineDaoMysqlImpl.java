package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.SystemDeifyDefineDao;
import com.lodogame.model.SystemDeifyDefine;

public class SystemDeifyDefineDaoMysqlImpl implements SystemDeifyDefineDao {

	@Autowired
	private Jdbc jdbc;
	
	@Override
	public SystemDeifyDefine get(int deifyId) {
		String sql = "select * from system_deify_define where deify_id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(deifyId);
		
		return jdbc.get(sql, SystemDeifyDefine.class, parameter);
	}

	@Override
	public List<SystemDeifyDefine> getByHeroId(int heroId) {
		String sql = "select * from system_deify_define where hero_id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(heroId);
		
		return jdbc.getList(sql, SystemDeifyDefine.class, parameter);
	}

	@Override
	public SystemDeifyDefine getByHeroIdAndType(int heroId, int type) {
		String sql = "select * from system_deify_define where hero_id = ? and equip_type = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(heroId);
		parameter.setInt(type);
		
		return jdbc.get(sql, SystemDeifyDefine.class, parameter);
	}

}
