package com.lodogame.game.dao.impl.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.SystemLoginRewardDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.model.SystemLoginReward;

public class SystemLoginRewardDaoCacheImpl implements SystemLoginRewardDao, ReloadAble {

	private SystemLoginRewardDao systemLoginRewardDaoMysqlImpl;

	private Map<Integer, SystemLoginReward> loginRewardListReward = new ConcurrentHashMap<Integer, SystemLoginReward>();

	private List<SystemLoginReward> systemLoginRewardList = new ArrayList<SystemLoginReward>();

	public void setSystemLoginRewardDaoMysqlImpl(SystemLoginRewardDao systemLoginRewardDaoMysqlImpl) {
		this.systemLoginRewardDaoMysqlImpl = systemLoginRewardDaoMysqlImpl;
	}

	@Override
	public SystemLoginReward getSystemLoginRewardByDay(int day) {
		if (loginRewardListReward.containsKey(day)) {
			return this.loginRewardListReward.get(day);
		} else {
			SystemLoginReward systemLoginReward = this.systemLoginRewardDaoMysqlImpl.getSystemLoginRewardByDay(day);
			this.loginRewardListReward.put(day, systemLoginReward);
			return systemLoginReward;
		}
	}

	@Override
	public List<SystemLoginReward> getSystemLoginReward() {
		if (systemLoginRewardList.size() == 0) {
			systemLoginRewardList = this.systemLoginRewardDaoMysqlImpl.getSystemLoginReward();
		}
		return systemLoginRewardList;
	}

	@Override
	public void reload() {
		loginRewardListReward.clear();
		systemLoginRewardList.clear();
	}

	@Override
	public void init() {
		ReloadManager.getInstance().register(getClass().getSimpleName(), this);
	}
}
