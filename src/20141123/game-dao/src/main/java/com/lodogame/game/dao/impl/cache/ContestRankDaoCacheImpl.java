package com.lodogame.game.dao.impl.cache;

import java.util.List;

import com.lodogame.game.dao.ContestRankDao;
import com.lodogame.game.dao.impl.mysql.ContestRankDaoMysqlImpl;
import com.lodogame.model.UserContestRank;

public class ContestRankDaoCacheImpl implements ContestRankDao {

	private ContestRankDaoMysqlImpl contestRankDaoMysqlImpl;
	
	public void setContestRankDaoMysqlImpl(
			ContestRankDaoMysqlImpl contestRankDaoMysqlImpl) {
		this.contestRankDaoMysqlImpl = contestRankDaoMysqlImpl;
	}

	@Override
	public void saveContestRank(UserContestRank userContestRank) {
		contestRankDaoMysqlImpl.saveContestRank(userContestRank);
		
	}

	@Override
	public List<UserContestRank> getLastSessionRank(int currentSession) {
		return contestRankDaoMysqlImpl.getLastSessionRank(currentSession);
	}

	@Override
	public UserContestRank getLatestSessionChamp() {
		return contestRankDaoMysqlImpl.getLatestSessionChamp();
	}

}
