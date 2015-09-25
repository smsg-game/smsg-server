package com.lodogame.game.dao.impl.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.SystemCheckInRewardDao;
import com.lodogame.game.dao.reload.ReloadAbleBase;
import com.lodogame.model.SystemCheckInReward;

public class SystemCheckInRewardDaoCacheImpl extends ReloadAbleBase implements SystemCheckInRewardDao {

	private SystemCheckInRewardDao systemCheckInRewardDaoMysqlImpl;

	private Map<Integer,List<SystemCheckInReward>> checkRewardListReward = new ConcurrentHashMap<Integer,List<SystemCheckInReward>>();
	
	public void setSystemCheckInRewardDaoMysqlImpl(SystemCheckInRewardDao systemCheckInRewardDaoMysqlImpl) {
		this.systemCheckInRewardDaoMysqlImpl = systemCheckInRewardDaoMysqlImpl;
	}

	@Override
	public SystemCheckInReward getSystemCheckInReward(int groupId, int day) {
		List<SystemCheckInReward> list = null;
		if(!checkRewardListReward.containsKey(groupId)){
			list = this.systemCheckInRewardDaoMysqlImpl.getSystemCheckInReward(groupId);
			checkRewardListReward.put(groupId, list);
		}else{
			list = checkRewardListReward.get(groupId);
		}
		if(list!=null){
			for(SystemCheckInReward checkInReward:list){
				if(checkInReward.getDay()==day){
					return checkInReward;
				}
			}
		}
		return null;
	}

	@Override
	public List<SystemCheckInReward> getSystemCheckInReward(int groupId) {
		List<SystemCheckInReward> list = null;
		if(!checkRewardListReward.containsKey(groupId)){
			list = this.systemCheckInRewardDaoMysqlImpl.getSystemCheckInReward(groupId);
			checkRewardListReward.put(groupId, list);
		}
		
		return checkRewardListReward.get(groupId);
	}

	@Override
	public void reload() {
		checkRewardListReward.clear();
	}

}
