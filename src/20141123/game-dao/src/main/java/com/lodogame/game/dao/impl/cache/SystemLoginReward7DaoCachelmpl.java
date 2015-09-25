package com.lodogame.game.dao.impl.cache;

import java.util.ArrayList;
import java.util.List;

import com.lodogame.game.dao.SystemLoginReward7Dao;
import com.lodogame.game.dao.reload.ReloadAbleBase;
import com.lodogame.model.SystemLoginReward7;

public class SystemLoginReward7DaoCachelmpl extends ReloadAbleBase implements SystemLoginReward7Dao {

	private SystemLoginReward7Dao systemLoginReward7DaoMysqlImpl;

	public void setSystemLoginReward7DaoMysqlImpl(SystemLoginReward7Dao systemLoginReward7DaoMysqlImpl) {
		this.systemLoginReward7DaoMysqlImpl = systemLoginReward7DaoMysqlImpl;
	}

	private List<SystemLoginReward7> rewardList = new ArrayList<SystemLoginReward7>();

	@Override
	public List<SystemLoginReward7> getAll() {
		if (rewardList.size() == 0) {
			rewardList = systemLoginReward7DaoMysqlImpl.getAll();
		}
		return rewardList;
	}

	@Override
	public void reload() {
		rewardList.clear();
	}

	@Override
	public SystemLoginReward7 getByDay(int day) {
		for (SystemLoginReward7 reward : rewardList) {
			if (reward.getDay() == day) {
				return reward;
			}
		}

		SystemLoginReward7 reward = systemLoginReward7DaoMysqlImpl.getByDay(day);
		rewardList.add(reward);
		return reward;
	}

}
