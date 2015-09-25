package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.RankLogDao;
import com.lodogame.model.RankLog;

public class RankLogDaoMysqlImpl implements RankLogDao {

	@Autowired
	private Jdbc jdbc;

	private final static String TABLE = "rank_log";

	@Override
	public RankLog getRankLog(String date, String rankKey) {
		String sql = "select * from " + TABLE + " where rank_key = ? order by create_time desc limit 1";
		SqlParameter params = new SqlParameter();
		params.setString(rankKey);
		return jdbc.get(sql, RankLog.class, params);
	}

	@Override
	public boolean add(RankLog rankLog) {
		return jdbc.insert(rankLog) == 1;
	}

	@Override
	public boolean delete(String date, String rankKey) {
		String sql = "delete from " + TABLE + " where date = ? and rank_key = ?";
		SqlParameter params = new SqlParameter();
		params.setString(date);
		params.setString(rankKey);
		return jdbc.update(sql, params) > 0;
	}

}
