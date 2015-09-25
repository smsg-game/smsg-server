package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.game.dao.SystemArenaRewardDao;
import com.lodogame.model.SystemArenaReward;

public class SystemArenaRewardDaoMysqlImpl implements SystemArenaRewardDao {

	String table = "system_arena_reward";
	
	@Autowired
	private Jdbc jdbc;
	
	@Override
	public SystemArenaReward getRewardByRank(int rank) {
		throw new NotImplementedException();
	}
	
	public List<SystemArenaReward> getRewardList() {
		String sql = "SELECT * FROM " + table;
		return this.jdbc.getList(sql, SystemArenaReward.class);
	}

}
