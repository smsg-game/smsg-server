package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.RankScoreCfgDao;
import com.lodogame.model.RankScoreCfg;

public class RankScoreCfgDaoMysqlImpl implements RankScoreCfgDao {
	@Autowired
	private Jdbc jdbc;

	public final static String table = "rank_score_cfg";

	public final static String columns = "*";
	
	@Override
	public RankScoreCfg getByRank(int rank) {
		String sql = "SELECT " + columns + " FROM " + table + " where rank = ? ";
		SqlParameter param = new SqlParameter();
		param.setInt(rank);
		return this.jdbc.get(sql, RankScoreCfg.class, param);
	}
	
	
}
