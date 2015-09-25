package com.lodogame.game.dao.impl.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lodogame.game.dao.ContestPlayerPairDao;
import com.lodogame.game.dao.impl.mysql.ContestPlayerPairDaoMysqlImpl;
import com.lodogame.model.ContestPlayerPair;

public class ContestPlayerPairDaoCacheImpl implements ContestPlayerPairDao{
	
	private ContestPlayerPairDaoMysqlImpl contestPlayerPairDaoMysqlImpl;
	
	public void setContestPlayerPairDaoMysqlImpl(ContestPlayerPairDaoMysqlImpl contestPlayerPairDaoMysqlImpl) {
		this.contestPlayerPairDaoMysqlImpl = contestPlayerPairDaoMysqlImpl;
	}


	Map<String, List<ContestPlayerPair>> playerPairMap = new HashMap<String, List<ContestPlayerPair>>();
	
	@Override
	public List<ContestPlayerPair> getPlayerPairsByContestId(String contestId) {
		List<ContestPlayerPair> playerPairList = playerPairMap.get(contestId);
		if (playerPairList == null) {
			playerPairList = contestPlayerPairDaoMysqlImpl.getPlayerPairsByContestId(contestId);
			playerPairMap.put(contestId, playerPairList);
		}
		return playerPairList;
	}

	@Override
	public ContestPlayerPair getLastPlayerPair(String contestId) {
		List<ContestPlayerPair> playerPairList = playerPairMap.get(contestId);

		if (playerPairList == null || playerPairList.size() == 0) {
			return null;
		} else {
			return playerPairList.get(playerPairList.size() - 1);
		}
	}

	@Override
	public void savePlayerPair(ContestPlayerPair playerPair) {
		
		List<ContestPlayerPair> playerPairList = playerPairMap.get(playerPair.getContestId());
		if (playerPairList == null) {
			playerPairList = new ArrayList<ContestPlayerPair>();
			playerPairMap.put(playerPair.getContestId(), playerPairList);
		}
		playerPairList.add(playerPair);
		
		contestPlayerPairDaoMysqlImpl.savePlayerPair(playerPair);
	}

	@Override
	public boolean updatePlayerPairFightResult(ContestPlayerPair playerPair) {
		return contestPlayerPairDaoMysqlImpl.updatePlayerPairFightResult(playerPair);
	}

	@Override
	public void saveDefUser(ContestPlayerPair playerPair) {
		contestPlayerPairDaoMysqlImpl.saveDefUser(playerPair);
	}

	@Override
	public void incrAttUserBetNum(ContestPlayerPair playerPair) {
		contestPlayerPairDaoMysqlImpl.incrAttUserBetNum(playerPair);
	}

	@Override
	public void incrDefUserBetNum(ContestPlayerPair playerPair) {
		contestPlayerPairDaoMysqlImpl.incrDefUserBetNum(playerPair);
	}

	
	
}
