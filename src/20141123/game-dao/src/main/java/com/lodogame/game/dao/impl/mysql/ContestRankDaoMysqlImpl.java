package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.ContestRankDao;
import com.lodogame.model.UserContestRank;

public class ContestRankDaoMysqlImpl implements ContestRankDao{
	
	@Autowired
	private Jdbc jdbc;
	
	@Override
	public void saveContestRank(UserContestRank userContestRank) {
		this.jdbc.insert(userContestRank);
	}

	@Override
	public List<UserContestRank> getLastSessionRank(int currentSession) {
		String sql = "SELECT * FROM user_contest_rank WHERE session = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(currentSession);
		return this.jdbc.getList(sql, UserContestRank.class, parameter);
	}

	@Override
	public UserContestRank getLatestSessionChamp() {
		String sql = "SELECT * FROM user_contest_rank WHERE rank = 1 ORDER BY session DESC limit 1";
		return this.jdbc.getList(sql, UserContestRank.class).get(0);
	}
}
