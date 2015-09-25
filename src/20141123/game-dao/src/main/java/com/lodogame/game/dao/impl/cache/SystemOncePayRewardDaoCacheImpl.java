package com.lodogame.game.dao.impl.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


import com.lodogame.game.dao.SystemOncePayRewardDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.model.SystemOncePayReward;

public class SystemOncePayRewardDaoCacheImpl implements SystemOncePayRewardDao, ReloadAble {

	private SystemOncePayRewardDao systemOncePayRewardDaoMysqlImpl;

	private Map<Integer, SystemOncePayReward> cache = new ConcurrentHashMap<Integer, SystemOncePayReward>();


	@Override
	public SystemOncePayReward getById(int id) {
		if (cache != null && cache.isEmpty()){
			return cache.get(id);
		}
		return null;
	}

	@Override
	public SystemOncePayReward getNextById(int id) {
		SystemOncePayReward nextReward = null;
		if (cache != null && !cache.isEmpty()){
			SystemOncePayReward reward = cache.get(id);
			for(SystemOncePayReward r : cache.values()){
				//为空则初始化
				if(nextReward == null){
					nextReward = r;
					continue;
				}
				//当前相同则直接返回
				if(r.getId() == reward.getId()){
					nextReward = r;
					continue;
				}
				
				//下一阶段必须大于指定的阶段
				if(nextReward.getPayMoney() < reward.getPayMoney()){
					nextReward = r;
					continue;
				}
				
				//如果nextReward小于r，则无需进行下一步，直接continue
				if(nextReward.getPayMoney() < r.getPayMoney()){
					continue;
				}
				//如果r 大于 指定阶段，则r替换nextReward
				if(r.getPayMoney() > reward.getPayMoney()){
					nextReward = r;
				}
			}
		}
		
		return nextReward;
	}

	@Override
	public void reload() {
		initCache();
	}

	private void initCache() {
		cache = new HashMap<Integer, SystemOncePayReward>();
		List<SystemOncePayReward> list = this.systemOncePayRewardDaoMysqlImpl.getAll();
		for (SystemOncePayReward reward : list) {
			cache.put(reward.getId(), reward);
		}
	}

	@Override
	public void init() {
		initCache();
		ReloadManager.getInstance().register(getClass().getSimpleName(), this);
	}

	public SystemOncePayRewardDao getSystemOncePayRewardDaoMysqlImpl() {
		return systemOncePayRewardDaoMysqlImpl;
	}

	public void setSystemOncePayRewardDaoMysqlImpl(SystemOncePayRewardDao systemOncePayRewardDaoMysqlImpl) {
		this.systemOncePayRewardDaoMysqlImpl = systemOncePayRewardDaoMysqlImpl;
	}

	@Override
	public List<SystemOncePayReward> getAll() {
		List<SystemOncePayReward> list = null;
		if (cache == null || cache.isEmpty()) {
			cache = new HashMap<Integer, SystemOncePayReward>();
			list = this.systemOncePayRewardDaoMysqlImpl.getAll();
			for (SystemOncePayReward reward : list) {
				cache.put(reward.getId(), reward);
			}
		} else {
			list = new ArrayList<SystemOncePayReward>();
			for (SystemOncePayReward reward : cache.values()) {
				list.add(reward);
			}
		}
		return list;
	}

}
