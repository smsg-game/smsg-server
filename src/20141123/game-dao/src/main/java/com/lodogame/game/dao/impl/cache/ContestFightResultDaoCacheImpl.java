package com.lodogame.game.dao.impl.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.ContestFightResultDao;
import com.lodogame.game.dao.impl.mysql.ContestFightResultDaoMysqlImpl;
import com.lodogame.model.ContestFightResult;

public class ContestFightResultDaoCacheImpl implements ContestFightResultDao{

	private ContestFightResultDaoMysqlImpl contestFightResultDaoMysqlImpl;
	public void setContestFightResultDaoMysqlImpl(
			ContestFightResultDaoMysqlImpl contestFightResultDaoMysqlImpl) {
		this.contestFightResultDaoMysqlImpl = contestFightResultDaoMysqlImpl;
	}

	/**
	 * key是playerPairId
	 */
	private Map<String, ContestFightResult> cacheMap = new ConcurrentHashMap<String, ContestFightResult>();

	@Override
	public boolean save(ContestFightResult fightResult) {
		return contestFightResultDaoMysqlImpl.save(fightResult);
	}

	@Override
	public ContestFightResult get(String playerPairId){
		ContestFightResult fightResult = cacheMap.get(playerPairId);
		if (fightResult == null) {
			fightResult = contestFightResultDaoMysqlImpl.get(playerPairId);
		}
		return fightResult;
	}
	

}
