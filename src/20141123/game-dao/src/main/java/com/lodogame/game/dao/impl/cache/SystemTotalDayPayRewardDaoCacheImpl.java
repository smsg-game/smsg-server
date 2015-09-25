package com.lodogame.game.dao.impl.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lodogame.game.dao.SystemTotalDayPayRewardDao;
import com.lodogame.game.dao.impl.mysql.SystemTotalDayPayRewardDaoMysqlImpl;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.model.SystemTotalDayPayReward;

/**
 * 积天充值奖励
 * 
 * @author zyz
 * 
 */
public class SystemTotalDayPayRewardDaoCacheImpl implements SystemTotalDayPayRewardDao, ReloadAble {

	private SystemTotalDayPayRewardDaoMysqlImpl systemTotalDayPayRewardDaoMysqlImpl;

	private List<SystemTotalDayPayReward> systemTotalDayPayRewardList = new ArrayList<SystemTotalDayPayReward>();
	
	private Map<Integer, SystemTotalDayPayReward> systemTotalDayPayRewardMap = new HashMap<Integer, SystemTotalDayPayReward>();

	
	public SystemTotalDayPayRewardDaoMysqlImpl getSystemTotalDayPayRewardDaoMysqlImpl() {
		return systemTotalDayPayRewardDaoMysqlImpl;
	}

	public void setSystemTotalDayPayRewardDaoMysqlImpl(SystemTotalDayPayRewardDaoMysqlImpl systemTotalDayPayRewardDaoMysqlImpl) {
		this.systemTotalDayPayRewardDaoMysqlImpl = systemTotalDayPayRewardDaoMysqlImpl;
	}

	public void setActivityDrawDaoMysqlImpl(SystemTotalDayPayRewardDaoMysqlImpl systemTotalDayPayRewardDaoMysqlImpl) {
		this.systemTotalDayPayRewardDaoMysqlImpl = systemTotalDayPayRewardDaoMysqlImpl;
	}

	@Override
	public List<SystemTotalDayPayReward> getAll() {
		if (systemTotalDayPayRewardList.isEmpty()){
			this.systemTotalDayPayRewardList = this.systemTotalDayPayRewardDaoMysqlImpl.getAll();
		}
		return systemTotalDayPayRewardList;
	}

	@Override
	public SystemTotalDayPayReward getPayRewardByAid(int aid) {
		
		SystemTotalDayPayReward systemTotalDayPayReward = null;
		
		if (!systemTotalDayPayRewardMap.containsKey(aid)){
			systemTotalDayPayReward = this.systemTotalDayPayRewardDaoMysqlImpl.getPayRewardByAid(aid);
			if (systemTotalDayPayReward != null){
				systemTotalDayPayRewardMap.put(systemTotalDayPayReward.getId(), systemTotalDayPayReward);
			}
		}
		
		return systemTotalDayPayRewardMap.get(aid);
	}

	@Override
	public void reload() {
		systemTotalDayPayRewardList.clear();
		systemTotalDayPayRewardMap.clear();
	}


	@Override
	public void init() {
		ReloadManager.getInstance().register(this.getClass().getSimpleName(), this);
	}



}
