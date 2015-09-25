package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.impl.PkGroupAwardDao;
import com.lodogame.model.PkGroupAward;

public class PkGroupAwardDaoMysqlImpl implements PkGroupAwardDao{

	@Autowired
	private Jdbc jdbc;
	
	@Override
	public PkGroupAward getGroupAward(int gid, int rank) {
		String sql = "select * from system_pk_group_award where group_id = ? and rank =?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(gid);
		parameter.setInt(rank);
		
		return jdbc.get(sql, PkGroupAward.class, parameter);
	}

}
