package com.lodogame.game.dao.impl.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lodogame.game.dao.SystemArenaRewardDao;
import com.lodogame.game.dao.impl.mysql.SystemArenaRewardDaoMysqlImpl;
import com.lodogame.model.SystemArenaReward;

public class SystemArenaRewardDaoCacheImpl implements SystemArenaRewardDao{

	private SystemArenaRewardDaoMysqlImpl systemArenaRewardDaoMysqlImpl;
	public void setSystemArenaRewardDaoMysqlImpl(
			SystemArenaRewardDaoMysqlImpl systemArenaRewardDaoMysqlImpl) {
		this.systemArenaRewardDaoMysqlImpl = systemArenaRewardDaoMysqlImpl;
	}

	private Map<Integer, SystemArenaReward> cache = new HashMap<Integer, SystemArenaReward>();

	@Override
	public SystemArenaReward getRewardByRank(int rank) {
		return cache.get(rank);
	}
	
	public void init() {
		List<SystemArenaReward> rewardList = systemArenaRewardDaoMysqlImpl.getRewardList();
		for (SystemArenaReward reward : rewardList) {
			cache.put(reward.getRank(), reward);
		}
	}

}
