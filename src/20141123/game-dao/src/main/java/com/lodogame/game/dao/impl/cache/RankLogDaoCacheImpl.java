package com.lodogame.game.dao.impl.cache;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.RankLogDao;
import com.lodogame.game.dao.impl.mysql.RankLogDaoMysqlImpl;
import com.lodogame.game.dao.impl.redis.RankLogDaoRedisImpl;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.model.RankLog;

public class RankLogDaoCacheImpl implements RankLogDao, ReloadAble {

	@Autowired
	private RankLogDaoMysqlImpl rankLogDaoMysqlImpl;

	@Autowired
	private RankLogDaoRedisImpl rankLogDaoRedisImpl;

	@Override
	public boolean add(RankLog rankLog) {
		if (rankLogDaoMysqlImpl.add(rankLog)) {
			return rankLogDaoRedisImpl.add(rankLog);
		}
		return false;
	}

	@Override
	public RankLog getRankLog(String date, String rankKey) {
		RankLog rankLog = rankLogDaoRedisImpl.getRankLog(date, rankKey);
		if (rankLog == null) {
			rankLog = rankLogDaoMysqlImpl.getRankLog(date, rankKey);
			if (rankLog != null) {
				rankLogDaoRedisImpl.add(rankLog);
			}
		}
		return rankLog;
	}

	public RankLogDaoMysqlImpl getRankLogDaoMysqlImpl() {
		return rankLogDaoMysqlImpl;
	}

	public void setRankLogDaoMysqlImpl(RankLogDaoMysqlImpl rankLogDaoMysqlImpl) {
		this.rankLogDaoMysqlImpl = rankLogDaoMysqlImpl;
	}

	public RankLogDaoRedisImpl getRankLogDaoRedisImpl() {
		return rankLogDaoRedisImpl;
	}

	public void setRankLogDaoRedisImpl(RankLogDaoRedisImpl rankLogDaoRedisImpl) {
		this.rankLogDaoRedisImpl = rankLogDaoRedisImpl;
	}

	@Override
	public boolean delete(String date, String rankKey) {
		if (this.rankLogDaoMysqlImpl.delete(date, rankKey)) {
			this.rankLogDaoRedisImpl.delete(date, rankKey);
		}
		return true;
	}

	@Override
	public void reload() {

	}

	@Override
	public void init() {

	}

}
